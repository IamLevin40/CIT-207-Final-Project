package src;

import javax.swing.*;
import java.awt.*;

import data.RegionCityData;
import src.UserService.BuyerService;
import src.UserService.SellerService;
import utils.JNumberTextField;

public class MainApp {
    private static JFrame activeFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showFrame(new MainMenu()));
    }

    public static void showFrame(JFrame newFrame) {
        if (activeFrame != null) {
            activeFrame.dispose();
        }
        newFrame.setResizable(false);
        newFrame.setLocationRelativeTo(null);
        activeFrame = newFrame;
        activeFrame.setVisible(true);
    }
}

class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Main Menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JButton sellerButton = new JButton("Seller");
        JButton buyerButton = new JButton("Buyer");
        JButton exitButton = new JButton("Exit");

        DatabaseHandler sellerDbHandler = new SellerDatabaseHandler();
        DatabaseHandler buyerDbHandler = new BuyerDatabaseHandler();
        UserService.SellerService sellerService = new UserService.SellerService(sellerDbHandler);
        UserService.BuyerService buyerService = new UserService.BuyerService(buyerDbHandler);

        sellerButton.addActionListener(e -> MainApp.showFrame(new SellerLoginFrame(sellerService)));
        buyerButton.addActionListener(e -> MainApp.showFrame(new BuyerLoginFrame(buyerService)));
        exitButton.addActionListener(e -> System.exit(0));

        add(sellerButton);
        add(buyerButton);
        add(exitButton);
    }
}

class SellerLoginFrame extends JFrame {
    public SellerLoginFrame(SellerService sellerService) {
        setTitle("Seller Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            sellerService.login(username, password, errorLabel, new UserService.LoginCallback() {
                @Override
                public void onSuccess() {
                    MainApp.showFrame(new SellerHomePage());
                }
            });
        });

        registerButton.addActionListener(e -> MainApp.showFrame(new SellerRegisterFrame(sellerService)));
        backButton.addActionListener(e -> MainApp.showFrame(new MainMenu()));

        add(backButton);
        add(new JLabel());
        add(new JLabel());
        add(new JLabel());
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
        add(loginButton);
        add(new JLabel());
        add(errorLabel);
    }
}

class BuyerLoginFrame extends JFrame {
    public BuyerLoginFrame(BuyerService buyerService) {
        setTitle("Buyer Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            buyerService.login(username, password, errorLabel, new UserService.LoginCallback() {
                @Override
                public void onSuccess() {
                    MainApp.showFrame(new BuyerHomePage());
                }
            });
        });

        registerButton.addActionListener(e -> MainApp.showFrame(new BuyerRegisterFrame(buyerService)));
        backButton.addActionListener(e -> MainApp.showFrame(new MainMenu()));

        add(backButton);
        add(new JLabel());
        add(new JLabel());
        add(new JLabel());
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
        add(loginButton);
        add(new JLabel());
        add(errorLabel);
    }
}

