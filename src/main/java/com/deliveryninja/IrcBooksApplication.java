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
                    .addListener(this)
                    .addServer("irc.irchighway.net", 9999)
                    .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
                    .buildConfiguration();

            PircBotX myBot = new PircBotX(config);

            myBot.startBot();

            myBot.send().message();
		};
	}

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {
        if(event.getMessage().contains("DeliveryNinja")){
            System.out.println("MESSAGE: " + event.getMessage());
        }

//        //Hello world
//        //This way to handle commands is useful for listeners that listen for multiple commands
//        if (event.getMessage().startsWith("?hello"))
//            event.respond("Hello World!");
//
//        //If this isn't a waittest, ignore
//        //This way to handle commands is useful for listers that only listen for one command
//        if (!event.getMessage().startsWith("?waitTest start"))
//            return;
//
//        //WaitTest has started
//        event.respond("Started...");
//        WaitForQueue queue = new WaitForQueue(event.getBot());
//        //Infinate loop since we might recieve messages that aren't WaitTest's.
//        while (true) {
//            //Use the waitFor() method to wait for a MessageEvent.
//            //This will block (wait) until a message event comes in, ignoring
//            //everything else
//            MessageEvent currentEvent = queue.waitFor(MessageEvent.class);
//            //Check if this message is the "ping" command
//            if (currentEvent.getMessage().startsWith("?waitTest ping"))
//                event.respond("pong");
//                //Check if this message is the "end" command
//            else if (currentEvent.getMessage().startsWith("?waitTest end")) {
//                event.respond("Stopping");
//                queue.close();
//                //Very important that we end the infinate loop or else the test
//                //will continue forever!
//                return;
//            }
    }
}
