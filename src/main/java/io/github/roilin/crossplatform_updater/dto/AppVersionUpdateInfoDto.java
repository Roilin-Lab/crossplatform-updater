package io.github.roilin.crossplatform_updater.dto;

public record AppVersionUpdateInfoDto(
  Long appId,
  String appTitle,
  String currentVersion,
  String availableVersion
) {}
