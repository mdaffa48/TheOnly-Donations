package me.aglerr.donations.utils;

import de.themoep.minedown.MineDown;
import me.aglerr.donations.ConfigValue;
import me.aglerr.donations.DonationPlugin;
import me.aglerr.donations.libs.ImageChar;
import me.aglerr.donations.libs.ImageMessage;
import me.aglerr.donations.libs.ImageMessageHex;
import me.aglerr.donations.managers.DependencyManager;
import me.aglerr.donations.objects.QueueDonation;
import me.aglerr.lazylibs.libs.Common;
import net.skinsrestorer.api.SkinsRestorerAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Utils {

    public static String[] getStartupLogo() {
        return new String[]{
                "  _______ _             ____        _       ",
                " |__   __| |           / __ \\      | |      ",
                "    | |  | |__   ___  | |  | |_ __ | |_   _ ",
                "    | |  | '_ \\ / _ \\ | |  | | '_ \\| | | | |",
                "    | |  | | | |  __/ | |__| | | | | | |_| |",
                "    |_|  |_| |_|\\___|  \\____/|_| |_|_|\\__, |",
                "                                       __/ |",
                "                                      |___/ ",
                "  ",
                "                Donations edition",
                "            Thanks for using and enjoy!",
                "  "
        };
    }

    public static void sendStartupLogo() {
        for (String message : getStartupLogo()) {
            System.out.println(message);
        }
    }

    public static String getMinepicURL(Player player) {
        return getMinepicURL(Bukkit.getOfflinePlayer(player.getUniqueId()));
    }

    public static String getMinepicURL(OfflinePlayer player) {
        String url = "https://minepic.org/avatar/8/";
        // Check if SkinsRestorer is enabled
        if (DependencyManager.SKINS_RESTORER_ENABLED) {
            // Get the SkinsRestorerAPI
            SkinsRestorerAPI api = SkinsRestorerAPI.getApi();
            // If the player is wearing skin, get the skin name
            url = url + (api.hasSkin(player.getName()) ? api.getSkinName(player.getName()) : player.getName());
        } else {
            // Code if the server doesn't use skins restorer
            url = url + (ConfigValue.USE_UUID ? player.getUniqueId().toString() : player.getName());
        }
        return url;
    }

    /**
     * Note: this method should be run in async
     */
    public static void broadcastDonation(QueueDonation donation) {
        try {
            // Get the URL
            URL url = new URL(getMinepicURL(donation.getPlayer()));
            // Get the buffered image from the url
            BufferedImage image = ImageIO.read(url);
            // Check if broadcast avatar is enabled
            if(ConfigValue.BROADCAST_AVATAR_ENABLED){
                // Check if hex color is enabled
                if(DonationPlugin.HEX_AVAILABLE){
                    // Create an image message with hex color
                    ImageMessageHex imageMessageHex = new ImageMessageHex(image, 8, ImageChar.BLOCK.getChar());
                    // Append the additional text, centered or not
                    imageMessageHex = ConfigValue.BROADCAST_AVATAR_CENTERED ?
                            imageMessageHex.appendCenteredText(ConfigValue.donationAvatar(donation)) :
                            imageMessageHex.appendText(ConfigValue.donationAvatar(donation));
                    // Finally broadcast the messages
                    imageMessageHex.sendToPlayers();
                } else {
                    // Now, we do the code if the hex color isn't available
                    ImageMessage imageMessage = new ImageMessage(image, 8, ImageChar.BLOCK.getChar());
                    // Append the additional text, centered or not
                    imageMessage = ConfigValue.BROADCAST_AVATAR_CENTERED ?
                            imageMessage.appendCenteredText(ConfigValue.donationAvatar(donation)) :
                            imageMessage.appendText(ConfigValue.donationAvatar(donation));
                    // Finally broadcast the message
                    imageMessage.sendToPlayers();
                }
            } else {
                //---------------------------------------------
                // Code when avatar message is disabled
                //---------------------------------------------
                // Check if the hex color is enabled
                if(DonationPlugin.HEX_AVAILABLE){
                    // Send the messages with hex color supports
                    Bukkit.getOnlinePlayers().forEach(player -> ConfigValue.donationNoAvatar(donation).forEach(message ->
                            player.spigot().sendMessage(MineDown.parse(message))));
                } else {
                    // Broadcast the message without hex color
                    ConfigValue.donationNoAvatar(donation).forEach(message ->
                            Bukkit.broadcastMessage(Common.color(message)));
                }
            }
        } catch (IOException ex) {
            Common.log(ChatColor.RED, "Failed to send a broadcast donation!");
            ex.printStackTrace();
        }
    }

}
