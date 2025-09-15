package io.github.roilin.crossplatform_updater.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppVersion {
  private String version;
  private LocalDateTime releaseDate;
  private String changeLog;
  private boolean isActive; 
  private UpdateType updateType;
  private Platform platform; 
}
