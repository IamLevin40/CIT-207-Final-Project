package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import main.UserService.BuyerService;
import main.UserService.SellerService;
import utils.NumberTextField;
import data.RegionCityData;
import utils.Global;

//
// Set up javafx scenes for account form page
//
public class AccountFormPage extends Application {
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

//
// Display main menu
// Be as seller or buyer, or just exit
//
class MainMenu {
    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER); // Set alignment of layout to center
        layout.getStyleClass().add("main-menu"); // Add .main-menu style to layout

        Button sellerButton = new Button("Seller"); // Button to refer to seller login frame
        Button buyerButton = new Button("Buyer"); // Button to refer to buyer login frame
        Button exitButton = new Button("Exit"); // Exit button

        // Set up database
        DatabaseHandler sellerDbHandler = new SellerDatabaseHandler();
        DatabaseHandler buyerDbHandler = new BuyerDatabaseHandler();
        UserService.SellerService sellerService = new UserService.SellerService(sellerDbHandler);
        UserService.BuyerService buyerService = new UserService.BuyerService(buyerDbHandler);

        // Add event listener for buttons
        sellerButton.setOnAction(e -> AccountFormPage.showScene(new SellerLoginFrame(sellerService).getScene()));
        buyerButton.setOnAction(e -> AccountFormPage.showScene(new BuyerLoginFrame(buyerService).getScene()));
        exitButton.setOnAction(e -> System.exit(0));

        // Add components to the layout
        layout.getChildren().addAll(sellerButton, buyerButton, exitButton);

        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}

//
// Login frame for seller
//
class SellerLoginFrame {
    private SellerService sellerService;

    public SellerLoginFrame(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER); // Set alignment of layout to center
        layout.getStyleClass().add("login-frame"); // Add .login-frame style to layout

        Label usernameLabel = new Label("Username:"); // Label with text "Username:"
        TextField usernameField = new TextField(); // Text field for username
        Label passwordLabel = new Label("Password:"); // Label with text "Password:"
        PasswordField passwordField = new PasswordField(); // Password field for password

        Button loginButton = new Button("Login"); // Button for handling login
        Button registerButton = new Button("Register"); // Button to refer to seller register frame
        Button backButton = new Button("Back"); // Button to refer to main menu frame
        Label errorLabel = new Label(); // Label for text displaying error message
        errorLabel.getStyleClass().add("error-label"); // Add .error-label style to errorLabel

        // Add event listener for buttons
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            sellerService.login(username, password, errorLabel,
                    new UserService.LoginCallback() {
                        @Override
                        public void onSuccess() {
                            AccountFormPage.showScene(new SellerHomePage().getScene());
                        }
                    });
        });
        registerButton.setOnAction(e -> AccountFormPage.showScene(new SellerRegisterFrame(sellerService).getScene()));
        backButton.setOnAction(e -> AccountFormPage.showScene(new MainMenu().getScene()));

        // Add components to the layout
        layout.getChildren().addAll(backButton, usernameLabel, usernameField, passwordLabel, passwordField,
                registerButton, loginButton, errorLabel);

        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}

//
// Login frame for buyer
//
class BuyerLoginFrame {
    private BuyerService buyerService;

    public BuyerLoginFrame(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("login-frame");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            buyerService.login(username, password, errorLabel,
                    new UserService.LoginCallback() {
                        @Override
                        public void onSuccess() {
                            AccountFormPage.showScene(new BuyerHomePage().getScene());
                        }
                    });
        });
        registerButton.setOnAction(e -> AccountFormPage.showScene(new BuyerRegisterFrame(buyerService).getScene()));
        backButton.setOnAction(e -> AccountFormPage.showScene(new MainMenu().getScene()));

        layout.getChildren().addAll(backButton, usernameLabel, usernameField, passwordLabel, passwordField,
                registerButton, loginButton, errorLabel);

        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}

//
// Register frame for seller
//
class SellerRegisterFrame {
    private SellerService sellerService;

    public SellerRegisterFrame(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER); // Set alignment of layout to center
        layout.getStyleClass().add("register-frame"); // Add .register-frame style to layout

        Label lastNameLabel = new Label("Last Name:"); // Label with text "Last Name:"
        TextField lastNameField = new TextField(); // Text field for last name
        Label firstNameLabel = new Label("First Name:"); // Label with text "First Name:"
        TextField firstNameField = new TextField(); // Text field for first name
        Label usernameLabel = new Label("Username:"); // Label with text "Username:"
        TextField usernameField = new TextField(); // Text field for username
        Label passwordLabel = new Label("Password:"); // Label with text "Password:"
        PasswordField passwordField = new PasswordField(); // Password field for password
        Label emailLabel = new Label("Email:"); // Label with text "Email:"
        TextField emailField = new TextField(); // Text field for email
        Label contactNumberLabel = new Label("Contact Number: +63"); // Label with text "Contact Number: +63"
        NumberTextField contactNumberField = new NumberTextField(10); // Number text field (custom extension of text
                                                                      // field) for contact number
        Label regionLabel = new Label("Region:"); // Label with text "Region:"
        ComboBox<String> regionComboBox = new ComboBox<>(); // Combo box for region
        Label cityLabel = new Label("City/Municipality:"); // Label with text "City/Municipality"
        ComboBox<String> cityComboBox = new ComboBox<>(); // Combo box for city/municipality
        Label barangayLabel = new Label("Barangay:"); // Label with text "Barangay"
        TextField barangayField = new TextField(); // Text field for barangay
        Label zipCodeLabel = new Label("Zip Code:"); // Label with text "Zip Code"
        NumberTextField zipCodeField = new NumberTextField(4); // Number text field (custom extension of text field) for
                                                               // zip code

