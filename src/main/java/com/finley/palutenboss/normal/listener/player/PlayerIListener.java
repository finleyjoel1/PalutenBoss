package com.finley.palutenboss.normal.listener.player;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlayerIListener implements Listener {

    private final List<String> titleNames = Arrays.asList("Settings", "Team Color", "Language", "Effect", "Choose World", "Health");

    public PlayerIListener() {
        Bukkit.getPluginManager().registerEvents(this, PalutenBoss.getInstance());
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
                if (!player.hasPermission(PalutenBoss.getInstance().getLoader().getPermission().getString("settingsPermission"))) {
                    openInventory.close();
                    return;
                }

                switch (titleName) {
                    case "Settings":
                        PalutenBoss.getInstance().getLoader().getMenuManager().registerMainMenu(player, itemName);
                        break;
                    case "Language":
                        PalutenBoss.getInstance().getLoader().getMenuManager().registerLanguages(player, itemName);
                        break;
                    case "Team Color":
                        PalutenBoss.getInstance().getLoader().getMenuManager().registerWools(player, itemName);
                        break;
                    case "Effect":
                        PalutenBoss.getInstance().getLoader().getMenuManager().registerEffect(player, itemName);
                        break;
                    case "Choose World":
                        PalutenBoss.getInstance().getLoader().getMenuManager().registerChoose(player, itemName);
                        break;
                    case "Health":
                        player.setLevel(25);
                        PalutenBoss.getInstance().getLoader().getMenuManager().registerHealth(player, itemName);
                        break;
                }

                PalutenBoss.getInstance().getLoader().getMenuManager().registerBack(player, itemName, titleName);
                break;
            }
        }
    }

    @EventHandler
    public void handleAnvil(PrepareAnvilEvent event) {
        event.setResult(event.getResult());

        if (Objects.requireNonNull(event.getResult()).getItemMeta() == null) {
            event.getView().getPlayer().sendMessage("null");
            return;
        }

        event.getView().getPlayer().sendMessage(event.getResult().getItemMeta().getDisplayName());
    }
}