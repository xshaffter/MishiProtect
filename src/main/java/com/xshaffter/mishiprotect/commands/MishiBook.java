package com.xshaffter.mishiprotect.commands;

import com.xshaffter.mishiprotect.commands.handlers.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MishiBook implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        switch (args[0]) {
            case "give":
                new GiveBook().handle(sender);
                break;
            case "rename":
                new ChangeProtectionName().handle(sender);
                break;
            case "edit":
                new EditProtection().handle(sender);
                break;
            case "add":
                new AddPermission().handle(sender);
                break;
            case "delete":
                new DeleteProtection().handle(sender);
                break;
            case "remove":
                new RemovePermission().handle(sender);
                break;
            case "create":
                new CreateProtection().handle(sender);
                break;
            case "recover":
                new RecoverBook().handle(sender);
                break;
            default:
                sender.sendMessage(ChatColor.DARK_RED + "[ERROR] this option does not exist");
        }
        return false;
    }
}
