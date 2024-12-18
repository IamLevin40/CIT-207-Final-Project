package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import utils.Global;

public interface DatabaseHandler {
    default Connection connect() throws SQLException {
        return DriverManager.getConnection(Global.DB_URL, Global.DB_USER, Global.DB_PASSWORD);
    }

    default Map<String, Integer> getColumnMaxLengths(String tableName) {
        Map<String, Integer> columnLengths = new HashMap<>();
        String query = "SELECT COLUMN_NAME, CHARACTER_MAXIMUM_LENGTH, COLUMN_TYPE " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = ? AND TABLE_SCHEMA = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tableName);
            stmt.setString(2, Global.DB_DATABASE);

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

    default boolean isSellerExist(String username) {
        String query = "SELECT COUNT(*) FROM " + Global.SELLER_TABLE_NAME + " WHERE username = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking seller id existing: " + e.getMessage());
        }
        return false;
    }

    default boolean isCropExist(String cropId) {
        String query = "SELECT COUNT(*) FROM " + Global.CROP_TABLE_NAME + " WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cropId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking crop id existing: " + e.getMessage());
        }
        return false;
    }

    default List<Map<String, Object>> executeReadQuery(String query, Object[] params) {
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            if (params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRowToMap(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing read query: " + e.getMessage());
        }
        return result;
    }

    default boolean executeUpdateQuery(String query, Object[] params) {
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            if (params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
            }
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error executing update query: " + e.getMessage());
        }
        return false;
    }

    private Map<String, Object> mapRowToMap(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Map<String, Object> row = new HashMap<>();

        for (int i = 1; i <= columnCount; i++) {
            row.put(metaData.getColumnName(i), rs.getObject(i));
        }
        return row;
    }
}

class SellerDatabaseHandler implements DatabaseHandler {
    public boolean registerSeller(String username, String lastName, String firstName, String password, String email,
            String contactNumber, String region, String cityOrMunicipality, String barangay, String zipCode) {
        String query = "INSERT INTO " + Global.SELLER_TABLE_NAME
                + " (username, last_name, first_name, password, email, contact_number, region, city_or_municipality, barangay, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

    public Map<String, Object> getFullNameByUsername(String username) {
        String query = "SELECT last_name, first_name FROM " + Global.SELLER_TABLE_NAME + " WHERE username = ?";
        List<Map<String, Object>> results = executeReadQuery(query, new Object[] { username });
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }
}

class BuyerDatabaseHandler implements DatabaseHandler {
    public boolean registerBuyer(String username, String oeganizationName, String password, String email,
            String contactNumber, String region, String cityOrMunicipality, String barangay, String zipCode) {
        String query = "INSERT INTO " + Global.BUYER_TABLE_NAME
                + " (username, organization_name, password, email, contact_number, region, city_or_municipality, barangay, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

class ProductDatabaseHandler implements DatabaseHandler {
    // ------------------------- CREATE -------------------------

    public boolean addFoodbank(String cropId, double quantity, double price, int discount, boolean isPopular,
            File image, String sellerId) throws FileNotFoundException, IOException {
        String query = "INSERT INTO " + Global.FOODBANK_TABLE_NAME
                + " (crop_id, quantity, price, discount, is_popular, image, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(query);
                InputStream inputStream = new FileInputStream(image)) {
            stmt.setString(1, cropId);
            stmt.setDouble(2, quantity);
            stmt.setDouble(3, price);
            stmt.setInt(4, discount);
            stmt.setBoolean(5, isPopular);
            stmt.setBlob(6, inputStream);
            stmt.setString(7, sellerId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding data to Foodbank: " + e.getMessage());
        }
        return false;
    }

    public boolean addCrop(String id, String name, String category) {
        String query = "INSERT INTO " + Global.CROP_TABLE_NAME + " (id, name, category) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, category);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding data to Crop: " + e.getMessage());
        }
        return false;
    }

    // ------------------------- UPDATE -------------------------

    public boolean updateQuantityPriceImage(int id, double quantity, double price) {
        String query = "UPDATE " + Global.FOODBANK_TABLE_NAME +
                " SET quantity = ?, price = ? WHERE id = ?";
        return executeUpdateQuery(query, new Object[] { quantity, price, id });
    }

