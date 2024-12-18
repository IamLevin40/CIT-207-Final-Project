package main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.control.*;
import javafx.scene.image.*;
import utils.Global;
import utils.Manipulate;
import utils.ProductDisplay;

public interface ProductService {
    interface Callback {
        void onSuccess();
    }

    default void displayErrorMessage(String errorMsg, Label errorLabel) {
        errorLabel.setText(errorMsg);
    }

    default boolean validateAndSetError(String errorMsg, Label errorLabel) {
        if (!errorMsg.isEmpty()) {
            displayErrorMessage(errorMsg, errorLabel);
            return false;
        }
        return true;
    }

    default String validateHasCharacters(String field, String input) {
        if (input.isEmpty()) {
            return Manipulate.capitalize(field.replace('_', ' ')) + " is empty.";
        }
        return "";
    }

    default String validateHasSelectedOption(String field, String input) {
        if (input.isEmpty()) {
            return Manipulate.capitalize(field.replace('_', ' ')) + " has no selected option.";
        }
        return "";
    }

    default String validateExceedCharLength(String field, String input, Map<String, Integer> columnLengths) {
        if (input.length() > columnLengths.get(field)) {
            return Manipulate.capitalize(field.replace('_', ' ')) + " exceeds maximum length of "
                    + columnLengths.get(field)
                    + " characters.";
        }
        return "";
    }

    default String validateOnlyFloatDigitCharacters(String field, String input) {
        if (!input.matches("^[0-9.]+$")) {
            return Manipulate.capitalize(field.replace('_', ' ')) + " must only contain float digits.";
        }
        return "";
    }

    default String validateImageExist(File image) {
        if (image == null) {
            return "Image does not exist.";
        }
        return "";
    }

