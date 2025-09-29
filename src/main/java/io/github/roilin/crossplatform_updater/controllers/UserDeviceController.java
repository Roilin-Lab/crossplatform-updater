package io.github.roilin.crossplatform_updater.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.dto.UserDeviceDto;
import io.github.roilin.crossplatform_updater.models.UserDevice;
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
  public List<UserDevice> getAllDevices(@RequestParam String username) {
    List<UserDevice> devices = userDeviceService.getAllByUsername(username);
    return devices;
  }

  @PostMapping("/devices")
  public ResponseEntity<UserDevice> createDevice(@RequestBody UserDeviceDto device) {
    UserDevice newDevice = userDeviceService.create(device);
    return ResponseEntity.status(HttpStatus.CREATED).body(newDevice);
  }
}
