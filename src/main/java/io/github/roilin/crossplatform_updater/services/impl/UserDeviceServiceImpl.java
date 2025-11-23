package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.UserDeviceRequest;
import io.github.roilin.crossplatform_updater.dto.UserDeviceResponse;
import io.github.roilin.crossplatform_updater.exception.ResourceNotFoundException;
import io.github.roilin.crossplatform_updater.mapper.UserDeviceMapper;
import io.github.roilin.crossplatform_updater.models.UserDevice;
import io.github.roilin.crossplatform_updater.models.user.User;
import io.github.roilin.crossplatform_updater.repositories.UserDeviceRepository;
import io.github.roilin.crossplatform_updater.repositories.UserRepository;
import io.github.roilin.crossplatform_updater.services.AuthenticationService;
import io.github.roilin.crossplatform_updater.services.UserDeviceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDeviceServiceImpl implements UserDeviceService {

  private final UserDeviceRepository userDeviceRepository;
  private final UserRepository userRepository;
  private final AuthenticationService authService;

  @Override
  public List<UserDeviceResponse> getAll() {
    User user = authService.getAuthenticatedUser();
    return userDeviceRepository.findAllByUser(user).stream().map(UserDeviceMapper::toResponseDto).collect(Collectors.toList());
  }

  @Override
  public List<UserDeviceResponse> getAllByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    return userDeviceRepository.findAllByUser(user).stream().map(UserDeviceMapper::toResponseDto).collect(Collectors.toList());
  }

  @Override
  public UserDeviceResponse getById(Long id) {
    return UserDeviceMapper.toResponseDto(userDeviceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Device", "id", id.toString())));
  }

  @Override
  public UserDeviceResponse create(UserDeviceRequest deviceDto) {
    User user = authService.getAuthenticatedUser();

    UserDevice device = UserDeviceMapper.toEntity(deviceDto);
    device.setUser(user);
    return UserDeviceMapper.toResponseDto(userDeviceRepository.save(device));
  }

  @Override
  public UserDeviceResponse update(UserDeviceRequest deviceDto, Long id) {
    UserDevice device = userDeviceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User device", "id", id.toString()));

    User user = authService.getAuthenticatedUser();

    device.setName(deviceDto.getName());
    device.setPlatform(deviceDto.getPlatform());
    device.setLastSeen(LocalDateTime.now());
    device.setUser(user);
    return UserDeviceMapper.toResponseDto(userDeviceRepository.save(device));
  }

  @Override
  public void deleteById(Long id) {
    userDeviceRepository.deleteById(id);
  }
}
