package altherneum.fr.listener;

import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import altherneum.fr.main.IDs;
import altherneum.fr.main.main;


/*
 * 
 * Need to check if message exist on copyMessage or else create message
 * 
 * Need to set Message directly on Repo to avoid use less discord Chanel for holding messages
 * 
 */

public class messageUpdater {
    public static void copyMessage(String channel, String messageID, int messageNumber) throws InterruptedException, ExecutionException {
        Message message = main.api.getMessageById(messageID, main.api.getServerTextChannelById("1081950904644816956").get()).get();
        //Message messageOld = main.api.getMessageById(NewMessageID, main.api.getServerTextChannelById(channel).get()).get();
        Message messageOld = main.api.getServerTextChannelById(channel).get().getMessages(messageNumber).get().getOldestMessage().get();
        //Channel channel2 = main.api.getChannelById(channel).get();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(message.getContent());
        
        messageOld.edit(embedBuilder);
        //Message newMessage = channel2.asServerTextChannel().get().sendMessage(embedBuilder).get();

        for (Reaction reaction : message.getReactions()) {
            messageOld.addReaction(reaction.getEmoji());
        }
    }

    public static void updateAllMessage() throws InterruptedException, ExecutionException {
        copyMessage(IDs.Ticket, "1083864323300012043", 1);
        copyMessage(IDs.ReglesNew, "1117917094906953809", 2);
        copyMessage(IDs.ReglesNew, "1121133776853024808", 1);
        copyMessage(IDs.Regles, "1086666682610745364", 1);
        copyMessage(IDs.NotrePub, "1086667328743288934", 1);
        copyMessage(IDs.NotrePub, "1086737989398429839", 2);
        copyMessage(IDs.Informations, "1086667520271990856", 1);
        copyMessage(IDs.CasinoCoinFlip, "1086668476724301927", 1);
        copyMessage(IDs.CasinoBoutique, "1086668570873835602", 1);
        copyMessage(IDs.CasinoDailyClick, "1086668723349372948", 1);
        copyMessage(IDs.Notifications, "1086668968196063392", 1);
        copyMessage(IDs.AutresJeux, "1086738477414092880", 1);
        copyMessage(IDs.LogsTachesStatff, "1086739575247994881", 2);
        copyMessage(IDs.LogsTachesStatff, "1092032505466593311", 1);
        copyMessage(IDs.Casino, "1092924243416727662", 1);
        copyMessage(IDs.MinecraftInfo, "1092926001169834055", 1);
        copyMessage(IDs.Recrutement, "1095846425503477822", 2);
        copyMessage(IDs.Recrutement, "1097519605993459732", 1);
        copyMessage(IDs.EventsNote, "1112010369012076645", 1);
        copyMessage(IDs.Profil, "1229852231822610503", 1);        
    }
}