package com.verdeo.verdeo_api.repository;

import com.verdeo.verdeo_api.model.RefreshToken;
import com.verdeo.verdeo_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    //  Buscar token válido (no revocado)
    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash);

    //  Token válido + no expirado
    Optional<RefreshToken> findByTokenHashAndRevokedFalseAndExpiresAtAfter(
            String tokenHash,
            LocalDateTime now
    );

    //  Tokens por usuario
    List<RefreshToken> findByUser(User user);

    //  Eliminar tokens por usuario
    void deleteByUser(User user);

    //  Limpiar tokens expirados
    void deleteByExpiresAtBefore(LocalDateTime now);
}