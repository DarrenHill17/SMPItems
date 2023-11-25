package org.phlyer.smpitems;

import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Sword implements Serializable {
    private static final ArrayList<Integer> progression = new ArrayList<>(Arrays.asList(5, 10, 25, 50, 75, 100, 250, 500, 750, 1000));
    private int currentXP;
    private int currentLevel;
    private int currentPrestige;
    public int calculateRequiredXP(int level){
        return progression.get(level);
    }

    public String getLevelName(int level){
        if (level <= 9 && level >= 0) return String.valueOf(level);
        else if (level == 10) return "P";
        return null;
    }

    public Sword(){
        this.currentXP = 0;
        this.currentLevel = 0;
        this.currentPrestige = 0;
    }

    public void addXP(int gainedXP, Player player){
        currentXP += gainedXP;
        if (currentXP >= calculateRequiredXP(currentLevel)){
            currentXP -= calculateRequiredXP(currentLevel);
            currentLevel += 1;
            player.sendMessage(Component.text("Your sword just leveled up!"));
            if (currentLevel >= progression.size()){
                currentPrestige += 1;
                currentLevel = 0;
                int tempXP = currentXP;
                currentXP = 0;
                player.sendMessage(Component.text("Your sword just prestiged!"));
                addXP(tempXP, player);
            }
        }
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getCurrentPrestige() {
        return currentPrestige;
    }
}
