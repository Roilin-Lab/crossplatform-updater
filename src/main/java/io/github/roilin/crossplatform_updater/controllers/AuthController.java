package io.github.roilin.crossplatform_updater.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.dto.LoginRequest;
import io.github.roilin.crossplatform_updater.dto.LoginResponse;
import io.github.roilin.crossplatform_updater.services.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
    @CookieValue(name = "access-token", required = false) String accessToken,
    @CookieValue(name = "refresh-token", required = false) String refreshToken,
    @RequestBody LoginRequest loginRequest
  ) {
    return authenticationService.login(loginRequest, accessToken, refreshToken);
  }
}
