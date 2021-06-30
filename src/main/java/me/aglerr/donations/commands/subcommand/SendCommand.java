package me.aglerr.donations.commands.subcommand;

import me.aglerr.donations.DonationPlugin;
import me.aglerr.donations.commands.abstraction.SubCommand;
import me.aglerr.donations.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SendCommand extends SubCommand {

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public List<String> parseTabCompletion(DonationPlugin plugin, CommandSender sender, String[] args) {
        if(args.length == 2){
            return plugin.getProductManager().getListOfProductName();
        }
        return null;
    }

    @Override
    public void execute(DonationPlugin plugin, CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

    }

}
