package altherneum.fr.listener;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;

import altherneum.fr.main.IDs;
import altherneum.fr.main.main;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/* import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean; */
import com.sun.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;



//Ajouter nombre channel (Voc / text / Forum / Thread), et user en cours de vocal

public class StatsTimer {
    public static void StatsTimerRunner() {
        TimerTask task = new TimerTask() {
            public void run() {
                String MessageRole = "";
                for (Role role : roles()) {
                    if (role.equals(IDs.RoleGenshinImpact) || role.equals(IDs.RoleHomme) || role.equals(IDs.RolePC)) {
                        MessageRole += "\n";
                    }
                    MessageRole += rolesString(role) + "\n";
                }

                int DND = 0;
                int Online = 0;
                int IDLE = 0;
                int Offline = 0;
                int Bot = 0;
                int roleLess = 0;
                int onlyRules = 0;
                for (User user : main.api.getServerById(IDs.serverID).get().getMembers()) {
                    if (user.isBot()) {
                        Bot++;
                    } else {
                        if (user.getRoles(main.api.getServerById(IDs.serverID).get()).size() == 1) {
                            roleLess++;
                        }
                        if (user.getRoles(main.api.getServerById(IDs.serverID).get())
                                .size() == IDs.defaultRoleSizeWithSplitterAndRulesAndEveryOne) {
                            if (user.getRoles(main.api.getServerById(IDs.serverID).get())
                                    .contains(IDs.RoleReglesValides)) {
                                onlyRules++;
                            }
                        }
                        if (user.getStatus().equals(UserStatus.DO_NOT_DISTURB)) {
                            DND++;
                        } else if (user.getStatus().equals(UserStatus.ONLINE)) {
                            Online++;
                        } else if (user.getStatus().equals(UserStatus.IDLE)) {
                            IDLE++;
                        } else if (user.getStatus().equals(UserStatus.OFFLINE)) {
                            Offline++;
                        }
                    }
                }

                String uptime = "";
                try{
                    uptime = getUptime();
                }catch(Exception e){

                }

                String MessageUser = ":green_circle: **En ligne** : " + Online + "\n" + ":red_circle: **Hors ligne** : "
                        + Offline + "\n" + ":no_entry: **Ne pas déranger** : " + DND + "\n"
                        + ":crescent_moon: **Absent** : " + IDLE + "\n" + ":robot: Bot : " + Bot;

                Role everyone = IDs.RoleEveryone;
                String everHereOne = rolesString(everyone);
                String StringroleLess = IDs.EmojiRocket + " Sans rôle : " + roleLess;
                String RulesOnly = IDs.RoleReglesValides.getMentionTag() + " uniquement : " + onlyRules;

                long time = Instant.now().getEpochSecond();
                Date date = new Date();
                date.setMinutes(date.getMinutes() + 15);
                long time2 = date.toInstant().getEpochSecond();

                String finalMessage = "<t:" + time + ":R>" + ", prochain : " + "<t:" + time2 + ":R>" + "\n\n"
                        + MessageUser + "\n\n" + StringroleLess
                        + "\n" + everHereOne + "\n" + RulesOnly + "\n\n" + MessageRole + "\n\n\n\n" + uptime;

                try {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setDescription(finalMessage);
                    main.api.getServerById(IDs.serverID).get().getChannelById(IDs.Statistiques).get()
                            .asServerTextChannel().get().getMessages(1).get().getOldestMessage().get()
                            .edit(embedBuilder);

                    setCounterOnCategory((Online + DND + IDLE + Offline), (Online + DND + IDLE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 1000 * 5, 1000 * 60 * 15);
    }

    public static ArrayList<Role> roles() {
        ArrayList<Role> role = new ArrayList<>();
        role.add(IDs.RoleBoost);
        role.add(IDs.RoleVIP);
        role.add(IDs.RoleReglesValides);
        //
        for (Role role2 : IDs.RolesJeux()) {
            role.add(role2);
        }
        //
        role.add(IDs.RoleHomme);
        role.add(IDs.RoleFemme);
        role.add(IDs.RoleMineur);
        role.add(IDs.RoleMajeur);
        //
        role.add(IDs.RolePC);
        role.add(IDs.RoleTelephone);
        role.add(IDs.RoleXBox);
        role.add(IDs.RolePS);
        role.add(IDs.RoleSwitch);
        return role;
    }

    public static String rolesString(Role role) {
        return role.getMentionTag() + " : " + role.getUsers().size();
    }

    public static DateFormat dateFormat() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public static String DateFormated(Date date) {
        return dateFormat().format(date);
    }

    public static void setCounterOnCategory(int users, int online) {
        IDs.CategoryAltherneum.updateName("●▬▬▬▬▬▬▬▬๑ " + users + "🙋" + "      " + online + "🟢").join();
    }

    public static String getUptime()
            throws UnknownHostException, IOException, InterruptedException, ExecutionException, NullPointerException {

        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = allocatedMemory - freeMemory;
        long totalRam = operatingSystemMXBean.getTotalPhysicalMemorySize();
        long totalRamFree = operatingSystemMXBean.getFreeMemorySize();
        long totalRamUsed = totalRam - totalRamFree;

        double cpuusage = operatingSystemMXBean.getSystemCpuLoad() * 100;

        File diskPartition = new File("/");
        long free = diskPartition.getFreeSpace();
        long total = diskPartition.getTotalSpace();

        boolean alternativePing = true;

        String adress1 = "google.fr";
        boolean isPinged = false;
        String adress2 = "discord.com";
        boolean isPinged2 = false;

        int port = 80;
        int timeout = 2000;

        long currentTime = System.currentTimeMillis();
        if (alternativePing) {
            isPinged = connectSocket(adress1, port, timeout);
        } else {
            isPinged = InetAddress.getByName(adress1).isReachable(timeout);
        }
        currentTime = System.currentTimeMillis() - currentTime;

        long currentTime2 = System.currentTimeMillis();
        if (alternativePing) {
            isPinged2 = InetAddress.getByName(adress2).isReachable(timeout);
        } else {
            isPinged2 = connectSocket(adress2, port, timeout);
        }
        currentTime2 = System.currentTimeMillis() - currentTime2;

        String message = "";

        message += "\n**__Bot RAM__** : " + Math.round(usedMemory / 1024 / 1024) + "/"
                + Math.round(maxMemory / 1024 / 1024) + " Mo";
        message += "\n**__RAM VPS__** : " + Math.round(totalRamUsed / 1024 / 1024) + "/"
                + Math.round(totalRam / 1024 / 1024) + " Mo";
        message += "\n**__CPU__** : " + Math.round(cpuusage) + "%";
        message += "\n**__HDD__** : " + Math.round(free / 1024 / 1024 / 1024) + "/"
                + Math.round(total / 1024 / 1024 / 1024) + " Go";

        if (isPinged) {
            message += "\n\n**__Ping__** " + adress1 + " : " + currentTime + " Ms";
        } else {
            message += "\n\n**__Ping__** : __Erreur__";
        }

        if (isPinged2) {
            message += "\n**__Ping__** " + adress2 + " : " + currentTime2 + " Ms";
        } else {
            message += "\n**__Ping__** : __Erreur__";
        }

        return message;
    }
    
    public static boolean connectSocket(String address, int port, int timeout) throws IOException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(address, port), timeout);
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        } finally {
            socket.close();
        }
    }
}