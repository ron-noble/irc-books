package com.deliveryninja;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IrcBooksApplication extends ListenerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(IrcBooksApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("IRC Books");

            Configuration config = new Configuration.Builder()
                    .setName("DeliveryNinja")
                    .setLogin("PircBotXUser")
                    .setAutoNickChange(true)
                    .addAutoJoinChannel("#ebooks")
                    .addServer("irc.irchighway.net", 9999)
                    .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
                    .addListener(this)
                    .buildConfiguration();

            PircBotX myBot = new PircBotX(config);

            ConsoleCommander commander = new ConsoleCommander(myBot);
            myBot.startBot();
		};
	}

    public void onGenericMessage(GenericMessageEvent event) throws Exception {
        System.out.println("IRC : " + event.getMessage());
    }
}
