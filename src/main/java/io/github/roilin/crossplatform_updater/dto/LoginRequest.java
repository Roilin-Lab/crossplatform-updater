package io.github.roilin.crossplatform_updater.dto;

import java.io.Serializable;

public record LoginRequest(
    String username,
    String password
) implements Serializable {}
