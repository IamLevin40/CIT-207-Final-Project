package src;

import java.util.Map;

import javax.swing.JLabel;

public interface UserService {
    void login(String username, String password, JLabel errorLabel);

    default void displayErrorMessage(String errorMsg, JLabel errorLabel) {
        errorLabel.setText(errorMsg);
    }

    default boolean validateAndSetError(String errorMsg, JLabel errorLabel) {
        if (!errorMsg.isEmpty()) {
            displayErrorMessage(errorMsg, errorLabel);
            return false;
        }
        return true;
    }

    default String validateHasCharacters(String field, String input) {
        if (input.isEmpty()) {
            return Utils.capitalize(field.replace('_', ' ')) + " is empty.";
        }
        return "";
    }

    default String validateCharLength(String field, String input, Map<String, Integer> columnLengths) {
        if (input.length() > columnLengths.get(field)) {
            return Utils.capitalize(field.replace('_', ' ')) + " exceeds maximum length of "
                    + columnLengths.get(field);
        }
        return "";
    }

    default String validateUsernameTaken(String input, DatabaseHandler dbHandler, String userType) {
        if (dbHandler.isUsernameTaken(input, userType)) {
            return "Username already exists.";
        }
        return "";
    }

    default String validateUsernameCharacters(String input) {
        if (!input.matches("^[a-zA-Z0-9_]+$")) {
            return "Username can only contain letters, numbers, and underscores.";
        }
        return "";
    }

    default String validateContactCharacters(String input) {
        if (!input.matches("^[0-9+]+$")) {
            return "Contact number must only contain digits.";
        }
        return "";
    }

    @SuppressWarnings("resource")
    default void handleLogin(DatabaseHandler dbHandler, String userType, String username, String password,
            JLabel errorLabel) {

        if (!validateAndSetError(validateHasCharacters("username", username), errorLabel))
            return;
        if (!validateAndSetError(validateHasCharacters("password", password), errorLabel))
            return;

        if (dbHandler.verifyLogin(username, password, userType)) {
            System.out.println(userType + " login successful. Welcome, " + username + "!");
            errorLabel.setText("");
            // Add function for proceeding to landing page
        } else {
            displayErrorMessage("Invalid username or password. Please try again.", errorLabel);
        }
    }

    public static class SellerService implements UserService {
        private final SellerDatabaseHandler sellerDbHandler;
        private final DatabaseHandler dbHandler;
        private final String TABLE_NAME = "Seller";

        public SellerService(DatabaseHandler dbHandler) {
            this.dbHandler = dbHandler;
            this.sellerDbHandler = new SellerDatabaseHandler();
        }

        public void register(String username, String displayName, String password, String address,
                String contactNumber, JLabel errorLabel) {
            Map<String, Integer> columnLengths = dbHandler.getColumnMaxLengths(TABLE_NAME);

            if (!validateAndSetError(validateHasCharacters("username", username), errorLabel))
                return;
            if (!validateAndSetError(validateCharLength("username", username, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateUsernameTaken(username, dbHandler, TABLE_NAME), errorLabel))
                return;
            if (!validateAndSetError(validateUsernameCharacters(username), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("display_name", displayName), errorLabel))
                return;
            if (!validateAndSetError(validateCharLength("display_name", displayName, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("password", password), errorLabel))
                return;
            if (!validateAndSetError(validateCharLength("password", password, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("address", address), errorLabel))
                return;
            if (!validateAndSetError(validateCharLength("address", address, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("contact_number", contactNumber), errorLabel))
                return;
            if (!validateAndSetError(validateCharLength("contact_number", contactNumber, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateContactCharacters(contactNumber), errorLabel))
                return;

            if (sellerDbHandler.registerSeller(username, displayName, password, address, contactNumber)) {
                System.out.println("Seller registration successful.");
                // Add function for proceeding to login page
            } else {
                displayErrorMessage("Seller registration failed.", errorLabel);
            }
        }

        @Override
        public void login(String username, String password, JLabel errorLabel) {
            handleLogin(dbHandler, "Seller", username, password, errorLabel);
        }
    }

    public static class BuyerService implements UserService {
        private final BuyerDatabaseHandler buyerDbHandler;
        private final DatabaseHandler dbHandler;
        private final String TABLE_NAME = "Buyer";

        public BuyerService(DatabaseHandler dbHandler) {
            this.dbHandler = dbHandler;
            this.buyerDbHandler = new BuyerDatabaseHandler();
        }

        public void register(String username, String displayName, String password, String address,
                String contactNumber, JLabel errorLabel) {
            Map<String, Integer> columnLengths = dbHandler.getColumnMaxLengths(TABLE_NAME);

            if (!validateAndSetError(validateHasCharacters("username", username), errorLabel))
                return;
            if (!validateAndSetError(validateCharLength("username", username, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateUsernameTaken(username, dbHandler, TABLE_NAME), errorLabel))
                return;
            if (!validateAndSetError(validateUsernameCharacters(username), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("display_name", displayName), errorLabel))
                return;
            if (!validateAndSetError(validateCharLength("display_name", displayName, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("password", password), errorLabel))
                return;
            if (!validateAndSetError(validateCharLength("password", password, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("address", address), errorLabel))
                return;
            if (!validateAndSetError(validateCharLength("address", address, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("contact_number", contactNumber), errorLabel))
                return;
            if (!validateAndSetError(validateCharLength("contact_number", contactNumber, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateContactCharacters(contactNumber), errorLabel))
                return;

            if (buyerDbHandler.registerBuyer(username, displayName, password, address, contactNumber)) {
                System.out.println("Buyer registration successful.");
                // Add function for proceeding to login page
            } else {
                displayErrorMessage("Buyer registration failed.", errorLabel);
            }
        }

        @Override
        public void login(String username, String password, JLabel errorLabel) {
            handleLogin(dbHandler, "Buyer", username, password, errorLabel);
        }
    }
}