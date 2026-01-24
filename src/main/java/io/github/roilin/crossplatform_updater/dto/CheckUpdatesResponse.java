package io.github.roilin.crossplatform_updater.dto;

import java.util.Set;

public record CheckUpdatesResponse(
  Set<AppVersionUpdateInfoDto> applications
) {}
