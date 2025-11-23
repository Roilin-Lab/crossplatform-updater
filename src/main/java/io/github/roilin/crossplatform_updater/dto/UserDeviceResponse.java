package io.github.roilin.crossplatform_updater.dto;

import java.util.Set;

import io.github.roilin.crossplatform_updater.models.enums.Platform;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDeviceResponse {
  private Long id;

  private String name;

  private Platform platform;

  private String owner;

  private Set<AppVersionDto> installedApps;
}
