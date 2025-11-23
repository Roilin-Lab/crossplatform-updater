package io.github.roilin.crossplatform_updater.dto;

import io.github.roilin.crossplatform_updater.models.enums.Platform;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDeviceRequest {
  @NotEmpty
  private String name;
  
  @NotEmpty
  private Platform platform;
}
