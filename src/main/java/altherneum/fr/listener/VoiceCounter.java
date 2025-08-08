package altherneum.fr.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import altherneum.fr.api.FileSystem;
import altherneum.fr.main.IDs;
import altherneum.fr.main.main;

public class VoiceCounter {
    public static void startVoiceEvent() throws IOException, ExecutionException, InterruptedException {
        VoiceChannelCreator();
    }

    public static void VoiceChannelCreator() throws IOException, ExecutionException, InterruptedException {
        main.api.addServerVoiceChannelMemberJoinListener(event -> {
            try {
                checkUserTime(event.getUser());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void checkUserTime(User user) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Server server = main.api.getServerById(IDs.serverID).get();
                    if (user.getConnectedVoiceChannel(server).isPresent()
                            && !user.getConnectedVoiceChannel(server).isEmpty()) {

                        if (server.getAfkChannel().isPresent() && user.getConnectedVoiceChannel(server).get()
                                .getIdAsString().equals(server.getAfkChannel().get().getIdAsString())) {

                            this.cancel();

                        } else {

                            if (!user.isSelfMuted(server) || !user.isSelfDeafened(server) || !user.isMuted(server) || !user.isDeafened(server)) {

                                if (user.getConnectedVoiceChannel(server).get().getConnectedUsers().size() >= 2) {

                                    File file = FileSystem.file(user);
                                    FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
                                    fileConfiguration.set("voiceTime", (fileConfiguration.getInt("voiceTime") + 1));
                                    fileConfiguration.save(file);

                                }
                                else if (user.getConnectedVoiceChannel(server).get().getConnectedUsers().size() == 1) {

                                    File file = FileSystem.file(user);
                                    FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
                                    fileConfiguration.set("voiceTimeSolo", (fileConfiguration.getInt("voiceTimeSolo") + 1));
                                    fileConfiguration.save(file);

                                }
                            }
                        }
                    } else {
                        this.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public static String voiceSecFormat(int duration) {
        if (duration < 60) {
            return duration + " secondes";
        } else if (duration >= 60 && duration < 3_600) {
            return (duration / 60) + " minutes";
        } else if (duration >= 3_600 && duration < 86_400) {
            return (duration / 3_600) + " heures";
        } else if (duration >= 86_400 && duration < 604_800) {
            return (duration / 86_400) + " jours";
        } else if (duration >= 604_800) {
            return (duration / 604_800) + " mois";
        }
        return "erreur";
    }
}
