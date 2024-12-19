package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import main.ProductService.ProductCreate;
import main.ProductService.ProductRead;
import main.ProductService.ProductUpdate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import utils.Global;
import utils.NumberTextField;
import utils.ProductDisplay;

//
// Seller Home Page
// Displays search bar, categories, popular products, and promo sales
//
class SellerHomePage {
    private final ProductRead productRead;
    private String username;

    public SellerHomePage(String username) {
        this.productRead = new ProductRead();
        this.username = username;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        // Content container
        VBox content = new VBox();
        content.setAlignment(Pos.TOP_CENTER);
        
        // Create footer
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #e0e0e0; -fx-border-width: 1 0 0 0;");
        
        Button profileButton = new Button("Profile");
        profileButton.setOnAction(e -> AppFrames.showScene(new SellerProfilePage(username).getScene()));
        
        footer.getChildren().add(profileButton);

        Label welcomeLabel = new Label("Welcome, " + username);
        Label popularTitleLabel = new Label("Popular Products");
        GridPane popularGridPane = createGridPane();
        Label promoSaleTitleLabel = new Label("Promo Sale");
        GridPane promoSaleGridPane = createGridPane();

        displayPopularProducts(popularGridPane);
        displayDiscountedProducts(promoSaleGridPane);

        content.getChildren().addAll(new Label(), welcomeLabel, new Label(), popularTitleLabel, popularGridPane,
                new Label(), promoSaleTitleLabel, promoSaleGridPane, new Label());

        layout.getChildren().addAll(content, footer);

        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }

    private void displayPopularProducts(GridPane gridPane) {
        gridPane.getChildren().clear();
        List<ProductDisplay> products = productRead.getPopularProductsForDisplay(6, 1);

        int columnCount = 3;
        for (int i = 0; i < products.size(); i++) {
            ProductDisplay product = products.get(i);
            VBox productBox = new VBox();
            productBox.setAlignment(Pos.TOP_LEFT);
            productBox.setStyle(
                    "-fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 10; -fx-padding: 10; -fx-min-width: 100; -fx-min-height: 160; -fx-max-width: 100; -fx-max-height: 160;");

            ImageView imageView = product.getImageView(80, 80);
            Label cropIdLabel = new Label(product.getCropName());
            Label quantityLabel = new Label(product.getQuantity() + " kg");
            Label sellerLabel = new Label("@" + product.getSellerId());

            Text discountedPriceText = new Text("₱ " + product.getDiscountedPrice() + " ");
            TextFlow priceFlow;
            if (product.getActualPrice() != product.getDiscountedPrice()) {
                Text actualPriceText = new Text("₱ " + product.getActualPrice());
                actualPriceText.setStrikethrough(true);
                actualPriceText.setStyle("-fx-fill: gray;");

                priceFlow = new TextFlow(discountedPriceText, actualPriceText);
            } else {
                priceFlow = new TextFlow(discountedPriceText);
            }

            productBox.getChildren().addAll(imageView, cropIdLabel, quantityLabel, priceFlow, sellerLabel);

            int row = i / columnCount;
            int col = i % columnCount;
            gridPane.add(productBox, col, row);
        }
    }

    private void displayDiscountedProducts(GridPane gridPane) {
        gridPane.getChildren().clear();
        List<ProductDisplay> products = productRead.getDiscountedProductsForDisplay(6, 1);

        int columnCount = 3;
        for (int i = 0; i < products.size(); i++) {
            ProductDisplay product = products.get(i);
            VBox productBox = new VBox();
            productBox.setAlignment(Pos.TOP_LEFT);
            productBox.setStyle(
                    "-fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 10; -fx-padding: 10; -fx-min-width: 100; -fx-min-height: 160; -fx-max-width: 100; -fx-max-height: 160;");

            ImageView imageView = product.getImageView(80, 80);
            Label cropIdLabel = new Label(product.getCropName());
            Label quantityLabel = new Label(product.getQuantity() + " kg");
            Label sellerLabel = new Label("@" + product.getSellerId());

            Text discountedPriceText = new Text("₱ " + product.getDiscountedPrice() + " ");
            TextFlow priceFlow;
            if (product.getActualPrice() != product.getDiscountedPrice()) {
                Text actualPriceText = new Text("₱ " + product.getActualPrice());
                actualPriceText.setStrikethrough(true);
                actualPriceText.setStyle("-fx-fill: gray;");

                priceFlow = new TextFlow(discountedPriceText, actualPriceText);
            } else {
                priceFlow = new TextFlow(discountedPriceText);
            }

            productBox.getChildren().addAll(imageView, cropIdLabel, quantityLabel, priceFlow, sellerLabel);

            int row = i / columnCount;
            int col = i % columnCount;
            gridPane.add(productBox, col, row);
        }
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        return gridPane;
    }
}

//
// Seller Profile Page
// Displays profile information and miscellaneous buttons
//
class SellerProfilePage {
    private String username;

