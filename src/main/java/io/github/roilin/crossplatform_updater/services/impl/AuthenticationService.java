package io.github.roilin.crossplatform_updater.services.impl;

import org.springframework.http.ResponseEntity;

import io.github.roilin.crossplatform_updater.dto.LoginRequest;
import io.github.roilin.crossplatform_updater.dto.LoginResponse;

public interface AuthenticationService {
  ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String access, String refresh);
}
