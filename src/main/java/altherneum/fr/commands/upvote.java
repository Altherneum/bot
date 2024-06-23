package altherneum.fr.commands;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import altherneum.fr.api.FileSystem;
import altherneum.fr.listener.casinoProfil;
import altherneum.fr.main.IDs;
import altherneum.fr.main.main;

public class upvote {
    public static void onUpvote() {
        main.api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equalsIgnoreCase("upvote")) {
                try {
                    InteractionImmediateResponseBuilder interactionImmediateResponseBuilder = slashCommandInteraction
                            .createImmediateResponder();

                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    User sender = slashCommandInteraction.getUser();
                    User user = slashCommandInteraction.getOptionUserValueByIndex(0).get();
                    String raison = slashCommandInteraction.getOptionStringValueByIndex(1).get();

                    embedBuilder.setThumbnail(user.getAvatar());
                    embedBuilder.setTitle("👍 Upvote");

                    if (!user.getIdAsString().equals(sender.getIdAsString())) {
                        if(CanVote(user, 60)){
                            File file = FileSystem.file(user);
                            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);

                            embedBuilder.addInlineField("Utilisateur", user.getMentionTag());
                            embedBuilder.addInlineField("ID", user.getIdAsString());
                            embedBuilder.addInlineField("Par", sender.getMentionTag());
                            embedBuilder.addInlineField("Raison", raison);

                            if (sender.getRoles(main.api.getServerById(IDs.serverID).get()).contains(IDs.RoleModeration)) {
                                int userUpvoteStaff = fileConfiguration.getInt("userUpvoteStaff");
                                fileConfiguration.set("userUpvoteStaff", userUpvoteStaff + 1);
                            } else {
                                int userUpvote = fileConfiguration.getInt("userUpvote");
                                fileConfiguration.set("userUpvote", userUpvote + 1);
                            }
                            fileConfiguration.save(file);
                            UpdateCanVoteDate(sender);
                        } else {
                            embedBuilder.addInlineField("Vous avez déjà voté il y a moins d'une heure",
                                    user.getMentionTag()); 
                        }
                    } else {
                        embedBuilder.addInlineField("Vous ne pouvez pas voter pour vous même", user.getMentionTag());
                    }

                    interactionImmediateResponseBuilder.addEmbed(embedBuilder);
                    interactionImmediateResponseBuilder.respond();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public static boolean CanVote(User user, int min) throws IOException {
        File file = FileSystem.file(user);
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (fileConfiguration.get("voteDate") != null) {
            Date date = (Date) fileConfiguration.get("voteDate");
            Date dateNow = new Date();
            Date dateCheck = casinoProfil.ReturnDateWithXMin(date, min);
            if (dateNow.after(dateCheck)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static void UpdateCanVoteDate(User user) throws IOException {
        File file = FileSystem.file(user);
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.set("voteDate", new Date());
        fileConfiguration.save(file);
    }
}
