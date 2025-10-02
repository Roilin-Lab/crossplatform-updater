package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.UserDeviceRequest;
import io.github.roilin.crossplatform_updater.dto.UserDeviceResponse;
import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.UserDevice;
import io.github.roilin.crossplatform_updater.models.user.User;
import io.github.roilin.crossplatform_updater.repositories.AppVersionRepository;
import io.github.roilin.crossplatform_updater.repositories.UserDeviceRepository;
import io.github.roilin.crossplatform_updater.repositories.UserRepository;
import io.github.roilin.crossplatform_updater.services.UserDeviceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDeviceServiceImpl implements UserDeviceService {

  private final UserDeviceRepository userDeviceRepository;
  private final UserRepository userRepository;
  private final AppVersionRepository appVersionRepository;

  @Override
  public List<UserDeviceResponse> getAllByUser(User user) {
    return userDeviceRepository.findAllByUser(user).stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public List<UserDeviceResponse> getAllByUsername(String username) {
    User user = userRepository.findByUsername(username).orElseThrow();
    return userDeviceRepository.findAllByUser(user).stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  public UserDeviceResponse getById(Long id) {
    return toDto(userDeviceRepository.findById(id).orElse(null));
  }

  @Override
  public UserDeviceResponse create(UserDeviceRequest deviceDto) {
    User user = userRepository.findByUsername(deviceDto.getOwnerUsername()).orElseThrow();

    AppVersion version = appVersionRepository
        .findFirstByPlatformAndIsActiveTrueOrderByReleaseDateDesc(deviceDto.getPlatform());

    UserDevice device = toEntity(deviceDto);
    device.setVersion(version);
    device.setUser(user);
    return toDto(userDeviceRepository.save(device));
  }

  @Override
  public UserDeviceResponse update(UserDeviceRequest deviceDto, Long id) {
    UserDevice device = userDeviceRepository.findById(id).orElse(null);

    User user = userRepository.findByUsername(deviceDto.getOwnerUsername()).orElseThrow();

    AppVersion version = appVersionRepository
        .findFirstByPlatformAndIsActiveTrueOrderByReleaseDateDesc(deviceDto.getPlatform());

    device.setName(deviceDto.getName());
    device.setPlatform(deviceDto.getPlatform());
    device.setLastSeen(LocalDateTime.now());
    device.setVersion(version);
    device.setUser(user);
    return toDto(userDeviceRepository.save(device));
  }

  @Override
  public void deleteById(Long id) {
    userDeviceRepository.deleteById(id);
  }

  private UserDeviceResponse toDto(UserDevice entity) {
    return new UserDeviceResponse(
        entity.getName(),
        entity.getPlatform(),
        entity.getUser().getUsername(),
        entity.getVersion().getVersion());
  }

  private UserDevice toEntity(UserDeviceRequest dto) {
    UserDevice device = new UserDevice();
    device.setName(dto.getName());
    device.setPlatform(dto.getPlatform());
    device.setLastSeen(LocalDateTime.now());
    return device;
  }
}
