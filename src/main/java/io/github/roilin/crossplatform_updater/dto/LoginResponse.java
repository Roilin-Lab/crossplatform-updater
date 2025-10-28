package io.github.roilin.crossplatform_updater.dto;

import java.io.Serializable;

public record LoginResponse(
    boolean isLogged,
    String username,
    String role
) implements Serializable {}
