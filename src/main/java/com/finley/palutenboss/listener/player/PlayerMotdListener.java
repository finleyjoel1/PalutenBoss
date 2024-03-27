package com.finley.palutenboss.listener.player;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerMotdListener implements Listener {


    @EventHandler
    public void handle(ServerListPingEvent event) {
        if (PalutenBoss.getInstance().getLoader().getFileBuilder().getBoolean("motd")) {
            String motd = String.format("§fWir verwenden das " + PalutenBoss.getInstance().getBossName() + " §fPlugin\n§fProgrammiert von §e§lfinleyjoel1 §7.");
            event.setMotd(motd);
        }
    }
}
