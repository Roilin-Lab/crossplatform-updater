package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.UserDeviceDto;
import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.UserDevice;
import io.github.roilin.crossplatform_updater.models.user.User;
import io.github.roilin.crossplatform_updater.repositories.UserDeviceRepository;
import io.github.roilin.crossplatform_updater.repositories.UserRepository;
import io.github.roilin.crossplatform_updater.services.AppVersionService;
import io.github.roilin.crossplatform_updater.services.UserDeviceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDeviceServiceImpl implements UserDeviceService {

  private final UserDeviceRepository userDeviceRepository;
  private final UserRepository userRepository;
  private final AppVersionService appVersionService;

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
  public UserDevice create(UserDeviceDto deviceDto) {
    User user = userRepository.findByUsername(deviceDto.getOwnerUsername()).orElseThrow();
    
    AppVersion version;
    if (deviceDto.getVersion() != null) {
      version = appVersionService.getByVersionAndPlatform(deviceDto.getVersion(), deviceDto.getPlatform());
    } else {
      version = appVersionService.getLatesByPlatform(deviceDto.getPlatform());
    }

    UserDevice device = new UserDevice();
    device.setName(deviceDto.getName());
    device.setPlatform(deviceDto.getPlatform());
    device.setLastSeen(LocalDateTime.now());
    device.setVersion(version);
    device.setUser(user);
    return userDeviceRepository.save(device);
  }

  @Override
  public void deleteById(Long id) {
    userDeviceRepository.deleteById(id);
  }
}
