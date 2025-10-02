package io.github.roilin.crossplatform_updater.dto;

import java.time.LocalDateTime;

import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.models.enums.UpdateType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppVersionResponse {
  private Integer id;

  private String version;

  private Platform platform;

  private LocalDateTime releaseDate;

  private String changeLog;

  private boolean isActive;

  private UpdateType updateType;
}
