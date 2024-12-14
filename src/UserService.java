package src;

import java.util.Map;
import java.util.regex.*;

import javax.swing.JLabel;

import utils.Utils;

public interface UserService {
    interface LoginCallback {
        void onSuccess();
    }

    interface RegisterCallback {
        void onSuccess();
    }

    void login(String username, String password, JLabel errorLabel, LoginCallback callback);

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

    default String validateHasSelectedOption(String field, String input) {
        if (input.isEmpty()) {
            return Utils.capitalize(field.replace('_', ' ')) + " has no selected option.";
        }
        return "";
    }

    default String validateExceedCharLength(String field, String input, Map<String, Integer> columnLengths) {
        if (input.length() > columnLengths.get(field)) {
            return Utils.capitalize(field.replace('_', ' ')) + " exceeds maximum length of " + columnLengths.get(field)
                    + " characters.";
        }
        return "";
    }

    default String validateExactCharLength(String field, String input, Map<String, Integer> columnLengths) {
        if (input.length() != columnLengths.get(field)) {
            return Utils.capitalize(field.replace('_', ' ')) + " must contain exactly " + columnLengths.get(field)
                    + " characters.";
        }
        return "";
    }

    default String validateOnlyAlphanumericCharacters(String field, String input) {
        if (!input.matches("^[a-zA-Z0-9_]+$")) {
            return Utils.capitalize(field.replace('_', ' ')) + " can only contain letters, numbers, and underscores.";
        }
        return "";
    }

    default String validateOnlyDigitCharacters(String field, String input) {
        if (!input.matches("^[0-9]+$")) {
            return Utils.capitalize(field.replace('_', ' ')) + " must only contain digits.";
        }
        return "";
    }

    default String validateEmail(String input) {
        final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            return "Not a valid email";
        }
        return "";
    }

    default String validateUsernameTaken(String input, DatabaseHandler dbHandler, String userType) {
        if (dbHandler.isUsernameTaken(input, userType)) {
            return "Username already exists.";
        }
        return "";
    }

    @SuppressWarnings("resource")
    default void handleLogin(DatabaseHandler dbHandler, String userType, String username, String password,
            JLabel errorLabel, LoginCallback callback) {

        if (!validateAndSetError(validateHasCharacters("username", username), errorLabel))
            return;
        if (!validateAndSetError(validateHasCharacters("password", password), errorLabel))
            return;

        if (dbHandler.verifyLogin(username, password, userType)) {
            System.out.println(userType + " login successful. Welcome, " + username + "!");
            errorLabel.setText("");
            callback.onSuccess();
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

        public void register(String lastName, String firstName, String username, String password, String email,
                String contactNumber, String region, String cityOrMunicipality, String barangay, String zipCode,
                JLabel errorLabel, RegisterCallback callback) {
            Map<String, Integer> columnLengths = dbHandler.getColumnMaxLengths(TABLE_NAME);

            if (!validateAndSetError(validateHasCharacters("last_name", lastName), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("last_name", lastName, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("first_name", firstName), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("first_name", firstName, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("username", username), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("username", username, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateUsernameTaken(username, dbHandler, TABLE_NAME), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyAlphanumericCharacters("username", username), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("password", password), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("password", password, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("email", email), errorLabel))
                return;
            if (!validateAndSetError(validateEmail(email), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("email", email, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("contact_number", contactNumber), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyDigitCharacters("contact_number", contactNumber), errorLabel))
                return;
            if (!validateAndSetError(validateExactCharLength("contact_number", contactNumber, columnLengths),
                    errorLabel))
                return;
            if (!validateAndSetError(validateHasSelectedOption("region", region), errorLabel))
                return;
            if (!validateAndSetError(validateHasSelectedOption("city_or_municipality", cityOrMunicipality), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("barangay", barangay), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("barangay", barangay, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("zip_code", zipCode), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyDigitCharacters("zip_code", zipCode), errorLabel))
                return;
            if (!validateAndSetError(validateExactCharLength("zip_code", zipCode, columnLengths),
                    errorLabel))
                return;

            if (sellerDbHandler.registerSeller(username, lastName, firstName, password, email, contactNumber, region,
                    cityOrMunicipality, barangay, zipCode)) {
                System.out.println("Seller registration successful.");
                callback.onSuccess();
            } else {
                displayErrorMessage("Seller registration failed.", errorLabel);
            }
        }

        @Override
        public void login(String username, String password, JLabel errorLabel, LoginCallback callback) {
            handleLogin(dbHandler, "Seller", username, password, errorLabel, callback);
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

        public void register(String organizationName, String username, String password, String email,
                String contactNumber, String region, String cityOrMunicipality, String barangay, String zipCode,
                JLabel errorLabel, RegisterCallback callback) {
            Map<String, Integer> columnLengths = dbHandler.getColumnMaxLengths(TABLE_NAME);

            if (!validateAndSetError(validateHasCharacters("organization_name", organizationName), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("organization_name", organizationName, columnLengths),
                    errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("username", username), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("username", username, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateUsernameTaken(username, dbHandler, TABLE_NAME), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyAlphanumericCharacters("username", username), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("password", password), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("password", password, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("email", email), errorLabel))
                return;
            if (!validateAndSetError(validateEmail(email), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("email", email, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("contact_number", contactNumber), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyDigitCharacters("contact_number", contactNumber), errorLabel))
                return;
            if (!validateAndSetError(validateExactCharLength("contact_number", contactNumber, columnLengths),
                    errorLabel))
                return;
            if (!validateAndSetError(validateHasSelectedOption("region", region), errorLabel))
                return;
            if (!validateAndSetError(validateHasSelectedOption("city_or_municipality", cityOrMunicipality), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("barangay", barangay), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("barangay", barangay, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("zip_code", zipCode), errorLabel))
                return;
            if (!validateAndSetError(validateOnlyDigitCharacters("zip_code", zipCode), errorLabel))
                return;
            if (!validateAndSetError(validateExactCharLength("zip_code", zipCode, columnLengths),
                    errorLabel))
                return;

            if (buyerDbHandler.registerBuyer(username, organizationName, password, email, contactNumber, region,
                    cityOrMunicipality, barangay, zipCode)) {
                System.out.println("Buyer registration successful.");
                callback.onSuccess();
            } else {
                displayErrorMessage("Buyer registration failed.", errorLabel);
            }
        }

        @Override
        public void login(String username, String password, JLabel errorLabel, LoginCallback callback) {
            handleLogin(dbHandler, "Buyer", username, password, errorLabel, callback);
        }
    }
}