package com.deliveryninja;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.types.GenericEvent;

public class ConsoleEvent implements GenericEvent {

    private String message;

    public ConsoleEvent(String message) {
        super();
        this.message = message;
    }

    public String getConsoleMessage(){
        return message;
    }

    @Override
    public void respond(String s) {
    }

    @Override
    public <T extends PircBotX> T getBot() {
        return null;
    }

    @Override
    public long getTimestamp() {
        return 0;
    }

    @Override
    public int compareTo(Event o) {
        return 0;
    }
}
