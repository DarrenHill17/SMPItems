package org.phlyer.smpitems;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Sword implements Serializable {
    private static final ArrayList<Integer> progression = new ArrayList<>(Arrays.asList(5, 10, 25, 50, 75, 100, 250, 500, 750, 1000));
    private int currentXP;
    private int currentLevel;
    private int currentPrestige;
    private String itemName;
    private int calculateRequiredXP(int level){
        return progression.get(level-1);
    }

    public Sword(String itemName){
        this.currentXP = 0;
        this.currentLevel = 0;
        this.currentPrestige = 0;
        this.itemName = itemName;
    }

    public void addXP(int gainedXP){
        currentXP += gainedXP;
        if (currentXP >= calculateRequiredXP(currentLevel)){
            currentXP -= calculateRequiredXP(currentLevel);
            currentLevel += 1;
            if (currentLevel >= progression.size()){
                currentPrestige += 1;
                currentLevel = 0;
                int tempXP = currentXP;
                currentXP = 0;
                addXP(tempXP);
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

    public String getItemName(){
        return itemName;
    }
}
