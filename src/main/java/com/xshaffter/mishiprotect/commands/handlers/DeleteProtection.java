package com.xshaffter.mishiprotect.commands.handlers;

import com.xshaffter.mishiprotect.Mishiprotect;
import com.xshaffter.mishiprotect.tools.ProtectionBook;
import com.xshaffter.mishiprotect.utils.Functions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class DeleteProtection {
    public boolean handle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (Functions.isProtectionTool(handItem, player)) {
            String protection = Mishiprotect.database.getProtectionID(player.getLocation());
            if (Objects.equals(protection, ProtectionBook.getProtectionID(handItem))) {
                String uuid = ProtectionBook.getProtectionID(handItem);
                boolean deleted = Mishiprotect.database.deleteProtection(uuid);
                if (deleted) {
                    player.sendMessage(ChatColor.GOLD + "¡Protección eliminada!");
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1);
                } else {
                    player.sendMessage(ChatColor.DARK_RED + "[ERROR] contacta a un administrador");
                }
            }
        }

        return false;
    }
}
