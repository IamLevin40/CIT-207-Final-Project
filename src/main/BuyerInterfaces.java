package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.ProductService.ProductCartManager;
import main.ProductService.ProductDelete;

import main.ProductService.ProductRead;
import main.ProductService.ProductUpdate;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ProductDisplay;
import utils.ProductInfo;
import utils.Global;

//
// Buyer Home Page
// Displays search bar, categories, popular products, and promo sales
//
class BuyerHomePage {
    private final ProductRead productRead;
    private final ProductCartManager productCartManager;
    private String username;

    public BuyerHomePage(String username) {
        this.productRead = new ProductRead();
        this.productCartManager = new ProductCartManager();
        this.username = username;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Label welcomeLabel = new Label("Welcome, " + username);
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        Label popularTitleLabel = new Label("Popular Products");
        Button seeAllPopularButton = new Button("See All");
        GridPane popularGridPane = createGridPane();
        Label promoSaleTitleLabel = new Label("Promo Sale");
        Button seeAllPromoButton = new Button("See All");
        GridPane promoSaleGridPane = createGridPane();
        Button profileButton = new Button("Profile");

        searchField.setPromptText("Enter search term...");
        searchButton
                .setOnAction(e -> AppFrames.showScene(new BuyerSearchPage(username, searchField.getText()).getScene()));
        seeAllPopularButton.setOnAction(e -> AppFrames.showScene(new BuyerPopularPage(username).getScene()));
        seeAllPromoButton.setOnAction(e -> AppFrames.showScene(new BuyerPromoPage(username).getScene()));
        displayPopularProducts(popularGridPane);
        displayDiscountedProducts(promoSaleGridPane);
        profileButton.setOnAction(e -> AppFrames.showScene(new BuyerProfilePage(username).getScene()));

        layout.getChildren().addAll(new Label(), welcomeLabel, searchField, searchButton, new Label(),
                popularTitleLabel, seeAllPopularButton, popularGridPane, new Label(), promoSaleTitleLabel,
                seeAllPromoButton, promoSaleGridPane, new Label(), profileButton);
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

            HBox sellerBox = new HBox(30);
            sellerBox.setAlignment(Pos.CENTER_LEFT);

            
            Label sellerLabel = new Label("@" + product.getSellerId());
            Button addToCartButton = new Button("+");

            addToCartButton.setStyle(
                    "-fx-background-color:rgb(33, 197, 118); -fx-text-fill: white; -fx-padding: 0 10; -fx-border-radius: 5; -fx-background-radius: 5;");
            addToCartButton.setOnAction(e -> productCartManager.addToCart(username, product.getId()));
            sellerBox.getChildren().addAll(sellerLabel, addToCartButton);
           


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

            HBox sellerBox = new HBox(30);
            sellerBox.setAlignment(Pos.CENTER_LEFT);

            
            Label sellerLabel = new Label("@" + product.getSellerId());
            Button addToCartButton = new Button("+");

            addToCartButton.setStyle(
                    "-fx-background-color:rgb(33, 197, 118); -fx-text-fill: white; -fx-padding: 0 10; -fx-border-radius: 5; -fx-background-radius: 5;");
            addToCartButton.setOnAction(e -> productCartManager.addToCart(username, product.getId()));
            sellerBox.getChildren().addAll(sellerLabel, addToCartButton);
           

            

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
// Buyer Popular Page
// Displays all current popular products
//
class BuyerPopularPage {
    private final ProductRead productRead;
    private final ProductCartManager productCartManager;
    private String username;

    public BuyerPopularPage(String username) {
        this.productRead = new ProductRead();
        this.productCartManager = new ProductCartManager();
        this.username = username;
        this.username = username;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Button backButton = new Button("Back");
        Label popularTitleLabel = new Label("Popular Products");
        GridPane popularGridPane = createGridPane();

        displayPopularProducts(popularGridPane);
        backButton.setOnAction(e -> AppFrames.showScene(new BuyerHomePage(username).getScene()));

        layout.getChildren().addAll(backButton, new Label(), popularTitleLabel, popularGridPane);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }

    private void displayPopularProducts(GridPane gridPane) {
        gridPane.getChildren().clear();
        List<ProductDisplay> products = productRead.getPopularProductsForDisplay(6, 1);

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
            Button addToCartButton = new Button("+");

            addToCartButton.setStyle(
                    "-fx-background-color:rgb(33, 197, 118); -fx-text-fill: white; -fx-padding: 0 10; -fx-border-radius: 5; -fx-background-radius: 5;");
            addToCartButton.setOnAction(e -> productCartManager.addToCart(username, product.getId()));
            sellerBox.getChildren().addAll(sellerLabel, addToCartButton);

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
// Buyer Promo Page
// Displays all current promo products
//
class BuyerPromoPage {
    private final ProductRead productRead;
    private final ProductCartManager productCartManager;
    private String username;

    public BuyerPromoPage(String username) {
        this.productRead = new ProductRead();
        this.productCartManager = new ProductCartManager();
        this.username = username;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Button backButton = new Button("Back");
        Label promoSaleTitleLabel = new Label("Promo Sale");
        GridPane promoSaleGridPane = createGridPane();

        displayDiscountedProducts(promoSaleGridPane);
        backButton.setOnAction(e -> AppFrames.showScene(new BuyerHomePage(username).getScene()));

        layout.getChildren().addAll(backButton, new Label(), promoSaleTitleLabel, promoSaleGridPane);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }

    private void displayDiscountedProducts(GridPane gridPane) {
        gridPane.getChildren().clear();
        List<ProductDisplay> products = productRead.getDiscountedProductsForDisplay(-1, -1);

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
            Button addToCartButton = new Button("+");

            addToCartButton.setStyle(
                    "-fx-background-color:rgb(33, 197, 118); -fx-text-fill: white; -fx-padding: 0 10; -fx-border-radius: 5; -fx-background-radius: 5;");
            addToCartButton.setOnAction(e -> productCartManager.addToCart(username, product.getId()));
            sellerBox.getChildren().addAll(sellerLabel, addToCartButton);

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
// Buyer Search Page
// Displays products based on the searched
//
class BuyerSearchPage {
    private final ProductRead productRead;
    private final ProductCartManager productCartManager;
    private String username;
    private String searchQuery;

    public BuyerSearchPage(String username, String searchQuery) {
        this.productRead = new ProductRead();
        this.productCartManager = new ProductCartManager();
        this.username = username;
        this.searchQuery = searchQuery;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Button backButton = new Button("Back");
        GridPane gridPane = createGridPane();
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");

        searchField.setPromptText("Enter search term...");
        searchField.setText(searchQuery);
        updateProductDisplay(searchField.getText(), gridPane);
        searchButton.setOnAction(e -> updateProductDisplay(searchField.getText(), gridPane));
        backButton.setOnAction(e -> AppFrames.showScene(new BuyerHomePage(username).getScene()));

        layout.getChildren().addAll(backButton, new Label(), searchField, searchButton, gridPane);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }

    private void updateProductDisplay(String searchQuery, GridPane gridPane) {
        gridPane.getChildren().clear();
        List<ProductDisplay> products = productRead.getProductsBySearchForDisplay(-1, -1, searchQuery);

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
            Button addToCartButton = new Button("+");

            addToCartButton.setStyle(
                    "-fx-background-color:rgb(33, 197, 118); -fx-text-fill: white; -fx-padding: 0 10; -fx-border-radius: 5; -fx-background-radius: 5;");
            addToCartButton.setOnAction(e -> productCartManager.addToCart(username, product.getId()));
            sellerBox.getChildren().addAll(sellerLabel, addToCartButton);

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
// Buyer Profile Page
// Displays profile information and miscellaneous buttons
//
class BuyerProfilePage {
    private String username;

    public BuyerProfilePage(String username) {
        this.username = username;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Button backButton = new Button("Back");
        Label profileLabel = new Label("Profile");
        Label welcomeLabel = new Label("Welcome, " + username);
        Button cartPageButton = new Button("My Cart");
        Button logOutButton = new Button("Log Out");

        cartPageButton.setOnAction(e -> AppFrames.showScene(new BuyerCartPage(username).getScene()));
        logOutButton.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));
        backButton.setOnAction(e -> AppFrames.showScene(new BuyerHomePage(username).getScene()));

        layout.getChildren().addAll(backButton, profileLabel, new Label(), welcomeLabel, cartPageButton, logOutButton);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}

//
// Buyer Cart Page
// Displays products that were added to cart from the system
//
class BuyerCartPage {
    private final ProductUpdate productUpdate;
    private final ProductDelete productDelete;
    private final ProductRead productRead;
    private final ProductCartManager productCartManager;
    private String username;

    private VBox summaryBox;
    private Map<Integer, ProductInfo> adjustedQuantities;

    public BuyerCartPage(String username) {
        this.productUpdate = new ProductUpdate();
        this.productDelete = new ProductDelete();
        this.productRead = new ProductRead();
        this.productCartManager = new ProductCartManager();
        this.username = username;
        this.adjustedQuantities = new HashMap<>();
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(10));
        layout.setSpacing(10);

        Button backButton = new Button("Back");
        Label titleLabel = new Label("Cart Page");
        GridPane gridPane = createGridPane();
        List<Integer> productIds = productCartManager.getProductIds(username);
        Button checkOutButton = new Button("Check Out");
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");

        summaryBox = createSummaryBox(productIds);
        updateProductDisplay(productIds, gridPane);
        updateSummaryBox();

        checkOutButton.setOnAction(e -> checkOut(errorLabel));
        backButton.setOnAction(e -> AppFrames.showScene(new BuyerProfilePage(username).getScene()));

        layout.getChildren().addAll(backButton, titleLabel, gridPane, summaryBox, checkOutButton, errorLabel);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }

    private void updateProductDisplay(List<Integer> productIds, GridPane gridPane) {
        gridPane.getChildren().clear();

        for (int i = 0; i < productIds.size(); i++) {
            ProductDisplay product = productRead.getProductByIdForDisplay(productIds.get(i));

            adjustedQuantities.put(product.getId(),
                    new ProductInfo(product.getQuantity(), product.getPricePerKg(), product.getDiscount()));

            gridPane.add(createProductLayout(product), 0, i);
        }
    }

    private HBox createProductLayout(ProductDisplay product) {
        HBox productRow = new HBox(10);
        productRow.setAlignment(Pos.CENTER_LEFT);
        productRow.setPadding(new Insets(10));

        ProductInfo productInfo = adjustedQuantities.get(product.getId());

        ImageView imageView = product.getImageView(80, 80);
        VBox detailsBox = new VBox(5);
        Label nameLabel = new Label(product.getCropName());
        Label quantityLabel = new Label("Available: " + product.getQuantity() + " kg");
        Label priceLabel = new Label("₱ " + String.format("%.2f", productInfo.computePrice()));

        HBox quantityControls = new HBox(5);
        Button minusButton = new Button("-");
        Button plusButton = new Button("+");
        Label quantityInputLabel = new Label(String.format("%.1f", productInfo.getQuantity()));
        Button trashButton = new Button("🗑");

        detailsBox.setAlignment(Pos.CENTER_LEFT);
        quantityControls.setAlignment(Pos.CENTER_LEFT);

        minusButton.setOnAction(e -> {
            if (productInfo.getQuantity() > 0.1d) {
                productInfo.setQuantity(productInfo.getQuantity() - 0.1d);
                quantityInputLabel.setText(String.format("%.1f", productInfo.getQuantity()));
                priceLabel.setText("₱ " + String.format("%.2f", productInfo.computePrice()));
                updateSummaryBox();
            }
        });

        plusButton.setOnAction(e -> {
            if (productInfo.getQuantity() < product.getQuantity()) {
                productInfo.setQuantity(productInfo.getQuantity() + 0.1d);
                quantityInputLabel.setText(String.format("%.1f", productInfo.getQuantity()));
                priceLabel.setText("₱ " + String.format("%.2f", productInfo.computePrice()));
                updateSummaryBox();
            }
        });

        trashButton.setOnAction(e -> {
            productCartManager.deleteProduct(username, product.getId());
            adjustedQuantities.remove(product.getId());
            AppFrames.showScene(new BuyerCartPage(username).getScene());
        });

        quantityControls.getChildren().addAll(minusButton, quantityInputLabel, plusButton);
        detailsBox.getChildren().addAll(nameLabel, quantityLabel, priceLabel, quantityControls);
        productRow.getChildren().addAll(imageView, detailsBox, trashButton);

        return productRow;
    }

    private VBox createSummaryBox(List<Integer> productIds) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10));

        updateSummaryContent(box);
        return box;
    }

    private void updateSummaryBox() {
        summaryBox.getChildren().clear();
        updateSummaryContent(summaryBox);
    }

    private void updateSummaryContent(VBox box) {
        int totalItems = adjustedQuantities.size();
        double totalWeight = 0.0;
        double totalPrice = 0.0;

        for (ProductInfo productInfo : adjustedQuantities.values()) {
            totalWeight += productInfo.getQuantity();
            totalPrice += productInfo.computePrice();
        }

        double shippingFee = 0.0;
        double overallPrice = totalPrice + shippingFee;

        Label totalItemlabel = new Label("Total Item: \t\t" + totalItems);
        Label totalWeightLabel = new Label("Total Weight: \t" + String.format("%.1f kg", totalWeight));
        Label totalPriceLabel = new Label("Total Price: \t\t₱ " + String.format("%.2f", totalPrice));
        Label shippingFeeLabel = new Label("Shipping Fee: \t\tFREE");
        Label overallPriceLabel = new Label("Overall Price: \t\t₱ " + String.format("%.2f", overallPrice));

        box.getChildren().addAll(totalItemlabel, totalWeightLabel, totalPriceLabel, shippingFeeLabel, new Separator(),
                overallPriceLabel);
    }

    private void checkOut(Label errorLabel) {
        for (Map.Entry<Integer, ProductInfo> entry : adjustedQuantities.entrySet()) {
            int productId = entry.getKey();
            ProductInfo productInfo = entry.getValue();

            DecimalFormat df = new DecimalFormat("#.##");
            double newQuantity = Double.parseDouble(df
                    .format(productRead.getProductByIdForDisplay(productId).getQuantity() - productInfo.getQuantity()));
            if (newQuantity < 0) {
                errorLabel.setText("Error: Quantity exceeds available stock for Product ID " + productId);
                break;
            }

            if (newQuantity == 0) {
                productDelete.deleteFoodbankById(productId, errorLabel, new ProductService.Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Successfully deleted Product ID: " + productId);
                    }
                });
            }

            productUpdate.updateQuantity(productId, String.valueOf(newQuantity), errorLabel,
                    new ProductService.Callback() {
                        @Override
                        public void onSuccess() {
                            System.out.println("Quantity updated successfully for Product ID: " + productId);
                        }
                    });

            productCartManager.deleteProduct(username, productId);
        }

        AppFrames.showScene(new BuyerOrderSuccessPage(username).getScene());
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
// Buyer Order Success Page
// Displays order placed successful
//
class BuyerOrderSuccessPage {
    private String username;

    public BuyerOrderSuccessPage(String username) {
        this.username = username;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Button backButton = new Button("Back");
        Label titleLabel = new Label("Order Placed Successfully!");
        Button continueButton = new Button("Continue");

        backButton.setOnAction(e -> AppFrames.showScene(new BuyerHomePage(username).getScene()));
        continueButton.setOnAction(e -> AppFrames.showScene(new BuyerHomePage(username).getScene()));

        layout.getChildren().addAll(backButton, titleLabel, new Label(), continueButton);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}