package com.xshaffter.mishiprotect.commands.handlers;

import com.xshaffter.mishiprotect.tools.ProtectionBook;
import com.xshaffter.mishiprotect.utils.Functions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditProtection {
    public boolean handle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (Functions.isProtectionTool(handItem, player)) {
            player.sendMessage(ChatColor.BLUE + "Selecciona el nuevo area de la zona");
            ProtectionBook.resetCoords(handItem);
        }

        return false;
    }
}
