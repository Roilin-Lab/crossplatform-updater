package io.github.roilin.crossplatform_updater.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.Platform;
import io.github.roilin.crossplatform_updater.services.AppVersionService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppVersionsController {

  private final AppVersionService appVersionService;

  @GetMapping("/versions")
  public Iterable<AppVersion> allVersions(@RequestParam(required = false) Platform platform) {
    return appVersionService.findAll();
  }

  @GetMapping("/versions/latest")
  public ResponseEntity<AppVersion> getLatestVersion(@RequestParam Platform platform) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(this.appVersionService.findLatesByPlatform(platform));
  }

  @PostMapping("/versions")
  public ResponseEntity<AppVersion> createVersion(@RequestBody AppVersion appVersion) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(this.appVersionService.save(appVersion));
  }
}
