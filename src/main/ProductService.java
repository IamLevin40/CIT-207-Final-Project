package main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if (!input.matches("^[0-9].$")) {
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

        public void addFoodbank(String cropId, double quantity, double price, int discount, boolean isPopular,
                File image, String sellerId, Label errorLabel, Callback callback)
                throws FileNotFoundException, IOException {

            if (!validateAndSetError(validateHasSelectedOption("crop_id", cropId), errorLabel))
                return;
            if (!validateAndSetError(validateCropExist(cropId, dbHandler), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("quantity", Double.toString(quantity)), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("quantity", Double.toString(quantity)),
                    errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("price", Double.toString(price)), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("price", Double.toString(price)), errorLabel))
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

            if (dbHandler.addFoodbank(cropId, quantity, price, discount, isPopular, image, sellerId)) {
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

        public void updateQuantityPriceImage(int id, double quantity, double price, File image, Label errorLabel,
                Callback callback) {
            if (!validateAndSetError(validateHasCharacters("id", Integer.toString(id)), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("id", Integer.toString(id)), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("quantity", Double.toString(quantity)), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("quantity", Double.toString(quantity)),
                    errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("price", Double.toString(price)), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyFloatDigitCharacters("price", Double.toString(price)), errorLabel))
                return;
            if (!validateAndSetError(validateImageExist(image), errorLabel))
                return;
            if (!validateAndSetError(validateImageType(image), errorLabel))
                return;

            if (dbHandler.updateQuantityPriceImage(id, quantity, price, image)) {
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

        public List<ProductDisplay> getProductsBySearchForDisplay(int limit, int page, String search) {
            List<Map<String, Object>> rows = dbHandler.getFoodbankBySearch(limit, page, search);
            List<ProductDisplay> productDisplays = new ArrayList<>();

            for (Map<String, Object> row : rows) {
                int id = (int) row.get("id");
                double quantity = (double) row.get("quantity");
                double pricePerKg = (double) row.get("price");
                double discount = (double) row.get("discount");

                double actualPrice = computePrice(quantity, pricePerKg, discount);

                ImageView imageView = loadImage(row.get("image"));

                productDisplays.add(new ProductDisplay(
                        id,
                        (String) row.get("crop_id"),
                        actualPrice,
                        imageView));
            }

            return productDisplays;
        }

        private double computePrice(double quantity, double pricePerKg, double discount) {
            double price = pricePerKg * quantity;
            if (discount > 0) {
                price *= (1 - discount);
            }
            return price;
        }

        private ImageView loadImage(Object imageBlob) {
            if (imageBlob instanceof byte[]) {
                Image image = new Image(new ByteArrayInputStream((byte[]) imageBlob));
                ImageView imageView = new ImageView(image);

                imageView.setFitWidth(Global.IMAGE_WIDTH);
                imageView.setFitHeight(Global.IMAGE_HEIGHT);
                imageView.setPreserveRatio(true);
                return imageView;
            }
            return new ImageView();
        }
    }
}
