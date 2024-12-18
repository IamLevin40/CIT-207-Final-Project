package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

//
// Set up javafx scenes for account form page
//
public class AppFrames extends Application {
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