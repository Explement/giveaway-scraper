package org.explement.bot;

import org.explement.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class InitializeBot {
    private static String token = config.token;
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        JDA bot = JDABuilder.createDefault(token)
                    .addEventListeners(new CommandManager())
                    .setActivity(Activity.playing("🎁 ON LOOKOUT FOR GAMES 🎁"))
                    .build();
    }
}