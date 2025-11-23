package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import io.github.roilin.crossplatform_updater.dto.UserDeviceRequest;
import io.github.roilin.crossplatform_updater.dto.UserDeviceResponse;

public interface UserDeviceService {
  List<UserDeviceResponse> getAll();

  List<UserDeviceResponse> getAllByUsername(String username);

  UserDeviceResponse getById(Long id);

  UserDeviceResponse create(UserDeviceRequest device);

  UserDeviceResponse update(UserDeviceRequest device, Long id);

  void deleteById(Long id);
}
