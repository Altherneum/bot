package altherneum.fr.main;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import altherneum.fr.api.messages;
import altherneum.fr.commands.commands;
import altherneum.fr.listener.*;

public class events {
    public static void startEvents() throws ExecutionException, InterruptedException, IOException {
        reconnect();
        messages.sendmessageOnline();
        //
        CheckInvite.start();
        messageCount.messageListener();
        JoinLeaveListener.startLeaveJoinEvent();
        AutoVoice.startVoiceEvent();
        reactionRole.startListener();
        ticketRoleReact.TicketEvent();
        StatsTimer.StatsTimerRunner();
        Tops.Start();
        userAcceptRules.onUserAcceptRules();
        boutique.onBoutique();
        casinoCoinFlip.startCoinFlip();
        casinoDailyClicker.onCasinoDailyClicker();
        messageLogger.onMessageLogger();
        casinoBanqueroute.startBanqueroute();
        VoiceCounter.startVoiceEvent();
        //
        commands();
    }

    public static void commands() throws ExecutionException, InterruptedException {
        commands.onCommands();
    }

    public static void reconnect() {
        main.api.addReconnectListener(event -> {
            try {
                main.api.getChannelById(IDs.Annonces).get().asServerTextChannel().get()
                        .sendMessage(messages.BotReconnect());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}