package altherneum.fr.listener;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import altherneum.fr.api.FileSystem;
import altherneum.fr.main.IDs;
import altherneum.fr.main.main;

public class casinoBanqueroute {
    public static void startBanqueroute() throws IOException, InterruptedException, ExecutionException {
        setAutoUP();
        main.api.addReactionAddListener(event -> {
            try {
                if (event.getChannel().getIdAsString().equals(IDs.CasinoBanqueRoute)
                        && !event.getUser().get().isBot()) {

                    String mentionTagEmoji = event.getEmoji().getMentionTag();
                    event.removeReaction();

                    if (mentionTagEmoji.equals(IDs.EmojiBanque)) {
                        doBanqueRoute(event.getUser().get(), event.getServerTextChannel().get(), true);
                    }
                }
            } catch (Exception e) {
            }
        });
    }

    public static void doBanqueRoute(User user, ServerTextChannel serverTextChannel, int amount)
            throws IOException, InterruptedException, ExecutionException {
        Thread t = new Thread() {
            int amount2 = amount;

            public void run() {
                try {

                    Boolean win = false;
                    int amountPlayed = 0;
                    int cost = getCost();

                    while (amount2 > 0 && !win) {
                        win = doBanqueRoute(user, serverTextChannel, false);

                        amountPlayed++;
                        amount2--;
                    }

                    if (win) {
                        int total = getTotal();
                        main.api.getServerTextChannelById(IDs.CasinoTextuelResultat).get()
                                .sendMessage(casinoProfil.gainGold(user, total,
                                        "🎉 Félicitations ! " + amountPlayed + " tentatives", serverTextChannel));
                    } else {
                        main.api.getServerTextChannelById(IDs.CasinoTextuelResultat).get()
                                .sendMessage(casinoProfil.gainGold(user, -cost, "Perdu " + amountPlayed + "x",
                                        serverTextChannel));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static Boolean doBanqueRoute(User user, ServerTextChannel serverTextChannel, Boolean message)
            throws IOException, InterruptedException, ExecutionException {
        int gold = casinoProfil.GetGold(user);
        int total = getTotal();// add 1k / day on it
        int cost = getCost();

        if (gold >= cost) {
            double rng = Math.random();
            if (rng < 0.001) {
                if (message) {
                    main.api.getServerTextChannelById(IDs.CasinoTextuelResultat).get()
                            .sendMessage(casinoProfil.gainGold(user, total, "🎉 Félicitations !", serverTextChannel));
                } else {
                    File file = FileSystem.file(user);
                    FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
                    int Gold = fileConfiguration.getInt("Gold");
                    fileConfiguration.set("Gold", Gold + total);
                    fileConfiguration.save(file);
                }
                setTotal(-total);
                updateMessage();
                return true;
            } else {
                if (message) {
                    main.api.getServerTextChannelById(IDs.CasinoTextuelResultat).get()
                            .sendMessage(casinoProfil.gainGold(user, -cost, "Perdu", serverTextChannel));
                } else {
                    File file = FileSystem.file(user);
                    FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
                    int Gold = fileConfiguration.getInt("Gold");
                    fileConfiguration.set("Gold", Gold - cost);
                    fileConfiguration.save(file);
                }
                setTotal(cost);
                updateMessage();
                return false;
            }
        } else {
            user.sendMessage("Vous n'avez pas assez d'argent pour jouer au" + serverTextChannel).get();
            return true;
        }
    }

    public static int getCost() throws IOException {
        // int total = getTotal();
        // return (int) (total * 0.01);
        return 1000;
    }

    public static void updateMessage() throws IOException, InterruptedException, ExecutionException {
        int total = getTotal();
        int cost = getCost();

        long time = Instant.now().getEpochSecond();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String message = "<t:" + time + ":R>\n"
                + "/banqueroute Nombre de tentatives\n"
                + "\n"
                + "\n**__Faites faire faillite à la banque !__**\n"
                + "\n"
                + "Vous avez **__0.1% de chance de gagner__**\n"
                + "Les **__pertes seront ajoutés à la cagnotte__**\n"
                + "**__Toutes les heures 10'000 golds sont ajoutés__** à la cagnotte\n"
                + "\n"
                + "Total à gagner : " + total + " :coin:\n"
                + "Coût de la tentative : **__" + cost + "__** " + IDs.EmojiCoin + "\n"
                + "\n"
                + "Pour jouer cliquer sur " + IDs.EmojiBanque + ", le message sera envoyé dans "
                + main.api.getServerTextChannelById(IDs.CasinoTextuelResultat).get().getMentionTag();

        embedBuilder.setDescription(message);

        main.api.getServerById(IDs.serverID).get().getChannelById(IDs.CasinoBanqueRoute).get()
                .asServerTextChannel().get().getMessages(1).get().getOldestMessage().get()
                .edit(embedBuilder);
    }

    public static int getTotal() throws IOException {
        File file = FileSystem.BanqueRoute();
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        return fileConfiguration.getInt("total");
    }

    public static void setTotal(int montant) throws IOException {
        File file = FileSystem.BanqueRoute();
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.set("total", getTotal() + montant);
        fileConfiguration.save(file);
    }

    public static void setAutoUP() {
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    setTotal(10000);
                    updateMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 1000, 1000 * 60 * 60 * 1);
    }
}
