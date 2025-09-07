package altherneum.fr.commands;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import altherneum.fr.listener.StatsTimer;
import altherneum.fr.main.main;

public class testping {
    public static void onTestPing() {
        main.api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equalsIgnoreCase("test")
                    || slashCommandInteraction.getCommandName().equalsIgnoreCase("ping")) {
                InteractionImmediateResponseBuilder interactionImmediateResponseBuilder = slashCommandInteraction
                        .createImmediateResponder();
                interactionImmediateResponseBuilder.setContent("\uD83D\uDC8E");
                interactionImmediateResponseBuilder.respond();
                if (slashCommandInteraction.getUser().isBotOwner()) {
                    try {
                        StatsTimer.BuildMessage(false, event.getSlashCommandInteraction().getChannel().get().getIdAsString());                    
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}