package org.phlyer.smpitems;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SwordManager implements Listener {
    private static final ArrayList<String> swordKeys = new ArrayList<String>(){
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
                    itemStack.setItemMeta(updateSwordLore(itemStack.getItemMeta(), heldSword));
                }
            }
        }
    }

    @EventHandler
    public static void swordCrafted(CraftItemEvent event){
        ItemStack itemStack = event.getInventory().getResult();
        if (swordKeys.contains(itemStack.getType().getKey().toString()) && !itemStack.getItemMeta().getPersistentDataContainer().has(swordData)){
            itemStack.setItemMeta(newSwordMetaData(itemStack));
        }
    }

    @EventHandler
    public static void swordUsed(PlayerInteractEvent event){
        assert event.getHand() != null;
        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getHand());
        if (swordKeys.contains(itemStack.getType().getKey().toString())){
            //Sword does not have metadata
            if (!itemStack.getItemMeta().getPersistentDataContainer().has(swordData)){
                itemStack.setItemMeta(newSwordMetaData(itemStack));
            }

            //Sword has metadata
            else{
                Sword heldSword = itemStack.getItemMeta().getPersistentDataContainer().get(swordData, new SwordDataType());
                itemStack.setItemMeta(updateSwordLore(itemStack.getItemMeta(), heldSword));
            }
        }
    }

    @EventHandler
    public static void swordKillsEntity(EntityDeathEvent event){
        Player killer = event.getEntity().getKiller();
        if (killer != null){
            ItemStack itemStack = killer.getInventory().getItemInMainHand();
            if (swordKeys.contains(itemStack.getType().getKey().toString())){
                // Sword does not have metadata
                if (!itemStack.getItemMeta().getPersistentDataContainer().has(swordData)){
                    itemStack.setItemMeta(newSwordMetaData(itemStack));
                }

                System.out.println(itemStack.getItemMeta().getPersistentDataContainer().has(swordData));

                // Add XP for kill
                ItemMeta meta = itemStack.getItemMeta();
                Sword heldSword = meta.getPersistentDataContainer().get(swordData, new SwordDataType());
                heldSword.addXP(2, killer);
                meta.getPersistentDataContainer().set(swordData, new SwordDataType(), heldSword);
                itemStack.setItemMeta(meta);

                // Update metadata
                itemStack.setItemMeta(updateSwordLore(itemStack.getItemMeta(), heldSword));
            }
        }
    }

    private static ItemMeta newSwordMetaData(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        meta.getPersistentDataContainer().set(swordData, new SwordDataType(), new Sword());
        Sword heldSword = meta.getPersistentDataContainer().get(swordData, new SwordDataType());
        return updateSwordLore(meta, heldSword);
    }

    private static ItemMeta updateSwordLore(ItemMeta meta, Sword sword){
        List<Component> loreComponents = new ArrayList<Component>(){
            {
                add(Component.text("Prestige: ")
                        .color(TextColor.color(0xAAAAAA)).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text(sword.getCurrentPrestige()).color(TextColor.color(0xFFAA00)).decoration(TextDecoration.ITALIC, false)));
                add(Component.text("Level: ")
                        .color(TextColor.color(0xAAAAAA)).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text(sword.getCurrentLevel()).color(TextColor.color(0xFFAA00)).decoration(TextDecoration.ITALIC, false)));
                add(Component.text("Next Level: ")
                        .color(TextColor.color(0xAAAAAA)).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text(sword.getCurrentXP() + "/" + sword.calculateRequiredXP(sword.getCurrentLevel())).color(TextColor.color(0xFFAA00)).decoration(TextDecoration.ITALIC, false)));
                add(Component.text(sword.getLevelName(sword.getCurrentLevel()) + " | " + "-".repeat((int) (10*sword.getCurrentXP()/sword.calculateRequiredXP(sword.getCurrentLevel()))))
                        .color(TextColor.color(0x00AA00)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true)
                        .append(Component.text("-".repeat(10-((int) (10*sword.getCurrentXP()/sword.calculateRequiredXP(sword.getCurrentLevel())))) + " | " + sword.getLevelName(sword.getCurrentLevel()+1)).color(TextColor.color(0xAAAAAA)).decoration(TextDecoration.BOLD, false).decoration(TextDecoration.ITALIC, false)));
            }
        };
        meta.lore(loreComponents);
        return meta;
    }
}
