package io.github.roilin.crossplatform_updater.services.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger logger = LoggerFactory.getLogger(UserDeviceServiceImpl.class);

  private final UserDeviceRepository userDeviceRepository;
  private final UserRepository userRepository;
  private final AuthenticationService authService;
  private final ApplicationService applicationService;
  private final AppVersionService appVersionService;

  @Override
  public UserDevice findById(Long id) {
    logger.debug("Fetching user device by ID: {}", id);
    return userDeviceRepository.findById(id)
        .orElseThrow(() -> {
          logger.warn("User device not found with ID: {}", id);
          return new ResourceNotFoundException("Device", "id", id.toString());
        });
  }

  @Override
  public List<UserDeviceResponse> getAll() {
    User user = authService.getAuthenticatedUser();
    logger.debug("Retrieving all devices for authenticated user: {}", user.getUsername());

    List<UserDeviceResponse> devices = userDeviceRepository.findAllByUser(user).stream()
        .map(UserDeviceMapper::toResponseDto)
        .toList();

    logger.debug("Found {} devices for user: {}", devices.size(), user.getUsername());
    return devices;
  }

  @Override
  public List<UserDeviceResponse> getAllByUsername(String username) {
    logger.debug("Fetching devices for user: {}", username);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> {
          logger.warn("User not found with username: {}", username);
          return new ResourceNotFoundException("User", "username", username);
        });

    List<UserDeviceResponse> devices = userDeviceRepository.findAllByUser(user).stream()
        .map(UserDeviceMapper::toResponseDto)
        .toList();

    logger.debug("Found {} devices for user: {}", devices.size(), username);
    return devices;
  }

  @Override
  public UserDeviceResponse getById(Long id) {
    logger.debug("Requesting device details for ID: {}", id);
    UserDeviceResponse response = UserDeviceMapper.toResponseDto(findById(id));
    logger.debug("Successfully retrieved device ID: {}", id);
    return response;
  }

  @Override
  public UserDeviceResponse create(UserDeviceRequest deviceDto) {
    User user = authService.getAuthenticatedUser();
    logger.info("Creating new device for user: {}, platform: {}",
        user.getUsername(), deviceDto.getPlatform());

    UserDevice device = UserDeviceMapper.toEntity(deviceDto);
    device.setUser(user);
    UserDevice saved = userDeviceRepository.save(device);

    logger.info("Created device ID: {} for user: {}", saved.getId(), user.getUsername());
    return UserDeviceMapper.toResponseDto(saved);
  }

  @Override
  public UserDeviceResponse update(UserDeviceRequest deviceDto, Long id) {
    logger.info("Updating device ID: {} with name: {}", id, deviceDto.getName());
    UserDevice device = findById(id);
    User user = authService.getAuthenticatedUser();

    device.setName(deviceDto.getName());
    device.setPlatform(deviceDto.getPlatform());
    device.setLastSeen(LocalDateTime.now());
    device.setUser(user);

    UserDevice updated = userDeviceRepository.save(device);
    logger.info("Successfully updated device ID: {}", updated.getId());
    return UserDeviceMapper.toResponseDto(updated);
  }

  @Override
  public void deleteById(Long id) {
    logger.info("Deleting device ID: {}", id);
    userDeviceRepository.deleteById(id);
    logger.info("Successfully deleted device ID: {}", id);
  }

  @Override
  public AppInstallResponse installApp(AppInstallRequest request, Long id) {
    logger.info("Installing app ID: {} on device ID: {}", request.appId(), id);
    UserDevice device = findById(id);

    if (!applicationService.isPlatformSupported(request.appId(), device.getPlatform())) {
      logger.warn("Installation blocked: App ID {} not supported on device platform {}",
          request.appId(), device.getPlatform());
      return new AppInstallResponse("failure", "This app does not support your platform.");
    }

    AppVersion version = appVersionService.findLatest(request.appId(), device.getPlatform());
    if (version == null) {
      logger.error("Critical: No latest version found for app ID: {} on platform: {}",
          request.appId(), device.getPlatform());
      throw new IllegalStateException("No available version for this platform");
    }

    if (device.getInstalledApps().contains(version)) {
      logger.debug("Installation skipped: App ID {} already installed on device ID: {}",
          request.appId(), id);
      return new AppInstallResponse("attention", "The application is already installed.");
    }

    device.addApplication(version);
    userDeviceRepository.save(device);
    logger.info("Successfully installed app ID: {} ({}) on device ID: {}",
        request.appId(), version.getVersion(), id);

    return new AppInstallResponse("success",
        String.format("The %s app has been installed successfully.", version.getApplication().getTitle()));
  }

  @Override
  public AppInstallResponse updateApp(AppInstallRequest request, Long id) {
    logger.info("Updating app ID: {} on device ID: {}", request.appId(), id);
    UserDevice device = findById(id);

    AppVersion installedVersion = device.getInstalledAppVersion(request.appId())
        .orElseThrow(() -> {
          logger.warn("Update failed: App ID {} not installed on device ID: {}",
              request.appId(), id);
          return new RuntimeException(
              String.format("The requested application is not installed: %s.", request.appId()));
        });

    AppVersion latestVersion = appVersionService.findLatest(request.appId(), device.getPlatform());
    if (latestVersion == null) {
      logger.error("Critical: No latest version found for update of app ID: {} on device ID: {}",
          request.appId(), id);
      throw new IllegalStateException("No available version for update");
    }

    if (installedVersion.equals(latestVersion)) {
      logger.debug("Update skipped: Latest version already installed for app ID: {} on device ID: {}",
          request.appId(), id);
      return new AppInstallResponse("attention", "The latest version is already installed.");
    }

    device.removeApplication(installedVersion);
    device.addApplication(latestVersion);
    userDeviceRepository.save(device);

    logger.info("Successfully updated app ID: {} from v{} to v{} on device ID: {}",
        request.appId(), installedVersion.getVersion(), latestVersion.getVersion(), id);

    return new AppInstallResponse("success",
        String.format("The %s app has been updated successfully.", latestVersion.getApplication().getTitle()));
  }

  @Override
  public CheckUpdatesResponse checkAppUpdates(Long id) {
    logger.debug("Checking updates for device ID: {}", id);

    UserDevice device = findById(id);
    Set<AppVersionUpdateInfoDto> apps = getApplicationsForUpdates(device);

    logger.debug("Found {} apps requiring updates for device ID: {}", apps.size(), id);
    return new CheckUpdatesResponse(apps);
  }

  public Set<AppVersionUpdateInfoDto> getApplicationsForUpdates(UserDevice device) {
    Set<AppVersionUpdateInfoDto> appsForUpdates = new HashSet<>();
    logger.trace("Checking updates for {} installed apps on device ID: {}",
        device.getInstalledApps().size(), device.getId());

    for (AppVersion installedVersion : device.getInstalledApps()) {
      Application app = installedVersion.getApplication();
      AppVersion latestVersion = appVersionService.findLatest(app.getId(), device.getPlatform());

      if (!latestVersion.equals(installedVersion)) {
        appsForUpdates.add(new AppVersionUpdateInfoDto(app.getId(), app.getTitle(), installedVersion.getVersion(),
            latestVersion.getVersion()));
        logger.debug("Update available for app ID: {} on device ID: {} (v{} -> v{})",
            app.getId(), device.getId(), installedVersion.getVersion(), latestVersion.getVersion());
      }
    }
    return appsForUpdates;
  }

  @Override
  public AppInstallResponse uninstallApp(AppInstallRequest request, Long id) {
    logger.info("Uninstalling app ID: {} from device ID: {}", request.appId(), id);
    UserDevice device = findById(id);

    AppVersion installedVersion = device.getInstalledAppVersion(request.appId())
        .orElseThrow(() -> {
          logger.warn("Uninstall failed: App ID {} not installed on device ID: {}",
              request.appId(), id);
          return new RuntimeException(
              String.format("The requested application is not installed: %s.", request.appId()));
        });

    device.removeApplication(installedVersion);
    userDeviceRepository.save(device);

    logger.info("Successfully uninstalled app ID: {} (v{}) from device ID: {}",
        request.appId(), installedVersion.getVersion(), id);

    return new AppInstallResponse("success",
        String.format("The %s app has been uninstalled successfully.", installedVersion.getApplication().getTitle()));
  }
}
