package io.github.roilin.crossplatform_updater.services.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import io.github.roilin.crossplatform_updater.config.FileStorageProperties;
import io.github.roilin.crossplatform_updater.exception.FileNotFoundException;
import io.github.roilin.crossplatform_updater.exception.FileStorageException;
import io.github.roilin.crossplatform_updater.services.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {

  private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

  private final Path fileStorageLocation;
  private static final String[] ALLOWED_EXTENSIONS = { "jpg", "jpeg", "png", "gif", "webp" };

  public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
    this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
        .toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileStorageLocation);
      logger.info("File storage directory initialized at: {}", this.fileStorageLocation);
    } catch (Exception ex) {
      logger.error("Could not create the directory with the path: {}", this.fileStorageLocation);
      throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  public String storeFile(MultipartFile file) {
    logger.debug("Attempting to store file: originalName={}", file.getOriginalFilename());

    if (file.isEmpty()) {
      logger.warn("Rejected empty file upload attempt");
      throw new FileStorageException("Cannot store empty file");
    }

    String cleanName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    String safeName = UUID.randomUUID() + "-" +
        cleanName.replaceAll("[^a-zA-Z0-9.-]", "_");

    String extension = safeName.substring(safeName.lastIndexOf('.') + 1).toLowerCase();
    if (!Set.of(ALLOWED_EXTENSIONS).contains(extension)) {
      logger.warn("Rejected file with unsupported extension: {}", extension);
      throw new FileStorageException("Unsupported file type");
    }

    if (safeName.contains("..") || safeName.contains("/")) {
      logger.warn("Rejected file with invalid path sequence: {}", safeName);
      throw new FileStorageException("Sorry! Filename contains invalid path sequence " + safeName);
    }

    try {
      Path targetLocation = this.fileStorageLocation.resolve(safeName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      logger.info("Successfully stored file: safeName={}, originalName={}", safeName, cleanName);
      return safeName;
    } catch (IOException ex) {
      logger.error("Failed to store file: safeName={}", safeName);
      throw new FileStorageException("Could not store file " + safeName + ". Please try again!", ex);
    }
  }

  public Resource loadFileAsResource(String fileName) {
    logger.debug("Loading file resource: {}", fileName);
    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        logger.debug("File found: {}", fileName);
        return resource;
      } else {
        logger.warn("File not found in storage: {}", fileName);
        throw new FileNotFoundException("File not found " + fileName);
      }
    } catch (MalformedURLException ex) {
      logger.error("Invalid file path format: {}", fileName);
      throw new FileNotFoundException("File not found " + fileName, ex);
    }
  }
}
