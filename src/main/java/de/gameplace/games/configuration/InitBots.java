package de.gameplace.games.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import de.gameplace.games.communication.AbilityBotTest;
import de.gameplace.games.communication.WizardBot;

@Configuration
public class InitBots {

    @Bean
    WizardBot wizardBot() {
        System.out.println("Init wizardBot");

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            //botsApi.registerBot(new WizardBot());
            botsApi.registerBot(new AbilityBotTest());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

        return null;
    }
    

}