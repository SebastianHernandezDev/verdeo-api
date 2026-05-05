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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTRO
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

    // LOGIN
    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmailIgnoreCaseAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Credenciales inválidas");
        }

        return generateTokens(user);
    }

    // REFRESH TOKEN
    public AuthResponse refresh(String refreshToken) {

        RefreshToken token = refreshTokenRepository
                .findByTokenHashAndRevokedFalseAndExpiresAtAfter(
                        refreshToken,
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new BadRequestException("Token inválido o expirado"));

        // validación adicional usando tu modelo
        if (token.isExpired()) {
            throw new BadRequestException("Token expirado");
        }

        return generateTokens(token.getUser());
    }

    // LOGOUT
    public void logout(String refreshToken) {

        RefreshToken token = refreshTokenRepository
                .findByTokenHashAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token no encontrado"));
        token.revoke();

        refreshTokenRepository.save(token);
    }

    // GENERACIÓN DE TOKENS (TEMPORAL - luego JWT real)
    private AuthResponse generateTokens(User user) {

        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setTokenHash(refreshToken);
        token.setExpiresAt(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(token);

        return new AuthResponse(accessToken, refreshToken);
    }
}