package com.xshaffter.mishiprotect.commands.handlers;

import com.xshaffter.mishiprotect.Mishiprotect;
import com.xshaffter.mishiprotect.tools.ProtectionBook;
import com.xshaffter.mishiprotect.utils.Functions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ChangeProtectionName {

    public boolean handle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (Functions.isProtectionTool(handItem, player)) {
            player.sendMessage(ChatColor.BLUE + "Escribe en el chat el nombre que quieres ponerle!");
            ProtectionBook.setActionStatus(handItem, "Changing name");
        }

        return false;
    }
}
