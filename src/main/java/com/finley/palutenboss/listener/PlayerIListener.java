package com.finley.palutenboss.listener;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PlayerIListener implements Listener {

    private final List<String> titleNames;

    public PlayerIListener() {
        Bukkit.getPluginManager().registerEvents(this, PalutenBoss.getInstance());
        titleNames = Arrays.asList("Settings", "Team Color", "Language", "Effect", "Choose World", "Health");
    }

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
                if (!player.hasPermission(PalutenBoss.getInstance().getLoader().getPermissionFile().getString("settingsPermission"))) {
                    openInventory.close();
                    return;
                }

                if (inventoryName.contains(prefix)) {
                    switch (titleName) {
                        case "Settings":
                            PalutenBoss.getInstance().getLoader().getRegisterManager().registerMainMenu(player, itemName);
                            break;
                        case "Language":
                            PalutenBoss.getInstance().getLoader().getRegisterManager().registerLanguages(player, itemName);
                            break;
                        case "Team Color":
                            PalutenBoss.getInstance().getLoader().getRegisterManager().registerWools(player, itemName);
                            break;
                        case "Effect":
                            PalutenBoss.getInstance().getLoader().getRegisterManager().registerEffect(player, itemName);
                            break;
                        case "Choose World":
                            PalutenBoss.getInstance().getLoader().getRegisterManager().registerChoose(player, itemName);
                            break;
                        case "Health":
                            PalutenBoss.getInstance().getLoader().getRegisterManager().registerHealth(player, itemName);
                            break;
                    }
                    event.setCancelled(true);
                }

                PalutenBoss.getInstance().getLoader().getRegisterManager().registerBack(player, itemName, titleName);
                break;
            }
        }
    }
}