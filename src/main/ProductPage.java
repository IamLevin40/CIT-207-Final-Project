package main;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import main.ProductService.ProductRead;

import java.util.List;

import utils.ProductDisplay;
import utils.Global;

//
// Seller Home Page
// Displays search bar, categories, popular products, and promo sales
//
class SellerHomePage {
    private final ProductRead productRead;
    private String username;

    public SellerHomePage(String username) {
        this.username = username;
        this.productRead = new ProductRead();
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        GridPane gridPane = createGridPane();
        Label welcomeLabel = new Label("Welcome, " + username);
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        Button shopButton = new Button("Add");
        Button backButton = new Button("Back");

        searchField.setPromptText("Enter search term...");
        searchButton.setOnAction(e -> updateProductDisplay(searchField.getText(), gridPane));
        shopButton.setOnAction(e -> AppFrames.showScene(new SellerProfilePage(username).getScene()));
        backButton.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));

        layout.getChildren().addAll(backButton, welcomeLabel, searchField, searchButton, gridPane, shopButton);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }

    private void updateProductDisplay(String searchQuery, GridPane gridPane) {
        gridPane.getChildren().clear();
        List<ProductDisplay> products = productRead.getProductsBySearchForDisplay(10, 1, searchQuery);

        for (ProductDisplay product : products) {
            VBox productBox = new VBox(5);
            Label cropIdLabel = new Label("Crop ID: " + product.getCropId());
            Label priceLabel = new Label("Price: $" + product.getPrice());
            ImageView imageView = product.getImageView();

            productBox.getChildren().addAll(cropIdLabel, priceLabel, imageView);
            gridPane.add(productBox, 0, gridPane.getRowCount());
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
// Buyer Home Page
// Displays search bar, categories, popular products, and promo sales
//
class BuyerHomePage {
    private final ProductRead productRead;
    private String username;

    public BuyerHomePage(String username) {
        this.username = username;
        this.productRead = new ProductRead();
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        GridPane gridPane = createGridPane();
        Label welcomeLabel = new Label("Welcome, " + username);
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");

        searchField.setPromptText("Enter search term...");
        searchButton.setOnAction(e -> updateProductDisplay(searchField.getText(), gridPane));
        backButton.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));

        layout.getChildren().addAll(backButton, welcomeLabel, searchField, searchButton, gridPane);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }

    private void updateProductDisplay(String searchQuery, GridPane gridPane) {
        gridPane.getChildren().clear();
        List<ProductDisplay> products = productRead.getProductsBySearchForDisplay(10, 1, searchQuery);

        for (ProductDisplay product : products) {
            VBox productBox = new VBox(5);
            Label cropIdLabel = new Label("Crop ID: " + product.getCropId());
            Label priceLabel = new Label("Price: $" + product.getPrice());
            ImageView imageView = product.getImageView();

            productBox.getChildren().addAll(cropIdLabel, priceLabel, imageView);
            gridPane.add(productBox, 0, gridPane.getRowCount());
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
//
class SellerProfilePage {
    private final ProductRead productRead;
    private String username;

    public SellerProfilePage(String username) {
        this.username = username;
        this.productRead = new ProductRead();
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Label profileLabel = new Label("Profile");
        Label welcomeLabel = new Label("Welcome, " + username);
        Button shopPageButton = new Button("My Shop");
        Button backButton = new Button("Back");

        shopPageButton.setOnAction(e -> AppFrames.showScene(new SellerShopPage(username).getScene()));
        backButton.setOnAction(e -> AppFrames.showScene(new SellerHomePage(username).getScene()));

        layout.getChildren().addAll(backButton, profileLabel, new Label(), welcomeLabel, shopPageButton);
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
        this.username = username;
        this.productRead = new ProductRead();
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Label titleLabel = new Label("My Shop");
        Button addButton = new Button("+");
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> AppFrames.showScene(new SellerProfilePage(username).getScene()));
        addButton.setOnAction(e -> AppFrames.showScene(new SellerAddProductPage(username).getScene()));

        layout.getChildren().addAll(backButton, new Label(), titleLabel, addButton);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}

//
// Seller Add Product Page
// User can add product to the system
//
class SellerAddProductPage {
    private final ProductRead productRead;
    private String username;

    public SellerAddProductPage(String username) {
        this.username = username;
        this.productRead = new ProductRead();
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("product-frame");

        Label titleLabel = new Label("Add Product");
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> AppFrames.showScene(new SellerShopPage(username).getScene()));

        layout.getChildren().addAll(backButton, new Label(), titleLabel);
        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}