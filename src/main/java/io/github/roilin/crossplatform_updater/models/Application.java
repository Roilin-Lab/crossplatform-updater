package io.github.roilin.crossplatform_updater.models;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.roilin.crossplatform_updater.models.enums.ApplicationType;
import io.github.roilin.crossplatform_updater.models.enums.Platform;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class Application {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String title;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ApplicationType type;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "application_supported_platforms", joinColumns = @JoinColumn(name = "application_id"))
  private Set<Platform> supportedPlatform;

  private String developer;
  private String publisher;
  private LocalDateTime lastUpdate;
  private LocalDateTime releaseDate;

  @OneToMany(mappedBy = "application")
  @JsonIgnore
  private Set<AppVersion> versions;
}
