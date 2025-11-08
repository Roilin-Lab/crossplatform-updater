package io.github.roilin.crossplatform_updater.services;

import org.springframework.http.ResponseEntity;

import io.github.roilin.crossplatform_updater.dto.ChangePasswordRequest;
import io.github.roilin.crossplatform_updater.dto.LoginRequest;
import io.github.roilin.crossplatform_updater.dto.LoginResponse;
import io.github.roilin.crossplatform_updater.dto.UserLoggedDto;

public interface AuthenticationService {
  ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String access, String refresh);
  ResponseEntity<LoginResponse> refresh(String refresh);
  ResponseEntity<LoginResponse> logout(String access, String refresh);
  ResponseEntity<LoginResponse> changePassword(ChangePasswordRequest changePasswordRequest, String access, String refresh);
  UserLoggedDto info();
}
