package io.github.roilin.crossplatform_updater.dto;

import java.io.Serializable;
import java.util.Set;

public record LoginResponse(
    Long id,
    String username,
    String password,
    String role,
    Set<String> permissions
) implements Serializable {}
