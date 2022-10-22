package com.xshaffter.mishiprotect;

import com.xshaffter.mishiprotect.commands.MishiBook;
import com.xshaffter.mishiprotect.commands.handlers.AddPermission;
import com.xshaffter.mishiprotect.commands.handlers.RemovePermission;
import com.xshaffter.mishiprotect.database.Driver;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Mishiprotect extends JavaPlugin implements Listener {
    public static Mishiprotect plugin;
    public static Driver database;
    public FileConfiguration customConfig;
    private File dataFile;
    @Override
    public void onEnable() {
        plugin = this;
        registerCommands();
        registerEventListeners();
        saveDataFile();
        database = new Driver();
        database.connect();
        database.initializeBD();
        System.out.println("MishiProtect loaded!");
    }

    private void registerEventListeners() {
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getPluginManager().registerEvents(new AddPermission(), this);
        Bukkit.getPluginManager().registerEvents(new RemovePermission(), this);
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands() {
        getCommand("mishi_book").setExecutor(new MishiBook());
    }

    private void saveDataFile() {
        dataFile = new File(getDataFolder(), "config.yml");
        if (!dataFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dataFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Disable the Power enchantment
    }
}
