package me.aglerr.donations.managers;

import me.aglerr.donations.objects.Product;
import me.aglerr.lazylibs.libs.Common;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {

    private final List<Product> productList = new ArrayList<>();

    public Product getProduct(String productName){
        return productList.stream().filter(product -> product.getName().equalsIgnoreCase(productName))
                .findAny().orElse(null);
    }

    public List<String> getListOfProductName(){
        List<String> productsName = new ArrayList<>();
        this.productList.forEach(product -> productsName.add(product.getName()));
        return productsName;
    }

    public void reloadProduct(){
        // Clear all loaded product
        productList.clear();
        // Load them back up
        this.loadProduct();
    }

    public void loadProduct(){
        FileConfiguration config = ConfigManager.PRODUCT.getConfig();
        // Send a console message
        Common.log(ChatColor.WHITE, "Loading all products...");
        // Return if there are no products
        if(!config.isConfigurationSection("products")){
            Common.log(ChatColor.RED, "Failed, because there are no products!");
            return;
        }
        // If there are products, loop through the config section
        for(String productName : config.getConfigurationSection("products").getKeys(false)){
            String displayName = config.getString("products." + productName + ".displayName");
            double price = config.getDouble("products." + productName + ".price");
            // After getting all the values, store them into the list
            productList.add(new Product(productName, displayName, price));
        }
    }

}
