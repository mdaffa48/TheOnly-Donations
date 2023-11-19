package me.aglerr.donations;

import me.aglerr.donations.commands.MainCommand;
import me.aglerr.donations.managers.*;
import me.aglerr.donations.metrics.Metrics;
import me.aglerr.donations.utils.Utils;
import me.aglerr.mclibs.MCLibs;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.libs.ConfigUpdater;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DonationPlugin extends JavaPlugin {

    public static boolean HEX_AVAILABLE = false;

    private final ProductManager productManager = new ProductManager();
    private QueueManager queueManager;

    private static DonationPlugin instance;
    private static SkinsRestorer skinsApi;

    @Override
    public void onEnable(){
        // Initialize the instance
        instance = this;
        skinsApi = SkinsRestorerProvider.get();
        // Injecting the libs
        MCLibs.init(this);
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
        // bStats metrics
        new Metrics(this, 10310);
        HEX_AVAILABLE = Bukkit.getVersion().contains("1.16") ||
                Bukkit.getVersion().contains("1.17") ||
                Bukkit.getVersion().contains("1.18") ||
                Bukkit.getVersion().contains("1.19");
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

    protected void updateConfig(){
        File file = new File(this.getDataFolder(), "config.yml");
        try{
            ConfigUpdater.update(this, "config.yml", file, new ArrayList<>());
        } catch (IOException e) {
            Common.log("&cFailed to update the config.yml");
            e.printStackTrace();
        }
    }

    public static DonationPlugin getInstance() {
        return instance;
    }

    public static SkinsRestorer getSkinsApi() {
        return skinsApi;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }
}
