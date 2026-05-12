package com.verdeo.verdeo_api.service;

import com.verdeo.verdeo_api.dto.AuthResponse;
import com.verdeo.verdeo_api.dto.LoginRequest;
import com.verdeo.verdeo_api.dto.RegisterRequest;
import com.verdeo.verdeo_api.exception.BadRequestException;
import com.verdeo.verdeo_api.exception.ResourceNotFoundException;
import com.verdeo.verdeo_api.model.RefreshToken;
import com.verdeo.verdeo_api.model.Role;
import com.verdeo.verdeo_api.model.User;
import com.verdeo.verdeo_api.repository.RefreshTokenRepository;
import com.verdeo.verdeo_api.repository.UserRepository;
import com.verdeo.verdeo_api.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;                          // ← nuevo

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository          = userRepository;
        this.refreshTokenRepository  = refreshTokenRepository;
        this.passwordEncoder         = passwordEncoder;
        this.jwtUtil                 = jwtUtil;
    }

    // ──────────────────────────────────────────
    // REGISTRO
    // ──────────────────────────────────────────
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BadRequestException("El correo ya está registrado");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        return generateTokens(user);
    }

    // ──────────────────────────────────────────
    // LOGIN
    // ──────────────────────────────────────────
    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmailIgnoreCaseAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Credenciales inválidas");
        }

        return generateTokens(user);
    }

    // ──────────────────────────────────────────
    // REFRESH TOKEN
    // ──────────────────────────────────────────
    public AuthResponse refresh(String refreshToken) {

        RefreshToken token = refreshTokenRepository
                .findByTokenHashAndRevokedFalseAndExpiresAtAfter(refreshToken, LocalDateTime.now())
                .orElseThrow(() -> new BadRequestException("Token inválido o expirado"));

        if (token.isExpired()) {
            throw new BadRequestException("Token expirado");
        }

        // Solo regeneramos el accessToken JWT — el refreshToken sigue siendo el mismo
        String newAccessToken = jwtUtil.generateToken(
                token.getUser().getIdUser(),
                token.getUser().getRole().name()
        );

        return new AuthResponse(newAccessToken, refreshToken);
    }

    // ──────────────────────────────────────────
    // LOGOUT
    // ──────────────────────────────────────────
    public void logout(String refreshToken) {

        RefreshToken token = refreshTokenRepository
                .findByTokenHashAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token no encontrado"));

        token.revoke();
        refreshTokenRepository.save(token);
    }

    // ──────────────────────────────────────────
    // GENERACIÓN DE TOKENS (JWT real)
    // ──────────────────────────────────────────
    private AuthResponse generateTokens(User user) {

        // accessToken: JWT firmado, expira en 15 min (configurado en properties)
        String accessToken = jwtUtil.generateToken(
                user.getIdUser(),
                user.getRole().name()   // "USER" o "ADMIN"
        );

        // refreshToken: UUID opaco guardado en BD, expira en 7 días
        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(refreshTokenValue);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, refreshTokenValue);
    }
}