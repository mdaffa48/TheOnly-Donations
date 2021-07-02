package me.aglerr.donations;

import me.aglerr.donations.commands.MainCommand;
import me.aglerr.donations.managers.*;
import me.aglerr.donations.metrics.Metrics;
import me.aglerr.donations.utils.Utils;
import me.aglerr.lazylibs.LazyLibs;
import me.aglerr.lazylibs.libs.Common;
import me.aglerr.lazylibs.libs.ConfigUpdater;
import me.aglerr.lazylibs.libs.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DonationPlugin extends JavaPlugin {

    private static int RESOURCE_ID = 73815;

    public static boolean HEX_AVAILABLE = false;

    private final ProductManager productManager = new ProductManager();
    private QueueManager queueManager;

    private static DonationPlugin instance;

    @Override
    public void onEnable(){
        // Initialize the instance
        instance = this;
        // Injecting the libs
        LazyLibs.inject(this);
        // Set the prefix of console messages
        Common.setPrefix("[TheOnly-Donations]");
        // Send startup logo
        Utils.sendStartupLogo();
        // Initialize the queue manager
        queueManager = new QueueManager();
        // Check the dependencies
        DependencyManager.checkDependencies();
        // Initialize all config
        ConfigManager.initialize();
        // Try to update the configuration
        updateConfig();
        // Initialize all config value
        ConfigValue.initialize();
        // Load all products
        productManager.loadProduct();
        // Load the donation goal
        DonationGoal.onLoad();
        // Register the command
        new MainCommand(this).registerThisCommand();
        // Hex color check
        HEX_AVAILABLE = versionCheck();
        // bStats metrics
        new Metrics(this, 10310);
        // Run update checker
        spigotUpdateChecker();
    }

    @Override
    public void onDisable(){
        DonationGoal.onSave();
    }

    public void reloadEverything(){
        // Reload all configs
        ConfigManager.CONFIG.reloadConfig();
        ConfigManager.PRODUCT.reloadConfig();
        // Re-initialize the config value
        ConfigValue.initialize();
        // Reload all products
        productManager.reloadProduct();
        // Reload the donation goal
        DonationGoal.reloadDonationGoal();
    }

    protected void spigotUpdateChecker(){
        UpdateChecker.init(this, RESOURCE_ID)
                .setDownloadLink("https://www.spigotmc.org/resources/73815/")
                .setDonationLink("https://paypal.me/mdaffa48")
                .setColoredConsoleOutput(true)
                .setNotifyOpsOnJoin(true)
                .checkNow();
    }

    protected void updateConfig(){
        File file = new File(this.getDataFolder(), "config.yml");
        try{
            ConfigUpdater.update(this, "config.yml", file, new ArrayList<>());
        } catch (IOException e) {
            Common.log(ChatColor.RED, "Failed to update the config.yml");
            e.printStackTrace();
        }
    }

    protected boolean versionCheck(){
        return Bukkit.getVersion().contains("1.16") ||
                Bukkit.getVersion().contains("1.17");
    }

    public static DonationPlugin getInstance() {
        return instance;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }
}
