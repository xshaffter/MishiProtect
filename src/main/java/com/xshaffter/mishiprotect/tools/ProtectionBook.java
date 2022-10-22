package com.xshaffter.mishiprotect.tools;

import com.xshaffter.mishiprotect.utils.Functions;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Destinated pages:
 * selected coords should apear at line 4 in book's lore
 * assigned area UUID should appear in the first line in the first Page after the cover
 */
public class ProtectionBook extends ItemStack {
    public static final String PROTECTION_IDENTIFIER = "\\[ID\\]: ([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})|(-1)";
    public static final int DATA_PAGE_NUMBER = 3;
    public static final int COORD_SELECT_INDEX = 2;
    public static final int ACTION_STATUS_INDEX = 3;

    public ProtectionBook(Player player) {
        super(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) this.getItemMeta();
        assert meta != null;
        meta.setAuthor(player.getName());
        meta.setTitle("Papeles de la propiedad");

        List<String> lore = new ArrayList<>();

        lore.add("Con este objeto podrás proteger un area.");
        lore.add("Cuídalo como a tus facturas del SAT");
        lore.add("Coords: (x,z), (x,z)");

        meta.setLore(lore);

        BookComponentBuilder bookBuilder = new BookComponentBuilder();

        bookBuilder.createPage(
                new TextComponent("COVER")
        );

        bookBuilder.createPage(
                bookBuilder.createButton(
                        BookColor.PURPLE + BookColor.BOLD + "[CREATE]",
                        "/mishi_book create",
                        null
                ),
                bookBuilder.createButton(
                        BookColor.DARK_YELLOW + BookColor.BOLD + "[EDIT]",
                        "/mishi_book edit",
                        null
                ),
                bookBuilder.createButton(
                        BookColor.DARK_GREEN + BookColor.BOLD + "[ADD]",
                        "/mishi_book add",
                        null
                ),
                bookBuilder.createButton(
                        BookColor.DARK_RED + BookColor.BOLD + "[REMOVE]",
                        "/mishi_book remove",
                        null
                ),
                bookBuilder.createButton(
                        BookColor.PURPLE + BookColor.BOLD + "[RENAME]",
                        "/mishi_book rename",
                        null
                ),
                bookBuilder.createButton(
                        BookColor.DARK_RED + BookColor.BOLD + "[DELETE]",
                        "/mishi_book delete",
                        null
                )
        );
        bookBuilder.createPage(
                new TextComponent(BookColor.DARK_BLUE + BookColor.BOLD + "[ID]: -1\n")
        );

        meta.spigot().setPages(bookBuilder.compile());

        this.setItemMeta(meta);

    }

    public ProtectionBook(Player player, String uuid) {

        super(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) this.getItemMeta();
        assert meta != null;
        meta.setAuthor(player.getName());
        meta.setTitle("Papeles de la propiedad");

        List<String> lore = new ArrayList<>();

        lore.add("Con este objeto podrás proteger un area.");
        lore.add("Cuídalo como a tus facturas del SAT");
        lore.add("Coords: (x,z), (x,z)");

        meta.setLore(lore);

        BookComponentBuilder bookBuilder = new BookComponentBuilder();

        bookBuilder.createPage(
                new TextComponent("COVER")
        );

        bookBuilder.createPage(
                bookBuilder.createButton(
                        BookColor.PURPLE + BookColor.BOLD + "[CREATE]",
                        "/mishi_book create",
                        null
                ),
                bookBuilder.createButton(
                        BookColor.DARK_YELLOW + BookColor.BOLD + "[EDIT]",
                        "/mishi_book edit",
                        null
                ),
                bookBuilder.createButton(
                        BookColor.DARK_GREEN + BookColor.BOLD + "[ADD]",
                        "/mishi_book add",
                        null
                ),
                bookBuilder.createButton(
                        BookColor.DARK_RED + BookColor.BOLD + "[REMOVE]",
                        "/mishi_book remove",
                        null
                ),
                bookBuilder.createButton(
                        BookColor.PURPLE + BookColor.BOLD + "[RENAME]",
                        "/mishi_book rename",
                        null
                ),
                bookBuilder.createButton(
                        BookColor.DARK_RED + BookColor.BOLD + "[DELETE]",
                        "/mishi_book delete",
                        null
                )
        );
        bookBuilder.createPage(
                new TextComponent(BookColor.DARK_BLUE + BookColor.BOLD + "[ID]: " + uuid + "\n")
        );

        meta.spigot().setPages(bookBuilder.compile());

        this.setItemMeta(meta);
    }

