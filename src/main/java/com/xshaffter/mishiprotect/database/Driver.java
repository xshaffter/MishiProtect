package com.xshaffter.mishiprotect.database;

import com.xshaffter.mishiprotect.Mishiprotect;
import org.bukkit.Location;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Driver {
    public Connection connect() {
        try {
            final String DB_DRIVER = Mishiprotect.plugin.customConfig.getString("DB_TYPE");
            final String DB_NAME = Mishiprotect.plugin.customConfig.getString("DB_NAME");
            final String DB_HOST = Mishiprotect.plugin.customConfig.getString("DB_HOST");
            final String username = Mishiprotect.plugin.customConfig.getString("DB_USERNAME");
            final String password = Mishiprotect.plugin.customConfig.getString("DB_PASSWORD");
            final String connection_string;

            if (DB_HOST != null) {
                connection_string = "jdbc:" + DB_DRIVER + "://" + DB_HOST + "/" + DB_NAME;
            } else {
                connection_string = "jdbc:" + DB_DRIVER + ":" + DB_NAME;
            }

            return DriverManager.getConnection(connection_string, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void initializeBD() {
        try {
            ScriptRunner sr;
            try (Connection connection = connect()) {
                sr = new ScriptRunner(connection, false, true);
                Reader reader = new InputStreamReader(Objects.requireNonNull(Mishiprotect.plugin.getResource("start_db.sql")));
                sr.runScript(reader);
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createProtection(UUID uuid, int x_1, int z_1, int x_2, int z_2, String player) {
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                stmt = connection.prepareStatement("insert into ProtectedAreas(uuid, x_1,z_1, x_2,z_2,owner) values(?,?,?,?,?,?);");

                int x1 = Math.min(x_1, x_2);
                int x2 = Math.max(x_1, x_2);
                int z1 = Math.min(z_1, z_2);
                int z2 = Math.max(z_1, z_2);
                stmt.setString(1, uuid.toString());
                stmt.setInt(2, x1);
                stmt.setInt(3, z1);
                stmt.setInt(4, x2);
                stmt.setInt(5, z2);
                stmt.setString(6, player);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createProtection(UUID uuid, int[] coords, String player) {
        createProtection(uuid, coords[0], coords[1], coords[2], coords[3], player);
    }

    public String getProtection(Location location) {
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                String query = "select owner from ProtectedAreas where (abs(x_1 - ?) + abs(x_2 - ?)) <= abs(x_1 - x_2) and (abs(z_1 - ?) + abs(z_2 - ?)) <= abs(z_1 - z_2);";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, (int) location.getX());
                stmt.setInt(2, (int) location.getX());
                stmt.setInt(3, (int) location.getZ());
                stmt.setInt(4, (int) location.getZ());
                ResultSet rs = stmt.executeQuery();
                try {
                    rs.next();
                    return rs.getString("owner");
                } catch (SQLException e) {
                    return null;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getProtectionID(Location location) {
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                String query = "select uuid from ProtectedAreas where (abs(x_1 - ?) + abs(x_2 - ?)) <= abs(x_1 - x_2) and (abs(z_1 - ?) + abs(z_2 - ?)) <= abs(z_1 - z_2);";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, (int) location.getX());
                stmt.setInt(2, (int) location.getX());
                stmt.setInt(3, (int) location.getZ());
                stmt.setInt(4, (int) location.getZ());
                ResultSet rs = stmt.executeQuery();
                try {
                    rs.next();
                    return rs.getString("uuid");
                } catch (SQLException e) {
                    return null;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean getProtectionOwnage(Location location, String player, String protectionID) {
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                String query = "select 1 as is_owner from ProtectedAreas where (abs(x_1 - ?) + abs(x_2 - ?)) <= abs(x_1 - x_2) and (abs(z_1 - ?) + abs(z_2 - ?)) <= abs(z_1 - z_2) and owner = ? and uuid = ?;";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, (int) location.getX());
                stmt.setInt(2, (int) location.getX());
                stmt.setInt(3, (int) location.getZ());
                stmt.setInt(4, (int) location.getZ());
                stmt.setString(5, player);
                stmt.setString(6, protectionID);
                ResultSet rs = stmt.executeQuery();
                try {
                    rs.next();
                    return rs.getBoolean("is_owner");
                } catch (SQLException e) {
                    return false;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String isProtectionOwner(Location location, String player) {
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                String query = "select uuid from ProtectedAreas where (abs(x_1 - ?) + abs(x_2 - ?)) <= abs(x_1 - x_2) and (abs(z_1 - ?) + abs(z_2 - ?)) <= abs(z_1 - z_2) and owner = ?;";
                stmt = connection.prepareStatement(query);
                stmt.setInt(1, (int) location.getX());
                stmt.setInt(2, (int) location.getX());
                stmt.setInt(3, (int) location.getZ());
                stmt.setInt(4, (int) location.getZ());
                stmt.setString(5, player);
                ResultSet rs = stmt.executeQuery();
                try {
                    rs.next();
                    return rs.getString("uuid");
                } catch (SQLException e) {
                    return null;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public String intersects(int[] coords) {
        return intersects(coords[0], coords[1], coords[2], coords[3]);
    }

    public String intersects(int x_1, int z_1, int x_2, int z_2) {
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                String query = "select owner from ProtectedAreas where (x_2 < ? or x_1 < ?) and (z_2 < ? or z_1 < ?) and (x_1 > ? or x_2 > ?) and(z_1 > ? or z_2 > ?)";
                stmt = connection.prepareStatement(query);
                int x1 = Math.min(x_1, x_2);
                int x2 = Math.max(x_1, x_2);
                int z1 = Math.min(z_1, z_2);
                int z2 = Math.max(z_1, z_2);
                stmt.setInt(1, x1);
                stmt.setInt(2, x2);
                stmt.setInt(3, z1);
                stmt.setInt(4, z2);
                stmt.setInt(5, x2);
                stmt.setInt(6, x1);
                stmt.setInt(7, z2);
                stmt.setInt(8, z1);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                return rs.getString("owner");
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    public boolean deleteProtection(String uuid) {
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                stmt = connection.prepareStatement("delete from ProtectedAreas where uuid = ?");
                stmt.setString(1, uuid);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void addProtectionBlock(Location location, String capturedPlayer) {
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                stmt = connection.prepareStatement("insert into AllowedBlocks(x,y,z,owner) values(?,?,?,?);");
                stmt.setInt(1, location.getBlockX());
                stmt.setInt(2, location.getBlockY());
                stmt.setInt(3, location.getBlockZ());
                stmt.setString(4, capturedPlayer);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean hasProtectionPermissions(Location location, String player) {
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                String query = "select 1 as is_owner from ProtectedAreas where (abs(x_1 - ?) + abs(x_2 - ?)) <= abs(x_1 - x_2) and (abs(z_1 - ?) + abs(z_2 - ?)) <= abs(z_1 - z_2) and owner = ?" +
                        "union select 1 as is_owner from AllowedBlocks where x = ? and y = ? and z = ? and owner = ?";
                stmt = connection.prepareStatement(query);

                stmt.setInt(1, location.getBlockX());
                stmt.setInt(2, location.getBlockX());
                stmt.setInt(3, location.getBlockZ());
                stmt.setInt(4, location.getBlockZ());
                stmt.setString(5, player);
                stmt.setInt(6, location.getBlockX());
                stmt.setInt(7, location.getBlockY());
                stmt.setInt(8, location.getBlockZ());
                stmt.setString(9, player);

                ResultSet rs = stmt.executeQuery();
                rs.next();
                return rs.getBoolean("is_owner");
            }
        } catch (SQLException sqlex) {
            return false;
        }
    }

    public List<String> getAllowedForLocation(Location location) {
        List<String> result = new ArrayList<>();
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                String query = "select owner from AllowedBlocks where x = ? and y = ? and z = ?";
                stmt = connection.prepareStatement(query);

                stmt.setInt(1, location.getBlockX());
                stmt.setInt(2, location.getBlockY());
                stmt.setInt(3, location.getBlockZ());

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    result.add(rs.getString("owner"));
                }
            }
        } catch (SQLException ignored) {
        }
        return result;
    }

    public void removePermissions(int[] coords, String player) {
        try {
            PreparedStatement stmt;
            try (Connection connection = connect()) {
                stmt = connection.prepareStatement("delete from AllowedBlocks where x = ? and y = ? and z = ? and owner = ?");
                stmt.setInt(1, coords[0]);
                stmt.setInt(2, coords[1]);
                stmt.setInt(3, coords[2]);
                stmt.setString(4, player);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removePermissions(Location floor, String player) {
        removePermissions(new int[]{floor.getBlockX(), floor.getBlockY(), floor.getBlockZ()}, player);
    }
}
