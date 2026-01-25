package io.github.roilin.crossplatform_updater.services;

public interface TelegramBotService {
  void sendMessage(String chatId, String text);

  void sendToAdmin(String text);
}
