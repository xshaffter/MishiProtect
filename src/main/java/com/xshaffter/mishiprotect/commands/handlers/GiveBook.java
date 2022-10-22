package com.xshaffter.mishiprotect.commands.handlers;

import com.xshaffter.mishiprotect.tools.ProtectionBook;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveBook {

    public boolean handle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        ItemStack handItem = new ProtectionBook(player);
        player.getInventory().addItem(handItem);

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1);
        return false;
    }
}
