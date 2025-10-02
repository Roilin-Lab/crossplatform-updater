package io.github.roilin.crossplatform_updater.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.dto.UserDeviceRequest;
import io.github.roilin.crossplatform_updater.dto.UserDeviceResponse;
import io.github.roilin.crossplatform_updater.services.UserDeviceService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserDeviceController {

  private final UserDeviceService userDeviceService;

  @GetMapping("/devices")
  public List<UserDeviceResponse> getAllDevices(@RequestParam String username) {
    return userDeviceService.getAllByUsername(username);
  }

  @PostMapping("/devices")
  public ResponseEntity<UserDeviceResponse> createDevice(@RequestBody UserDeviceRequest device) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userDeviceService.create(device));
  }
}
