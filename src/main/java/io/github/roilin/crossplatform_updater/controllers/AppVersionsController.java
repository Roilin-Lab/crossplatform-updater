package io.github.roilin.crossplatform_updater.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.Platform;
import io.github.roilin.crossplatform_updater.models.UpdateType;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppVersionsController {
  private List<AppVersion> versions = new ArrayList<>(Arrays.asList(
      new AppVersion("1.1.3", LocalDateTime.of(2025, 8, 1, 0, 0, 0), "fix bugs", true, UpdateType.MANDATORY,
          Platform.IOS),
      new AppVersion("1.0.4", LocalDateTime.of(2025, 7, 21, 0, 0, 0), "fix bugs", false, UpdateType.MANDATORY,
          Platform.IOS),
      new AppVersion("1.0.1", LocalDateTime.of(2025, 9, 11, 0, 0, 0), "fix bugs", false, UpdateType.MANDATORY, Platform.IOS),
      new AppVersion("1.0.3", LocalDateTime.of(2023, 5, 21, 0, 0, 0), "fix bugs", false, UpdateType.MANDATORY, Platform.ANDROID),
      new AppVersion("1.0.4", LocalDateTime.of(2025, 6, 21, 0, 0, 0), "fix bugs", false, UpdateType.MANDATORY, Platform.ANDROID),
      new AppVersion("1.0.5", LocalDateTime.of(2025, 7, 21, 0, 0, 0), "fix bugs", false, UpdateType.MANDATORY, Platform.ANDROID),
      new AppVersion("1.1.1", LocalDateTime.of(2025, 8, 21, 0, 0, 0), "fix bugs", false, UpdateType.MANDATORY, Platform.ANDROID)));

  @GetMapping("/versions")
  public List<AppVersion> allVersions(@RequestParam(required = false) Platform platform) {
    if (platform == null)
      return versions;

    List<AppVersion> filtered = new ArrayList<AppVersion>();
    for (AppVersion appVersion : versions) {
      if (appVersion.getPlatform() == platform) {
        filtered.add(appVersion);
      }
    }
    return filtered;
  }

  @GetMapping("/versions/latest")
  public AppVersion getLatestVersion(@RequestParam Platform platform) {
    List<AppVersion> filtered = new ArrayList<AppVersion>();
    for (AppVersion appVersion : versions) {
      if (appVersion.getPlatform() == platform) {
        filtered.add(appVersion);
      }
    }
    filtered.sort((a, b) -> {
      return b.getReleaseDate().compareTo(a.getReleaseDate());
    });
    return filtered.get(0);
  }

  @PostMapping("/versions")
  public ResponseEntity<AppVersion> createVersion(@RequestBody AppVersion entity) {
    versions.add(entity);
    return ResponseEntity.status(HttpStatus.CREATED).body(entity);
  }
}
