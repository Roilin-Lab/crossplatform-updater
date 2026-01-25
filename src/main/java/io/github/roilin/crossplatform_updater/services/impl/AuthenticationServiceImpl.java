package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import io.github.roilin.crossplatform_updater.services.TelegramBotService;
import io.github.roilin.crossplatform_updater.services.UserService;
import io.github.roilin.crossplatform_updater.util.CookieUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final TelegramBotService botService;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtil cookieUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

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
        logger.info("User: {}, is authenticated", user.getUsername());

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
            logger.info("A new access token has been created for user {username}", user.getUsername());
        }
        if (!refreshValid || accessValid) {
            newRefresh = jwtTokenProvider.generatedRefreshToken(refreshDurationDays, ChronoUnit.DAYS, user);
            newRefresh.setUser(user);
            addRefreshTokenCookie(headers, newRefresh);
            tokenRepository.save(newRefresh);
            logger.info("A new refresh token has been created for user {username}", user.getUsername());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponse loginResponse = new LoginResponse(true, user.getUsername(), user.getRole().getName());

        logger.info("Login complete for user {}. Role: {}", user.getUsername(), user.getRole().getName());

        try {
            botService.sendToAdmin(
                    String.format("User: %s%nEmail: %s%nRole: %s%nSuccessfully logged in", user.getUsername(),
                            user.getEmail(), user.getRole().getName()));
        } catch (Exception e) {
            logger.error("Failed to send login notification to admin");
        }

        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    @Override
    public ResponseEntity<LoginResponse> refresh(String refresh) {
        if (!jwtTokenProvider.isValid(refresh)) {
            logger.error("Token is invalid");
            throw new RuntimeException("Token is invalid");
        }
        User user = userService.getUser(jwtTokenProvider.getUsername(refresh));
        Token newAccess = jwtTokenProvider.generatedAccessToken(Map.of("role", user.getRole().getAuthority()),
                accessDurationMinute, ChronoUnit.MINUTES, user);
        newAccess.setUser(user);
        logger.info("A new access token has been updated for user {username}", user.getUsername());
        HttpHeaders headers = new HttpHeaders();
        addAccessTokenCookie(headers, newAccess);
        tokenRepository.save(newAccess);

        LoginResponse loginResponse = new LoginResponse(true, user.getUsername(), user.getRole().getName());
        logger.info("Login complete for user {}. Role: {}", user.getUsername(), user.getRole().getName());
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
        logger.info("Logout complete for user {}. Role: {}", user.getUsername(), user.getRole().getName());
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    @Override
    public UserLoggedDto info() {
        User user = getAuthenticatedUser();

        return UserMapper.toUserLoggedDto(user);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken) {
            logger.info("User: {} with this name does not exist", auth.getName());
            throw new RuntimeException("No user");
        }

        return userService.getUser(auth.getName());
    }

    @Override
    public ResponseEntity<LoginResponse> changePassword(ChangePasswordRequest changePasswordRequest, String access,
            String refresh) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());

        logger.info("User: {} is trying to change the password", user.getUsername());
        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
            logger.error("Incorrectly entered old password", user.getUsername());
            throw new UserBadRequestException("Incorrectly entered old password");
        }
        if (!changePasswordRequest.newPassword().equals(changePasswordRequest.repeatedNew())) {
            logger.error("The new passwords entered do not match", user.getUsername());
            throw new RuntimeException("The new passwords entered do not match");
        }
        if (changePasswordRequest.oldPassword().equals(changePasswordRequest.newPassword())) {
            logger.error("The new password is the same as the old password", user.getUsername());
            throw new RuntimeException("The new password is the same as the old password");
        }

        try {
            userService.changePassword(auth.getName(), passwordEncoder.encode(changePasswordRequest.newPassword()));
            logger.info("User: {} has successfully changed the password", user.getUsername());
        } catch (Exception e) {
            logger.info("User: {}. Failed to change password", user.getUsername());
        }
        return logout(access, refresh);
    }
}
