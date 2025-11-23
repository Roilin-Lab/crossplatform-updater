package io.github.roilin.crossplatform_updater.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.dto.UserDeviceRequest;
import io.github.roilin.crossplatform_updater.dto.UserDeviceResponse;
import io.github.roilin.crossplatform_updater.models.UserDevice;
import io.github.roilin.crossplatform_updater.services.UserDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "User device endpoints")
public class UserDeviceController {

  private final UserDeviceService userDeviceService;

  @Operation(summary = "Get all user device")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the user devices") })
  @GetMapping("/devices")
  public List<UserDeviceResponse> getAllDevices() {
    return userDeviceService.getAll();
  }

  @Operation(summary = "Get user device by id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the device"),
      @ApiResponse(responseCode = "404", description = "Device with that id was not found") })
  @GetMapping("/devices/{id}")
  public ResponseEntity<UserDeviceResponse> getDevice(
      @Parameter(description = "Id of device to be searched") @PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userDeviceService.getById(id));
  }

  @Operation(summary = "Create new user device")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Device was created") })
  @PostMapping("/devices")
  public ResponseEntity<UserDeviceResponse> createDevice(@RequestBody UserDeviceRequest device) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userDeviceService.create(device));
  }

  @Operation(summary = "Update user device")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Device was updated"),
      @ApiResponse(responseCode = "404", description = "Device with that id was not found") })
  @PutMapping("devices/{id}")
  public ResponseEntity<UserDeviceResponse> updateDevice(
      @Parameter(description = "Id of device to be updated") @PathVariable Long id,
      @RequestBody UserDeviceRequest entity) {
    return ResponseEntity.status(HttpStatus.OK).body(userDeviceService.update(entity, id));
  }

  @Operation(summary = "Delete device")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Device was deleted"),
      @ApiResponse(responseCode = "404", description = "Device with that id was not found") })
  @DeleteMapping("devices/{id}")
  public ResponseEntity<String> deleteDevice(
      @Parameter(description = "Id of device to be deleted") @PathVariable Long id) {
    userDeviceService.deleteById(id);
    return ResponseEntity.status(HttpStatus.OK).body("Device deleted successfully.");
  }
}
