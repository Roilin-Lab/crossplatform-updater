package io.github.roilin.crossplatform_updater.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.roilin.crossplatform_updater.models.user.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>  {
    Optional<Permission> findByResourceAndOperation(String resource, String operation); 
}
