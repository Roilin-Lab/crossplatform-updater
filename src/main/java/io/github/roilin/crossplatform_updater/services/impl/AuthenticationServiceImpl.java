package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.LoginRequest;
import io.github.roilin.crossplatform_updater.dto.LoginResponse;
import io.github.roilin.crossplatform_updater.jwt.JwtTokenProvider;
import io.github.roilin.crossplatform_updater.models.Token;
import io.github.roilin.crossplatform_updater.models.user.User;
import io.github.roilin.crossplatform_updater.repositories.TokenRepository;
import io.github.roilin.crossplatform_updater.repositories.UserRepository;
import io.github.roilin.crossplatform_updater.services.UserService;
import io.github.roilin.crossplatform_updater.util.CookieUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {
    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.access.duration.minutes}")
    private long accessDurationMinute;
    @Value("${jwt.access.duration.second}")
    private long accessDurationSecond;
    @Value("${jwt.refresh.duration.days}")
    private long refreshDurationDays;
    @Value("${jwt.refresh.duration.second}")
    private long refreshDurationSecond;

    private void addAccessTokenCookie(HttpHeaders headers, Token token) {
        headers.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createAccessCookie(token.getValue(), accessDurationSecond).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders headers, Token token) {
        headers.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createRefreshCookie(token.getValue(), refreshDurationSecond).toString());
    }

    private void revokeAllTokens(User user) {
        Set<Token> tokens = user.getTokens();
        tokens.forEach(token -> {
            if (token.getExpiringTime().isBefore(LocalDateTime.now())) {
                tokenRepository.delete(token);
            } else if (!token.isDisable()) {
                token.setDisable(true);
                tokenRepository.save(token);
            }
        });
    }

    // public ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String access, String refresh) {
    //     Authentication authentication = authenticationManager
    //             .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
    //     User user = userService.getUser(loginRequest.username());
    //     boolean accessValid = jwtTokenProvider.isValid(access);
    //     boolean refreshValid = jwtTokenProvider.isValid(refresh);
    //     HttpHeaders headers = new HttpHeaders();
    //     revokeAllTokens(user);
    //     if (!accessValid) {
    //         Token newAccess = jwtTokenProvider.generatedAccessToken(Map.of("role", user.getRole().getAuthority()),
    //                 accessDurationMinute, ChronoUnit.MINUTES, user);
    //         newAccess.setUser(user);
    //         addAccessTokenCookie(headers, newAccess);
    //         tokenRepository.save(newAccess);
    //     }
    //     if (!refreshValid) {
    //         Token newRefresh = jwtTokenProvider.generatedRefreshToken(refreshDurationDays, ChronoUnit.DAYS, user);
    //         newRefresh.setUser(user);
    //         addRefreshTokenCookie(headers, newRefresh);
    //         tokenRepository.save(newRefresh);
    //     }
    // }
}