    public boolean updateDiscountAndIsPopular(int id, int discount, boolean isPopular) {
        String query = "UPDATE " + Global.FOODBANK_TABLE_NAME +
                " SET discount = ?, is_popular = ? WHERE id = ?";
        return executeUpdateQuery(query, new Object[] { discount, isPopular, id });
    }

    // ------------------------- DELETE -------------------------

    public boolean deleteFoodDataById(int id) {
        return executeUpdateQuery("DELETE FROM " + Global.FOODBANK_TABLE_NAME + " WHERE id = ?", new Object[] { id });
    }

    // ------------------------- READ -------------------------

    public Map<String, Object> getFoodbankById(int id) {
        String query = "SELECT * FROM " + Global.FOODBANK_TABLE_NAME + " WHERE id = ?";
        List<Map<String, Object>> results = executeReadQuery(query, new Object[] { id });
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    public List<Map<String, Object>> getAllCrops() {
        return executeReadQuery("SELECT id, name FROM " + Global.CROP_TABLE_NAME, new Object[] {});
    }

    public Map<String, Object> getCropNameById(String id) {
        String query = "SELECT name FROM " + Global.CROP_TABLE_NAME + " WHERE id = ? ORDER BY " + Global.CROP_TABLE_NAME
                + ".id ASC";
        List<Map<String, Object>> results = executeReadQuery(query, new Object[] { id });
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    public List<Map<String, Object>> getFoodbankBySearch(int limit, int page, String search) {
        boolean noLimit = (limit == -1 && page == -1);
        String query = "SELECT f.* FROM " + Global.FOODBANK_TABLE_NAME + " f " +
                "JOIN " + Global.CROP_TABLE_NAME + " c ON f.crop_id = c.id " +
                "WHERE c.name LIKE ? OR c.category LIKE ? " +
                "ORDER BY f.id ASC" + (noLimit ? "" : " LIMIT ? OFFSET ?");

        if (noLimit) {
            return executeReadQuery(query, new Object[] { "%" + search + "%", "%" + search + "%" });
        } else {
            int offset = limit * (page - 1);
            return executeReadQuery(query, new Object[] { "%" + search + "%", "%" + search + "%", limit, offset });
        }
    }

    public List<Map<String, Object>> getPopularFoodbank(int limit, int page) {
        boolean noLimit = (limit == -1 && page == -1);
        String query = "SELECT * FROM " + Global.FOODBANK_TABLE_NAME + " WHERE is_popular = true " +
                "ORDER BY quantity DESC" + (noLimit ? "" : " LIMIT ? OFFSET ?");

        if (noLimit) {
            return executeReadQuery(query, new Object[] {});
        } else {
            int offset = limit * (page - 1);
            return executeReadQuery(query, new Object[] { limit, offset });
        }
    }

    public List<Map<String, Object>> getDiscountedFoodbank(int limit, int page) {
        boolean noLimit = (limit == -1 && page == -1);
        String query = "SELECT * FROM " + Global.FOODBANK_TABLE_NAME + " WHERE discount > 0 " +
                "ORDER BY discount DESC" + (noLimit ? "" : " LIMIT ? OFFSET ?");

        if (noLimit) {
            return executeReadQuery(query, new Object[] {});
        } else {
            int offset = limit * (page - 1);
            return executeReadQuery(query, new Object[] { limit, offset });
        }
    }

    public List<Map<String, Object>> getFoodbankBySeller(int limit, int page, String sellerId) {
        boolean noLimit = (limit == -1 && page == -1);
        String query = "SELECT * FROM " + Global.FOODBANK_TABLE_NAME + " WHERE seller_id = ? " +
                "ORDER BY id ASC" + (noLimit ? "" : " LIMIT ? OFFSET ?");

        if (noLimit) {
            return executeReadQuery(query, new Object[] { sellerId });
        } else {
            int offset = limit * (page - 1);
            return executeReadQuery(query, new Object[] { sellerId, limit, offset });
        }
    }
}