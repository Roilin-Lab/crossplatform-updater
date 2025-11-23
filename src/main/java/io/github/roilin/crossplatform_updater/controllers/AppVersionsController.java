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
import io.github.roilin.crossplatform_updater.services.AppVersionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppVersionsController {

  private final AppVersionService appVersionService;

  @GetMapping("/versions")
  public Iterable<AppVersionResponse> allVersions(@RequestParam() Long applicationId) {
    return appVersionService.getAll(applicationId);
  }

  @GetMapping("/versions/{id}")
  public ResponseEntity<AppVersionResponse> getVersions(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(appVersionService.getById(id));
  }

  @GetMapping("/versions/latest")
  public ResponseEntity<AppVersionResponse> getLatestVersion(@RequestParam Long applicationId) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(this.appVersionService.getLatest(applicationId));
  }

  @GetMapping("/versions/rangeDate")
  public ResponseEntity<Object> getByRangeDate(
      @RequestParam(required = false) LocalDateTime max,
      @RequestParam(required = false) LocalDateTime min,
      @PageableDefault(page = 0, size = 3, sort = "releaseDate") Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(appVersionService.getByRangeDate(max, min, pageable));
  }

  @PostMapping("/versions")
  public ResponseEntity<AppVersionResponse> createVersion(@RequestBody AppVersionRequest appVersion) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(this.appVersionService.create(appVersion));
  }

  @PutMapping("versions/{id}")
  public ResponseEntity<AppVersionResponse> updateVersion(
      @PathVariable Long id,
      @RequestBody AppVersionRequest appVersion) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(appVersionService.update(appVersion, id));
  }

  @DeleteMapping("versions/{id}")
  public ResponseEntity<String> deleteVersion(@PathVariable Long id) {
    appVersionService.deleteById(id);
    return ResponseEntity.status(HttpStatus.OK).body("App version deleted successfully.");
  }
}
