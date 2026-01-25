package io.github.roilin.crossplatform_updater.services.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.github.roilin.crossplatform_updater.services.TelegramBotService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HealthCheck {
      private final TelegramBotService botService;

    @Scheduled(cron = "${interval-in-cron}")
    public void sendHealthCheck () {
        botService.sendToAdmin("I am still alive!");
    }
}
