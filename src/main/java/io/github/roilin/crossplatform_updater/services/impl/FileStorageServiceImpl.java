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

  private final Path fileStorageLocation;
  private static final String[] ALLOWED_EXTENSIONS = { "jpg", "jpeg", "png", "gif", "webp" };

  public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
    this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
        .toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  public String storeFile(MultipartFile file) {
    if (file.isEmpty()) {
      throw new FileStorageException("Cannot store empty file");
    }

    String cleanName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    String safeName = UUID.randomUUID() + "-" +
        cleanName.replaceAll("[^a-zA-Z0-9.-]", "_");

    String extension = safeName.substring(safeName.lastIndexOf('.') + 1).toLowerCase();
    if (!Set.of(ALLOWED_EXTENSIONS).contains(extension)) {
      throw new FileStorageException("Unsupported file type");
    }

    if (safeName.contains("..") || safeName.contains("/")) {
      throw new FileStorageException("Sorry! Filename contains invalid path sequence " + safeName);
    }

    try {
      Path targetLocation = this.fileStorageLocation.resolve(safeName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      return safeName;
    } catch (IOException ex) {
      throw new FileStorageException("Could not store file " + safeName + ". Please try again!", ex);
    }
  }

  public Resource loadFileAsResource(String fileName) {
    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new FileNotFoundException("File not found " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new FileNotFoundException("File not found " + fileName, ex);
    }
  }
}
