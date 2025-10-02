package io.github.roilin.crossplatform_updater.dto;

import io.github.roilin.crossplatform_updater.models.enums.Platform;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDeviceResponse {
  private String name;

  private Platform platform;

  private String ownerUsername;

  private String currentVersion;
}
