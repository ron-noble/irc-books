package com.deliveryninja;

import org.pircbotx.PircBotX;

import java.util.List;
import java.util.Scanner;

public class ConsoleCommander {

    private final PircBotX bot;

    public static final String SEARCH_PREFIX = "@search";
    private final String ircChannel;

    public ConsoleCommander(PircBotX bot, String ircChannel){
        this.bot = bot;
        this.ircChannel = ircChannel;
    }

    public void run(){
        Thread thread = new Thread(this::search);
        thread.start();
    }

    public void search() {
        System.out.print("Search: ");
        Scanner scanner = new Scanner(System.in);
        String search = scanner.nextLine();

        if(search.isEmpty())
            search();

        System.out.println("searching... " + search);

        if(search.equals("exit")){
            System.out.println("Goodbye!");
            System.exit(1);
        }

        bot.send().message(ircChannel, SEARCH_PREFIX + " " + search);
    }

    public void selectDownload(List<String> lines) {
        System.out.print("Choose Download or exit: ");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();

        if(command.equals("exit")){
            return;
        }

        Integer index = Integer.valueOf(command);

        String search = lines.get(index);
        int i = search.lastIndexOf("  ");
        search = search.substring(0, i);

        System.out.println("Trying to Download: " + search);
        bot.send().message(ircChannel,  search);

        selectDownload(lines);
    }
}
