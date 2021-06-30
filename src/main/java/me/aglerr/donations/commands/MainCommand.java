package me.aglerr.donations.commands;

import me.aglerr.donations.DonationPlugin;
import me.aglerr.donations.commands.abstraction.SubCommand;
import me.aglerr.donations.commands.subcommand.SendCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommandMap = new HashMap<>();

    private final DonationPlugin plugin;
    public MainCommand(DonationPlugin plugin){
        this.plugin = plugin;

        subCommandMap.put("send", new SendCommand());
    }

    public void registerThisCommand(){
        plugin.getCommand("donations").setExecutor(this);
        plugin.getCommand("donations").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        // Return if the args length is 0 and send help messages
        if(args.length == 0){
            sender.sendMessage("Help Messages");
            return true;
        }

        // Trying to get subcommand from 'args[0]'
        SubCommand subCommand = this.subCommandMap.get(args[0].toLowerCase());

        // Return if there is no subcommand with 'args[0]' and send help messages
        if(subCommand == null) {
            sender.sendMessage("Help Messages");
            return true;
        }

        // Check if sub command has permission
        if(subCommand.getPermission() != null){
            // Check if sender/player doesn't have permission for the subcommand
            if(!(sender.hasPermission(subCommand.getPermission()))){
                // Return and send messages
                sender.sendMessage("No Permission");
                return true;
            }
        }

        // Execute the sub command
        subCommand.execute(plugin, sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if(args.length == 1){
            List<String> suggestions = new ArrayList<>();

            return suggestions;
        }

        if(args.length >= 2){
            SubCommand subCommand = this.subCommandMap.get(args[0].toLowerCase());
            if(subCommand == null) return null;

            if(subCommand.getPermission() == null){
                return subCommand.parseTabCompletion(plugin, sender, args);
            }
            if(sender.hasPermission(subCommand.getPermission())){
                return subCommand.parseTabCompletion(plugin, sender, args);
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

}
