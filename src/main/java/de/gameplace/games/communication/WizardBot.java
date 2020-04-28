package de.gameplace.games.communication;

import org.telegram.telegrambots.meta.api.objects.Update;

public class WizardBot extends TelegramBot {

    @Override
    public String getBotUsername() {
        return "Julestest"; // TODO
    }

    @Override
    public String getBotToken() {
        return ""; // TODO
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (isBotCommand(update)) {
            
        } else if (isReply(update)) {

        } else {
            // text
        }
        /*
        if (update.hasMessage()) {

            if (update.getMessage().hasEntities() && update.getMessage()
                
            }

            if (update.getMessage().hasText()) {

                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText("Text: " + update.getMessage().getText() + ", isReply: " + update.getMessage().isReply());
                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
            if (update.getMessage().hasEntities()) {
                for (MessageEntity entity : update.getMessage().getEntities()) {
                    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText("Entity1: " + entity.getType() + ", text: 0" + entity.getText())
                        .setReplyMarkup(getKeyboard());
                    try {
                        execute(message); // Call method to send the message
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        }
        */
        
    }

}