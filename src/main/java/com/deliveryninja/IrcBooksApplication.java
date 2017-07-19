package com.deliveryninja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootApplication
public class IrcBooksApplication {

    @Value("${temp.download.folder}")
    private String tempDownloadFolder;

    @Value("${download.folder}")
    private String downloadFolder;

    @Value("${bot.name}")
    private String botName;

    @Value("${irc.channel}")
    private String ircChannel;

    @Value("${irc.server}")
    private String ircServer;

    @Autowired
    private IrcClient ircClient;

	public static void main(String[] args) {
		SpringApplication.run(IrcBooksApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
            ircClient.run();
        };
	}

	@Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
