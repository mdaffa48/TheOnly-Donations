package me.aglerr.donations.commands.subcommand;

import me.aglerr.donations.ConfigValue;
import me.aglerr.donations.DonationPlugin;
import me.aglerr.donations.commands.abstraction.SubCommand;
import me.aglerr.donations.managers.ProductManager;
import me.aglerr.donations.managers.QueueManager;
import me.aglerr.donations.objects.Product;
import me.aglerr.lazylibs.libs.Common;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SendCommand extends SubCommand {

    @Override
    public String getPermission() {
        return "donations.admin";
    }

    @Override
    public List<String> parseTabCompletion(DonationPlugin plugin, CommandSender sender, String[] args) {
        if(args.length == 2){
            return Common.getOnlinePlayersByName();
        }
        if(args.length == 3){
            return plugin.getProductManager().getListOfProductName();
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(DonationPlugin plugin, CommandSender sender, String[] args) {
        if(args.length < 3){
            sender.sendMessage(Common.color("&cUsage: /donations send <player> <product>"));
            return;
        }
        // Get the product manager
        ProductManager productManager = plugin.getProductManager();
        // Get the player from the command argument
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        // Get the product from the command argument
        Product product = productManager.getProduct(args[2]);
        // Return if there is no product with that name
        if(product == null){
            sender.sendMessage(Common.color(ConfigValue.INVALID_PRODUCT));
            return;
        }
        // If the product is exist, add the donation to the queue
        // First, get the queue manager
        QueueManager queueManager = plugin.getQueueManager();
        // Finally, add the donation to the queue
        queueManager.addQueue(player, product);
        // Send a success message
        sender.sendMessage(Common.color(ConfigValue.PERFORM_DONATION
                .replace("{player}", args[1])));
    }

}
