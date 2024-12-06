package src;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public interface DatabaseHandler {
    String URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12749367?serverTimezone=UTC";
    String DATABASE = "sql12749367";
    String USER = "sql12749367";
    String PASSWORD = "7F27vnN5rw";

    default Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    default Map<String, Integer> getColumnMaxLengths(String tableName) {
        Map<String, Integer> columnLengths = new HashMap<>();
        String query = "SELECT COLUMN_NAME, CHARACTER_MAXIMUM_LENGTH " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = ? AND TABLE_SCHEMA = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tableName);
            stmt.setString(2, DATABASE);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    columnLengths.put(rs.getString("COLUMN_NAME"), rs.getInt("CHARACTER_MAXIMUM_LENGTH"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching column constraints: " + e.getMessage());
        }

        return columnLengths;
    }

    default boolean isUsernameTaken(String username, String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE username = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking username availability: " + e.getMessage());
        }
        return false;
    }

    default boolean verifyLogin(String username, String password, String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE username = ? AND password = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during login verification: " + e.getMessage());
        }
        return false;
    }
}

class SellerDatabaseHandler implements DatabaseHandler {
    public boolean registerSeller(String username, String displayName, String password, String address,
            String contactNumber) {
        String query = "INSERT INTO Seller (username, display_name, password, address, contact_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, displayName);
            stmt.setString(3, password);
            stmt.setString(4, address);
            stmt.setString(5, contactNumber);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering seller: " + e.getMessage());
        }
        return false;
    }
}

class BuyerDatabaseHandler implements DatabaseHandler {
    public boolean registerBuyer(String username, String displayName, String password, String address,
            String contactNumber) {
        String query = "INSERT INTO Buyer (username, display_name, password, address, contact_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, displayName);
            stmt.setString(3, password);
            stmt.setString(4, address);
            stmt.setString(5, contactNumber);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering buyer: " + e.getMessage());
        }
        return false;
    }
}
