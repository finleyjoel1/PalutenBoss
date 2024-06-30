package com.finley.palutenboss.listener;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerMListener implements Listener {

    public PlayerMListener() {
        Bukkit.getPluginManager().registerEvents(this, PalutenBoss.getInstance());
    }

    @EventHandler
    public void handle(ServerListPingEvent event) {
        if (PalutenBoss.getInstance().getLoader().getConfigBuilder().getBoolean("motd")) {
            String motd = String.format("§fWe are using the " + ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getBossName()) + " §fPlugin\n§fProgrammed from §e§lfinleyjoel1");
            event.setMotd(motd);
        }
    }
}