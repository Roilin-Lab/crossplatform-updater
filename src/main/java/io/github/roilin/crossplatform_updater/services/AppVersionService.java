package io.github.roilin.crossplatform_updater.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.roilin.crossplatform_updater.dto.AppVersionRequest;
import io.github.roilin.crossplatform_updater.dto.AppVersionResponse;
import io.github.roilin.crossplatform_updater.models.enums.Platform;

public interface AppVersionService {
  List<AppVersionResponse> getAll(Platform platform);

  AppVersionResponse getById(Integer id);

  AppVersionResponse getLatesByPlatform(Platform platform);

  AppVersionResponse getByVersionAndPlatform(String version, Platform platform);

  AppVersionResponse create(AppVersionRequest version);

  AppVersionResponse update(AppVersionRequest version, Integer id);

  void deleteById(Integer id);

  Page<AppVersionResponse> getByRangeDate(LocalDateTime max, LocalDateTime min, Pageable pageable);
}
