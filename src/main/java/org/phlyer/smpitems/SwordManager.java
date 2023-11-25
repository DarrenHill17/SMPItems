package org.phlyer.smpitems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class SwordManager implements Listener {
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
                    itemStack.setItemMeta(newSwordMetaData(itemStack));
                }

                //Sword has metadata
                else{
                    Sword heldSword = itemStack.getItemMeta().getPersistentDataContainer().get(swordData, new SwordDataType());
                    itemStack.setItemMeta(updateSwordLore(itemStack, heldSword));
                }
            }
        }
    }

    private static ItemMeta newSwordMetaData(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(swordData, new SwordDataType(), new Sword());
        Sword heldSword = meta.getPersistentDataContainer().get(swordData, new SwordDataType());
        meta = updateSwordLore(itemStack, heldSword);
        return meta;
    }

    private static ItemMeta updateSwordLore(ItemStack itemStack, Sword sword){
        ItemMeta meta = itemStack.getItemMeta();
        List<Component> loreComponents = new ArrayList<Component>(){
            {
                add(Component.text("Prestige: ")
                        .color(TextColor.color(0xFFFFFF)).decoration(TextDecoration.BOLD, true)
                        .append(Component.text(sword.getCurrentPrestige()).color(TextColor.color(0xFFAA00)).decoration(TextDecoration.BOLD, true)));
                add(Component.text("Level: ")
                        .color(TextColor.color(0xFFFFFF)).decoration(TextDecoration.BOLD, true)
                        .append(Component.text(sword.getCurrentLevel()).color(TextColor.color(0xFFAA00)).decoration(TextDecoration.BOLD, true)));
                add(Component.text("XP towards next level: ")
                        .color(TextColor.color(0xFFFFFF)).decoration(TextDecoration.BOLD, true)
                        .append(Component.text(sword.getCurrentXP()).color(TextColor.color(0xFFAA00)).decoration(TextDecoration.BOLD, true)));
            }
        };
        meta.lore(loreComponents);
        return meta;
    }
}
