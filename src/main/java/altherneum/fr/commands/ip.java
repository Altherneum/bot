package altherneum.fr.commands;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import altherneum.fr.main.IDs;
import altherneum.fr.main.main;

public class ip {
    public static void onIP() {
        main.api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equalsIgnoreCase("ip")) {
                try {
                    InteractionImmediateResponseBuilder interactionImmediateResponseBuilder = slashCommandInteraction
                            .createImmediateResponder();
                    interactionImmediateResponseBuilder
                            .addEmbed(commandes());
                    interactionImmediateResponseBuilder.respond();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static EmbedBuilder commandes() throws IOException, InterruptedException, ExecutionException {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setThumbnail(main.api.getYourself().getAvatar());
        embedBuilder.setTitle("📚 IP/DNS d'" + main.api.getYourself().getName());
        embedBuilder.addField("Adresse : ", "**__Altherneum.fr__**  ou  __**play**__.altherneum.fr");
        ServerTextChannel MinecraftInfo = main.api.getServerTextChannelById(IDs.MinecraftInfo).get();
        ServerTextChannel MinecraftReboot = main.api.getServerTextChannelById(IDs.MinecraftReboot).get();
        ServerTextChannel MinecraftUptime = main.api.getServerTextChannelById(IDs.MinecraftUptime).get();
        ServerTextChannel MinecraftMAJ = main.api.getServerTextChannelById(IDs.MinecraftMAJ).get();

        embedBuilder.addField("Salons : ", MinecraftInfo.getMentionTag() + "\n" + MinecraftReboot.getMentionTag() + "\n"
                + MinecraftUptime.getMentionTag() + "\n" + MinecraftMAJ.getMentionTag());

        String string = "";
        for (User user : main.api.getServerById(IDs.serverID).get().getMembers()) {
            if (user.getRoles(main.api.getServerById(IDs.serverID).get()).contains(IDs.RoleBotsMC)) {
                string += "\n" + user.getMentionTag();
            }
        }
        embedBuilder.addField("Serveurs : ", string);

        return embedBuilder;
    }
}
