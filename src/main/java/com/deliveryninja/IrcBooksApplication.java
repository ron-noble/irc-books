package com.deliveryninja;

import com.github.junrar.extract.ExtractArchive;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class IrcBooksApplication extends ListenerAdapter {

	private static final String DESTINATION_PATH = "C:\\local\\ebooks\\temp\\";
    private static final String EBOOKS_PATH = "C:\\local\\ebooks\\";
    private static final boolean EXTENSIONS_DISABLED = true;

    public static void main(String[] args) {
		SpringApplication.run(IrcBooksApplication.class, args);
	}

	private ConsoleCommander consoleCommander;

	List<String> fileExtensions = Arrays.asList("mobi");

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

            consoleCommander = new ConsoleCommander(myBot);
            consoleCommander.run();
            myBot.startBot();
		};
	}

	@Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        System.out.println("Private Msg: " + event.getMessage());
	}

	@Override
	public void onIncomingFileTransfer(IncomingFileTransferEvent event) throws Exception {
        System.out.println("Downloading DCC Request");

	    final File destination = new File(DESTINATION_PATH);

//        if(destination.isDirectory()){
//            Arrays.stream(destination.listFiles()).forEach(File::delete);
//        }

	    String prefix = "temp"+ "-";
		String suffix = event.getRawFilename();
		File file = File.createTempFile(prefix, suffix, new File(DESTINATION_PATH));

        event.accept(file).transfer();

        //System.out.println("filename: " + file.getName());
        if(file.getName().contains("SearchBot")){
            System.out.println("--== Search Results ==--");
            //System.out.println("Clearing files");

            File outputFile = ZipUtils.unzip(file.getAbsolutePath(), destination.getPath());
            file.delete();

            readFile(outputFile.getAbsolutePath());
        } else {
            ExtractArchive extractArchive = new ExtractArchive();
            extractArchive.extractArchive(file, new File(EBOOKS_PATH));

            System.out.println("-== Download Complete ==-");
            System.out.println("-== " + file.getName().replace("temp-", "") + " ==-");
            file.delete();
        }

        consoleCommander.run();
	}

	public void readFile(String file){
        List<String> lines = new ArrayList<>();
        //read file into stream, try-with-resources

        try (Stream<String> stream = Files.lines(Paths.get(file))){
            stream.filter(this::checkExtension).forEach(lines::add);
            int i = 0;
            for(String book : lines){
                System.out.println(i + " " + book);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(lines.size() > 0){
            consoleCommander.selectDownload(lines);
        } else {
            System.out.println("No search results");
        }
	}

	private boolean checkExtension(String bookName){
	    if(EXTENSIONS_DISABLED) return true;

	    boolean keep = false;
	    for(String extension : fileExtensions){
	        if(bookName.contains(extension)){
	            keep = true;
            }
        }

        return keep;
    }
}
