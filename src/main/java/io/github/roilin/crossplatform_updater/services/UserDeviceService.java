package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import io.github.roilin.crossplatform_updater.dto.AppInstallRequest;
import io.github.roilin.crossplatform_updater.dto.AppInstallResponse;
import io.github.roilin.crossplatform_updater.dto.CheckUpdatesResponse;
import io.github.roilin.crossplatform_updater.dto.UserDeviceRequest;
import io.github.roilin.crossplatform_updater.dto.UserDeviceResponse;
import io.github.roilin.crossplatform_updater.models.UserDevice;

public interface UserDeviceService {
  UserDevice findById(Long id);

  List<UserDeviceResponse> getAll();

  List<UserDeviceResponse> getAllByUsername(String username);

  UserDeviceResponse getById(Long id);

  UserDeviceResponse create(UserDeviceRequest device);

  UserDeviceResponse update(UserDeviceRequest device, Long id);

  AppInstallResponse installApp(AppInstallRequest request, Long id);

  AppInstallResponse updateApp(AppInstallRequest request, Long id);

  CheckUpdatesResponse checkAppUpdates(Long id);

  AppInstallResponse uninstallApp(AppInstallRequest request, Long id);

  void deleteById(Long id);
}
