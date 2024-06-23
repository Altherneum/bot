package altherneum.fr.commands;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import altherneum.fr.api.FileSystem;
import altherneum.fr.main.IDs;
import altherneum.fr.main.main;

public class upvote {
    public static void onUpvote() {
        main.api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equalsIgnoreCase("upvote")) {
                try {
                    InteractionImmediateResponseBuilder interactionImmediateResponseBuilder = slashCommandInteraction.createImmediateResponder();

                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    User sender = slashCommandInteraction.getUser();
                    User user = slashCommandInteraction.getOptionUserValueByIndex(0).get();
                    String raison = slashCommandInteraction.getOptionStringValueByIndex(1).get();
                    
                    /*
                     * Ajouter un timer pour éviter le spam d'upvote
                     */

                    if(!user.getIdAsString().equals(sender.getIdAsString())){
                        File file = FileSystem.file(user);
                        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);

                        embedBuilder.setThumbnail(user.getAvatar());
                        embedBuilder.setTitle("👍 Upvote");
                        embedBuilder.addInlineField("Utilisateur", user.getMentionTag());
                        embedBuilder.addInlineField("ID", user.getIdAsString());
                        embedBuilder.addInlineField("Par", sender.getMentionTag());
                        embedBuilder.addInlineField("Raison", raison);

                        if(user.getRoles(main.api.getServerById(IDs.serverID).get()).contains(IDs.RoleModeration))
                        {
                            int userUpvoteStaff = fileConfiguration.getInt("userUpvoteStaff");
                            fileConfiguration.set("userUpvoteStaff", userUpvoteStaff + 1);
                        }
                        else
                        {
                            int userUpvote = fileConfiguration.getInt("userUpvote");
                            fileConfiguration.set("userUpvote", userUpvote + 1);
                        }
                        fileConfiguration.save(file);
                    }
                    else{
                        embedBuilder.setThumbnail(user.getAvatar());
                        embedBuilder.setTitle("👍 Upvote");
                        embedBuilder.addInlineField("Vous ne pouvez pas voter pour vous même", user.getMentionTag());
                    }
                    

                    interactionImmediateResponseBuilder.addEmbed(embedBuilder);
                    interactionImmediateResponseBuilder.respond();
                 }
                 catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
