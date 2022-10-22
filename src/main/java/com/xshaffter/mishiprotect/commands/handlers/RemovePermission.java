package com.xshaffter.mishiprotect.commands.handlers;

import com.xshaffter.mishiprotect.Mishiprotect;
import com.xshaffter.mishiprotect.tools.ProtectionBook;
import com.xshaffter.mishiprotect.tools.ProtectionManager;
import com.xshaffter.mishiprotect.utils.Functions;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RemovePermission implements Listener {
    private static final String MENU_TITLE = "Hey!";

    public boolean handle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (Functions.isProtectionTool(handItem, player)) {
            ProtectionBook.setActionStatus(handItem, "Capture Block!");
            player.sendMessage(ChatColor.BLUE + "Ahora golpea al jugador que quieres quitar");
        }

        return false;
    }

    public static Inventory getPlayerList(Location location, Player player) {
        Inventory inventory = Bukkit.createInventory(player, 54, ChatColor.RED + "(" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ") permissions");
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta meta = closeButton.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "Cerrar panel");
        meta.setLore(Collections.singletonList(ChatColor.AQUA + "Cierra la ventana"));
        closeButton.setItemMeta(meta);
        inventory.setItem(0, closeButton);
        int index = 1;

        List<String> players = Mishiprotect.database.getAllowedForLocation(location);
        for (String playerName : players) {
            ItemStack playerSkull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) playerSkull.getItemMeta();
            assert skullMeta != null;
            skullMeta.setOwner(playerName);
            playerSkull.setItemMeta(skullMeta);

            inventory.setItem(index++, playerSkull);
        }
        return inventory;
    }

    @EventHandler
    public void onSelectHead(InventoryClickEvent e) {
        ItemStack selectedItem = e.getCurrentItem();
        String title = e.getView().getTitle();
        if (title.contains("permissions") && selectedItem != null) {
            Player player = (Player) e.getWhoClicked();
            if (e.getRawSlot() != 0) {
                String coords = title.replace("permissions", "").replace(ChatColor.RED.toString(), "").replace(" ", "");
                coords = coords.substring(1, coords.length() - 1);
                String[] coord_array = coords.split(",");
                SkullMeta skullMeta = (SkullMeta) selectedItem.getItemMeta();
                assert skullMeta != null;
                String skullPlayer = skullMeta.getOwner();
                int[] coordsInt = Arrays.stream(coord_array).mapToInt(Integer::parseInt).toArray();
                Block block = Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(coordsInt[0], coordsInt[1], coordsInt[2]);

                ProtectionManager manager = new ProtectionManager();
                Location middleLocation = manager.getDoubleBlockLocation(block);
                manager.removeProtectionDoubleBlock(middleLocation, skullPlayer);

                e.getInventory().setItem(e.getRawSlot(), null);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1);
            } else {
                player.closeInventory();
                e.setCancelled(true);
            }
        }
    }
}
