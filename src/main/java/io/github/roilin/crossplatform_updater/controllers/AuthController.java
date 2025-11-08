package io.github.roilin.crossplatform_updater.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.dto.ChangePasswordRequest;
import io.github.roilin.crossplatform_updater.dto.LoginRequest;
import io.github.roilin.crossplatform_updater.dto.LoginResponse;
import io.github.roilin.crossplatform_updater.dto.UserLoggedDto;
import io.github.roilin.crossplatform_updater.services.AuthenticationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
      @CookieValue(name = "access-token", required = false) String accessToken,
      @CookieValue(name = "refresh-token", required = false) String refreshToken,
      @RequestBody LoginRequest loginRequest) {
    return authenticationService.login(loginRequest, accessToken, refreshToken);
  }

  @PatchMapping("/password/change")
  public ResponseEntity<LoginResponse> changePassword(
      @CookieValue(name = "access-token", required = false) String accessToken,
      @CookieValue(name = "refresh-token", required = false) String refreshToken,
      @RequestBody ChangePasswordRequest changePasswordRequest) {
    return authenticationService.changePassword(changePasswordRequest, accessToken, refreshToken);
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponse> refresh(
      @CookieValue(name = "refresh-token", required = false) String refreshToken) {
    return authenticationService.refresh(refreshToken);
  }

  @PostMapping("/logout")
  public ResponseEntity<LoginResponse> logout(
      @CookieValue(name = "access-token", required = false) String accessToken,
      @CookieValue(name = "refresh-token", required = false) String refreshToken) {
    return authenticationService.logout(accessToken, refreshToken);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/info")
  public ResponseEntity<UserLoggedDto> userLoggedInfo() {
    return ResponseEntity.ok(authenticationService.info());
  }
}
