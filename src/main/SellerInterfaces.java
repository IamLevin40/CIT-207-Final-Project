package main;

import javafx.geometry.Pos;
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

import java.io.File;
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

        Label welcomeLabel = new Label("Welcome, " + username);
        Label popularTitleLabel = new Label("Popular Products");
        GridPane popularGridPane = createGridPane();
        Label promoSaleTitleLabel = new Label("Promo Sale");
        GridPane promoSaleGridPane = createGridPane();
        Button profileButton = new Button("Profile");

        displayPopularProducts(popularGridPane);
        displayDiscountedProducts(promoSaleGridPane);
        profileButton.setOnAction(e -> AppFrames.showScene(new SellerProfilePage(username).getScene()));

        layout.getChildren().addAll(new Label(), welcomeLabel, new Label(), popularTitleLabel, popularGridPane,
                new Label(), promoSaleTitleLabel, promoSaleGridPane, new Label(), profileButton);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }

    private void displayPopularProducts(GridPane gridPane) {
        gridPane.getChildren().clear();
        List<ProductDisplay> products = productRead.getPopularProductsForDisplay(6, 1);

        int columnCount = 2;
        for (int i = 0; i < products.size(); i++) {
            ProductDisplay product = products.get(i);
            VBox productBox = new VBox(5);
            productBox.setAlignment(Pos.TOP_LEFT);
            productBox.setStyle(
                    "-fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 10; -fx-padding: 10; -fx-min-width: 100; -fx-min-height: 180; -fx-max-width: 100; -fx-max-height: 180;");

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

        int columnCount = 2;
        for (int i = 0; i < products.size(); i++) {
            ProductDisplay product = products.get(i);
            VBox productBox = new VBox(5);
            productBox.setAlignment(Pos.TOP_LEFT);
            productBox.setStyle(
                    "-fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 10; -fx-padding: 10; -fx-min-width: 100; -fx-min-height: 180; -fx-max-width: 100; -fx-max-height: 180;");

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
            VBox productBox = new VBox(5);
            productBox.setAlignment(Pos.TOP_LEFT);
            productBox.setStyle(
                    "-fx-border-color: lightgray; -fx-border-radius: 10; -fx-background-color: #f9f9f9; -fx-background-radius: 10; -fx-padding: 10; -fx-min-width: 150; -fx-min-height: 230; -fx-max-width: 150; -fx-max-height: 230;");

            ImageView imageView = product.getImageView(130, 130);
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
// Seller Add Product Page
// User can add product to the system
//
class SellerAddProductPage {
    private final ProductCreate productCreate;
    private final ProductRead productRead;
    private String username;

    public SellerAddProductPage(String username) {
        this.username = username;
        this.productCreate = new ProductCreate();
        this.productRead = new ProductRead();
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Button backButton = new Button("Back");
        Label titleLabel = new Label("Add Product");
        Label imageLabel = new Label("Upload Image:");
        VBox imageUploadBox = new VBox(10);
        Label dragDropLabel = new Label("Drag and Drop Image Here");
        Button chooseFileButton = new Button("Choose Image");
        Label cropIdLabel = new Label("Select Crop ID:");
        ComboBox<String> cropIdComboBox = new ComboBox<>();
        Label quantityLabel = new Label("Quantity (kg):");
        NumberTextField quantityField = new NumberTextField(10);
        Label priceLabel = new Label("Price (per kg):");
        NumberTextField priceField = new NumberTextField(10);
        Button addProductButton = new Button("Add Product");
        Label errorLabel = new Label();

        File[] selectedImage = { null };
        imageUploadBox.setAlignment(Pos.CENTER);
        imageUploadBox
                .setStyle("-fx-border-style: dashed; -fx-border-width: 2; -fx-border-color: #aaa; -fx-padding: 20;"); // Internal
                                                                                                                      // style
                                                                                                                      // css
        imageUploadBox.getChildren().addAll(dragDropLabel, chooseFileButton);

        // Drag and Drop functionality
        imageUploadBox.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        imageUploadBox.setOnDragDropped(event -> {
            File file = event.getDragboard().getFiles().get(0);
            if (file != null && file.isFile()) {
                dragDropLabel.setText(file.getName());
                selectedImage[0] = file;
            }
            event.setDropCompleted(true);
            event.consume();
        });

        // File chooser functionality
        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                dragDropLabel.setText(file.getName());
                selectedImage[0] = file;
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