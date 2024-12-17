package main;

import java.util.Map;
import java.util.regex.*;

import javafx.scene.control.*;
import utils.Global;
import utils.Manipulate;

public interface UserService {
    interface LoginCallback {
        void onSuccess();
    }

    interface RegisterCallback {
        void onSuccess();
    }

    void login(String username, String password, Label errorLabel, LoginCallback callback);

    default void displayErrorMessage(String errorMsg, Label errorLabel) {
        errorLabel.setText(errorMsg);
    }

    default boolean validateAndSetError(String errorMsg, Label errorLabel) {
        if (!errorMsg.isEmpty()) {
            displayErrorMessage(errorMsg, errorLabel);
            return false;
        }
        return true;
    }

    default String validateHasCharacters(String field, String input) {
        if (input.isEmpty()) {
            return Manipulate.capitalize(field.replace('_', ' ')) + " is empty.";
        }
        return "";
    }

    default String validateHasSelectedOption(String field, String input) {
        if (input.isEmpty()) {
            return Manipulate.capitalize(field.replace('_', ' ')) + " has no selected option.";
        }
        return "";
    }

    default String validateExceedCharLength(String field, String input, Map<String, Integer> columnLengths) {
        if (input.length() > columnLengths.get(field)) {
            return Manipulate.capitalize(field.replace('_', ' ')) + " exceeds maximum length of "
                    + columnLengths.get(field)
                    + " characters.";
        }
        return "";
    }

    default String validateExactCharLength(String field, String input, Map<String, Integer> columnLengths) {
        if (input.length() != columnLengths.get(field)) {
            return Manipulate.capitalize(field.replace('_', ' ')) + " must contain exactly " + columnLengths.get(field)
                    + " characters.";
        }
        return "";
    }

    default String validateOnlyAlphanumericCharacters(String field, String input) {
        if (!input.matches("^[a-zA-Z0-9_]+$")) {
            return Manipulate.capitalize(field.replace('_', ' '))
                    + " can only contain letters, numbers, and underscores.";
        }
        return "";
    }

    default String validateOnlyDigitCharacters(String field, String input) {
        if (!input.matches("^[0-9]+$")) {
            return Manipulate.capitalize(field.replace('_', ' ')) + " must only contain digits.";
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
            Label errorLabel, LoginCallback callback) {

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
        private final SellerDatabaseHandler dbHandler;

        public SellerService() {
            this.dbHandler = new SellerDatabaseHandler();
        }

        public void register(String lastName, String firstName, String username, String password, String email,
                String contactNumber, String region, String cityOrMunicipality, String barangay, String zipCode,
                Label errorLabel, RegisterCallback callback) {
            Map<String, Integer> columnLengths = dbHandler.getColumnMaxLengths(Global.SELLER_TABLE_NAME);

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
            if (!validateAndSetError(validateUsernameTaken(username, dbHandler, Global.SELLER_TABLE_NAME), errorLabel))
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

            if (dbHandler.registerSeller(username, lastName, firstName, password, email, contactNumber, region,
                    cityOrMunicipality, barangay, zipCode)) {
                System.out.println("Seller registration successful.");
                callback.onSuccess();
            } else {
                displayErrorMessage("Seller registration failed.", errorLabel);
            }
        }

        @Override
        public void login(String username, String password, Label errorLabel, LoginCallback callback) {
            handleLogin(dbHandler, Global.SELLER_TABLE_NAME, username, password, errorLabel, callback);
        }
    }

    public static class BuyerService implements UserService {
        private final BuyerDatabaseHandler dbHandler;

        public BuyerService() {
            this.dbHandler = new BuyerDatabaseHandler();
        }

        public void register(String organizationName, String username, String password, String email,
                String contactNumber, String region, String cityOrMunicipality, String barangay, String zipCode,
                Label errorLabel, RegisterCallback callback) {
            Map<String, Integer> columnLengths = dbHandler.getColumnMaxLengths(Global.BUYER_TABLE_NAME);

            if (!validateAndSetError(validateHasCharacters("organization_name", organizationName), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("organization_name", organizationName, columnLengths),
                    errorLabel))
                return;
            if (!validateAndSetError(validateHasCharacters("username", username), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength("username", username, columnLengths), errorLabel))
                return;
            if (!validateAndSetError(validateUsernameTaken(username, dbHandler, Global.BUYER_TABLE_NAME), errorLabel))
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

            if (dbHandler.registerBuyer(username, organizationName, password, email, contactNumber, region,
                    cityOrMunicipality, barangay, zipCode)) {
                System.out.println("Buyer registration successful.");
                callback.onSuccess();
            } else {
                displayErrorMessage("Buyer registration failed.", errorLabel);
            }
        }

        @Override
        public void login(String username, String password, Label errorLabel, LoginCallback callback) {
            handleLogin(dbHandler, Global.BUYER_TABLE_NAME, username, password, errorLabel, callback);
        }
    }
}