    public SellerProfilePage(String username) {
        this.username = username;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Button backButton = new Button("Back");
        Label profileLabel = new Label("Profile");
        Label welcomeLabel = new Label("Welcome, " + username);
        Button shopPageButton = new Button("My Shop");
        Button logOutButton = new Button("Log Out");

        shopPageButton.setOnAction(e -> AppFrames.showScene(new SellerShopPage(username).getScene()));
        backButton.setOnAction(e -> AppFrames.showScene(new SellerHomePage(username).getScene()));
        logOutButton.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));

        layout.getChildren().addAll(backButton, profileLabel, new Label(), welcomeLabel, shopPageButton, logOutButton);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}

//
// Seller Shop Page
// Displays products that the seller is selling
//
class SellerShopPage {
    private final ProductRead productRead;
    private String username;

    public SellerShopPage(String username) {
        this.productRead = new ProductRead();
        this.username = username;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Button backButton = new Button("Back");
        Label titleLabel = new Label("My Shop");
        GridPane gridPane = createGridPane();
        Button addButton = new Button("+");

        displayProducts(gridPane);
        backButton.setOnAction(e -> AppFrames.showScene(new SellerProfilePage(username).getScene()));
        addButton.setOnAction(e -> AppFrames.showScene(new SellerAddProductPage(username).getScene()));

        layout.getChildren().addAll(backButton, new Label(), titleLabel, gridPane, addButton);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }

    private void displayProducts(GridPane gridPane) {
        gridPane.getChildren().clear();
        List<ProductDisplay> products = productRead.getProductsOfSellerForDisplay(-1, -1, username);

        int columnCount = 2;
        for (int i = 0; i < products.size(); i++) {
            ProductDisplay product = products.get(i);
            VBox productBox = new VBox();
            productBox.setAlignment(Pos.TOP_LEFT);
            productBox.setStyle(
                    "-fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 10; -fx-padding: 10; -fx-min-width: 150; -fx-min-height: 210; -fx-max-width: 150; -fx-max-height: 210;");

            ImageView imageView = product.getImageView(130, 130);
            Label cropIdLabel = new Label(product.getCropName());
            Label quantityLabel = new Label(product.getQuantity() + " kg");

            HBox sellerBox = new HBox(30);
            sellerBox.setAlignment(Pos.CENTER_LEFT);
            Label sellerLabel = new Label("@" + product.getSellerId());
            Button editButton = new Button("E");

            editButton.setStyle(
                    "-fx-background-color: #2196f3; -fx-text-fill: white; -fx-padding: 0 10; -fx-border-radius: 5; -fx-background-radius: 5;");
            editButton.setOnAction(
                    e -> AppFrames.showScene(new SellerEditProductPage(username, product).getScene()));
            sellerBox.getChildren().addAll(sellerLabel, editButton);

            Text discountedPriceText = new Text("₱ " + product.getDiscountedPrice() + " ");
            TextFlow priceFlow;
            if (product.getActualPrice() != product.getDiscountedPrice()) {
                Text actualPriceText = new Text("₱ " + product.getActualPrice());
                actualPriceText.setStrikethrough(true);
                actualPriceText.setStyle("-fx-fill: gray;");

                priceFlow = new TextFlow(discountedPriceText, actualPriceText);
            } else {
                priceFlow = new TextFlow(discountedPriceText);
            }

            productBox.getChildren().addAll(imageView, cropIdLabel, quantityLabel, priceFlow, sellerBox);

            int row = i / columnCount;
            int col = i % columnCount;
            gridPane.add(productBox, col, row);
        }
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        return gridPane;
    }
}

//
// Seller Add Product Page
// User can add product to the system
//
class SellerAddProductPage {
    private final ProductCreate productCreate;
    private final ProductRead productRead;
    private String username;
    private File selectedImageFile = null;

