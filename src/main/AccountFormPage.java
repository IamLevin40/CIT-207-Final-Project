package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;

// for images:
import javafx.scene.image.ImageView;

import main.UserService.BuyerService;
import main.UserService.SellerService;
import utils.NumberTextField;

import data.RegionCityData;
import utils.Global;

// START SCREEN
class Start {
    public Scene getScene() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER); // Set alignment of layout to center
        layout.getStyleClass().add("main-menu"); // Add .main-menu style to layout

        // image
        ImageView iv = new ImageView(getClass().getResource("/images/signup.png").toExternalForm());
        iv.setFitHeight(291);
        iv.setFitWidth(300);

        // welcome to
        Label welcome = new Label("Welcome to");
        welcome.getStyleClass().add("text-welcome");

        // foodai
        Label foodai = new Label("Foodai");
        foodai.getStyleClass().add("text-foodai");

        Button loginEmail = new Button("Log in with email"); // Button to refer to seller login frame
        loginEmail.getStyleClass().add("login-email");

        Label or = new Label("OR");
        or.getStyleClass().add("text-or");

        // Create a container for sellerButton and buyerButton
        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER); // Set alignment of container to center
        buttonContainer.getStyleClass().add("button-container"); // Add .button-container style to container

        Button googleBtn = new Button();
        googleBtn.getStyleClass().add("google-btn");

        Button appleBtn = new Button();
        appleBtn.getStyleClass().add("apple-btn");

        Button twitterBtn = new Button();
        twitterBtn.getStyleClass().add("twitter-btn");

        Button fbBtn = new Button();
        fbBtn.getStyleClass().add("facebook-btn");

        buttonContainer.getChildren().addAll(googleBtn, appleBtn, twitterBtn, fbBtn); // Add buttons to container

        // ============================================= //

        // Add event listener for buttons
        loginEmail.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));
        googleBtn.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));
        appleBtn.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));
        twitterBtn.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));
        fbBtn.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));

        // Add components to the layout
        layout.getChildren().addAll(iv, welcome, foodai, loginEmail, or, buttonContainer);

        return new Scene(layout, Global.WIDTH, Global.HEIGHT);
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

        // welcome to
        Label welcome = new Label("Welcome to");
        welcome.getStyleClass().add("text-welcome");
        // foodai
        Label foodai = new Label("Foodai");
        foodai.getStyleClass().add("text-foodai_menu");

        Label menuLabel = new Label("Please select the role that fits you");
        menuLabel.getStyleClass().add("menu-label");

        Button sellerButton = new Button("Seller"); // Button to refer to seller login frame
        sellerButton.getStyleClass().add("seller-btn");

        Label or = new Label("OR");
        or.getStyleClass().add("text-or_menu");

        Button buyerButton = new Button("Buyer"); // Button to refer to buyer login frame
        buyerButton.getStyleClass().add("buyer-btn");

        Button exitButton = new Button("Exit"); // Exit button
        exitButton.getStyleClass().add("exit-btn");

        // Set up database
        UserService.SellerService sellerService = new UserService.SellerService();
        UserService.BuyerService buyerService = new UserService.BuyerService();

        // Add event listener for buttons
        sellerButton.setOnAction(e -> AppFrames.showScene(new SellerLoginFrame(sellerService).getScene()));
        buyerButton.setOnAction(e -> AppFrames.showScene(new BuyerLoginFrame(buyerService).getScene()));
        exitButton.setOnAction(e -> System.exit(0));

        // Add components to the layout
        layout.getChildren().addAll(welcome, foodai, menuLabel, sellerButton, or, buyerButton);

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
        layout.setAlignment(Pos.TOP_LEFT); // Set alignment of layout to center
        layout.getStyleClass().add("login-frame"); // Add .login-frame style to layout

        // welcome to
        Label welcome = new Label("Welcome back to");
        welcome.getStyleClass().add("text-welcome");
        // foodai
        Label foodai = new Label("Foodai");
        foodai.getStyleClass().add("text-foodai_menu");

        
        Button backButton = new Button("< Back"); // Button to refer to main menu frame
        backButton.getStyleClass().add("back-btn");

        VBox formContainer = new VBox();
        formContainer.getStyleClass().add("form-container");

            Label usernameLabel = new Label("Username:");
            usernameLabel.getStyleClass().add("labelsss");
            TextField usernameField = new TextField();
            usernameField.getStyleClass().add("text-field");

            Label separator1 = new Label("");
            separator1.getStyleClass().add("separator_form");

            Label passwordLabel = new Label("Password:");
            passwordLabel.getStyleClass().add("labelsss");
            PasswordField passwordField = new PasswordField();
            passwordField.getStyleClass().add("password-field");

            Label separator2 = new Label("");
            separator2.getStyleClass().add("separator_form");


            Button loginButton = new Button("Login"); // Button for handling login
            loginButton.getStyleClass().add("login-btn");


            HBox regisContainer = new HBox();

                Label regisLabel = new Label("Don't have an account yet?  ");
                regisLabel.getStyleClass().add("regis-label");
                Button registerButton = new Button("Register"); // Button to refer to seller register frame
                registerButton.getStyleClass().add("register-txt");

            regisContainer.getChildren().addAll(regisLabel, registerButton);
            regisContainer.getStyleClass().add("regis-container");

        formContainer.getChildren().addAll(
                usernameLabel, usernameField, separator1,
                         passwordLabel,passwordField, separator2,
                         loginButton, regisContainer);

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
                            AppFrames.showScene(new SellerHomePage(username).getScene());
                        }
                    });
        });
        registerButton.setOnAction(e -> AppFrames.showScene(new SellerRegisterFrame(sellerService).getScene()));
        backButton.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));

        // Add components to the layout
        layout.getChildren().addAll(
                welcome, foodai,
                backButton, formContainer, errorLabel);

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
        layout.setAlignment(Pos.TOP_LEFT); // Set alignment of layout to center
        layout.getStyleClass().add("login-frame"); // Add .login-frame style to layout

        // welcome to
        Label welcome = new Label("Welcome back to");
        welcome.getStyleClass().add("text-welcome");
        // foodai
        Label foodai = new Label("Foodai");
        foodai.getStyleClass().add("text-foodai_menu");

        Button backButton = new Button("< Back"); // Button to refer to main menu frame
        backButton.getStyleClass().add("back-btn");

        VBox formContainer = new VBox();
        formContainer.getStyleClass().add("form-container");

        Label usernameLabel = new Label("Username:");
        usernameLabel.getStyleClass().add("labelsss");
        TextField usernameField = new TextField();
        usernameField.getStyleClass().add("text-field");

        Label separator1 = new Label("");
        separator1.getStyleClass().add("separator_form");

        Label passwordLabel = new Label("Password:");
        passwordLabel.getStyleClass().add("labelsss");
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");

        Label separator2 = new Label("");
        separator2.getStyleClass().add("separator_form");

        Button loginButton = new Button("Login"); // Button for handling login
        loginButton.getStyleClass().add("login-btn");

        HBox regisContainer = new HBox();

        Label regisLabel = new Label("Don't have an account yet?  ");
        regisLabel.getStyleClass().add("regis-label");
        Button registerButton = new Button("Register"); // Button to refer to seller register frame
        registerButton.getStyleClass().add("register-txt");

        regisContainer.getChildren().addAll(regisLabel, registerButton);
        regisContainer.getStyleClass().add("regis-container");

        formContainer.getChildren().addAll(
                usernameLabel, usernameField, separator1,
                passwordLabel, passwordField, separator2,
                loginButton, regisContainer);

        Label errorLabel = new Label(); // Label for text displaying error message
        errorLabel.getStyleClass().add("error-label"); // Add .error-label style to errorLabel

        // Add event listener for buttons
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            buyerService.login(username, password, errorLabel,
                    new UserService.LoginCallback() {
                        @Override
                        public void onSuccess() {
                            AppFrames.showScene(new BuyerHomePage(username).getScene());
                        }
                    });
        });
        registerButton.setOnAction(e -> AppFrames.showScene(new BuyerRegisterFrame(buyerService).getScene()));
        backButton.setOnAction(e -> AppFrames.showScene(new MainMenu().getScene()));

        // Add components to the layout
        layout.getChildren().addAll(
                welcome, foodai,
                backButton, formContainer, errorLabel);

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
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.TOP_LEFT); // Set alignment of layout to center
        layout.getStyleClass().add("register-frame"); // Add .register-frame style to layout
        layout.setPadding(new Insets(20));

        Label welcome = new Label("Register your");
        welcome.getStyleClass().add("text-welcome");
        // foodai
        Label foodai = new Label("Foodai");
        foodai.getStyleClass().add("text-foodai_regis");

        Button backButton = new Button("< Back"); // Button to refer to main menu frame
        backButton.getStyleClass().add("back-btn");

        VBox registerContainer = new VBox();
        registerContainer.getStyleClass().add("register-container");

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
            NumberTextField contactNumberField = new NumberTextField(10); // Number text field (custom extension of text field) for contact number
                                                                        
       
            HBox regionZipContainer = new HBox(10);
            

                // Left side
                VBox regionBox = new VBox(5);
                Label regionLabel = new Label("Region:");
                ComboBox<String> regionComboBox = new ComboBox<>();
                regionBox.getChildren().addAll(regionLabel, regionComboBox);

                // Right side
                VBox zipBox = new VBox(5);
                Label zipCodeLabel = new Label("Zip Code:");
                NumberTextField zipCodeField = new NumberTextField(4);
                zipBox.getChildren().addAll(zipCodeLabel, zipCodeField);

                // Set preferred widths for consistent sizing
                regionComboBox.setPrefWidth(150);
                zipCodeField.setPrefWidth(150);

            regionZipContainer.getChildren().addAll(regionBox, zipBox);

            
            Label barangayLabel = new Label("Barangay:"); // Label with text "Barangay"
            TextField barangayField = new TextField();
            
            Label cityLabel = new Label("City/Municipality:"); // Label with text "City/Municipality"
            ComboBox<String> cityComboBox = new ComboBox<>(); // Combo box for city/municipality
        
        
            // Populate combo boxes
            regionComboBox.getItems().add("Select Region");
            regionComboBox.getItems().addAll(RegionCityData.getRegions());
            cityComboBox.setDisable(true);
        

            Button registerButton = new Button("Register"); // Button for handling register
            registerButton.getStyleClass().addAll("register-btn");

            Label errorLabel = new Label(); // Label for text displaying error message
            errorLabel.getStyleClass().add("error-label"); // Add .error-label style to errorLabel

            lastNameLabel.getStyleClass().add("labelsss");
            firstNameLabel.getStyleClass().add("labelsss");
            usernameLabel.getStyleClass().add("labelsss");
            passwordLabel.getStyleClass().add("labelsss");
            emailLabel.getStyleClass().add("labelsss");
            contactNumberLabel.getStyleClass().add("labelsss");
            regionLabel.getStyleClass().add("labelsss");
            zipCodeLabel.getStyleClass().add("labelsss");
            barangayLabel.getStyleClass().add("labelsss");
            cityLabel.getStyleClass().add("labelsss");

            lastNameField.getStyleClass().add("fieldsss");
            firstNameField.getStyleClass().add("fieldsss");
            usernameField.getStyleClass().add("fieldsss");
            passwordField.getStyleClass().add("fieldsss");
            emailField.getStyleClass().add("fieldsss");
            contactNumberField.getStyleClass().add("fieldsss");
            regionComboBox.getStyleClass().add("fieldsss");
            zipCodeField.getStyleClass().add("fieldsss");
            cityComboBox.getStyleClass().add("fieldsss");
            barangayField.getStyleClass().add("fieldsss");

        registerContainer.getChildren().addAll(
                lastNameLabel, lastNameField,
                firstNameLabel, firstNameField, 
                usernameLabel, usernameField, 
                passwordLabel, passwordField,
                emailLabel, emailField,
                contactNumberLabel, contactNumberField,
                regionZipContainer,
                barangayLabel, barangayField,
                cityLabel, cityComboBox,
                registerButton,
                errorLabel);
        

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
                            AppFrames.showScene(new SellerLoginFrame(sellerService).getScene());
                        }
                    });
        });
        backButton.setOnAction(e -> AppFrames.showScene(new SellerLoginFrame(sellerService).getScene()));



        // Add components to the layout
        layout.getChildren().addAll(welcome, foodai,backButton, registerContainer);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS); // Always show vertical scrollbar
        scrollPane.setPrefViewportHeight(Global.HEIGHT);
        
        // Set minimum sizes to ensure scrolling works
        layout.setMinHeight(Region.USE_PREF_SIZE);
        layout.setPrefWidth(Global.WIDTH - 20); // Account for scrollbar width
    
        return new Scene(scrollPane, Global.WIDTH, Global.HEIGHT);
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
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.TOP_LEFT);
        layout.getStyleClass().add("register-frame");
        layout.setPadding(new Insets(20));

        Label welcome = new Label("Register your");
        welcome.getStyleClass().add("text-welcome");
        Label foodai = new Label("Foodai");
        foodai.getStyleClass().add("text-foodai_regis");

        Button backButton = new Button("< Back");
        backButton.getStyleClass().add("back-btn");

        VBox registerContainer = new VBox();
        registerContainer.getStyleClass().add("register-container");

        // Form fields
        Label orgNameLabel = new Label("Organization Name:");
        TextField orgNameField = new TextField();
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label contactNumberLabel = new Label("Contact Number: +63");
        NumberTextField contactNumberField = new NumberTextField(10);

        // Region and Zip container
        HBox regionZipContainer = new HBox(10);
        
        VBox regionBox = new VBox(5);
        Label regionLabel = new Label("Region:");
        ComboBox<String> regionComboBox = new ComboBox<>();
        regionBox.getChildren().addAll(regionLabel, regionComboBox);

        VBox zipBox = new VBox(5);
        Label zipCodeLabel = new Label("Zip Code:");
        NumberTextField zipCodeField = new NumberTextField(4);
        zipBox.getChildren().addAll(zipCodeLabel, zipCodeField);

        regionComboBox.setPrefWidth(150);
        zipCodeField.setPrefWidth(150);

        regionZipContainer.getChildren().addAll(regionBox, zipBox);

        Label barangayLabel = new Label("Barangay:");
        TextField barangayField = new TextField();
        
        Label cityLabel = new Label("City/Municipality:");
        ComboBox<String> cityComboBox = new ComboBox<>();
        
        // Populate combo boxes
        regionComboBox.getItems().add("Select Region");
        regionComboBox.getItems().addAll(RegionCityData.getRegions());
        cityComboBox.setDisable(true);

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("register-btn");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");

        // Add styles to labels
        orgNameLabel.getStyleClass().add("labelsss");
        usernameLabel.getStyleClass().add("labelsss");
        passwordLabel.getStyleClass().add("labelsss");
        emailLabel.getStyleClass().add("labelsss");
        contactNumberLabel.getStyleClass().add("labelsss");
        regionLabel.getStyleClass().add("labelsss");
        zipCodeLabel.getStyleClass().add("labelsss");
        barangayLabel.getStyleClass().add("labelsss");
        cityLabel.getStyleClass().add("labelsss");

        // Add styles to fields
        orgNameField.getStyleClass().add("fieldsss");
        usernameField.getStyleClass().add("fieldsss");
        passwordField.getStyleClass().add("fieldsss");
        emailField.getStyleClass().add("fieldsss");
        contactNumberField.getStyleClass().add("fieldsss");
        regionComboBox.getStyleClass().add("fieldsss");
        zipCodeField.getStyleClass().add("fieldsss");
        cityComboBox.getStyleClass().add("fieldsss");
        barangayField.getStyleClass().add("fieldsss");

        // Add components to register container
        registerContainer.getChildren().addAll(
            orgNameLabel, orgNameField,
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            emailLabel, emailField,
            contactNumberLabel, contactNumberField,
            regionZipContainer,
            barangayLabel, barangayField,
            cityLabel, cityComboBox,
            registerButton,
            errorLabel
        );

        // Keep existing event handlers
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
                            AppFrames.showScene(new BuyerLoginFrame(buyerService).getScene());
                        }
                    });
        });

        backButton.setOnAction(e -> AppFrames.showScene(new BuyerLoginFrame(buyerService).getScene()));

        // Add components to main layout
        layout.getChildren().addAll(welcome, foodai, backButton, registerContainer);

        // Add ScrollPane
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefViewportHeight(Global.HEIGHT);
        
        layout.setMinHeight(Region.USE_PREF_SIZE);
        layout.setPrefWidth(Global.WIDTH - 20);
    
        return new Scene(scrollPane, Global.WIDTH, Global.HEIGHT);
    }
}