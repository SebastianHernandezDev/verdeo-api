package com.verdeo.verdeo_api.service;

import com.verdeo.verdeo_api.model.RefreshToken;
import com.verdeo.verdeo_api.model.User;
import com.verdeo.verdeo_api.repository.RefreshTokenRepository;
import com.verdeo.verdeo_api.repository.UserRepository;
import com.verdeo.verdeo_api.exception.ResourceNotFoundException;
import com.verdeo.verdeo_api.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }


    // GENERAR TOKEN

    public RefreshToken createToken(UUID userId, String tokenHash, int daysToExpire) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        RefreshToken refreshToken = new RefreshToken(
                user,
                tokenHash,
                LocalDateTime.now().plusDays(daysToExpire)
        );

        return refreshTokenRepository.save(refreshToken);
    }


    // VALIDAR TOKEN

    public RefreshToken validateToken(String tokenHash) {

        RefreshToken token = refreshTokenRepository
                .findByTokenHashAndRevokedFalseAndExpiresAtAfter(
                        tokenHash,
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new BadRequestException("Refresh token inválido o expirado"));

        if (!token.isValid()) {
            throw new BadRequestException("Refresh token no válido");
        }

        return token;
    }


    // REVOKE TOKEN (LOGOUT)

    public void revokeToken(String tokenHash) {

        RefreshToken token = refreshTokenRepository
                .findByTokenHashAndRevokedFalse(tokenHash)
                .orElseThrow(() -> new ResourceNotFoundException("Token no encontrado"));

        token.revoke();

        refreshTokenRepository.save(token);
    }


    // REVOKE TODOS LOS TOKENS DEL USUARIO

    public void revokeAllUserTokens(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<RefreshToken> tokens = refreshTokenRepository.findByUser(user);

        for (RefreshToken token : tokens) {
            token.revoke();
        }

        refreshTokenRepository.saveAll(tokens);
    }


    // LIMPIAR TOKENS EXPIRADOS

    public void cleanExpiredTokens() {
        refreshTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}