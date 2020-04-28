package de.gameplace.games.communication;

import static de.gameplace.games.communication.MessageHelper.getKeyboard;
import static de.gameplace.games.communication.MessageHelper.isReplyToBot;
import static de.gameplace.games.communication.MessageHelper.isReplyToMessageEnd;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.model.Player;
import de.gameplace.games.service.GameManagerService;

@Component
public class WizardBot extends AbilityBot {

    @Autowired
    private GameManagerService gameManagerService;

    private static String MSG_INIT = " möchte ein neues Spiel starten. Bis du dabei?";

    /*
    @Value("${game.command.init})")
    private static String COMMAND_INIT;

    @Value("${game.command.join})")
    private static String COMMAND_JOIN;

    @Value("${game.command.start})")
    private static String COMMAND_START;

    @Value("${game.wizard.command.play.placeBet})")
    private static String COMMAND_PLACE_BET;

    @Value("${game.wizard.command.play.playCard})")
    private static String COMMAND_PLAY_CARD;

    @Value("${game.command.play.okay})")
    private static String COMMAND_PLAY_OKAY;
*/

    public WizardBot() {
        super("", "");
    }

    @Override
    public int creatorId() {
        return -1;
    }

    public Ability initGameCommand() {
        return Ability.builder()
                .name("init")
                .info("Du willst ein neues Spiel beginnen!")
                .locality(Locality.GROUP)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> executeInitGameCommand(ctx))
                .reply(
                    upd -> executeJoinGameReply(upd),
                    Flag.MESSAGE,
                    Flag.REPLY,
                    isReplyToBot(getBotUsername()),
                    isReplyToMessageEnd(MSG_INIT))
                .build();
    }

    public Ability joinGameCommand() {
        return Ability.builder()
                      .name("join")
                      .info("Nehme an am Spiel teil")
                      .locality(Locality.GROUP)
                      .privacy(Privacy.PUBLIC)
                      .action(ctx -> executeJoinGameCommand(ctx.update()))
                      .build();
    }

    public Ability startGameCommand() {
        return Ability.builder()
                      .name("start")
                      .info("Starte das Spiel")
                      .locality(Locality.GROUP)
                      .privacy(Privacy.PUBLIC)
                      .action(ctx -> executeStartGameCommand(ctx))
                      .build();
    }

    private void executeInitGameCommand(MessageContext ctx) {
        SendMessage message = new SendMessage()
                                    .setChatId(ctx.chatId());
        try {
            gameManagerService.initNewGame(String.valueOf(ctx.chatId()), "wizard", String.valueOf(ctx.user().getId()), ctx.user().getFirstName());
            message.setText(ctx.user().getFirstName() + MSG_INIT);
            message.setReplyMarkup(getKeyboard(2, 1, "ja", "nein"));

        } catch (GameException | TelegramApiException e) {
            message.setText("ERROR: Neues Spiel kann nicht erzeugt werden. Fehler=" + e.getMessage());
        }
        
        executeMessage(message);
    }

    private void executeJoinGameReply(Update update) {
        if (update.hasMessage() && update.getMessage().getText().equals("ja")) {
            executeJoinGameCommand(update);
        }
    }

    private void executeJoinGameCommand(Update update) {
        SendMessage message = new SendMessage()
                                    .setChatId(update.getMessage().getChatId());
        try {
            gameManagerService.joinGame(String.valueOf(update.getMessage().getChatId()),
                                        String.valueOf(update.getMessage().getFrom().getId()),
                                        update.getMessage().getFrom().getFirstName());
            
            message.setText("Wuhuu, " + update.getMessage().getFrom().getFirstName() + " ist mit dabei! Gleich geht es los.<br>Wenn du das Spiel initiert hast, " +
                            "sende /start um das Spiel zu starten, sobald genug Spieler zugesagt haben.");
        } catch (GameException e) {
            message.setText("Du kannst dem Spiel nicht beitreten. Fehler=" + e.getMessage());
        }

        executeMessage(message);
    }

    private void executeStartGameCommand(MessageContext ctx) {
        SendMessage message = new SendMessage()
                                    .setChatId(ctx.chatId());

        try {
            gameManagerService.startGame(String.valueOf(ctx.chatId()), String.valueOf(ctx.user().getId()));
            message.setText("Und los gehts mit dem Spiel! Alles weitere im persoenlichen Chats mit mir, dem Wizard-Bot.");
            executeMessage(message);

            sendStartMsgToPlayers(String.valueOf(ctx.chatId()));

        } catch (GameException e) {
            message.setText("Spiel kann nicht gestartet werden. Fehler=" + e.getMessage());
            executeMessage(message);
        }
        
    }

    private void sendStartMsgToPlayers(String gameId) throws GameException {
        List<Player> players = gameManagerService.getGameManager(gameId).getGame().getPlayers();
        String playerNames = players.stream().map(Player::getName).collect(Collectors.joining(" & "));
        
        for (Player player : players) {
            SendMessage msg = new SendMessage()
                                .setChatId(player.getId())
                                .setText("Hallo bei deinem Wizard Spiel! Mit dabei sind: " + playerNames);
            executeMessage(msg);
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace(); // TODO
        }
    }

}