package com.deliveryninja;

import org.pircbotx.PircBotX;

import java.util.Scanner;

public class ConsoleCommander {

    private final PircBotX bot;

    public ConsoleCommander(PircBotX bot){
        this.bot = bot;

        Runnable runnable = new Runnable() {

            public static final String SEARCH_PREFIX = "@search";

            @Override
            public void run() {
                search();
            }

            public void search() {
                System.out.println("Search: ");
                Scanner scanner = new Scanner(System.in);
                String search = scanner.nextLine();
                System.out.println("SEARCH STARTED : " + search);

                bot.send().message("#ebooks", SEARCH_PREFIX + " " + search);
            }

        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

}
