package altherneum.fr.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.interaction.SlashCommandInteraction;

import altherneum.fr.main.main;

public class getYT {
    public static void onYT() {
        main.api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equalsIgnoreCase("yt")) {
                try {
                    String id = slashCommandInteraction.getOptionStringValueByIndex(0).get();
                    dumpVideoFromChannel(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public static void dumpVideoFromChannel(String channelID) throws InterruptedException, ExecutionException{
        ServerTextChannel serverTextChannel = main.api.getServerTextChannelById(channelID).get();
        
        int count = 0;
        for(Message message : serverTextChannel.getMessages(500).get()){
            String messageText = message.getContent();
            
            count++;
            System.out.println("-> " + count);

            if(messageText.contains("https://www.youtube.com/") || messageText.contains("https://youtu.be/") || messageText.contains("https://youtube.com/shorts/") || messageText.contains("https://music.youtube.com/")){
                checkForYoutubeMessage(messageText);
                //message.delete().get();
            }
        }



        // Output results
        System.out.println("YouTube URLs found:");
        System.out.println(youtubeUrls.toString());
        serverTextChannel.sendMessage(youtubeUrls.toString());

        System.out.println("\nNon-matching text:");
        System.out.println(nonMatchingText.toString());
        serverTextChannel.sendMessage(nonMatchingText.toString());
    }

    // List to store extracted YouTube URLs
    public static List<String> youtubeUrls = new ArrayList<>();
    // StringBuffer to build the remaining non-matching text
    public static StringBuffer nonMatchingText = new StringBuffer();

    public static void checkForYoutubeMessage(String text){
        // Pattern to match YouTube URLs
        Pattern MY_PATTERN = Pattern.compile("((http(s)?://)?)(www\\.|music\\.)?((youtube\\.com\\/)|(youtu\\.be\\/))[\\S]+");

        Matcher m = MY_PATTERN.matcher(text);
        int lastEnd = 0;

        while (m.find()) {
            // Add the YouTube URL to the list
            youtubeUrls.add(m.group());

            // Append the non-matching text between the last end and current start
            nonMatchingText.append(text.substring(lastEnd, m.start()));
            lastEnd = m.end();
        }

        // Append the remaining text after the last match
        nonMatchingText.append(text.substring(lastEnd));
    }
}
