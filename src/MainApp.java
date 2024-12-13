package src;

import javax.swing.*;

import src.UserService.BuyerService;
import src.UserService.SellerService;

import java.awt.*;

public class MainApp {
    private static JFrame activeFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showFrame(new MainMenu()));
    }

    public static void showFrame(JFrame newFrame) {
        if (activeFrame != null) {
            activeFrame.dispose();
        }
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

        sellerButton.addActionListener(e -> MainApp.showFrame(new SellerLoginFrame("Seller", sellerService)));
        buyerButton.addActionListener(e -> MainApp.showFrame(new BuyerLoginFrame("Buyer", buyerService)));
        exitButton.addActionListener(e -> System.exit(0));

        add(sellerButton);
        add(buyerButton);
        add(exitButton);
    }
}

class SellerLoginFrame extends JFrame {
    public SellerLoginFrame(String userType, SellerService sellerService) {
        setTitle(userType + " Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            sellerService.login(username, password, errorLabel);
        });

        registerButton.addActionListener(e -> MainApp.showFrame(new SellerRegisterFrame(userType, sellerService)));

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);
        add(new JLabel());
        add(errorLabel);
    }
}

class BuyerLoginFrame extends JFrame {
    public BuyerLoginFrame(String userType, BuyerService buyerService) {
        setTitle(userType + " Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            buyerService.login(username, password, errorLabel);
        });

        registerButton.addActionListener(e -> MainApp.showFrame(new BuyerRegisterFrame(userType, buyerService)));

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);
        add(new JLabel());
        add(errorLabel);
    }
}

class SellerRegisterFrame extends JFrame {
    public SellerRegisterFrame(String userType, SellerService sellerService) {
        setTitle(userType + " Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel displayNameLabel = new JLabel("Display Name:");
        JTextField displayNameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        JLabel contactLabel = new JLabel("Contact Number:");
        JTextField contactField = new JTextField();

        JButton registerButton = new JButton("Register");

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String displayName = displayNameField.getText();
            String password = new String(passwordField.getPassword());
            String address = addressField.getText();
            String contact = contactField.getText();
            sellerService.register(username, displayName, password, address, contact, errorLabel);
        });

        add(usernameLabel);
        add(usernameField);
        add(displayNameLabel);
        add(displayNameField);
        add(passwordLabel);
        add(passwordField);
        add(addressLabel);
        add(addressField);
        add(contactLabel);
        add(contactField);
        add(new JLabel());
        add(registerButton);
        add(new JLabel());
        add(errorLabel);
    }
}

class BuyerRegisterFrame extends JFrame {
    public BuyerRegisterFrame(String userType, BuyerService buyerService) {
        setTitle(userType + " Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(7, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel displayNameLabel = new JLabel("Display Name:");
        JTextField displayNameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        JLabel contactLabel = new JLabel("Contact Number:");
        JTextField contactField = new JTextField();

        JButton registerButton = new JButton("Register");

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String displayName = displayNameField.getText();
            String password = new String(passwordField.getPassword());
            String address = addressField.getText();
            String contact = contactField.getText();
            buyerService.register(username, displayName, password, address, contact, errorLabel);
        });

        add(usernameLabel);
        add(usernameField);
        add(displayNameLabel);
        add(displayNameField);
        add(passwordLabel);
        add(passwordField);
        add(addressLabel);
        add(addressField);
        add(contactLabel);
        add(contactField);
        add(new JLabel());
        add(registerButton);
        add(new JLabel());
        add(errorLabel);
    }
}