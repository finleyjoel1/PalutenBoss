package com.finley.palutenboss.test;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.api.NPCManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Test {

    public void createNPC(Player player, Location location) {
        NPCManager npcManager = new NPCManager();
        npcManager.createNPC(PalutenBoss.getInstance().getBossName(), location, "Paluten");

        player.sendMessage("Erfolgreich.");
    }
}
