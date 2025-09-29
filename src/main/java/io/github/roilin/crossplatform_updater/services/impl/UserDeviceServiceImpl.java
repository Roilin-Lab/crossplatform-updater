package io.github.roilin.crossplatform_updater.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.models.UserDevice;
import io.github.roilin.crossplatform_updater.models.user.User;
import io.github.roilin.crossplatform_updater.repositories.UserDeviceRepository;
import io.github.roilin.crossplatform_updater.repositories.UserRepository;
import io.github.roilin.crossplatform_updater.services.UserDeviceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDeviceServiceImpl implements UserDeviceService {

  private final UserDeviceRepository userDeviceRepository;
  private final UserRepository userRepository;

  @Override
  public List<UserDevice> getAllByUser(User user) {
    return userDeviceRepository.findAllByUser(user);
  }

  @Override
  public List<UserDevice> getAllByUsername(String username) {
    User user = userRepository.findByUsername(username).orElseThrow();
    return userDeviceRepository.findAllByUser(user);
  }

  @Override
  public UserDevice getById(Long id) {
    return userDeviceRepository.findById(id).orElse(null);
  }

  @Override
  public UserDevice create(UserDevice device, String username) {
    User user = userRepository.findByUsername(username).orElseThrow();
    device.setUser(user);
    return userDeviceRepository.save(device);
  }

  @Override
  public void deleteById(Long id) {
    userDeviceRepository.deleteById(id);
  }
}
