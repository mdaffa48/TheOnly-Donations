package me.aglerr.donations.utils;

import me.aglerr.donations.DonationPlugin;
import me.aglerr.lazylibs.LazyLibs;
import me.aglerr.lazylibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Events {

    private static final DonationPlugin plugin = (DonationPlugin) LazyLibs.getInstance();

    public static void playAllEvents(OfflinePlayer player){
        eventEffects();
        eventSound();
        eventTitleBar(player);
        eventCommand(player);
    }

    public static void eventEffects(){
        FileConfiguration config = plugin.getConfig();
        // Return if the effect event is disabled
        if(!config.getBoolean("events.effect.enabled")) return;
        // Get all the effects
        List<String> effects = config.getStringList("events.effect.effects");
        // Loop through all online players
        Bukkit.getOnlinePlayers().forEach(player -> {
            // Loop through all effects
            for(String effectString : effects){
                String[] effectSection = effectString.split(";");
                // Get the effect type
                PotionEffectType effectType = PotionEffectType.getByName(effectSection[0]);
                // Skip if the effect type is doesn't exist
                if(effectType == null) continue;
                // Get the amplifier of the effect
                int amplifier = Integer.parseInt(effectSection[1]) + 1;
                // Get the duration in seconds
                int duration = Integer.parseInt(effectSection[2]) * 20;
                // Create the potion effect
                PotionEffect potionEffect = new PotionEffect(effectType, duration, amplifier);
                // First, remove all potion effect if exist
                player.removePotionEffect(effectType);
                // Add the potion effect to the player
                player.addPotionEffect(potionEffect);
            }
        });
    }

    public static void eventSound(){
        FileConfiguration config = plugin.getConfig();
        // Return if the sound event is disabled
        if(!config.getBoolean("events.sound.enabled")) return;
        // Get the sound name from the config
        Sound sound = Sound.valueOf(config.getString("events.sound.sound"));
        // Get the volume
        float volume = (float) config.getDouble("events.sound.volume");
        // Get the pitch
        float pitch = (float) config.getDouble("events.sound.pitch");
        // Loop through all online players
        Bukkit.getOnlinePlayers().forEach(player -> {
            // Play the sound
            player.playSound(player.getLocation(), sound, volume, pitch);
        });
    }

    public static void eventTitleBar(OfflinePlayer offlinePlayer){
        FileConfiguration config = plugin.getConfig();
        // Return if the title bar event is disabled
        if(!config.getBoolean("events.titleBar.enabled")) return;
        // Get the title
        String title = Common.color(config.getString("events.titleBar.title")
                .replace("{player}", offlinePlayer.getName()));
        // Get the subtitle
        String subTitle = Common.color(config.getString("events.titleBar.subTitle")
                .replace("{player}", offlinePlayer.getName()));
        // Get the fade in animation duration
        int fadeIn = config.getInt("events.titleBar.fadeIn");
        // Get the stay duration
        int stay = config.getInt("events.titleBar.stay");
        // Get the fade out animation duration
        int fadeOut = config.getInt("events.titleBar.fadeOut");
        // Loop through all online players
        Bukkit.getOnlinePlayers().forEach(player -> {
            // Send the title bar messages
            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
        });
    }

    public static void eventCommand(OfflinePlayer offlinePlayer){
        FileConfiguration config = plugin.getConfig();
        // Return if the command event is disabled
        if(!config.getBoolean("events.command.enabled")) return;
        // Loop through all the commands
        config.getStringList("events.command.commands").forEach(command ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                        .replace("{player}", offlinePlayer.getName())));
    }

}
