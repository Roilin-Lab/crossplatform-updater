package io.github.roilin.crossplatform_updater.dto;

import io.github.roilin.crossplatform_updater.models.enums.Platform;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDeviceDto {

  @NotEmpty
  private String name;
  
  @NotEmpty
  private Platform platform;

  @NotEmpty
  private String ownerUsername;
  
  private String version;
}
