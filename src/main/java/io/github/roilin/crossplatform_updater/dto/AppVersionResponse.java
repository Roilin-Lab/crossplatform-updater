package io.github.roilin.crossplatform_updater.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.github.roilin.crossplatform_updater.models.enums.Platform;
import io.github.roilin.crossplatform_updater.models.enums.UpdateType;

public record AppVersionResponse(
    Long id,
    String version,
    Platform platform,
    LocalDateTime releaseDate,
    ApplicationDto application,
    String changeLog,
    boolean isActive,
    UpdateType updateType) implements Serializable {}
