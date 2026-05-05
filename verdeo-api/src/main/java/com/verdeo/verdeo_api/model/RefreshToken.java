package com.verdeo.verdeo_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public RefreshToken() {}

    public RefreshToken(User user, String tokenHash, LocalDateTime expiresAt) {
        this.user = user;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.revoked = false;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 🔹 Getters

    public UUID getId() { return id; }

    public User getUser() { return user; }

    public String getTokenHash() { return tokenHash; }

    public LocalDateTime getExpiresAt() { return expiresAt; }

    public boolean isRevoked() { return revoked; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    // 🔹 Setters necesarios

    public void setUser(User user) { this.user = user; }

    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }

    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    // 🔥 Lógica de dominio

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !revoked && !isExpired();
    }

    public void revoke() {
        this.revoked = true;
    }
}