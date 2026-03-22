package me.aglerr.donations.managers;

import com.muhammaddaffa.mdlib.utils.Logger;
import me.aglerr.donations.DonationPlugin;
import me.aglerr.donations.objects.Product;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

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
        String basePath = section.getCurrentPath(); // e.g. "products.vip"
        List<String> errors = new ArrayList<>();

        String displayName = section.getString("displayName", null);
        if (displayName == null || displayName.isBlank()) {
            errors.add(basePath + ".displayName is missing or empty");
        }

        Object rawPrice = section.get("price");
        Double price = null;
        if (rawPrice == null) {
            errors.add(basePath + ".price is missing");
        } else if (rawPrice instanceof Number number) {
            price = number.doubleValue();
            if (price < 0) errors.add(basePath + ".price must be >= 0 (got " + price + ")");
        } else {
            errors.add(basePath + ".price must be a number (got " + rawPrice.getClass().getSimpleName() + ")");
        }

        List<String> commandList = section.getStringList("command");
        if (commandList == null || commandList.isEmpty()) {
            errors.add(basePath + ".command is missing or empty (must be a list of commands)");
        }

        // If errors exist, print them and SKIP loading this product
        if (!errors.isEmpty()) {
            Logger.info("&cFailed to load product '" + id + "':");
            for (String error : errors) {
                Logger.info("&7- &c" + error);
            }
            return;
        }

        // Safe to load now
        Product product = new Product(id, displayName, price, commandList);
        this.productList.put(id, product);
        Logger.info("Successfully loaded '" + id + "'");
    }

}
