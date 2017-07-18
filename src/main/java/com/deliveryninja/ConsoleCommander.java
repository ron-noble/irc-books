package com.deliveryninja;

import org.pircbotx.PircBotX;

import java.util.List;
import java.util.Scanner;

public class ConsoleCommander {

    private final PircBotX bot;

    public static final String SEARCH_PREFIX = "@search";

    public ConsoleCommander(PircBotX bot){
        this.bot = bot;
    }

    public void run(){
        Thread thread = new Thread(this::search);
        thread.start();
    }

    public void search() {
        System.out.println("Search: ");
        Scanner scanner = new Scanner(System.in);
        String search = scanner.nextLine();
        System.out.println("searching... " + search);

        if(search.equals("exit")){
            System.exit(1);
        }

        bot.send().message("#ebooks", SEARCH_PREFIX + " " + search);
    }

    public void selectDownload(List<String> lines) {
        System.out.println("Choose Download or exit: ");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();

        if(command.equals("exit")){
            search();
        }

        Integer index = Integer.valueOf(command);

        String search = lines.get(index);
        int i = search.lastIndexOf("  ");
        search = search.substring(0, i);

        System.out.println("Trying to Download: " + search);
        bot.send().message("#ebooks",  search);
    }
}
