package me.aglerr.donations.utils;

import com.muhammaddaffa.mdlib.utils.Common;
import com.muhammaddaffa.mdlib.xseries.XSound;
import me.aglerr.donations.DonationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Optional;

public class Events {

    private static final DonationPlugin plugin = JavaPlugin.getPlugin(DonationPlugin.class);

    public static void playAllEvents(OfflinePlayer player){
        eventEffects();
        eventSound();
        eventTitleBar(player);
        eventCommand(player);
    }

    public static void eventEffects(){
        FileConfiguration config = DonationPlugin.DEFAULT_CONFIG.getConfig();
        // Return if the effect event is disabled
        if(!config.getBoolean("events.effect.enabled")) {
            return;
        }
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
        FileConfiguration config = DonationPlugin.DEFAULT_CONFIG.getConfig();
        // Get all the sounds
        List<String> sounds = config.getStringList("events.sounds");
        // Loop through all online players
        Bukkit.getOnlinePlayers().forEach(player -> {
            // Loop through all sounds
            for(String soundString : sounds){
                String[] section = soundString.split(";");
                // Get the sound type
                Optional<XSound> xSound = XSound.matchXSound(section[0]);
                // Skip if the sound type doesn't exist
                if (!xSound.isPresent()) {
                    continue;
                }
                Sound sound = xSound.get().parseSound();
                // If the sound still not exist, skip
                if (sound == null) {
                    continue;
                }
                // Get the volume of the sound
                double volume = Double.parseDouble(section[1]);
                // Get the pitch of the sound
                double pitch = Double.parseDouble(section[2]);
                // Play the sound
                player.playSound(player.getLocation(), sound, (float) volume, (float) pitch);
            }
        });
    }

    public static void eventTitleBar(OfflinePlayer offlinePlayer){
        FileConfiguration config = DonationPlugin.DEFAULT_CONFIG.getConfig();
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
            Common.sendTitle(player, title, subTitle, fadeIn, stay, fadeOut);
        });
    }

    public static void eventCommand(OfflinePlayer offlinePlayer){
        FileConfiguration config = DonationPlugin.DEFAULT_CONFIG.getConfig();
        // Return if the command event is disabled
        if(!config.getBoolean("events.command.enabled")) return;
        // Loop through all the commands
        config.getStringList("events.command.commands").forEach(command ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                        .replace("{player}", offlinePlayer.getName())));
    }

}
