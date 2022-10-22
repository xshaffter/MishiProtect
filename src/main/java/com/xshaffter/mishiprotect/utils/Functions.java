package com.xshaffter.mishiprotect.utils;

import com.xshaffter.mishiprotect.tools.ProtectionBook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {

    public static boolean isProtectionTool(ItemStack item, Player player) {
        boolean isBook = item.getType() == Material.WRITTEN_BOOK;
        if (isBook) {
            BookMeta bookMeta = (BookMeta) item.getItemMeta();
            assert bookMeta != null;
            String dataPage = bookMeta.getPage(ProtectionBook.DATA_PAGE_NUMBER);

            Pattern pattern = Pattern.compile(ProtectionBook.PROTECTION_IDENTIFIER, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(dataPage);

            boolean isItem = matcher.find();
            boolean isAuthor = Objects.equals(bookMeta.getAuthor(), player.getName());
            return isItem && isAuthor;

        }
        return false;
    }

    public static int countMatches(String text, String substring) {

        String temp = text.replace(substring, "");
        return (text.length() - temp.length()) / substring.length();
    }

    public static String getPlayerName(Player player) {
        try {
            ItemStack playerHelmet = Objects.requireNonNull(player.getEquipment()).getHelmet();
            assert playerHelmet != null;
            SkullMeta meta = (SkullMeta) playerHelmet.getItemMeta();
            assert meta != null;
            return Objects.requireNonNull(meta.getOwnerProfile()).getName();
        } catch (Exception ex) {
            return player.getName();
        }
    }

    public static Location ceil(Location location) {
        int x = (int) Math.ceil(location.getX());
        int y = (int) Math.ceil(location.getY());
        int z = (int) Math.ceil(location.getZ());
        return new Location(location.getWorld(), x, y, z);
    }

    public static Location floor(Location location) {
        int x = (int) Math.floor(location.getX());
        int y = (int) Math.floor(location.getY());
        int z = (int) Math.floor(location.getZ());
        return new Location(location.getWorld(), x, y, z);
    }

    public static ItemStack getHand(Player player) {
        return player.getInventory().getItemInMainHand();
    }
}
