package io.github.roilin.crossplatform_updater.controllers;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.dto.AppVersionRequest;
import io.github.roilin.crossplatform_updater.dto.AppVersionResponse;
import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.services.AppVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppVersionsController {

  private final AppVersionService appVersionService;

  @Operation(summary = "Get all app version")
  @Tag(name = "Various get versions methods")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the app versions") })
  @GetMapping("/versions")
  public Iterable<AppVersionResponse> allVersions(
      @Parameter(description = "Platform to get the versions, not required") @RequestParam(required = false) Platform platform) {
    return appVersionService.getAll(platform);
  }

  @Operation(summary = "Get app version by id")
  @Tag(name = "Various get versions methods")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the app version"),
      @ApiResponse(responseCode = "404", description = "App version with that id was not found") })
  @GetMapping("/versions/{id}")
  public ResponseEntity<AppVersionResponse> getVersions(
      @Parameter(description = "Id of app version to be searched") @PathVariable Integer id) {
    return ResponseEntity.status(HttpStatus.OK).body(appVersionService.getById(id));
  }

  @Operation(summary = "Get latest app version by platform")
  @Tag(name = "Various get versions methods")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the app version") })
  @GetMapping("/versions/latest")
  public ResponseEntity<AppVersionResponse> getLatestVersion(
      @Parameter(description = "Platform to get the latest version") @RequestParam Platform platform) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(this.appVersionService.getLatesByPlatform(platform));
  }

  @Operation(summary = "Get app version by release date range")
  @Tag(name = "Various get versions methods")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the app versions") })
  @GetMapping("/versions/rangeDate")
  public ResponseEntity<Object> getByRangeDate(
      @Parameter(description = "Maximum of date in format UTC YYYY-DD-MM HH:MM:SS") @RequestParam(required = false) LocalDateTime max,
      @Parameter(description = "Minimum of date in format UTC YYYY-DD-MM HH:MM:SS") @RequestParam(required = false) LocalDateTime min,
      @PageableDefault(page = 0, size = 3, sort = "releaseDate") Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(appVersionService.getByRangeDate(max, min, pageable));
  }

  @Operation(summary = "Create new app version")
  @Tag(name = "Another actions with app version")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "App version was created") })
  @PostMapping("/versions")
  public ResponseEntity<AppVersionResponse> createVersion(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(value = "{\"version\": \"1.0.1\", \"platform\": \"IOS\", \"releaseDate\": \"2025-10-06T09:03:34.559Z\", \"changeLog\": \"New features added\", \"updateType\": \"MANDATORY\", \"active\": true}"))) @RequestBody AppVersionRequest appVersion) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(this.appVersionService.create(appVersion));
  }

  @Operation(summary = "Update app version")
  @Tag(name = "Another actions with app version")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "App version was updated"),
      @ApiResponse(responseCode = "404", description = "App version with that id was not found") })
  @PutMapping("versions/{id}")
  public ResponseEntity<AppVersionResponse> updateVersion(
      @Parameter(description = "Id of app version to be updated") @PathVariable Integer id,
      @RequestBody AppVersionRequest appVersion) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(appVersionService.update(appVersion, id));
  }

  @Operation(summary = "Delete app version")
  @Tag(name = "Another actions with app version")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "App version was deleted"),
      @ApiResponse(responseCode = "404", description = "App version with that id was not found") })
  @DeleteMapping("versions/{id}")
  public ResponseEntity<String> deleteVersion(
      @Parameter(description = "Id of app version to be deleted") @PathVariable Integer id) {
    appVersionService.deleteById(id);
    return ResponseEntity.status(HttpStatus.OK).body("App version deleted successfully.");
  }
}
