package de.gameplace.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class GamesApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();

		SpringApplication.run(GamesApplication.class, args);
	}

}
