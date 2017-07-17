package com.deliveryninja;

import com.github.junrar.extract.ExtractArchive;
import com.google.common.annotations.VisibleForTesting;
import org.junit.jupiter.api.Test;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class IrcBooksApplication extends ListenerAdapter {

	private static final String DESTINATION_PATH = "D:\\local\\ebooks\\temp\\";

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

	@Override
	public void onIncomingFileTransfer(IncomingFileTransferEvent event) throws Exception {
		System.out.println("--== SEARCH RESULTS ==--");

		//Generate a file prefix
		String prefix = "pircbotxFile" + System.currentTimeMillis() + "-";

		String suffix = event.getSafeFilename();

		//Create this file in the temp directory
		File file = File.createTempFile(prefix, suffix, new File(DESTINATION_PATH));

		//Receive the file from the user
		event.accept(file).transfer();

		unrarFile(file);
	}

	public void unrarFile(File file){
		final File destination = new File(DESTINATION_PATH);
		ExtractArchive extractArchive = new ExtractArchive();
		extractArchive.extractArchive(file, destination);

		readFile(destination);
	}

	public void readFile(File file){
		List<String> lines = new ArrayList<>();

		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();

			int i = 0;
			while (line != null) {
				lines.add(line);
				System.out.println("RESULT " + i + " " + line);

				i++;
				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
