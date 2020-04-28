package de.gameplace.games.communication;

import java.util.ArrayList;
import java.util.Optional;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public abstract class TelegramBot extends TelegramLongPollingBot {

    protected ReplyKeyboardMarkup getKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(new ArrayList<KeyboardRow>());
        keyboardMarkup.getKeyboard().add(new KeyboardRow());
        keyboardMarkup.getKeyboard().get(0).add("text1");
        keyboardMarkup.getKeyboard().get(0).add("text2");
        keyboardMarkup.getKeyboard().add(new KeyboardRow());
        keyboardMarkup.getKeyboard().get(1).add("text3");
        keyboardMarkup.getKeyboard().get(1).add("text4");
        return keyboardMarkup;
    }

    protected boolean isBotCommand(Update update) {
        return update.hasMessage() && update.getMessage().hasEntities() && update.getMessage().getEntities().get(0).getType().equalsIgnoreCase("bot_command"); // TODO
    }

    protected Optional<String> getBotCommand(Update update) {
        if (isBotCommand(update)) {
            return Optional.of(update.getMessage().getEntities().get(0).getText());
        } else {
            return Optional.empty();
        }
    }

    protected boolean isReply(Update update) {
        return update.hasMessage() && update.getMessage().isReply();
    }


}