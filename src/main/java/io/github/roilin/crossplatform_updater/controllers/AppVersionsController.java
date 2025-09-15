package io.github.roilin.crossplatform_updater.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.Platform;
import io.github.roilin.crossplatform_updater.models.UpdateType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppVersionsController {
  private List<AppVersion> versions = new ArrayList<>(Arrays.asList(
      new AppVersion("1.1.3", LocalDateTime.now(), "fix bugs", true, UpdateType.MANDATORY, Platform.ANDRIOD),
      new AppVersion("1.0.4", LocalDateTime.now(), "fix bugs", false, UpdateType.MANDATORY, Platform.ANDRIOD),
      new AppVersion("1.0.1", LocalDateTime.now(), "fix bugs", false, UpdateType.MANDATORY, Platform.ANDRIOD)));

  @GetMapping("/versions")
  public List<AppVersion> allVersions() {
    return versions;
  }
}
