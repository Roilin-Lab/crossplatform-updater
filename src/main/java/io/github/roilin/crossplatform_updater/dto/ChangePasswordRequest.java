package io.github.roilin.crossplatform_updater.dto;

import java.io.Serializable;

public record ChangePasswordRequest(
    String oldPassword,
    String newPassword,
    String repeatedNew
) implements Serializable {}
