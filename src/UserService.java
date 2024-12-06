package src;

import java.util.Map;
import java.util.Scanner;

public interface UserService {
    void register();

    void login();

    default boolean validateCharLength(String field, String input, Map<String, Integer> columnLengths,
            DatabaseHandler dbHandler) {
        if (input.length() > columnLengths.get(field)) {
            System.out.println("Error: " + field + " exceeds maximum length of " + columnLengths.get(field));
            return false;
        }
        return true;
    }

    default boolean validateUsernameTaken(String input, DatabaseHandler dbHandler, String userType) {
        if (dbHandler.isUsernameTaken(input, userType)) {
            System.out.println("Error: Username already exists.");
            return false;
        }
        return true;
    }

    default boolean validateUsernameCharacters(String input) {
        if (!input.matches("^[a-zA-Z0-9_]+$")) {
            System.out.println("Error: Username can only contain letters, numbers, and underscores.");
            return false;
        }
        return true;
    }

    default boolean validateContactCharacters(String input) {
        if (!input.matches("^[0-9+]+$")) {
            System.out.println("Error: Contact number must only contain digits.");
            return false;
        }
        return true;
    }

    @SuppressWarnings("resource")
    default void handleLogin(DatabaseHandler dbHandler, String userType) {
        System.out.println("\n--- Log in as " + userType + " ---");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (dbHandler.verifyLogin(username, password, userType)) {
            System.out.println(userType + " login successful. Welcome, " + username + "!");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }
}

class SellerService implements UserService {
    private final SellerDatabaseHandler sellerDbHandler;
    private final DatabaseHandler dbHandler;
    private final String TABLE_NAME = "Seller";

    public SellerService(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.sellerDbHandler = new SellerDatabaseHandler();
    }

    @SuppressWarnings("resource")
    @Override
    public void register() {
        System.out.println("\n--- Register as Seller ---");
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> columnLengths = dbHandler.getColumnMaxLengths(TABLE_NAME);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (!validateCharLength("username", username, columnLengths, dbHandler))
            return;
        if (!validateUsernameTaken(username, dbHandler, TABLE_NAME))
            return;
        if (!validateUsernameCharacters(username))
            return;

        System.out.print("Enter display name: ");
        String displayName = scanner.nextLine();
        if (!validateCharLength("display_name", displayName, columnLengths, dbHandler))
            return;

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (!validateCharLength("password", password, columnLengths, dbHandler))
            return;

        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        if (!validateCharLength("address", address, columnLengths, dbHandler))
            return;

        System.out.print("Enter contact number: ");
        String contactNumber = scanner.nextLine();
        if (!validateCharLength("contact_number", contactNumber, columnLengths, dbHandler))
            return;
        if (!validateContactCharacters(contactNumber))
            return;

        if (sellerDbHandler.registerSeller(username, displayName, password, address, contactNumber)) {
            System.out.println("Seller registration successful.");
        } else {
            System.out.println("Seller registration failed.");
        }
    }

    @Override
    public void login() {
        handleLogin(dbHandler, "Seller");
    }
}

class BuyerService implements UserService {
    private final BuyerDatabaseHandler buyerDbHandler;
    private final DatabaseHandler dbHandler;
    private final String TABLE_NAME = "Buyer";

    public BuyerService(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.buyerDbHandler = new BuyerDatabaseHandler();
    }

    @SuppressWarnings("resource")
    @Override
    public void register() {
        System.out.println("\n--- Register as Buyer ---");
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> columnLengths = dbHandler.getColumnMaxLengths(TABLE_NAME);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (!validateCharLength("username", username, columnLengths, dbHandler))
            return;

        System.out.print("Enter display name: ");
        String displayName = scanner.nextLine();
        if (!validateCharLength("display_name", displayName, columnLengths, dbHandler))
            return;

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (!validateCharLength("password", password, columnLengths, dbHandler))
            return;

        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        if (!validateCharLength("address", address, columnLengths, dbHandler))
            return;

        System.out.print("Enter contact number: ");
        String contactNumber = scanner.nextLine();
        if (!validateCharLength("contact_number", contactNumber, columnLengths, dbHandler))
            return;

        if (buyerDbHandler.registerBuyer(username, displayName, password, address, contactNumber)) {
            System.out.println("Buyer registration successful.");
        } else {
            System.out.println("Buyer registration failed.");
        }
    }

    @Override
    public void login() {
        handleLogin(dbHandler, "Buyer");
    }
}
