package me.aglerr.donations.commands.abstraction;

import me.aglerr.donations.DonationPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    public abstract String getPermission();

    public abstract List<String> parseTabCompletion(DonationPlugin plugin, CommandSender sender, String[] args);

    public abstract void execute(DonationPlugin plugin, CommandSender sender, String[] args);

}
