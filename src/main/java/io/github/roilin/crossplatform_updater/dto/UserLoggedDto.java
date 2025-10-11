package io.github.roilin.crossplatform_updater.dto;

import java.io.Serializable;
import java.util.Set;

public record UserLoggedDto(
    String username,
    String role,
    Set<String> permissions
) implements Serializable {}
