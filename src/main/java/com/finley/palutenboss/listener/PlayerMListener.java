package com.finley.palutenboss.listener;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;

import java.io.File;

//MOTD-LISTENER
public class PlayerMListener implements Listener {

    private final boolean updateOnPing;
    private final boolean toggled;
    private CachedServerIcon icon;

    public PlayerMListener() {
        Bukkit.getPluginManager().registerEvents(this, PalutenBoss.getInstance());

        toggled = PalutenBoss.getInstance().getLoader().getConfigBuilder().getBoolean("motd.toggled");
        updateOnPing = PalutenBoss.getInstance().getLoader().getConfigBuilder().getBoolean("motd.updateOnPing");

        if (!updateOnPing && toggled) {
            updateIcon();
        }
    }

    @EventHandler
    public void handle(@NotNull ServerListPingEvent event) {
        if (toggled) {
            event.setMotd("§fWe are using the §6§lPaluten§e§lBoss\n§fMade by §afinleyjoel1yt");

            if (updateOnPing) {
                updateIcon();
            }

            event.setServerIcon(icon != null ? icon : Bukkit.getServerIcon());
        }
    }

    private void updateIcon() {
        try {
            File file = new File(PalutenBoss.getInstance().getDataFolder(), "icons/icon.png");

            if (file.exists()) {
                try {
                    icon = Bukkit.loadServerIcon(file);
                } catch (NullPointerException ignored) {
                    icon = Bukkit.getServerIcon();
                }
            } else {
                file.createNewFile();
                if (PalutenBoss.getInstance().isDebugMode()) {
                    Bukkit.getLogger().severe("File (" + file.getAbsolutePath() + ") not found. (created file)");
                }
            }
        } catch (Exception ignored) {
        }
    }
}