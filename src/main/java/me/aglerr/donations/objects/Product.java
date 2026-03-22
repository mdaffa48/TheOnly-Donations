package me.aglerr.donations.objects;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {

    private final String name;
    private final String displayName;
    private final double price;
    private final List<String> command;

    public Product(String name, String displayName, double price, List<String> command) {
        this.name = name;
        this.displayName = displayName;
        this.price = price;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getCommand() {
        return command;
    }

    public void execute(OfflinePlayer player) {
        for (String command : this.command) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                    .replace("{player}", player.getName()));
        }
    }
}
