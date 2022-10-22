package com.xshaffter.mishiprotect.commands.handlers;

import com.xshaffter.mishiprotect.tools.ProtectionBook;
import com.xshaffter.mishiprotect.tools.ProtectionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RecoverBook {
    public boolean handle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        String uuid = new ProtectionManager().isOwner(player);

        if (uuid != null) {
            player.getInventory().addItem(new ProtectionBook(player, uuid));
        }

        return false;
    }
}
