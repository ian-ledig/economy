package com.economy;

import com.economy.command.MoneyCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        databaseManager = new DatabaseManager();
        databaseManager.connect();

        getCommand("money").setExecutor(new MoneyCommand(databaseManager));
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
    }
}