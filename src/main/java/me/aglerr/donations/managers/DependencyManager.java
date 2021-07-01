package me.aglerr.donations.managers;

import me.aglerr.donations.api.DonationExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class DependencyManager {

    public static boolean SKINS_RESTORER_ENABLED = false;
    public static boolean PLACEHOLDER_API_ENABLED = false;

    public static void checkDependencies(){
        PluginManager pm = Bukkit.getPluginManager();

        SKINS_RESTORER_ENABLED = pm.getPlugin("SkinsRestorer") != null;
        PLACEHOLDER_API_ENABLED = pm.getPlugin("PlaceholderAPI") != null;

        hookPlaceholderAPI();
    }

    protected static void hookPlaceholderAPI(){
        if(PLACEHOLDER_API_ENABLED)
            new DonationExpansion().register();
    }

}