    default String validateImageType(File image) {
        if (image.exists() && image.isFile()) {
            String fileName = image.getName().toLowerCase();
            if (!(fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))) {
                return "Invalid image file type.";
            }
        }
        return "";
    }

    default String validateSellerExist(String input, DatabaseHandler dbHandler) {
        if (!dbHandler.isSellerExist(input)) {
            return "Seller does not exist.";
        }
        return "";
    }

    default String validateCropExist(String input, DatabaseHandler dbHandler) {
        if (!dbHandler.isCropExist(input)) {
            return "Crop does not exist.";
        }
        return "";
    }

    public static class ProductCreate implements ProductService {
        private final ProductDatabaseHandler dbHandler;

        public ProductCreate() {
            this.dbHandler = new ProductDatabaseHandler();
        }

        public void addFoodbank(String cropId, String quantity, String price, int discount, boolean isPopular,
                File image, String sellerId, Label errorLabel, Callback callback)
                throws FileNotFoundException, IOException {

            if (!validateAndSetError(validateHasSelectedOption("crop_id", cropId), errorLabel))
                return;
            if (!validateAndSetError(validateCropExist(cropId, dbHandler), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("quantity", quantity), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("quantity", quantity),
                    errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("price", price), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("price", price), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("discount", Integer.toString(discount)), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("discount", Integer.toString(discount)),
                    errorLabel))
                return;
            if (!validateAndSetError(validateImageExist(image), errorLabel))
                return;
            if (!validateAndSetError(validateImageType(image), errorLabel))
                return;
            if (!validateAndSetError(validateSellerExist(sellerId, dbHandler), errorLabel))
                return;

            if (dbHandler.addFoodbank(cropId, Double.parseDouble(quantity), Double.parseDouble(price), discount,
                    isPopular, image, sellerId)) {
                System.out.println("Product adding to foodbank successful.");
                callback.onSuccess();
            } else {
                displayErrorMessage("Product adding to foodbank failed.", errorLabel);
            }
        }

        public void addCrop(String id, String name, String category, Label errorLabel, Callback callback) {
            Map<String, Integer> columnLengths = dbHandler.getColumnMaxLengths(Global.CROP_TABLE_NAME);

            if (!validateAndSetError(validateHasCharacters("id", id), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("id", id, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("name", name), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("name", name, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("category", category), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("category", category, columnLengths), errorLabel))
                return;

            if (dbHandler.addCrop(id, name, category)) {
                System.out.println("Crop creating successful.");
                callback.onSuccess();
            } else {
                displayErrorMessage("Crop creating failed.", errorLabel);
            }
        }
    }

    public static class ProductUpdate implements ProductService {
        private final ProductDatabaseHandler dbHandler;

        public ProductUpdate() {
            this.dbHandler = new ProductDatabaseHandler();
        }

        public void updateQuantityPriceImage(int id, String quantity, String price, File image, Label errorLabel,
                Callback callback) throws NumberFormatException, FileNotFoundException {
            if (!validateAndSetError(validateHasCharacters("id", Integer.toString(id)), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("id", Integer.toString(id)), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("quantity", quantity), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("quantity", quantity),
                    errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("price", price), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("price", price), errorLabel))
                return;
            if (!validateAndSetError(validateImageExist(image), errorLabel))
                return;
            if (!validateAndSetError(validateImageType(image), errorLabel))
                return;

            if (dbHandler.updateQuantityPriceImage(id, Double.parseDouble(quantity), Double.parseDouble(price),
                    image)) {
                System.out.println("Product data updating successful.");
                callback.onSuccess();
            } else {
                displayErrorMessage("Product data updating failed.", errorLabel);
            }
        }

        public void updateDiscountAndIsPopular(int id, int discount, boolean isPopular, Label errorLabel,
                Callback callback) {
            if (!validateAndSetError(validateHasCharacters("id", Integer.toString(id)), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("id", Integer.toString(id)), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("discount", Integer.toString(discount)), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("discount", Integer.toString(discount)),
                    errorLabel))
                return;

            if (dbHandler.updateDiscountAndIsPopular(id, discount, isPopular)) {
                System.out.println("Product data updating successful.");
                callback.onSuccess();
            } else {
                displayErrorMessage("Product data updating failed.", errorLabel);
            }
        }
    }

    public static class ProductDelete implements ProductService {
        private final ProductDatabaseHandler dbHandler;

        public ProductDelete() {
            this.dbHandler = new ProductDatabaseHandler();
        }

        public void deleteFoodbankById(int id, Label errorLabel, Callback callback) {
            if (!validateAndSetError(validateHasCharacters("id", Integer.toString(id)), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("id", Integer.toString(id)), errorLabel))
                return;

            if (dbHandler.deleteFoodDataById(id)) {
                System.out.println("Product data deleting successful.");
                callback.onSuccess();
            } else {
                displayErrorMessage("Product data deleting failed.", errorLabel);
            }
        }
    }

    public static class ProductRead implements ProductService {
        private final ProductDatabaseHandler dbHandler;

        public ProductRead() {
            this.dbHandler = new ProductDatabaseHandler();
        }

        public ProductDisplay getProductByIdForDisplay(int id) {
            Map<String, Object> product = dbHandler.getFoodbankById(id);
            int pId = (int) product.get("id");
            String cropId = (String) product.get("crop_id");
            double quantity = (double) product.get("quantity");
            double pricePerKg = (double) product.get("price");
            int discount = (int) product.get("discount");
            String sellerId = (String) product.get("seller_id");

            double actualPrice = computePrice(quantity, pricePerKg, 0);
            double discountedPrice = computePrice(quantity, pricePerKg, discount);
            ImageView imageView = loadImage(product.get("image"));

            Map<String, Object> crop = dbHandler.getCropNameById(cropId);
            String cropName = (String) crop.get("name");

            return new ProductDisplay(pId, cropName, quantity, pricePerKg, discount, actualPrice, discountedPrice,
                    imageView, sellerId);
        }

        public Map<String, String> getAllCrops() {
            List<Map<String, Object>> rows = dbHandler.getAllCrops();
            Map<String, String> cropMap = new LinkedHashMap<>();

            for (Map<String, Object> row : rows) {
                String id = (String) row.get("id");
                String name = (String) row.get("name");
                cropMap.put(id, name);
            }

            return cropMap;
        }

        public List<ProductDisplay> getProductsForDisplay(List<Map<String, Object>> rows) {
            List<ProductDisplay> productDisplays = new ArrayList<>();

            for (Map<String, Object> row : rows) {
                int id = (int) row.get("id");
                String cropId = (String) row.get("crop_id");
                double quantity = (double) row.get("quantity");
                double pricePerKg = (double) row.get("price");
                int discount = (int) row.get("discount");
                String sellerId = (String) row.get("seller_id");

                double actualPrice = computePrice(quantity, pricePerKg, 0);
                double discountedPrice = computePrice(quantity, pricePerKg, discount);
                ImageView imageView = loadImage(row.get("image"));

                Map<String, Object> crop = dbHandler.getCropNameById(cropId);
                String cropName = (String) crop.get("name");

                productDisplays.add(
                        new ProductDisplay(id, cropName, quantity, pricePerKg, discount, actualPrice, discountedPrice,
                                imageView, sellerId));
            }

            return productDisplays;
        }

        public List<ProductDisplay> getProductsBySearchForDisplay(int limit, int page, String search) {
            List<Map<String, Object>> rows = dbHandler.getFoodbankBySearch(limit, page, search);
            return getProductsForDisplay(rows);
        }

        public List<ProductDisplay> getProductsOfSellerForDisplay(int limit, int page, String sellerId) {
            List<Map<String, Object>> rows = dbHandler.getFoodbankBySeller(limit, page, sellerId);
            return getProductsForDisplay(rows);
        }

        public List<ProductDisplay> getPopularProductsForDisplay(int limit, int page) {
            List<Map<String, Object>> rows = dbHandler.getPopularFoodbank(limit, page);
            return getProductsForDisplay(rows);
        }

        public List<ProductDisplay> getDiscountedProductsForDisplay(int limit, int page) {
            List<Map<String, Object>> rows = dbHandler.getDiscountedFoodbank(limit, page);
            return getProductsForDisplay(rows);
        }

        public double computePrice(double quantity, double pricePerKg, int discount) {
            double price;
            if (discount > 0) {
                price = (pricePerKg * (0.01 * (100 - discount))) * quantity;
            } else {
                price = pricePerKg * quantity;
            }
            DecimalFormat df = new DecimalFormat("#.##");
            return Double.parseDouble(df.format(price));
        }

        public ImageView loadImage(Object imageBlob) {
            if (imageBlob instanceof byte[]) {
                Image originalImage = new Image(new ByteArrayInputStream((byte[]) imageBlob));
                double originalWidth = originalImage.getWidth();
                double originalHeight = originalImage.getHeight();

                double squareSize = Math.min(originalWidth, originalHeight);

                PixelReader reader = originalImage.getPixelReader();
                WritableImage squareImage = new WritableImage(reader,
                        (int) ((originalWidth - squareSize) / 2),
                        (int) ((originalHeight - squareSize) / 2),
                        (int) squareSize,
                        (int) squareSize);

                ImageView imageView = new ImageView(squareImage);
                imageView.setPreserveRatio(true);

                return imageView;
            }
            return new ImageView();
        }

        public ImageView processAndLoadImage(File file) {
            try {
                byte[] imageBytes = Files.readAllBytes(file.toPath());
                return loadImage(imageBytes);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    public static class ProductCartManager implements ProductService {
        private static final String CART_FILE = "data/cart_data.txt";

        public void addToCart(String username, int productId) {
            Map<String, Set<String>> cartData = loadCartData();

            cartData.putIfAbsent(username, new HashSet<>());
            if (cartData.get(username).add(Integer.toString(productId))) {
                saveCartData(cartData);
                System.out.println("Added product " + productId + " to " + username + "'s cart.");
            } else {
                System.out.println("Product " + productId + " is already in " + username + "'s cart.");
            }
        }

        public List<Integer> getProductIds(String username) {
            Map<String, Set<String>> cartData = loadCartData();
            Set<String> productIds = cartData.getOrDefault(username, Collections.emptySet());

            // Convert product IDs from Strings to Integers
            List<Integer> integerProductIds = new ArrayList<>();
            for (String productId : productIds) {
                try {
                    integerProductIds.add(Integer.parseInt(productId));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid product ID format: " + productId);
                }
            }
            return integerProductIds;
        }

        public void deleteProduct(String username, int productId) {
            Map<String, Set<String>> cartData = loadCartData();

            if (cartData.containsKey(username)) {
                Set<String> productIds = cartData.get(username);
                String productIdStr = String.valueOf(productId);

                if (productIds.remove(productIdStr)) {
                    saveCartData(cartData);
                    System.out.println("Removed product " + productId + " from " + username + "'s cart.");
                } else {
                    System.out.println("Product " + productId + " not found in " + username + "'s cart.");
                }
            } else {
                System.out.println("No cart found for username: " + username);
            }
        }

        @SuppressWarnings("unchecked")
        private Map<String, Set<String>> loadCartData() {
            Map<String, Set<String>> cartData = new HashMap<>();
            File file = new File(CART_FILE);

            if (file.exists() && file.length() > 0) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    Object data = ois.readObject();
                    if (data instanceof Map) {
                        cartData = (Map<String, Set<String>>) data;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Error reading cart data: " + e.getMessage());
                }
            }

            return cartData;
        }

        private void saveCartData(Map<String, Set<String>> cartData) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CART_FILE))) {
                oos.writeObject(cartData);
            } catch (IOException e) {
                System.err.println("Error saving cart data: " + e.getMessage());
            }
        }

        public Set<String> getUserCart(String username) {
            Map<String, Set<String>> cartData = loadCartData();
            return cartData.getOrDefault(username, new HashSet<>());
        }
    }
}
