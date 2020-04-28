package de.gameplace.games.communication;

import java.util.function.Predicate;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AbilityBotTest extends AbilityBot {

    public AbilityBotTest() {
        super("", "Julestest_bot");
        // TODO Auto-generated constructor stub
    }

    @Override
    public int creatorId() {
        // TODO Auto-generated method stub
        return 1116603902;
    }

    public Ability sayHelloWorld() {
        return Ability
                  .builder()
                  .name("hello")
                  .info("says hello world!")
                  .locality(Locality.ALL)
                  .privacy(Privacy.PUBLIC)
                  .action(ctx -> silent.send("Hello world!", ctx.chatId()))
                  .build();
    }

    public Ability playWithMe() {
        String msg = "play with me!";
        return Ability
                    .builder()
                    .name("play")
                    .info("playing?")
                    .privacy(Privacy.PUBLIC)
                    .locality(Locality.ALL)
                    .input(0)
                    .action(ctx -> silent.forceReply(msg, ctx.chatId()))
                    .reply(upd-> {
                        System.out.println("reply!");
                        silent.send("Nice playing with you", upd.getMessage().getChatId());
                        },
                        Flag.MESSAGE,
                        Flag.REPLY,
                        isReplyToBot(),
                        isReplyToMessage(msg))
                    .build();
                    
    }

    private Predicate<Update> isReplyToMessage(String message) {
        return upd -> {
          Message reply = upd.getMessage().getReplyToMessage();
          return reply.hasText() && reply.getText().equalsIgnoreCase(message);
        };
      }
    
      private Predicate<Update> isReplyToBot() {
        return upd -> upd.getMessage().getReplyToMessage().getFrom().getUserName().equalsIgnoreCase(getBotUsername());
      }


}