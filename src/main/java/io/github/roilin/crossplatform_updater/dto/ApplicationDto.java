package io.github.roilin.crossplatform_updater.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import io.github.roilin.crossplatform_updater.models.enums.ApplicationType;
import io.github.roilin.crossplatform_updater.models.enums.Platform;

public record ApplicationDto(
    Long id,
    String title,
    ApplicationType type,
    Set<Platform> supportedPlatform,
    String developer,
    String publisher,
    LocalDateTime lastUpdate,
    LocalDateTime releaseDate,
    Set<AppVersionDto> versions) implements Serializable {}
