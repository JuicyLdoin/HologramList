package ua.Ldoin.HologramList;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main plugin;
    public static FileConfiguration config;

    public void onEnable() {

        plugin = this;

        if (!getDataFolder().isDirectory())
            saveDefaultConfig();

        config = getConfig();

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);

        getCommand("hologramlist").setExecutor(new Commands());

        Hologram.loadHolograms();

    }

    public static void sendMessage(CommandSender sender, String message) {

        if (config.getBoolean("message.enabled"))
            sender.sendMessage(message.replace("&", "§"));

    }

    public void onDisable() {

        Hologram.saveHolograms();

    }
}