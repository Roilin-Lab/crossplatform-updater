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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserDeviceController {

  private final UserDeviceService userDeviceService;

  @GetMapping("/devices")
  public List<UserDeviceResponse> getAllDevices(@RequestParam String username) {
    return userDeviceService.getAllByUsername(username);
  }

  @GetMapping("/devices/{id}")
  public ResponseEntity<UserDeviceResponse> getDevice(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userDeviceService.getById(id));
  }

  @PostMapping("/devices")
  public ResponseEntity<UserDeviceResponse> createDevice(@RequestBody UserDeviceRequest device) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userDeviceService.create(device));
  }

  @PutMapping("devices/{id}")
  public ResponseEntity<UserDeviceResponse> updateDevice(@PathVariable Long id, @RequestBody UserDeviceRequest entity) {
    return ResponseEntity.status(HttpStatus.OK).body(userDeviceService.update(entity, id));
  }

  @DeleteMapping("devices/{id}")
  public ResponseEntity<String> deleteDevice(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body("Device deleted successfully.");
  }
}
