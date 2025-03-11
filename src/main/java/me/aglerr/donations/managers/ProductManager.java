package me.aglerr.donations.managers;

import com.muhammaddaffa.mdlib.utils.Common;
import com.muhammaddaffa.mdlib.utils.Logger;
import me.aglerr.donations.DonationPlugin;
import me.aglerr.donations.objects.Product;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class ProductManager {

    private final Map<String, Product> productList = new HashMap<>();

    @Nullable
    public Product getProduct(String product) {
        return this.productList.get(product);
    }

    public void addProduct(String id, Product product){
        this.productList.put(id, product);
    }

    public Set<String> getListOfProductName(){
        return this.productList.keySet();
    }

    public void reloadProduct(){
        // Clear all loaded product
        productList.clear();
        // Load them back up
        loadProduct();
    }

    public void loadProduct() {
        Logger.info("Starting to load all products...");
        // load all product inside product.yml
        this.loadProduct(DonationPlugin.PRODUCT_CONFIG.getConfig());
        // send log message
        Logger.info("Successfully loaded " + this.productList.size() + " product!");
    }

    public void loadProduct(FileConfiguration config) {
        ConfigurationSection productSection = config.getConfigurationSection("products");
        if (productSection == null) {
            Logger.info("&cNo products found in config!");
            return;
        }

        for (String id : productSection.getKeys(false)) {
            ConfigurationSection section = productSection.getConfigurationSection(id);
            if (section == null) {
                continue;
            }
            this.loadProduct(id, section);
        }
    }


    public void loadProduct(String id, ConfigurationSection section) {
        String displayName = section.getString("displayName");
        double price = section.getDouble("price");

        // Register all product inside
        Product product = new Product(id, displayName, price);
        this.productList.put(id, product);
        Logger.info("Successfully loaded '" +id+ "'");
    }



        /*public Product getProduct(String productName){
        return productList.stream().filter(product -> product.getName().equalsIgnoreCase(productName))
                .findAny().orElse(null);
        }*/


        /*public void loadProduct(){
        FileConfiguration config = DonationPlugin.PRODUCT_CONFIG.getConfig();
        // Send a console message
        Logger.info("&rLoading all products...");
        // Return if there are no products
        if(!config.isConfigurationSection("products")){
            Logger.info("&cFailed, because there are no products!");
            return;
        }
        // If there are products, loop through the config section
        for (String productName : config.getConfigurationSection("products").getKeys(false)){
            String displayName = config.getString("products." + productName + ".displayName");
            double price = config.getDouble("products." + productName + ".price");
            // After getting all the values, store them into the list
            productList.add(new Product(productName, displayName, price));
            // Log to the console
            Logger.info("&rLoaded " + productName + " product.");
        }
        Logger.info("&rSuccessfully loaded all products!");
    }*/
}
