package altherneum.fr.listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.javacord.api.entity.message.MessageSet;

import altherneum.fr.api.FileSystem;
import altherneum.fr.main.main;

import java.io.File;

public class messageCount {
    public static void messageListener() {
        main.api.addMessageCreateListener(event -> {
            try {
                if (event.getMessageAuthor().isRegularUser()) {
                    if (!event.getMessageAuthor().asUser().get().getActiveTimeout(event.getServer().get())
                            .isPresent()) {

                        Boolean found = false;

                        MessageSet messageSet = event.getChannel().getMessagesBefore(1, event.getMessage()).get();

                        if (messageSet.getOldestMessage().get().getAuthor().equals(event.getMessageAuthor())) {
                            found = true;
                        }

                        if (!found) {
                            File file = FileSystem.file(event.getMessageAuthor().asUser().get());
                            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
                            int i = fileConfiguration.getInt("Messages") + 1;
                            int gold = fileConfiguration.getInt("Gold") + 5;
                            fileConfiguration.set("Gold", gold);
                            fileConfiguration.set("Messages", i);
                            fileConfiguration.save(file);
                            bonusRoles.Member(i, event.getMessageAuthor().asUser().get());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}