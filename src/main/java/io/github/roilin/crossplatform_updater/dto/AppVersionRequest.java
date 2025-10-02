package io.github.roilin.crossplatform_updater.dto;

import java.time.LocalDateTime;

import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.models.enums.UpdateType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AppVersionRequest {

  @NotEmpty
  @Pattern(regexp = "^\\d+\\.\\d+\\.\\d+$")
  private String version;

  @NotEmpty
  private Platform platform;

  @NotEmpty
  private LocalDateTime releaseDate;

  @NotNull
  private String changeLog;

  @NotEmpty
  private boolean isActive;

  @NotEmpty
  private UpdateType updateType;
}
