package io.github.roilin.crossplatform_updater.services;

import java.util.List;

import io.github.roilin.crossplatform_updater.dto.UserDeviceDto;
import io.github.roilin.crossplatform_updater.models.UserDevice;
import io.github.roilin.crossplatform_updater.models.user.User;

public interface UserDeviceService {
  List<UserDevice> getAllByUser(User user);

  List<UserDevice> getAllByUsername(String username);

  UserDevice getById(Long id);

  UserDevice create(UserDeviceDto device);

  void deleteById(Long id);
}
