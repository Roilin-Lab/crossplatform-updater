package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.roilin.crossplatform_updater.dto.AppInstallRequest;
import io.github.roilin.crossplatform_updater.dto.AppInstallResponse;
import io.github.roilin.crossplatform_updater.dto.AppVersionUpdateInfoDto;
import io.github.roilin.crossplatform_updater.dto.CheckUpdatesResponse;
import io.github.roilin.crossplatform_updater.dto.UserDeviceRequest;
import io.github.roilin.crossplatform_updater.dto.UserDeviceResponse;
import io.github.roilin.crossplatform_updater.exception.ResourceNotFoundException;
import io.github.roilin.crossplatform_updater.mapper.UserDeviceMapper;
import io.github.roilin.crossplatform_updater.models.AppVersion;
import io.github.roilin.crossplatform_updater.models.Application;
import io.github.roilin.crossplatform_updater.models.UserDevice;
import io.github.roilin.crossplatform_updater.models.user.User;
import io.github.roilin.crossplatform_updater.repositories.UserDeviceRepository;
import io.github.roilin.crossplatform_updater.repositories.UserRepository;
import io.github.roilin.crossplatform_updater.services.AppVersionService;
import io.github.roilin.crossplatform_updater.services.ApplicationService;
import io.github.roilin.crossplatform_updater.services.AuthenticationService;
import io.github.roilin.crossplatform_updater.services.UserDeviceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDeviceServiceImpl implements UserDeviceService {

  private final UserDeviceRepository userDeviceRepository;
  private final UserRepository userRepository;
  private final AuthenticationService authService;
  private final ApplicationService applicationService;
  private final AppVersionService appVersionService;

  @Override
  public UserDevice findById(Long id) {
    return userDeviceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Device", "id", id.toString()));
  }

  @Override
  public List<UserDeviceResponse> getAll() {
    User user = authService.getAuthenticatedUser();
    return userDeviceRepository.findAllByUser(user).stream().map(UserDeviceMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserDeviceResponse> getAllByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    return userDeviceRepository.findAllByUser(user).stream().map(UserDeviceMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  public UserDeviceResponse getById(Long id) {
    return UserDeviceMapper.toResponseDto(findById(id));
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
    UserDevice device = findById(id);

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

  @Override
  public AppInstallResponse installApp(AppInstallRequest request, Long id) {
    UserDevice device = findById(id);

    if (!applicationService.isPlatformSupported(request.appId(), device.getPlatform())) {
      return new AppInstallResponse("failure", "This app does not support your platform.");
    }

    AppVersion version = appVersionService.findLatest(request.appId(), device.getPlatform());

    if (device.getInstalledApps().contains(version)) {
      return new AppInstallResponse("attention", "The application is already installed.");
    }

    device.addApplication(version);
    userDeviceRepository.save(device);

    return new AppInstallResponse("success",
        String.format("The %s app has been installed successfully.", version.getApplication().getTitle()));
  }

  @Override
  public AppInstallResponse updateApp(AppInstallRequest request, Long id) {
    UserDevice device = findById(id);

    AppVersion installedVersion = device.getInstalledAppVersion(request.appId()).orElseThrow(
        () -> new RuntimeException(String.format("The requested application is not installed: %s.", request.appId())));
    AppVersion latestVersion = appVersionService.findLatest(request.appId(), device.getPlatform());

    if (installedVersion == latestVersion) {
      return new AppInstallResponse("attention", "The latest version is already installed.");
    }

    device.removeApplication(installedVersion);
    device.addApplication(latestVersion);
    userDeviceRepository.save(device);

    return new AppInstallResponse("success",
        String.format("The %s app has been updated successfully.", latestVersion.getApplication().getTitle()));
  }

  @Override
  public CheckUpdatesResponse checkAppUpdates(Long id) {
    UserDevice device = findById(id);
    Set<AppVersionUpdateInfoDto> apps = getApplicationsForUpdates(device);

    return new CheckUpdatesResponse(apps);
  }

  public Set<AppVersionUpdateInfoDto> getApplicationsForUpdates(UserDevice device) {
    Set<AppVersionUpdateInfoDto> appsForUpdates = new HashSet<>();
    for (AppVersion installedVersion : device.getInstalledApps()) {
      Application app = installedVersion.getApplication();
      AppVersion latestVersion = appVersionService.findLatest(app.getId(), device.getPlatform());

      if (latestVersion != installedVersion) {
        appsForUpdates.add(new AppVersionUpdateInfoDto(app.getId(), app.getTitle(), installedVersion.getVersion(),
            latestVersion.getVersion()));
      }
    }
    return appsForUpdates;
  }

  @Override
  public AppInstallResponse uninstallApp(AppInstallRequest request, Long id) {
    UserDevice device = findById(id);

    AppVersion installedVersion = device.getInstalledAppVersion(request.appId()).orElseThrow(
        () -> new RuntimeException(String.format("The requested application is not installed: %s.", request.appId())));

    device.removeApplication(installedVersion);
    userDeviceRepository.save(device);

    return new AppInstallResponse("success",
        String.format("The %s app has been uninstalled successfully.", installedVersion.getApplication().getTitle()));
  }
}
