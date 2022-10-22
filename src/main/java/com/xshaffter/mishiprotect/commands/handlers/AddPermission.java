package com.xshaffter.mishiprotect.commands.handlers;

import com.xshaffter.mishiprotect.Mishiprotect;
import com.xshaffter.mishiprotect.tools.ProtectionBook;
import com.xshaffter.mishiprotect.tools.ProtectionManager;
import com.xshaffter.mishiprotect.utils.Constants;
import com.xshaffter.mishiprotect.utils.Functions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class AddPermission implements Listener {
    public boolean handle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (Functions.isProtectionTool(handItem, player)) {
            boolean hasOwnage = Mishiprotect.database.getProtectionOwnage(player.getLocation(), player.getName(), ProtectionBook.getProtectionID(handItem));
            if (hasOwnage) {
                player.sendMessage(ChatColor.BLUE + "Ahora golpea al jugador que quieres agregar");
                ProtectionBook.setCapturingPlayer(handItem, true);
            } else {
                player.sendMessage(ChatColor.RED + "No tienes permiso para hacer esto");
            }
        }

        return false;
    }

    @EventHandler
    public void onHitPlayerForAdding(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        Entity damaged = e.getEntity();

        if (damager.getType() == EntityType.PLAYER && EntityType.PLAYER == damaged.getType()) {
            Player player = (Player) damager;
            Player targetPlayer = (Player) damaged;
            ItemStack book = Functions.getHand(player);

            if (Functions.isProtectionTool(book, player) && ProtectionBook.isCapturingPlayer(book)) {
                ProtectionBook.setCapturedPlayer(book, targetPlayer.getName());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSelectAddedBlock(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        Player player = e.getPlayer();
        if (block == null) {
            return;
        }
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            ItemStack handItem = player.getInventory().getItemInMainHand();
            if (Functions.isProtectionTool(handItem, player)) {
                boolean blockProtection = Mishiprotect.database.getProtectionOwnage(block.getLocation(), player.getName(), ProtectionBook.getProtectionID(handItem));
                if (blockProtection) {
                    String capturedPlayer = ProtectionBook.getCapturedPlayer(handItem);
                    if (Objects.equals(capturedPlayer, "Capture Block!")) {
                        player.openInventory(RemovePermission.getPlayerList(block.getLocation(), player));
                        ProtectionBook.setActionStatus(handItem, null);
                    }
                    else if (capturedPlayer != null) {
                        ProtectionManager manager = new ProtectionManager();
                        Location middleLocation = manager.getDoubleBlockLocation(block);
                        manager.protectDoubleBlock(middleLocation, capturedPlayer);

                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1);
                        ProtectionBook.setCapturingPlayer(handItem, false);
                    }
                }
                e.setCancelled(true);
            }
        }
    }

}
