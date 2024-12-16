package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import utils.Global;

public class HomePage extends Application {
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @SuppressWarnings("static-access")
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        showScene(new MainMenu().getScene());
    }

    public static void showScene(Scene scene) {
        scene.getStylesheets().add("styles.css"); // Link the CSS file here
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        stage.setTitle("Foodai");
    }
}

class SellerHomePage {
    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("home-page-frame");

        Label welcomeLabel = new Label("Welcome to the Seller Home Page!");
        Button logoutButton = new Button("Logout");

        logoutButton.setOnAction(e -> AccountFormPage.showScene(new MainMenu().getScene()));

        layout.getChildren().addAll(welcomeLabel, logoutButton);

        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}

class BuyerHomePage {
    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("home-page-frame");

        Label welcomeLabel = new Label("Welcome to the Buyer Home Page!");
        Button logoutButton = new Button("Logout");

        logoutButton.setOnAction(e -> AccountFormPage.showScene(new MainMenu().getScene()));

        layout.getChildren().addAll(welcomeLabel, logoutButton);

        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}