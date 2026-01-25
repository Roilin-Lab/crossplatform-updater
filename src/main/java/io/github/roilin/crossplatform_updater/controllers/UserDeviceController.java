package io.github.roilin.crossplatform_updater.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.dto.AppInstallRequest;
import io.github.roilin.crossplatform_updater.dto.AppInstallResponse;
import io.github.roilin.crossplatform_updater.dto.CheckUpdatesResponse;
import io.github.roilin.crossplatform_updater.dto.UserDeviceRequest;
import io.github.roilin.crossplatform_updater.dto.UserDeviceResponse;
import io.github.roilin.crossplatform_updater.services.UserDeviceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserDeviceController {

  private final UserDeviceService userDeviceService;

  @GetMapping("/devices")
  public List<UserDeviceResponse> getAllDevices() {
    return userDeviceService.getAll();
  }

  @PreAuthorize("@userDeviceServiceImpl.isDeviceOwner(#id, authentication)")
  @GetMapping("/devices/{id}")
  public ResponseEntity<UserDeviceResponse> getDevice(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userDeviceService.getById(id));
  }

  @PostMapping("/devices")
  public ResponseEntity<UserDeviceResponse> createDevice(@RequestBody UserDeviceRequest device) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userDeviceService.create(device));
  }

  @PreAuthorize("@userDeviceServiceImpl.isDeviceOwner(#id, authentication)")
  @PutMapping("devices/{id}")
  public ResponseEntity<UserDeviceResponse> updateDevice(
      @PathVariable Long id,
      @RequestBody UserDeviceRequest entity) {
    return ResponseEntity.status(HttpStatus.OK).body(userDeviceService.update(entity, id));
  }

  @PreAuthorize("@userDeviceServiceImpl.isDeviceOwner(#id, authentication)")
  @PatchMapping("devices/{id}/apps/install")
  public ResponseEntity<AppInstallResponse> installApp(
      @PathVariable Long id,
      @RequestBody AppInstallRequest entity) {
    return ResponseEntity.status(HttpStatus.OK).body(userDeviceService.installApp(entity, id));
  }

  @PreAuthorize("@userDeviceServiceImpl.isDeviceOwner(#id, authentication)")
  @PatchMapping("devices/{id}/apps/update")
  public ResponseEntity<AppInstallResponse> updateApp(
      @PathVariable Long id,
      @RequestBody AppInstallRequest entity) {
    return ResponseEntity.status(HttpStatus.OK).body(userDeviceService.updateApp(entity, id));
  }

  @PreAuthorize("@userDeviceServiceImpl.isDeviceOwner(#id, authentication)")
  @DeleteMapping("devices/{id}/apps/uninstall")
  public ResponseEntity<AppInstallResponse> uninstallApp(
      @PathVariable Long id,
      @RequestBody AppInstallRequest entity) {
    return ResponseEntity.status(HttpStatus.OK).body(userDeviceService.uninstallApp(entity, id));
  }

  @PreAuthorize("@userDeviceServiceImpl.isDeviceOwner(#id, authentication)")
  @GetMapping("devices/{id}/apps/updates")
  public ResponseEntity<CheckUpdatesResponse> checkAppUpdates(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userDeviceService.checkAppUpdates(id));
  }

  @PreAuthorize("@userDeviceServiceImpl.isDeviceOwner(#id, authentication)")
  @DeleteMapping("devices/{id}")
  public ResponseEntity<String> deleteDevice(@PathVariable Long id) {
    userDeviceService.deleteById(id);
    return ResponseEntity.status(HttpStatus.OK).body("Device deleted successfully.");
  }
}
