package me.aglerr.donations;

import me.aglerr.donations.libs.ImageMessage;
import me.aglerr.donations.managers.ConfigManager;
import me.aglerr.donations.objects.Product;
import me.aglerr.donations.objects.QueueDonation;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigValue {

    public static boolean USE_UUID;

    public static boolean BROADCAST_AVATAR_ENABLED;
    public static boolean BROADCAST_AVATAR_CENTERED;
    public static String LINE_1;
    public static String LINE_2;
    public static String LINE_3;
    public static String LINE_4;
    public static String LINE_5;
    public static String LINE_6;
    public static String LINE_7;
    public static String LINE_8;

    public static boolean BROADCAST_NO_AVATAR_CENTERED;
    public static List<String> BROADCAST_NO_AVATAR;

    public static void initialize() {
        FileConfiguration config = ConfigManager.CONFIG.getConfig();
        USE_UUID = config.getBoolean("options.useUUID");
        BROADCAST_AVATAR_ENABLED = config.getBoolean("donationsMessage.messageWithAvatar.enabled");
        BROADCAST_AVATAR_CENTERED = config.getBoolean("donationsMessage.messageWithAvatar.centered");
        LINE_1 = config.getString("donationsMessage.messageWithAvatar.messages.line1");
        LINE_2 = config.getString("donationsMessage.messageWithAvatar.messages.line2");
        LINE_3 = config.getString("donationsMessage.messageWithAvatar.messages.line3");
        LINE_4 = config.getString("donationsMessage.messageWithAvatar.messages.line4");
        LINE_5 = config.getString("donationsMessage.messageWithAvatar.messages.line5");
        LINE_6 = config.getString("donationsMessage.messageWithAvatar.messages.line6");
        LINE_7 = config.getString("donationsMessage.messageWithAvatar.messages.line7");
        LINE_8 = config.getString("donationsMessage.messageWithAvatar.messages.line8");
        BROADCAST_NO_AVATAR_CENTERED = config.getBoolean("donationsMessage.messageWithoutAvatar.centered");
        BROADCAST_NO_AVATAR = config.getStringList("donationsMessage.messageWithoutAvatar.messages");
    }

    public static String[] donationAvatar(QueueDonation donation) {
        return new String[]{
                parseProduct(LINE_1, donation.getProduct()),
                parseProduct(LINE_2, donation.getProduct()),
                parseProduct(LINE_3, donation.getProduct()),
                parseProduct(LINE_4, donation.getProduct()),
                parseProduct(LINE_5, donation.getProduct()),
                parseProduct(LINE_6, donation.getProduct()),
                parseProduct(LINE_7, donation.getProduct()),
                parseProduct(LINE_8, donation.getProduct())
        };

    }

    public static List<String> donationNoAvatar(QueueDonation donation){
        List<String> messages = new ArrayList<>();
        BROADCAST_NO_AVATAR.forEach(message -> messages.add(parseProduct(message, donation.getProduct())));
        return messages;
    }

    public static String parseProduct(String string, Product product) {
        return string
                .replace("{product_name}", product.getName())
                .replace("{product_displayname}", product.getDisplayName())
                .replace("{product_price}", product.getPrice() + "");
    }

}
