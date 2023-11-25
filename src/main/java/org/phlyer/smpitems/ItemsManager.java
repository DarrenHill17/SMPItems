package org.phlyer.smpitems;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemsManager implements Listener {
    @EventHandler
    public static void swordHeld(PlayerItemHeldEvent event){
        Inventory inventory = event.getPlayer().getInventory();
        ItemStack itemStack = inventory.getItem(event.getNewSlot());
        event.getPlayer().sendMessage("You just held " + itemStack);
    }
}
