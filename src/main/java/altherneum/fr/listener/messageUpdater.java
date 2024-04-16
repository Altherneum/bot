package altherneum.fr.listener;

import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import altherneum.fr.main.IDs;
import altherneum.fr.main.main;

public class messageUpdater {
    public static void copyMessage(String channel, String messageID) throws InterruptedException, ExecutionException {

        Message message = main.api.getMessageById(messageID, main.api.getServerTextChannelById(channel).get()).get();
        Channel channel2 = main.api.getChannelById(channel).get();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(message.getContent());
        Message newMessage = channel2.asServerTextChannel().get().sendMessage(embedBuilder).get();

        for (Reaction reaction : message.getReactions()) {
            newMessage.addReaction(reaction.getEmoji());
        }
    }

    public static void updateAllMessage() throws InterruptedException, ExecutionException {
        copyMessage(IDs.Ticket, "1083864323300012043");
        copyMessage(IDs.ReglesNew, "1117917094906953809");
        copyMessage(IDs.ReglesNew, "1121133776853024808");
        copyMessage(IDs.Regles, "1086666682610745364");
        copyMessage(IDs.NotrePub, "1086667328743288934");
        copyMessage(IDs.NotrePub, "1086737989398429839");
        copyMessage(IDs.Informations, "1086667520271990856");
        copyMessage(IDs.CasinoCoinFlip, "1086668476724301927");
        copyMessage(IDs.CasinoBoutique, "1086668570873835602");
        copyMessage(IDs.CasinoDailyClick, "1086668723349372948");
        copyMessage(IDs.Notifications, "1086668968196063392");
        copyMessage(IDs.AutresJeux, "1086738477414092880");
        copyMessage(IDs.LogsTachesStatff, "1086739575247994881");
        copyMessage(IDs.LogsTachesStatff, "1092032505466593311");
        copyMessage(IDs.Casino, "1092924243416727662");
        copyMessage(IDs.MinecraftInfo, "1092926001169834055");
        copyMessage(IDs.Recrutement, "1095846425503477822");
        copyMessage(IDs.Recrutement, "1097519605993459732");
        copyMessage(IDs.EventsNote, "1112010369012076645");
        copyMessage(IDs.Profil, "1229852231822610503");        
    }
}