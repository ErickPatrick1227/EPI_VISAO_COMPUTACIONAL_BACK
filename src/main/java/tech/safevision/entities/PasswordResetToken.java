package tech.safevision.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private boolean usado = false;

    @PrePersist
    public void prePersist() {
        // Expira em 30 minutos
        this.expiresAt = LocalDateTime.now().plusMinutes(30);
    }

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public boolean isUsado() { return usado; }
    public void setUsado(boolean usado) { this.usado = usado; }
}