    public SellerAddProductPage(String username) {
        this.productCreate = new ProductCreate();
        this.productRead = new ProductRead();
        this.username = username;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Button backButton = new Button("Back");
        Label titleLabel = new Label("Add Product");
        Label imageLabel = new Label("Upload Image:");
        ImageView imageView = new ImageView();
        VBox imageUploadBox = new VBox();
        Label dragDropLabel = new Label("Drag and Drop Image Here");
        Button chooseFileButton = new Button("Choose Image");
        Label cropIdLabel = new Label("Select Crop:");
        ComboBox<String> cropIdComboBox = new ComboBox<>();
        Label quantityLabel = new Label("Quantity (kg):");
        NumberTextField quantityField = new NumberTextField(10);
        Label priceLabel = new Label("Price (per kg):");
        NumberTextField priceField = new NumberTextField(10);
        Button addProductButton = new Button("Add Product");
        Label errorLabel = new Label();

        File[] selectedImage = { null };
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageUploadBox.setAlignment(Pos.CENTER);
        imageUploadBox
                .setStyle("-fx-border-style: dashed; -fx-border-width: 2; -fx-border-color: #aaa; -fx-padding: 20;");
        imageUploadBox.getChildren().addAll(dragDropLabel, imageView, chooseFileButton);

        imageUploadBox.setOnDragOver(event -> {
            if (event.getGestureSource() != imageUploadBox && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        imageUploadBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                selectedImageFile = db.getFiles().get(0);
                ImageView newImageView = productRead.processAndLoadImage(selectedImageFile);
                if (newImageView != null) {
                    layout.getChildren().remove(imageView);
                    imageView.setImage(newImageView.getImage());
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        // FileChooser Handler
        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                selectedImageFile = file;
                ImageView newImageView = productRead.processAndLoadImage(selectedImageFile);
                if (newImageView != null) {
                    imageView.setImage(newImageView.getImage());
                }
            }
        });

        Map<String, String> cropMap = productRead.getAllCrops();
        cropIdComboBox.getItems().addAll(cropMap.values());

        addProductButton.setOnAction(e -> {
            String cropName = cropIdComboBox.getValue();
            String cropId = getKeyByValue(cropMap, cropName);
            String quantity = quantityField.getText();
            String price = priceField.getText();
            int discount = 0;
            boolean isPopular = false;

            try {
                productCreate.addFoodbank(cropId, quantity, price, discount, isPopular, selectedImage[0], username,
                        errorLabel, new ProductService.Callback() {
                            @Override
                            public void onSuccess() {
                                AppFrames.showScene(new SellerShopPage(username).getScene());
                            }
                        });
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        errorLabel.getStyleClass().add("error-label");
        backButton.setOnAction(e -> AppFrames.showScene(new SellerShopPage(username).getScene()));

        layout.getChildren().addAll(backButton, new Label(), titleLabel, imageLabel, imageUploadBox, cropIdLabel,
                cropIdComboBox, quantityLabel, quantityField, priceLabel, priceField, addProductButton, errorLabel);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }

    private <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}

//
// Seller Edit Product Page
// User can edit the product from the system
//
class SellerEditProductPage {
    private final ProductUpdate productUpdate;
    private final ProductRead productRead;
    private String username;
    private ProductDisplay product;
    private File selectedImageFile = null;

    public SellerEditProductPage(String username, ProductDisplay product) {
        this.productUpdate = new ProductUpdate();
        this.productRead = new ProductRead();
        this.username = username;
        this.product = product;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Button backButton = new Button("Back");
        Label titleLabel = new Label("Edit Product");
        ImageView imageView = product.getImageView(200, 200);
        VBox imageUploadBox = new VBox();
        Label dragDropLabel = new Label("Drag and Drop Image Here");
        Button chooseFileButton = new Button("Choose Image");
        Label cropNameLabel = new Label("Crop Name:");
        Label cropNameText = new Label(product.getCropName());
        Label quantityLabel = new Label("Quantity (kg):");
        NumberTextField quantityField = new NumberTextField(10);
        Label priceLabel = new Label("Price (per kg):");
        NumberTextField priceField = new NumberTextField(10);
        Button saveButton = new Button("Save Changes");
        Label errorLabel = new Label();

        imageUploadBox.setAlignment(Pos.CENTER);
        imageUploadBox
                .setStyle("-fx-border-style: dashed; -fx-border-width: 2; -fx-border-color: #aaa; -fx-padding: 20;");
        imageUploadBox.getChildren().addAll(dragDropLabel, imageView, chooseFileButton);

        imageUploadBox.setOnDragOver(event -> {
            if (event.getGestureSource() != imageUploadBox && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        imageUploadBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                selectedImageFile = db.getFiles().get(0);
                ImageView newImageView = productRead.processAndLoadImage(selectedImageFile);
                if (newImageView != null) {
                    layout.getChildren().remove(imageView);
                    imageView.setImage(newImageView.getImage());
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        // FileChooser Handler
        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                selectedImageFile = file;
                ImageView newImageView = productRead.processAndLoadImage(selectedImageFile);
                if (newImageView != null) {
                    imageView.setImage(newImageView.getImage());
                }
            }
        });

        quantityField.setText(Double.toString(product.getQuantity()));
        priceField.setText(Double.toString(product.getPricePerKg()));
        saveButton.setOnAction(e -> {
            String quantity = quantityField.getText();
            String price = priceField.getText();

            try {
                productUpdate.updateQuantityPriceImage(product.getId(), quantity, price, selectedImageFile, errorLabel,
                        new ProductUpdate.Callback() {
                            @Override
                            public void onSuccess() {
                                AppFrames.showScene(new SellerShopPage(username).getScene());
                            }
                        });
            } catch (NumberFormatException | FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        errorLabel.getStyleClass().add("error-label");
        backButton.setOnAction(e -> AppFrames.showScene(new SellerShopPage(username).getScene()));

        layout.getChildren().addAll(backButton, new Label(), titleLabel, imageUploadBox, cropNameLabel, cropNameText,
                quantityLabel, quantityField, priceLabel, priceField, saveButton, errorLabel);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}