class SellerRegisterFrame extends JFrame {
    public SellerRegisterFrame(SellerService sellerService) {
        setTitle("Seller Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(12, 2));

        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel contactNumberLabel = new JLabel("Contact Number: +63");
        JNumberTextField contactNumberField = new JNumberTextField(10);
        JLabel regionLabel = new JLabel("Region:");
        JComboBox<String> regionComboBox = new JComboBox<>();
        regionComboBox.addItem("Select Region");
        RegionCityData.getRegions().forEach(regionComboBox::addItem);
        JLabel cityOrMunicipalityLabel = new JLabel("City/Municipality:");
        JComboBox<String> cityOrMunicipalityComboBox = new JComboBox<>();
        JLabel barangayLabel = new JLabel("Barangay");
        JTextField barangayField = new JTextField();
        JLabel zipCodeLabel = new JLabel("Zip Code:");
        JNumberTextField zipCodeField = new JNumberTextField(4);

        cityOrMunicipalityComboBox.setEnabled(false);
        regionComboBox.addActionListener(e -> {
            String selectedRegion = (String) regionComboBox.getSelectedItem();
            if ("Select Region".equals(selectedRegion)) {
                cityOrMunicipalityComboBox.setEnabled(false);
                cityOrMunicipalityComboBox.removeAllItems();
            } else {
                cityOrMunicipalityComboBox.setEnabled(true);
                cityOrMunicipalityComboBox.removeAllItems();
                for (String city : RegionCityData.getCities(selectedRegion)) {
                    cityOrMunicipalityComboBox.addItem(city);
                }
            }
        });

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        registerButton.addActionListener(e -> {
            String lastName = lastNameField.getText();
            String firstName = firstNameField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String contactNumber = contactNumberField.getText();
            String region = (String) regionComboBox.getSelectedItem();
            String cityOrMunicipality = (String) cityOrMunicipalityComboBox.getSelectedItem();
            String barangay = barangayField.getText();
            String zipCode = zipCodeField.getText();
            sellerService.register(lastName, firstName, username, password, email, contactNumber, region,
                    cityOrMunicipality, barangay, zipCode, errorLabel,
                    new UserService.RegisterCallback() {
                        @Override
                        public void onSuccess() {
                            MainApp.showFrame(new SellerLoginFrame(sellerService));
                        }
                    });
        });

        backButton.addActionListener(e -> MainApp.showFrame(new SellerLoginFrame(sellerService)));

        add(lastNameLabel);
        add(lastNameField);
        add(firstNameLabel);
        add(firstNameField);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(emailLabel);
        add(emailField);
        add(contactNumberLabel);
        add(contactNumberField);
        add(regionLabel);
        add(regionComboBox);
        add(cityOrMunicipalityLabel);
        add(cityOrMunicipalityComboBox);
        add(barangayLabel);
        add(barangayField);
        add(zipCodeLabel);
        add(zipCodeField);
        add(backButton);
        add(registerButton);
        add(new JLabel());
        add(errorLabel);
    }
}

class BuyerRegisterFrame extends JFrame {
    public BuyerRegisterFrame(BuyerService buyerService) {
        setTitle("Buyer Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(11, 2));

        JLabel organizationNameLabel = new JLabel("Organization Name:");
        JTextField organizationNameField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel contactNumberLabel = new JLabel("Contact Number: +63");
        JNumberTextField contactNumberField = new JNumberTextField(10);
        JLabel regionLabel = new JLabel("Region:");
        JComboBox<String> regionComboBox = new JComboBox<>();
        regionComboBox.addItem("Select Region");
        RegionCityData.getRegions().forEach(regionComboBox::addItem);
        JLabel cityOrMunicipalityLabel = new JLabel("City/Municipality:");
        JComboBox<String> cityOrMunicipalityComboBox = new JComboBox<>();
        JLabel barangayLabel = new JLabel("Barangay");
        JTextField barangayField = new JTextField();
        JLabel zipCodeLabel = new JLabel("Zip Code:");
        JNumberTextField zipCodeField = new JNumberTextField(4);

        cityOrMunicipalityComboBox.setEnabled(false);
        regionComboBox.addActionListener(e -> {
            String selectedRegion = (String) regionComboBox.getSelectedItem();
            if ("Select Region".equals(selectedRegion)) {
                cityOrMunicipalityComboBox.setEnabled(false);
                cityOrMunicipalityComboBox.removeAllItems();
            } else {
                cityOrMunicipalityComboBox.setEnabled(true);
                cityOrMunicipalityComboBox.removeAllItems();
                for (String city : RegionCityData.getCities(selectedRegion)) {
                    cityOrMunicipalityComboBox.addItem(city);
                }
            }
        });

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        registerButton.addActionListener(e -> {
            String organizationName = organizationNameField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String contactNumber = contactNumberField.getText();
            String region = (String) regionComboBox.getSelectedItem();
            String cityOrMunicipality = (String) cityOrMunicipalityComboBox.getSelectedItem();
            String barangay = barangayField.getText();
            String zipCode = zipCodeField.getText();
            buyerService.register(organizationName, username, password, email, contactNumber, region,
                    cityOrMunicipality, barangay, zipCode, errorLabel,
                    new UserService.RegisterCallback() {
                        @Override
                        public void onSuccess() {
                            MainApp.showFrame(new BuyerLoginFrame(buyerService));
                        }
                    });
        });

        backButton.addActionListener(e -> MainApp.showFrame(new BuyerLoginFrame(buyerService)));

        add(organizationNameLabel);
        add(organizationNameField);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(emailLabel);
        add(emailField);
        add(contactNumberLabel);
        add(contactNumberField);
        add(regionLabel);
        add(regionComboBox);
        add(cityOrMunicipalityLabel);
        add(cityOrMunicipalityComboBox);
        add(barangayLabel);
        add(barangayField);
        add(zipCodeLabel);
        add(zipCodeField);
        add(backButton);
        add(registerButton);
        add(new JLabel());
        add(errorLabel);
    }
}

class SellerHomePage extends JFrame {
    public SellerHomePage() {
        setTitle("Seller Home Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the Seller Home Page!");
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> MainApp.showFrame(new MainMenu()));

        add(welcomeLabel);
        add(logoutButton);
    }
}

class BuyerHomePage extends JFrame {
    public BuyerHomePage() {
        setTitle("Buyer Home Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the Buyer Home Page!");
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> MainApp.showFrame(new MainMenu()));

        add(welcomeLabel);
        add(logoutButton);
    }
}
