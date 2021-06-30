package me.aglerr.donations;

import me.aglerr.donations.commands.MainCommand;
import me.aglerr.donations.managers.ConfigManager;
import me.aglerr.donations.managers.ProductManager;
import me.aglerr.donations.utils.Utils;
import me.aglerr.lazylibs.LazyLibs;
import me.aglerr.lazylibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DonationPlugin extends JavaPlugin {

    public static boolean HEX_AVAILABLE = true;

    private final ProductManager productManager = new ProductManager();

    @Override
    public void onEnable(){
        // Injecting the libs
        LazyLibs.inject(this);
        // Set the prefix of console messages
        Common.setPrefix("[TheOnly-Donations]");
        // Send startup logo
        Utils.sendStartupLogo();
        // Initialize all config
        ConfigManager.initialize();
        // Initialize all config value
        ConfigValue.initialize();
        // Load all products
        productManager.loadProduct();
        // Register the command
        new MainCommand(this).registerThisCommand();
        // Hex color check
        HEX_AVAILABLE = versionCheck();
    }

    private boolean versionCheck(){
        return Bukkit.getVersion().contains("1.16") ||
                Bukkit.getVersion().contains("1.17");
    }

    public ProductManager getProductManager() {
        return productManager;
    }
}
