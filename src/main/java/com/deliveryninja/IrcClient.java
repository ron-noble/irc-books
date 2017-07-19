package com.deliveryninja;

import com.github.junrar.extract.ExtractArchive;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectAttemptFailedEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class IrcClient extends ListenerAdapter {

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

    @Value("${irc.port}")
    private int port;

    @Value("${irc.enable.ssl:false}")
    private boolean enableSSL;

    private static final boolean EXTENSIONS_DISABLED = true;
    private static final List<String> fileExtensions = Arrays.asList("mobi");

    private ConsoleCommander consoleCommander;

    public IrcClient(){
    }

    public void run(){
        System.out.println("IRC Books");
        System.out.println("Server: " + ircServer + port);
        System.out.println("Channel: " + ircChannel);
        if(enableSSL){
            System.out.println("Encryption enabled");
        }

        Configuration.Builder configBuilder = new Configuration.Builder()
                .setName(botName)
                .setLogin("xxsuperhotfirexx")
                .setAutoNickChange(true)
                .addAutoJoinChannel(ircChannel)
                .addServer(ircServer, port)
                .addListener(this);

        if(enableSSL){
            configBuilder = configBuilder.setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates());
        }

        Configuration config = configBuilder.buildConfiguration();
        PircBotX myBot = new PircBotX(config);

        consoleCommander = new ConsoleCommander(myBot, ircChannel);
        consoleCommander.run();

        try {
            myBot.startBot();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnect(ConnectEvent event) throws Exception {
        System.out.println("Connected");
    }

    @Override
    public void onConnectAttemptFailed(ConnectAttemptFailedEvent event) throws Exception {
        System.out.println("Connection failed");
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        System.out.println("Private Msg: " + event.getMessage());
    }

    @Override
    public void onIncomingFileTransfer(IncomingFileTransferEvent event) throws Exception {
        final File destination = new File(tempDownloadFolder);

        String prefix = "temp"+ "-";
        String suffix = event.getSafeFilename();
        File file = File.createTempFile(prefix, suffix, new File(tempDownloadFolder));

        event.accept(file).transfer();

        if(file.getName().contains("Search")){
            System.out.println("Downloading Search Results...");
            processSearchDCC(destination, file);
        } else {
            System.out.println("Downloading Ebook...");
            processEbookDCC(file);
        }

        consoleCommander.run();
    }

    private void processEbookDCC(File file) {
        ExtractArchive extractArchive = new ExtractArchive();
        extractArchive.extractArchive(file, new File(downloadFolder));

        System.out.println("-== Download Complete ==-");
        System.out.println("-== " + file.getName().replace("temp-", "") + " ==-");
        file.delete();
    }

    private void processSearchDCC(File destination, File file) {
        System.out.println("--== Search Results ==--");

        File outputFile = ZipUtils.unzip(file.getAbsolutePath(), destination.getPath());
        file.delete();

        readFile(outputFile.getAbsolutePath());
    }


    public void readFile(String file){
        List<String> lines = new ArrayList<>();
        //read file into stream, try-with-resources

        try (Stream<String> stream = Files.lines(Paths.get(file))){
            stream.filter(this::checkExtension).filter(this::checkJunk).forEach(lines::add);
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

    private boolean checkJunk(String bookName) {
        return bookName.startsWith("!");
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
