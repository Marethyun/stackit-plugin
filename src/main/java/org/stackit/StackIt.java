package org.stackit;

import org.bukkit.plugin.java.JavaPlugin;

public class StackIt extends JavaPlugin {

    private static StackIt instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static StackIt getInstance(){
        return instance;
    }
}
