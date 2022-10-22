package com.xshaffter.mishiprotect.commands.handlers;

import com.xshaffter.mishiprotect.Mishiprotect;
import com.xshaffter.mishiprotect.tools.ProtectionBook;
import com.xshaffter.mishiprotect.utils.Functions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class CreateProtection {
    public boolean handle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        ItemStack handItem = Functions.getHand(player);

        if (Functions.isProtectionTool(handItem, player)) {
            BookMeta meta = (BookMeta) handItem.getItemMeta();
            assert meta != null;
            List<String> lore = meta.getLore();
            assert lore != null;

            String coordLore = lore.get(ProtectionBook.COORD_SELECT_INDEX);

            String[] cleanCords = coordLore.replace("Coords: ", "").replace("(", "").replace(")", "").replace(" ", "").split(",");
            int[] coords = Stream.of(cleanCords).mapToInt(Integer::parseInt).toArray();

            String protection = Mishiprotect.database.intersects(coords);
            if (protection == null || protection.equals(player.getName())) {
                UUID uuid = UUID.randomUUID();
                Mishiprotect.database.createProtection(uuid, coords, player.getName());
                player.sendMessage(ChatColor.GREEN + "¡Zona protegida!");
                ProtectionBook.setProtectionID(handItem, uuid);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1);
            } else {
                player.sendMessage(ChatColor.RED + "[ERROR] ¡otra zona protegida está en el camino!");
            }
        }
        return false;
    }
}
