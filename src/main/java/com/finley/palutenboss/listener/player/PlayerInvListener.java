package com.finley.palutenboss.listener.player;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.manager.player.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;

import java.util.Arrays;
import java.util.List;

public class PlayerInvListener implements Listener {

    private final List<String> titleNames = Arrays.asList("Settings", "Team Color", "Language", "Effect", "Choose World");


    @EventHandler
    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getItemMeta() == null) {
            return;
        }

        String itemName = clickedItem.getItemMeta().getDisplayName();
        InventoryView openInventory = player.getOpenInventory();
        String inventoryName = openInventory.getTitle();
        String prefix = PalutenBoss.getInstance().getPrefix() + "§a§l";

        for (String titleName : titleNames) {
            if (inventoryName.equalsIgnoreCase(prefix + titleName)) {
                if (!player.hasPermission(PalutenBoss.getInstance().getLoader().getPermission().getString("settingsPermission"))) {
                    openInventory.close();
                    return;
                }

                switch (titleName) {
                    case "Settings":
                        MenuManager.registerMainMenu(player, itemName);
                        break;
                    case "Language":
                        MenuManager.registerLanguages(player, itemName);
                        break;
                    case "Team Color":
                        MenuManager.registerWools(player, itemName);
                        break;
                    case "Effect":
                        MenuManager.registerEffect(player, itemName);
                        break;
                    case "Choose World":
                        MenuManager.registerChoose(player, itemName);
                        break;
                    case "Health":
                        MenuManager.registerHealth(player, itemName);
                        break;
                }

                MenuManager.registerBack(player, itemName, titleName);
                event.setCancelled(true);
                break;
            }
        }
    }
}