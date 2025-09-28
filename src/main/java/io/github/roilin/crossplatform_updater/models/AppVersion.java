package io.github.roilin.crossplatform_updater.models;

import java.time.LocalDateTime;

import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.models.enums.UpdateType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_version", uniqueConstraints = { @UniqueConstraint(columnNames = { "version", "platform" }) })
public class AppVersion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String version;

  @Enumerated(EnumType.STRING)
  private Platform platform;

  private LocalDateTime releaseDate;
  private String changeLog;
  private boolean isActive;

  @Enumerated(EnumType.STRING)
  private UpdateType updateType;
}
