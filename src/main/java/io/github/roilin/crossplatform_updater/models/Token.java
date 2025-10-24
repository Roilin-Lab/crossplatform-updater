package io.github.roilin.crossplatform_updater.models;

import java.time.LocalDateTime;

import io.github.roilin.crossplatform_updater.models.enums.TokenType;
import io.github.roilin.crossplatform_updater.models.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Token {
    @Id
    private Long id;
    private TokenType type;
    private String value;
    private LocalDateTime expiringTime;
    private boolean disable;
    @ManyToOne
    private User user;

    public Token(boolean disable, LocalDateTime expiringTime, TokenType type, User user, String value) {
        this.disable = disable;
        this.expiringTime = expiringTime;
        this.type = type;
        this.user = user;
        this.value = value;
    }

}
