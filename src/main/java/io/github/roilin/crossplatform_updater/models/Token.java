package io.github.roilin.crossplatform_updater.models;

import java.time.LocalDateTime;

import io.github.roilin.crossplatform_updater.models.enums.TokenType;
import io.github.roilin.crossplatform_updater.models.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
