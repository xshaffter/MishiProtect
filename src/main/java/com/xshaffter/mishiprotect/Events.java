package com.xshaffter.mishiprotect;

import com.xshaffter.mishiprotect.tools.ProtectionBook;
import com.xshaffter.mishiprotect.tools.ProtectionManager;
import com.xshaffter.mishiprotect.utils.Constants;
import com.xshaffter.mishiprotect.utils.Functions;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {

    @EventHandler
    public void onBlockPlacedByPlayer(BlockPlaceEvent e) {
        Block block = e.getBlock();
        String blockProtection = Mishiprotect.database.getProtection(block.getLocation());
        Player player = e.getPlayer();
        if (blockProtection != null && !blockProtection.equals(player.getName())) {
            e.getPlayer().sendMessage(ChatColor.RED + "no puedes hacer eso");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();
        String blockProtection = Mishiprotect.database.getProtection(block.getLocation());

        if (blockProtection != null) {
            if (!blockProtection.equals(player.getName())) {
                player.sendMessage(ChatColor.RED + "no puedes hacer eso");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onUseBlock(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        Player player = e.getPlayer();
        if (block == null) {
            return;
        }

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            boolean hasProtectionPermission = new ProtectionManager().hasProtection(block.getLocation(), player.getName());
            if (!hasProtectionPermission && !Constants.FREE_BLOCKS.contains(block.getType().toString())) {
                e.getPlayer().sendMessage(ChatColor.RED + "no puedes hacer eso");
                e.setCancelled(true);
            }
        } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            ItemStack handItem = player.getInventory().getItemInMainHand();
            if (Functions.isProtectionTool(handItem, player)) {
                String blockProtection = Mishiprotect.database.getProtection(block.getLocation());
                if (blockProtection == null) {
                    ProtectionBook.editCoords(handItem, block, player);
                } else if (!blockProtection.equals(player.getName())) {
                    player.sendMessage(ChatColor.RED + "Esta zona ya está protegida!!");
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChatHandler(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        ItemStack book = Functions.getHand(player);
        if (Functions.isProtectionTool(book, player) && ProtectionBook.isChangingName(book)) {
            ProtectionBook.setNewName(book, e.getMessage());
            ProtectionBook.setActionStatus(book, null);
            e.setCancelled(true);
        }
    }
}
