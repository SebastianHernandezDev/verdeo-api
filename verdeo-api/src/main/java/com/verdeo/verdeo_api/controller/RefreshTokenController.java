package com.verdeo.verdeo_api.controller;

import com.verdeo.verdeo_api.model.RefreshToken;
import com.verdeo.verdeo_api.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/refresh-tokens")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    public RefreshTokenController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    // POST crear token manualmente (uso interno / admin)
    @PostMapping
    public ResponseEntity<RefreshToken> createToken(@RequestBody Map<String, Object> body) {
        UUID userId = UUID.fromString((String) body.get("userId"));
        String tokenHash = (String) body.get("tokenHash");
        int daysToExpire = (int) body.get("daysToExpire");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenService.createToken(userId, tokenHash, daysToExpire));
    }

    // GET validar token
    @GetMapping("/validate")
    public ResponseEntity<RefreshToken> validateToken(@RequestParam String tokenHash) {
        return ResponseEntity.ok(refreshTokenService.validateToken(tokenHash));
    }

    // PATCH revocar un token específico (logout manual)
    @PatchMapping("/revoke")
    public ResponseEntity<Void> revokeToken(@RequestParam String tokenHash) {
        refreshTokenService.revokeToken(tokenHash);
        return ResponseEntity.noContent().build();
    }

    // PATCH revocar todos los tokens de un usuario
    @PatchMapping("/revoke/user/{userId}")
    public ResponseEntity<Void> revokeAllUserTokens(@PathVariable UUID userId) {
        refreshTokenService.revokeAllUserTokens(userId);
        return ResponseEntity.noContent().build();
    }

    // DELETE limpiar tokens expirados (tarea de mantenimiento / admin)
    @DeleteMapping("/expired")
    public ResponseEntity<Void> cleanExpiredTokens() {
        refreshTokenService.cleanExpiredTokens();
        return ResponseEntity.noContent().build();
    }
}