package io.github.roilin.crossplatform_updater.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.models.enums.UpdateType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_versions", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "version", "platform", "application_id" }) })
public class AppVersion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String version;

  @Enumerated(EnumType.STRING)
  private Platform platform;

  @ManyToOne()
  @JoinColumn(name = "application_id")
  private Application application;

  @ManyToMany(mappedBy = "installedApps")
  @JsonIgnore
  private Set<UserDevice> devices = new HashSet<>();

  private LocalDateTime releaseDate;
  private String changeLog;
  private boolean isActive;

  @Enumerated(EnumType.STRING)
  private UpdateType updateType;
}
