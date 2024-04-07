package altherneum.fr.api;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;

import altherneum.fr.main.IDs;
import altherneum.fr.main.main;

public class logCommands {
    public static void logCommandChannel(String cmdName, String raison, ServerTextChannel channel, User user) {
        main.api.getChannelById(IDs.LogsCmd).get().asServerTextChannel().get()
                .sendMessage(
                        "/**__" + cmdName + "__** : " + raison + " : " + channel.getMentionTag() + " par "
                                + user.getMentionTag());
    }
}
