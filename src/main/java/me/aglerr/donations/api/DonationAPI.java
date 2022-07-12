package me.aglerr.donations.api;

import me.aglerr.donations.DonationPlugin;
import me.aglerr.donations.managers.ConfigManager;
import me.aglerr.donations.managers.ProductManager;
import me.aglerr.donations.objects.Product;
import me.aglerr.donations.objects.QueueDonation;
import me.aglerr.mclibs.libs.CustomConfig;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DonationAPI {

    /**
     * Get a {@link me.aglerr.donations.objects.Product} object
     *
     * @param productName - the product name
     * @return A {@link me.aglerr.donations.objects.Product} object to perform a donation
     */
    @Nullable
    public static Product getProduct(String productName){
        // Get the product manager
        ProductManager productManager = DonationPlugin.getInstance().getProductManager();
        // Get the product from the list, will return null if it doesn't exist
        return productManager.getProduct(productName);
    }

    /**
     * Add an entry to the donation's queue manager
     *
     * @param player - A {@link org.bukkit.OfflinePlayer}
     * @param product - A {@link me.aglerr.donations.objects.Product}
     */
    public static void performDonation(OfflinePlayer player, @NotNull Product product){
        performDonation(new QueueDonation(player, product));
    }

    /**
     * Add an entry to the donation's queue manager
     *
     * @param donation - A {@link me.aglerr.donations.objects.QueueDonation}
     */
    public static void performDonation(QueueDonation donation){
        DonationPlugin.getInstance().getQueueManager()
                .addQueue(donation);
    }

    /**
     * Add a new product to this plugin, product can be used
     * when you perform /donations send command
     *
     * @param name - the name of the product (used in getProduct method)
     * @param displayName - the display name of the product
     * @param price - the price of the product
     * @param save - should we save the product to the configuration? (Recommended if you want product persist on restart)
     */
    public static Product createProduct(String name, String displayName, double price, boolean save){
        // Get the product manager
        ProductManager productManager = DonationPlugin.getInstance().getProductManager();
        // Create the product object
        Product product = new Product(name, displayName, price);
        // Add product to the list
        productManager.addProduct(product);
        // Should we save the product to the configuration?
        // Recommended if you want it to persist on restart
        if(save){
            CustomConfig productConfig = ConfigManager.PRODUCT;
            FileConfiguration config = productConfig.getConfig();
            // Set the display name
            config.set("products." + name + ".displayName", displayName);
            // Set the price
            config.set("products." + name + ".price", price);
            // Finally save the config
            productConfig.saveConfig();
        }
        return product;
    }

}
