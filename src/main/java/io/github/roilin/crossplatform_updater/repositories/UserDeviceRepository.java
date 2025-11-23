package io.github.roilin.crossplatform_updater.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.roilin.crossplatform_updater.models.UserDevice;
import io.github.roilin.crossplatform_updater.models.user.User;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
  List<UserDevice> findAllByUser(User user);
}
