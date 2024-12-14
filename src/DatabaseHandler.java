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
        String query = "SELECT COLUMN_NAME, CHARACTER_MAXIMUM_LENGTH, COLUMN_TYPE " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = ? AND TABLE_SCHEMA = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tableName);
            stmt.setString(2, DATABASE);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    Integer maxLength = rs.getObject("CHARACTER_MAXIMUM_LENGTH") != null
                            ? rs.getInt("CHARACTER_MAXIMUM_LENGTH")
                            : null;
                    String columnType = rs.getString("COLUMN_TYPE");

                    if (maxLength == null || maxLength == 0) {
                        maxLength = extractNumericPrecision(columnType);
                    }

                    columnLengths.put(columnName, maxLength != null ? maxLength : 0);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching column constraints: " + e.getMessage());
        }

        return columnLengths;
    }

    private Integer extractNumericPrecision(String columnType) {
        if (columnType == null)
            return null;

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\((\\d+)\\)");
        java.util.regex.Matcher matcher = pattern.matcher(columnType);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
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
    public boolean registerSeller(String username, String lastName, String firstName, String password, String email,
            String contactNumber, String region, String cityOrMunicipality, String barangay, String zipCode) {
        String query = "INSERT INTO Seller (username, last_name, first_name, password, email, contact_number, region, city_or_municipality, barangay, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, lastName);
            stmt.setString(3, firstName);
            stmt.setString(4, password);
            stmt.setString(5, email);
            stmt.setString(6, contactNumber);
            stmt.setString(7, region);
            stmt.setString(8, cityOrMunicipality);
            stmt.setString(9, barangay);
            stmt.setString(10, zipCode);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering seller: " + e.getMessage());
        }
        return false;
    }
}

class BuyerDatabaseHandler implements DatabaseHandler {
    public boolean registerBuyer(String username, String oeganizationName, String password, String email,
            String contactNumber, String region, String cityOrMunicipality, String barangay, String zipCode) {
        String query = "INSERT INTO Buyer (username, organization_name, password, email, contact_number, region, city_or_municipality, barangay, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, oeganizationName);
            stmt.setString(3, password);
            stmt.setString(4, email);
            stmt.setString(5, contactNumber);
            stmt.setString(6, region);
            stmt.setString(7, cityOrMunicipality);
            stmt.setString(8, barangay);
            stmt.setString(9, zipCode);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error registering buyer: " + e.getMessage());
        }
        return false;
    }
}