        Label errorLabel = new Label(); // Label for text displaying error message
        errorLabel.getStyleClass().add("error-label"); // Add .error-label style to errorLabel

        Button registerButton = new Button("Register"); // Button for handling register
        Button backButton = new Button("Back"); // Button to refer to main menu frame

        // Populate combo boxes
        regionComboBox.getItems().add("Select Region");
        regionComboBox.getItems().addAll(RegionCityData.getRegions());
        cityComboBox.setDisable(true);

        regionComboBox.setOnAction(e -> {
            String selectedRegion = regionComboBox.getValue();
            cityComboBox.getItems().clear();
            if ("Select Region".equals(selectedRegion)) {
                cityComboBox.setDisable(true);
            } else {
                cityComboBox.setDisable(false);
                cityComboBox.getItems().addAll(RegionCityData.getCities(selectedRegion));
            }
        });

        // Add event listener for buttons
        registerButton.setOnAction(e -> {
            String lastName = lastNameField.getText();
            String firstName = firstNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String contactNumber = contactNumberField.getText();
            String region = regionComboBox.getValue();
            String city = cityComboBox.getValue();
            String barangay = barangayField.getText();
            String zipCode = zipCodeField.getText();

            sellerService.register(lastName, firstName, username, password, email, contactNumber, region, city,
                    barangay, zipCode, errorLabel, new UserService.RegisterCallback() {
                        @Override
                        public void onSuccess() {
                            AccountFormPage.showScene(new SellerLoginFrame(sellerService).getScene());
                        }
                    });
        });
        backButton.setOnAction(e -> AccountFormPage.showScene(new SellerLoginFrame(sellerService).getScene()));

        // Add components to the layout
        layout.getChildren().addAll(lastNameLabel, lastNameField, firstNameLabel, firstNameField, usernameLabel,
                usernameField, passwordLabel, passwordField, emailLabel, emailField, contactNumberLabel,
                contactNumberField, regionLabel, regionComboBox, cityLabel, cityComboBox, barangayLabel, barangayField,
                zipCodeLabel, zipCodeField, errorLabel, registerButton, backButton);

        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}

//
// Register frame for buyer
//
class BuyerRegisterFrame {
    private BuyerService buyerService;

    public BuyerRegisterFrame(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("register-frame");

        Label orgNameLabel = new Label("Organization Name:");
        TextField orgNameField = new TextField();
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label contactNumberLabel = new Label("Contact Number: +63");
        TextField contactNumberField = new TextField();
        contactNumberField.setPromptText("10 digits");
        Label regionLabel = new Label("Region:");
        ComboBox<String> regionComboBox = new ComboBox<>();
        regionComboBox.getItems().add("Select Region");
        regionComboBox.getItems().addAll(RegionCityData.getRegions());
        Label cityLabel = new Label("City/Municipality:");
        ComboBox<String> cityComboBox = new ComboBox<>();
        cityComboBox.setDisable(true);
        Label barangayLabel = new Label("Barangay:");
        TextField barangayField = new TextField();
        Label zipCodeLabel = new Label("Zip Code:");
        TextField zipCodeField = new TextField();
        zipCodeField.setPromptText("4 digits");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        regionComboBox.setOnAction(e -> {
            String selectedRegion = regionComboBox.getValue();
            cityComboBox.getItems().clear();
            if ("Select Region".equals(selectedRegion)) {
                cityComboBox.setDisable(true);
            } else {
                cityComboBox.setDisable(false);
                cityComboBox.getItems().addAll(RegionCityData.getCities(selectedRegion));
            }
        });

        registerButton.setOnAction(e -> {
            String orgName = orgNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String contactNumber = contactNumberField.getText();
            String region = regionComboBox.getValue();
            String city = cityComboBox.getValue();
            String barangay = barangayField.getText();
            String zipCode = zipCodeField.getText();

            buyerService.register(orgName, username, password, email, contactNumber, region, city, barangay, zipCode,
                    errorLabel, new UserService.RegisterCallback() {
                        @Override
                        public void onSuccess() {
                            AccountFormPage.showScene(new BuyerLoginFrame(buyerService).getScene());
                        }
                    });
        });

        backButton.setOnAction(e -> AccountFormPage.showScene(new BuyerLoginFrame(buyerService).getScene()));

        layout.getChildren().addAll(orgNameLabel, orgNameField, usernameLabel, usernameField, passwordLabel,
                passwordField, emailLabel, emailField, contactNumberLabel, contactNumberField, regionLabel,
                regionComboBox, cityLabel, cityComboBox, barangayLabel, barangayField, zipCodeLabel, zipCodeField,
                errorLabel, registerButton, backButton);

        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
    }
}