    public static void editCoords(ItemStack handItem, Block block, Player player) {
        Location location = block.getLocation();
        BookMeta meta = (BookMeta) handItem.getItemMeta();
        assert meta != null;

        List<String> lore = meta.getLore();
        assert lore != null;

        String coordLore = lore.get(ProtectionBook.COORD_SELECT_INDEX);

        int occ = Functions.countMatches(coordLore, "(x,z)");
        int x = (int) location.getX();
        int z = (int) location.getZ();

        if (occ == 2) {
            lore.set(ProtectionBook.COORD_SELECT_INDEX, "Coords: (" + x + "," + z + "), (x,z)");
            meta.setLore(lore);
        } else if (occ == 1) {
            String newMsg = coordLore.split(", ")[0] + ", (" + x + "," + z + ")";
            lore.set(ProtectionBook.COORD_SELECT_INDEX, newMsg);
            meta.setLore(lore);
        } else {
            player.sendMessage(ChatColor.RED + "¡Si quieres seleccionar de nuevo debes reiniciar!\nusa el botón de " + ChatColor.GOLD + "[EDIT]" + ChatColor.RED + " para eso");
            return;
        }
        handItem.setItemMeta(meta);
    }

    public static void resetCoords(ItemStack handItem) {
        BookMeta meta = (BookMeta) handItem.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        assert lore != null;
        lore.set(ProtectionBook.COORD_SELECT_INDEX, "Coords: (x,z), (x,z)");
        meta.setLore(lore);
        handItem.setItemMeta(meta);
    }

    public static void setCapturingPlayer(ItemStack handItem, boolean value) {
        BookMeta meta = (BookMeta) handItem.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        assert lore != null;
        if (value) {
            if (lore.size() > ProtectionBook.ACTION_STATUS_INDEX) {
                lore.set(ProtectionBook.ACTION_STATUS_INDEX, "Capturing player");
            } else {
                lore.add("Capturing player");
            }
        } else {
            lore.remove(ProtectionBook.ACTION_STATUS_INDEX);
        }
        meta.setLore(lore);
        handItem.setItemMeta(meta);
    }

    public static void setActionStatus(ItemStack handItem, String value) {
        BookMeta meta = (BookMeta) handItem.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        assert lore != null;
        if (value != null) {
            if (lore.size() > ProtectionBook.ACTION_STATUS_INDEX) {
                lore.set(ProtectionBook.ACTION_STATUS_INDEX, value);
            } else {
                lore.add(value);
            }
        } else {
            lore.remove(ProtectionBook.ACTION_STATUS_INDEX);
        }
        meta.setLore(lore);
        handItem.setItemMeta(meta);
    }

    public static boolean isCapturingPlayer(ItemStack handItem) {
        BookMeta meta = (BookMeta) handItem.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        assert lore != null;
        try {
            return Objects.equals(lore.get(ProtectionBook.ACTION_STATUS_INDEX), "Capturing player");
        } catch (Exception ex) {
            return false;
        }
    }

    public static void setCapturedPlayer(ItemStack handItem, String playerName) {
        BookMeta meta = (BookMeta) handItem.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        assert lore != null;
        lore.set(ProtectionBook.ACTION_STATUS_INDEX, playerName);
        meta.setLore(lore);
        handItem.setItemMeta(meta);
    }

    public static String getCapturedPlayer(ItemStack handItem) {
        BookMeta meta = (BookMeta) handItem.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        assert lore != null;
        try {
            return lore.get(ProtectionBook.ACTION_STATUS_INDEX);
        } catch (IndexOutOfBoundsException ioobe) {
            return null;
        }
    }

    public static void setProtectionID(ItemStack handItem, UUID uuid) {
        BookMeta meta = (BookMeta) handItem.getItemMeta();
        assert meta != null;

        String dataPage = meta.getPage(ProtectionBook.DATA_PAGE_NUMBER);
        String newPage = dataPage.replace("[ID]: -1", "[ID]: " + uuid.toString());

        meta.setPage(ProtectionBook.DATA_PAGE_NUMBER, newPage);

        handItem.setItemMeta(meta);
    }

    public static String getProtectionID(ItemStack handItem) {
        BookMeta meta = (BookMeta) handItem.getItemMeta();
        assert meta != null;

        String dataPage = meta.getPage(ProtectionBook.DATA_PAGE_NUMBER);
        String[] lines = dataPage.split("\n");
        return lines[0].replace(BookColor.DARK_BLUE + BookColor.BOLD + "[ID]: ", "");
    }

    public static boolean isChangingName(ItemStack book) {
        BookMeta meta = (BookMeta) book.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        assert lore != null;
        try {
            return Objects.equals(lore.get(ProtectionBook.ACTION_STATUS_INDEX), "Changing name");
        } catch (Exception ex) {
            return false;
        }
    }

    public static void setNewName(ItemStack book, String newName) {
        BookMeta meta = (BookMeta) book.getItemMeta();
        assert meta != null;
        meta.setTitle(ChatColor.GOLD + newName);
        meta.setPage(1, String.format("%s[%s]", BookColor.DARK_YELLOW + BookColor.BOLD, newName.toUpperCase()));
        book.setItemMeta(meta);
    }
}
