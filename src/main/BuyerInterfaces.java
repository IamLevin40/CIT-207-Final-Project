package main;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.ProductService.ProductRead;

import java.util.List;

import utils.ProductDisplay;
import utils.Global;

//
// Buyer Home Page
// Displays search bar, categories, popular products, and promo sales
//
class BuyerHomePage {
    private final ProductRead productRead;
    private String username;

    public BuyerHomePage(String username) {
        this.productRead = new ProductRead();
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
        GridPane popularGridPane = createGridPane();
        Label promoSaleTitleLabel = new Label("Promo Sale");
        GridPane promoSaleGridPane = createGridPane();
        Button profileButton = new Button("Profile");

        searchField.setPromptText("Enter search term...");
        searchButton
                .setOnAction(e -> AppFrames.showScene(new BuyerSearchPage(username, searchField.getText()).getScene()));
        displayPopularProducts(popularGridPane);
        displayDiscountedProducts(promoSaleGridPane);
        profileButton.setOnAction(e -> AppFrames.showScene(new BuyerProfilePage(username).getScene()));

        layout.getChildren().addAll(new Label(), welcomeLabel, searchField, searchButton, new Label(),
                popularTitleLabel, popularGridPane,
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
// Buyer Search Page
// Displays products based on the searched
//
class BuyerSearchPage {
    private final ProductRead productRead;
    private String username;
    private String searchQuery;

    public BuyerSearchPage(String username, String searchQuery) {
        this.username = username;
        this.searchQuery = searchQuery;
        this.productRead = new ProductRead();
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
        Button logOutButton = new Button("Log Out");

        backButton.setOnAction(e -> AppFrames.showScene(new SellerHomePage(username).getScene()));
        logOutButton.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));

        layout.getChildren().addAll(backButton, profileLabel, new Label(), welcomeLabel, logOutButton);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}