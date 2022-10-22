package com.xshaffter.mishiprotect.tools;

import com.xshaffter.mishiprotect.Mishiprotect;
import com.xshaffter.mishiprotect.utils.Constants;
import com.xshaffter.mishiprotect.utils.Functions;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

public class ProtectionManager {
    public boolean hasProtection(Location location, String player) {
        return Mishiprotect.database.hasProtectionPermissions(location, player) || Mishiprotect.database.getProtection(location) == null;
    }

    public Location getDoubleBlockLocation(Block block) {

        Location location = block.getLocation();
        if (Constants.DOUBLE_BLOCKS.contains(block.getType().toString())) {
            BlockData data = block.getBlockData();
            if (data instanceof Door) {
                Door door = (Door) data;
                if (door.getHalf().toString().equals("TOP")) {
                    location.setY(location.getY() - 0.5);
                } else {
                    location.setY(location.getY() + 0.5);
                }
            } else if (data instanceof Bed) {
                Bed bed = (Bed) data;
                int modifier;
                if (bed.getPart().toString().equals("FOOT")) {
                    modifier = 1;
                } else {
                    modifier = -1;
                }

                switch (bed.getFacing().toString()) {
                    case "NORTH":
                        location.setZ(location.getZ() - (0.5 * modifier));
                        break;
                    case "SOUTH":
                        location.setZ(location.getZ() + (0.5 * modifier));
                        break;
                    case "WEST":
                        location.setX(location.getX() - (0.5 * modifier));
                        break;
                    case "EAST":
                        location.setX(location.getX() + (0.5 * modifier));
                        break;
                }
            }
        } else {
            BlockState state = block.getState();
            if (state instanceof Chest) {
                Inventory inventory = ((Chest) state).getInventory();
                if (inventory instanceof DoubleChestInventory) {
                    DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
                    assert doubleChest != null;
                    location = doubleChest.getLocation();
                }
            }
        }
        return location;
    }

    public void protectDoubleBlock(Location location, String player) {
        Location floor = Functions.floor(location);
        Location ceil = Functions.ceil(location);

        Mishiprotect.database.addProtectionBlock(floor, player);
        Mishiprotect.database.addProtectionBlock(ceil, player);

    }

    public void removeProtectionDoubleBlock(Location location, String player) {
        Location floor = Functions.floor(location);
        Location ceil = Functions.ceil(location);

        Mishiprotect.database.removePermissions(floor, player);
        Mishiprotect.database.removePermissions(ceil, player);
    }

    public String isOwner(Player player) {
        return Mishiprotect.database.isProtectionOwner(player.getLocation(), player.getName());
    }
}
