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
        System.out.println("SEARCH STARTED : " + search);

        bot.send().message("#ebooks", SEARCH_PREFIX + " " + search);
    }

    public void selectDownload(List<String> lines) {
        System.out.println("choose download or exit: ");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();

        if(command.equals("exit")){
            search();
        }

        Integer index = scanner.nextInt();

        String search = lines.get(index);
        int i = search.lastIndexOf("  ");
        search = search.substring(0, i);

        System.out.println("Trying to download: " + search);
        bot.send().message("#ebooks",  search);
    }
}
