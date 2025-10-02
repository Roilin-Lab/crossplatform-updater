package io.github.roilin.crossplatform_updater.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.dto.AppVersionRequest;
import io.github.roilin.crossplatform_updater.dto.AppVersionResponse;
import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.services.AppVersionService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppVersionsController {

  private final AppVersionService appVersionService;

  @GetMapping("/versions")
  public Iterable<AppVersionResponse> allVersions(@RequestParam(required = false) Platform platform) {
    return appVersionService.getAll(platform);
  }

  @GetMapping("/versions/latest")
  public ResponseEntity<AppVersionResponse> getLatestVersion(@RequestParam Platform platform) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(this.appVersionService.getLatesByPlatform(platform));
  }

  @PostMapping("/versions")
  public ResponseEntity<AppVersionResponse> createVersion(@RequestBody AppVersionRequest appVersion) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(this.appVersionService.create(appVersion));
  }

  @PutMapping("versions/{id}")
  public ResponseEntity<AppVersionResponse> updateVersion(@PathVariable Integer id, @RequestBody AppVersionRequest appVersion) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(appVersionService.update(appVersion, id));
  }

  @DeleteMapping("versions/{id}")
  public ResponseEntity<String> deleteVersion(@PathVariable Integer id) {
    appVersionService.deleteById(id);
    return ResponseEntity.status(HttpStatus.OK).body("App version deleted successfully.");
  }
}
