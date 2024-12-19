package main;

import java.util.List;
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

    void register(Map<String, String> userDetails, Label errorLabel, RegisterCallback callback);

    DatabaseHandler getDatabaseHandler();

    String getUserTable();

    default void handleRegistration(Map<String, String> userDetails, Label errorLabel, RegisterCallback callback) {
        Map<String, Integer> columnLengths = getDatabaseHandler().getColumnMaxLengths(getUserTable());

        List<String> validationOrder = List.of(
                "last_name", "first_name", "username", "password", "email",
                "contact_number", "region", "city_or_municipality", "barangay", "zip_code");

        for (String field : validationOrder) {
            String value = userDetails.get(field);

            if (!validateAndSetError(validateHasCharacters(field, value), errorLabel))
                return;
            if (!validateAndSetError(validateExceedCharLength(field, value, columnLengths), errorLabel))
                return;

            if (field.equals("username")) {
                if (!validateAndSetError(validateUsernameTaken(value, getDatabaseHandler(), getUserTable()),
                        errorLabel))
                    return;
                if (!validateAndSetError(validateOnlyAlphanumericCharacters(field, value), errorLabel))
                    return;
            }

            if (field.equals("email") && !validateAndSetError(validateEmail(value), errorLabel))
                return;
            if (field.equals("contact_number")) {
                if (!validateAndSetError(validateOnlyDigitCharacters(field, value), errorLabel))
                    return;
                if (!validateAndSetError(validateExactCharLength(field, value, columnLengths), errorLabel))
                    return;
            }
            if (field.equals("region") && !validateAndSetError(validateHasSelectedOption(field, value), errorLabel))
                return;
            if (field.equals("city_or_municipality")
                    && !validateAndSetError(validateHasSelectedOption(field, value), errorLabel))
                return;
            if (field.equals("zip_code")) {
                if (!validateAndSetError(validateOnlyDigitCharacters(field, value), errorLabel))
                    return;
                if (!validateAndSetError(validateExactCharLength(field, value, columnLengths), errorLabel))
                    return;
            }
        }

        if (getDatabaseHandler().registerUser(userDetails)) {
            System.out.println(getUserTable() + " registration successful.");
            callback.onSuccess();
        } else {
            displayErrorMessage(getUserTable() + " registration failed.", errorLabel);
        }
    }

    default void handleLogin(String username, String password, Label errorLabel, LoginCallback callback) {
        if (!validateAndSetError(validateHasCharacters("username", username), errorLabel))
            return;
        if (!validateAndSetError(validateHasCharacters("password", password), errorLabel))
            return;

        if (getDatabaseHandler().verifyLogin(username, password, getUserTable())) {
            System.out.println(getUserTable() + " login successful. Welcome, " + username + "!");
            errorLabel.setText("");
            callback.onSuccess();
        } else {
            displayErrorMessage("Invalid username or password. Please try again.", errorLabel);
        }
    }

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

    class SellerService implements UserService {
        private final SellerDatabaseHandler dbHandler;

        public SellerService() {
            this.dbHandler = new SellerDatabaseHandler();
        }

        @Override
        public void login(String username, String password, Label errorLabel, LoginCallback callback) {
            handleLogin(username, password, errorLabel, callback);
        }

        @Override
        public void register(Map<String, String> userDetails, Label errorLabel, RegisterCallback callback) {
            handleRegistration(userDetails, errorLabel, callback);
        }

        @Override
        public DatabaseHandler getDatabaseHandler() {
            return dbHandler;
        }

        @Override
        public String getUserTable() {
            return Global.SELLER_TABLE_NAME;
        }

        public void register(
                String lastName, String firstName, String username, String password, String email,
                String contactNumber, String region, String city, String barangay, String zipCode,
                Label errorLabel, RegisterCallback callback) {

            lastName = lastName != null ? lastName : "";
            firstName = firstName != null ? firstName : "";
            username = username != null ? username : "";
            password = password != null ? password : "";
            email = email != null ? email : "";
            contactNumber = contactNumber != null ? contactNumber : "";
            region = region != null ? region : "";
            city = city != null ? city : "";
            barangay = barangay != null ? barangay : "";
            zipCode = zipCode != null ? zipCode : "";

            Map<String, String> userDetails = Map.of(
                    "last_name", lastName,
                    "first_name", firstName,
                    "username", username,
                    "password", password,
                    "email", email,
                    "contact_number", contactNumber,
                    "region", region,
                    "city_or_municipality", city,
                    "barangay", barangay,
                    "zip_code", zipCode);

            register(userDetails, errorLabel, callback);
        }
    }

    class BuyerService implements UserService {
        private final BuyerDatabaseHandler dbHandler;

        public BuyerService() {
            this.dbHandler = new BuyerDatabaseHandler();
        }

        @Override
        public void login(String username, String password, Label errorLabel, LoginCallback callback) {
            handleLogin(username, password, errorLabel, callback);
        }

        @Override
        public void register(Map<String, String> userDetails, Label errorLabel, RegisterCallback callback) {
            handleRegistration(userDetails, errorLabel, callback);
        }

        @Override
        public DatabaseHandler getDatabaseHandler() {
            return dbHandler;
        }

        @Override
        public String getUserTable() {
            return Global.BUYER_TABLE_NAME;
        }

        public void register(
                String orgName, String username, String password, String email,
                String contactNumber, String region, String city, String barangay, String zipCode,
                Label errorLabel, RegisterCallback callback) {

            orgName = orgName != null ? orgName : "";
            username = username != null ? username : "";
            password = password != null ? password : "";
            email = email != null ? email : "";
            contactNumber = contactNumber != null ? contactNumber : "";
            region = region != null ? region : "";
            city = city != null ? city : "";
            barangay = barangay != null ? barangay : "";
            zipCode = zipCode != null ? zipCode : "";

            Map<String, String> userDetails = Map.of(
                    "organization_name", orgName,
                    "username", username,
                    "password", password,
                    "email", email,
                    "contact_number", contactNumber,
                    "region", region,
                    "city_or_municipality", city,
                    "barangay", barangay,
                    "zip_code", zipCode);

            register(userDetails, errorLabel, callback);
        }
    }
}