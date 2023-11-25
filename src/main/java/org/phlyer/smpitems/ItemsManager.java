package org.phlyer.smpitems;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;

import javax.naming.Name;
import java.awt.*;
import java.util.ArrayList;

public class ItemsManager implements Listener {
    private static ArrayList<String> swordKeys = new ArrayList<String>(){
        {
            add("minecraft:wooden_sword");
            add("minecraft:stone_sword");
            add("minecraft:iron_sword");
            add("minecraft:diamond_sword");
            add("minecraft:golden_sword");
            add("minecraft:netherite_sword");
        }
    };

    private static final NamespacedKey swordData = new NamespacedKey(SMPItems.getPlugin(SMPItems.class), "sword_data");

    @EventHandler
    public static void swordHeld(PlayerItemHeldEvent event){
        Inventory inventory = event.getPlayer().getInventory();
        ItemStack itemStack = inventory.getItem(event.getNewSlot());
        if (itemStack != null){
            if (swordKeys.contains(itemStack.getType().getKey().toString())){
//                TextComponent itemName = (TextComponent) itemStack.getItemMeta().displayName();
//                event.getPlayer().sendMessage(itemName);

                //Sword does not have metadata
                if (!itemStack.getItemMeta().getPersistentDataContainer().has(swordData)){
                    itemStack.setItemMeta(newSwordMeta(itemStack));
                }

                //Sword has metadata
                else{
                    Sword heldSword = itemStack.getItemMeta().getPersistentDataContainer().get(swordData, new SwordDataType());
                    event.getPlayer().sendMessage(heldSword.getCurrentXP() + ", " + heldSword.getCurrentLevel() + ", " + heldSword.getCurrentPrestige());
                }
            }
        }
    }

    private static ItemMeta newSwordMeta(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(swordData, new SwordDataType(), new Sword());
        return meta;
    }
}
