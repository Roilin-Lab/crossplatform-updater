package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import io.github.roilin.crossplatform_updater.dto.UserDeviceRequest;
import io.github.roilin.crossplatform_updater.dto.UserDeviceResponse;
import io.github.roilin.crossplatform_updater.models.user.User;

public interface UserDeviceService {
  List<UserDeviceResponse> getAllByUser(User user);

  List<UserDeviceResponse> getAllByUsername(String username);

  UserDeviceResponse getById(Long id);

  UserDeviceResponse create(UserDeviceRequest device);

  void deleteById(Long id);
}
