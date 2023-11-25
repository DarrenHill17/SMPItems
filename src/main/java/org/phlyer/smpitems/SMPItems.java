package org.phlyer.smpitems;

import org.bukkit.plugin.java.JavaPlugin;

public final class SMPItems extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ItemsManager(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
