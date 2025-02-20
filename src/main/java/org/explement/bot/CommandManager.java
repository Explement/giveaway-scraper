package org.explement.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.explement.redditpost.SendPost;
import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class CommandManager extends ListenerAdapter {

    private final Map<String, String> modalLinkMap = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) { // When a slash command is entered
        String command = event.getName();
        if (command.equals("search")) { // Search command
            HashMap<String, String> postResponse = org.explement.getposts.CollectPosts.getPosts();
            if (postResponse != null && !postResponse.isEmpty()) { // If posts were found
                // Loop through the map
                for (Map.Entry<String, String> entry : postResponse.entrySet()) {
                    if (!entry.getKey().isEmpty()) {
                        // Get user properties
                        String userId = event.getUser().getId();
                        String userPing = "<@" + userId + ">";
                        String editedString = userPing + "```" + entry.getKey() + "```";

                        // Make ID for identification
                        String uniqueId = "comment_" + System.nanoTime();
                        modalLinkMap.put(uniqueId, entry.getValue());

                        // Make the buttons
                        Button linkRedirect = Button.link(entry.getValue(), "Take Me");
                        Button postComment = Button.primary(uniqueId, "Post Comment");

                        // Get the history and make it save as a 'message' list
                        MessageHistory history = MessageHistory.getHistoryFromBeginning(event.getChannel()).complete();
                        List<Message> messages = history.getRetrievedHistory();

                        if (!compareMessageHistory(messages, editedString)) { // Make sure theres no duplicates
                            if (event.isAcknowledged()) {
                                event.getChannel().sendMessage(editedString).setActionRow(linkRedirect, postComment).queue();
                            } else {
                                event.reply(editedString).setActionRow(linkRedirect, postComment).queue();
                            }
                        } else {
                            event.reply("No posts found.").setEphemeral(true).queue();
                        }
                    }
                }
            } else { // Couldn't find any posts
                event.reply("No posts found.").setEphemeral(true).queue();
            }
        } else if (command.equals("clear")) { // Clear command
            // Get history as a 'message' list
            MessageHistory history = MessageHistory.getHistoryFromBeginning(event.getChannel()).complete();
            List<Message> messages = history.getRetrievedHistory();

            // Make a new thread for effeciency and start it
            Thread thread = new Thread() {
                public void run() {
                    for (Message m : messages) {
                        m.delete().queue();
                    }
                }
            };
            thread.start();

            event.reply("Executing **Clear**").setEphemeral(true).queue();
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) { // When guild is loaded
        // Make a new list for commands
        List<CommandData> commandData = new ArrayList<>();

        // Add all commands to the list
        commandData.add(Commands.slash("search", "Searches for reddit posts."));
        commandData.add(Commands.slash("clear", "Clears all previous messages in the channel."));

        // Update the commands in the guild
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) { // When a button is pressed
        String buttonId = event.getComponentId(); // Get the ID for identification
        if (modalLinkMap.containsKey(buttonId)) {
            // Subject (comment)
            TextInput subject = TextInput.create("subject", "Comment", TextInputStyle.SHORT)
                    .setPlaceholder("What goes here will be commented on the post.")
                    .setRequiredRange(0, 200)
                    .build();
            // Modal (interactable)
            Modal modal = Modal.create(buttonId, "Comment")
                    .addComponents(ActionRow.of(subject))
                    .build();

            // Reply
            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) { // When a modal is interacted with
        String modalId = event.getModalId(); // Get the ID for identification
        if (modalLinkMap.containsKey(modalId)) {
            // Get the link and comment
            String link = modalLinkMap.get(modalId);
            String subject = event.getValue("subject").getAsString();

            // Reply 
            event.reply("Message **Processed**").setEphemeral(true).queue();

            // Send a request to post on reddit via 'redditpost' package
            SendPost.postOnReddit(link, subject);
        }
    }

    private static boolean compareMessageHistory(List<Message> messages, String content) { // Compares messages so there isn't any duplicates
        for (Message m : messages) { 
            if (content.equals(m.getContentRaw())) { // Get the raw message content
                return true; // Message is a duplicate
            }
        }

        return false;
    }
}
