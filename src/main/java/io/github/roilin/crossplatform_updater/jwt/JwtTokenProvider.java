package io.github.roilin.crossplatform_updater.jwt;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.models.Token;
import io.github.roilin.crossplatform_updater.models.enums.TokenType;
import io.github.roilin.crossplatform_updater.repositories.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtTokenProvider {
    TokenRepository tokenRepository;

    @Value("${jwt.secret}")
    private final String key;

    public String getUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public LocalDateTime getExpiration(String token) {
        return toLocalDateTime(extractClaim(token, Claims::getExpiration));
    }

    public Token generatedAccessToken(Map<String, Object> extra, long duration, TemporalUnit durationType,
            UserDetails user) {
        String username = user.getUsername();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationDate = now.plus(duration, durationType);
        String value = Jwts.builder().setClaims(extra).setSubject(username).setIssuedAt(toDate(now))
                .setExpiration(toDate(expirationDate)).signWith(decodeSecretKey(key), SignatureAlgorithm.HS256)
                .compact();
        return new Token(false, expirationDate, TokenType.ACCESS, null, value);
    }

    public Token generatedRefreshToken(long duration, TemporalUnit durationType, UserDetails user) {
        String username = user.getUsername();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationDate = now.plus(duration, durationType);
        String value = Jwts.builder().setSubject(username).setIssuedAt(toDate(now))
                .setExpiration(toDate(expirationDate)).signWith(decodeSecretKey(key), SignatureAlgorithm.HS256)
                .compact();
        return new Token(false, expirationDate, TokenType.REFRESH, null, value);
    }

    public boolean isValid(String token) {
        if (token == null)
            return false;
        try {
            Jwts.parserBuilder().setSigningKey(decodeSecretKey(key)).build().parseClaimsJwt(token);
            return !isDisable(token);
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isDisable(String value) {
        Token token = tokenRepository.findByValue(value).orElse(null);
        if (token == null)
            return true;
        return token.isDisable();
    }

    private Date toDate(LocalDateTime time) {
        return Date.from(time.toInstant(ZoneOffset.UTC));
    }

    private LocalDateTime toLocalDateTime(Date time) {
        return time.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(decodeSecretKey(key)).build().parseClaimsJwt(token).getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        return claimResolver.apply(extractAllClaims(token));
    }

    private Key decodeSecretKey(String key) {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
    }
}
