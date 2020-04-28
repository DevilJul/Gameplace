package de.gameplace.games.communication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageHelper {

    protected static ReplyKeyboardMarkup getKeyboard(int rows, int cols, String...valuesArray)
            throws TelegramApiException {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        if (valuesArray == null || valuesArray.length != rows * cols) {
            throw new TelegramApiException("Unexpected number of keyboard values");
        }

        keyboardMarkup.setKeyboard(new ArrayList<KeyboardRow>());

        List<String> values = Arrays.asList(valuesArray);

        for (int iterRows = 0; iterRows < rows; iterRows++) {
            keyboardMarkup.getKeyboard().add(new KeyboardRow());
            keyboardMarkup.getKeyboard().get(iterRows).addAll(values.subList(iterRows * cols, iterRows * cols + cols));
        }

        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    protected static SendMessage generateMessage(long chatId, String msg, String...keyboardButtons)
            throws TelegramApiException {
        return new SendMessage()
                    .setChatId(chatId)
                    .setText(msg)
                    .setReplyMarkup(getKeyboard(2, 1, keyboardButtons));

    }

    protected static Predicate<Update> isReplyToMessageEnd(String messageEnd) {
        return upd -> {
          Message reply = upd.getMessage().getReplyToMessage();
          return reply.hasText() && reply.getText().endsWith(messageEnd);
        };
    }

    protected static Predicate<Update> isReplyToMessage(String message) {
        return upd -> {
          Message reply = upd.getMessage().getReplyToMessage();
          return reply.hasText() && reply.getText().equalsIgnoreCase(message);
        };
    }
    
    protected static Predicate<Update> isReplyToBot(String botUserName) {
        return upd -> upd.getMessage().getReplyToMessage().getFrom().getUserName().equalsIgnoreCase(botUserName);
    }

}