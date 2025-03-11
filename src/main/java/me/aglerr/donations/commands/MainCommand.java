package me.aglerr.donations.commands;

import com.muhammaddaffa.mdlib.commandapi.CommandAPICommand;
import com.muhammaddaffa.mdlib.commandapi.arguments.ArgumentSuggestions;
import com.muhammaddaffa.mdlib.commandapi.arguments.OfflinePlayerArgument;
import com.muhammaddaffa.mdlib.commandapi.arguments.PlayerArgument;
import com.muhammaddaffa.mdlib.commandapi.arguments.StringArgument;
import com.muhammaddaffa.mdlib.utils.Common;
import com.muhammaddaffa.mdlib.utils.Placeholder;
import me.aglerr.donations.ConfigValue;
import me.aglerr.donations.DonationPlugin;
import me.aglerr.donations.commands.abstraction.SubCommand;
import me.aglerr.donations.commands.subcommand.ReloadCommand;
import me.aglerr.donations.commands.subcommand.SendCommand;
import me.aglerr.donations.managers.ProductManager;
import me.aglerr.donations.managers.QueueManager;
import me.aglerr.donations.objects.Product;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MainCommand {

    private CommandAPICommand command;

    public MainCommand() {
        this.command = new CommandAPICommand("donations")
                .withPermission("donations.admin")
                .executes((sender, args) -> {
                    if (sender.hasPermission("donations.admin")) {
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(sender, "messages.help");
                    } else {
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(sender, "messages.noPermission");
                    }
                });

        // Register the SubCommand
        this.getSubCommandSend();
        this.getSubCommandReload();
        this.getSubCommandReset();

        // Register the command
        this.command.register(DonationPlugin.getInstance());
    }

    private void getSubCommandSend() {
        this.command.withSubcommand(new CommandAPICommand("send")
                        .withArguments(new OfflinePlayerArgument("target"))
                        .withArguments(new StringArgument("product")
                                .replaceSuggestions(ArgumentSuggestions.strings(info -> {
                                    return DonationPlugin.getInstance().getProductManager()
                                            .getListOfProductName().toArray(String[]::new);
                                })))
                .withPermission("donations.admin")
                .executes((sender, args) -> {
                    if (!(sender.hasPermission("donations.admin"))) {
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(sender, "messages.noPermission");
                        return;
                    }

                    // Get the product manager
                    ProductManager productManager = DonationPlugin.getInstance().getProductManager();

                    // Get all Player Variable
                    OfflinePlayer target = (OfflinePlayer) args.get("target");
                    String string = (String) args.get("product");

                    // Get the product from the command argument
                    Product product = productManager.getProduct(string);
                    // Return if there is no product with that name
                    if (product == null){
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(sender, "messages.invalidProduct");
                        return;
                    }
                    // If the product is existed, add the donation to the queue
                    // First, get the queue manager
                    QueueManager queueManager = DonationPlugin.getInstance().getQueueManager();
                    // Finally, add the donation to the queue
                    queueManager.addQueue(target, product);
                    // Send a success message
                    DonationPlugin.DEFAULT_CONFIG.sendMessage(sender, "messages.performDonation", new Placeholder()
                            .add("{player}", target.getName()));
                }));
    }

    private void getSubCommandReload() {
        this.command.withSubcommand(new CommandAPICommand("reload")
                .withPermission("donations.admin")
                .executes((sender, args) -> {
                    if (!(sender.hasPermission("donations.admin"))) {
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(sender, "messages.noPermission");
                        return;
                    }
                    DonationPlugin.getInstance().reloadEverything();
                    DonationPlugin.DEFAULT_CONFIG.sendMessage(sender, "messages.reload");
                }));
    }

    private void getSubCommandReset() {
        this.command.withSubcommand(new CommandAPICommand("reset")
                .withPermission("donations.admin")
                .executes((sender, args) -> {
                    if (!(sender.hasPermission("donations.admin"))) {
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(sender, "messages.noPermission");
                        return;
                    }
                    DonationPlugin.getInstance().resetDonation();
                    DonationPlugin.DEFAULT_CONFIG.sendMessage(sender, "messages.reset");
                }));
    }
}
