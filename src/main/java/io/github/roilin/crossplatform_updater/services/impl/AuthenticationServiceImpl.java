package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.ChangePasswordRequest;
import io.github.roilin.crossplatform_updater.dto.LoginRequest;
import io.github.roilin.crossplatform_updater.dto.LoginResponse;
import io.github.roilin.crossplatform_updater.dto.UserLoggedDto;
import io.github.roilin.crossplatform_updater.exception.UserBadRequestException;
import io.github.roilin.crossplatform_updater.jwt.JwtTokenProvider;
import io.github.roilin.crossplatform_updater.mapper.UserMapper;
import io.github.roilin.crossplatform_updater.models.Token;
import io.github.roilin.crossplatform_updater.models.user.User;
import io.github.roilin.crossplatform_updater.repositories.TokenRepository;
import io.github.roilin.crossplatform_updater.services.AuthenticationService;
import io.github.roilin.crossplatform_updater.services.UserService;
import io.github.roilin.crossplatform_updater.util.CookieUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String access, String refresh) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        User user = userService.getUser(loginRequest.username());

        boolean accessValid = jwtTokenProvider.isValid(access);
        boolean refreshValid = jwtTokenProvider.isValid(refresh);

        HttpHeaders headers = new HttpHeaders();
        Token newAccess, newRefresh;

        revokeAllTokens(user);

        if (!accessValid) {
            newAccess = jwtTokenProvider.generatedAccessToken(Map.of("role", user.getRole().getAuthority()),
                    accessDurationMinute, ChronoUnit.MINUTES, user);
            newAccess.setUser(user);
            addAccessTokenCookie(headers, newAccess);
            tokenRepository.save(newAccess);
        }
        if (!refreshValid || accessValid) {
            newRefresh = jwtTokenProvider.generatedRefreshToken(refreshDurationDays, ChronoUnit.DAYS, user);
            newRefresh.setUser(user);
            addRefreshTokenCookie(headers, newRefresh);
            tokenRepository.save(newRefresh);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse loginResponse = new LoginResponse(true, user.getUsername(), user.getRole().getName());
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    @Override
    public ResponseEntity<LoginResponse> refresh(String refresh) {
        if (!jwtTokenProvider.isValid(refresh)) {
            throw new RuntimeException("Token is invalid");
        }
        User user = userService.getUser(jwtTokenProvider.getUsername(refresh));
        Token newAccess = jwtTokenProvider.generatedAccessToken(Map.of("role", user.getRole().getAuthority()),
                accessDurationMinute, ChronoUnit.MINUTES, user);
        newAccess.setUser(user);
        HttpHeaders headers = new HttpHeaders();
        addAccessTokenCookie(headers, newAccess);
        tokenRepository.save(newAccess);

        LoginResponse loginResponse = new LoginResponse(true, user.getUsername(), user.getRole().getName());
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    @Override
    public ResponseEntity<LoginResponse> logout(String access, String refresh) {
        SecurityContextHolder.clearContext();
        User user = userService.getUser(jwtTokenProvider.getUsername(access));
        revokeAllTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessCookie().toString());
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteRefreshCookie().toString());

        LoginResponse loginResponse = new LoginResponse(false, null, null);
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    @Override
    public UserLoggedDto info() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("No user");
        }
        User user = userService.getUser(auth.getName());

        return UserMapper.toUserLoggedDto(user);
    }

    @Override
    public ResponseEntity<LoginResponse> changePassword(ChangePasswordRequest changePasswordRequest, String access,
            String refresh) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());

        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
            throw new UserBadRequestException("Incorrectly entered old password");
        }
        if (!changePasswordRequest.newPassword().equals(changePasswordRequest.repeatedNew())) {
            throw new RuntimeException("The new passwords entered do not match");
        }
        if (changePasswordRequest.oldPassword().equals(changePasswordRequest.newPassword())) {
            throw new RuntimeException("The new password is the same as the old password");
        }

        userService.changePassword(auth.getName(), passwordEncoder.encode(changePasswordRequest.newPassword()));
        return logout(access, refresh);
    }
}
