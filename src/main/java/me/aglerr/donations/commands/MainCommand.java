package me.aglerr.donations.commands;

import com.muhammaddaffa.mdlib.commands.args.ArgSuggester;
import com.muhammaddaffa.mdlib.commands.args.builtin.OfflinePlayerArg;
import com.muhammaddaffa.mdlib.commands.args.builtin.StringArg;
import com.muhammaddaffa.mdlib.commands.commands.RoutedCommand;
import com.muhammaddaffa.mdlib.utils.Placeholder;
import me.aglerr.donations.DonationPlugin;
import me.aglerr.donations.managers.ProductManager;
import me.aglerr.donations.managers.QueueManager;
import me.aglerr.donations.objects.Product;
import org.bukkit.OfflinePlayer;

import java.util.*;

public class MainCommand extends RoutedCommand {

    public MainCommand() {
        super("donations");

        root()
                .perm("donations.admin")
                .exec((commandSender, commandContext) -> {
                    if (commandSender.hasPermission("donations.admin")) {
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(commandSender, "messages.help");
                    } else {
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(commandSender, "messages.noPermission");
                    }
                });

        // Register the SubCommand
        this.getSubCommandSend();
        this.getSubCommandReload();
        this.getSubCommandReset();
    }

    private void getSubCommandSend() {
        sub("send")
                .perm("donations.admin")
                .arg("target", new OfflinePlayerArg())
                .arg("product", new StringArg(), ArgSuggester.ofDynamic((commandSender, string) -> {
                        return new ArrayList<>(DonationPlugin.getInstance().getProductManager()
                                .getListOfProductName());
                }))
                .exec((commandSender, commandContext) -> {
                    // Get the product manager
                    ProductManager productManager = DonationPlugin.getInstance().getProductManager();
                    // Get all Player Variable
                    OfflinePlayer target = commandContext.get("target", OfflinePlayer.class);
                    String string = commandContext.get("product", String.class);
                    // Get the product from the command argument
                    Product product = productManager.getProduct(string);
                    // Return if there is no product with that name
                    if (product == null){
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(commandSender, "messages.invalidProduct");
                        return;
                    }
                    // If the product is existed, add the donation to the queue
                    // First, get the queue manager
                    QueueManager queueManager = DonationPlugin.getInstance().getQueueManager();
                    // Finally, add the donation to the queue
                    queueManager.addQueue(target, product);
                    // Send a success message
                    DonationPlugin.DEFAULT_CONFIG.sendMessage(commandSender, "messages.performDonation", new Placeholder()
                            .add("{player}", target.getName()));
                });
    }

    private void getSubCommandReload() {
        sub("reload")
                .perm("donations.admin")
                .exec((commandSender, commandContext) -> {
                    if (!(commandSender.hasPermission("donations.admin"))) {
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(commandSender, "messages.noPermission");
                        return;
                    }
                    DonationPlugin.getInstance().reloadEverything();
                    DonationPlugin.DEFAULT_CONFIG.sendMessage(commandSender, "messages.reload");
                });
    }

    private void getSubCommandReset() {
        sub("reset")
                .perm("donations.admin")
                .exec((commandSender, commandContext) -> {
                    if (!(commandSender.hasPermission("donations.admin"))) {
                        DonationPlugin.DEFAULT_CONFIG.sendMessage(commandSender, "messages.noPermission");
                        return;
                    }
                    DonationPlugin.getInstance().resetDonation();
                    DonationPlugin.DEFAULT_CONFIG.sendMessage(commandSender, "messages.reset");
                });
    }
}
