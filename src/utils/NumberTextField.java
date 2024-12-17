package utils;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {
    private final int limit;

    public NumberTextField(int limit) {
        this.limit = limit;
        // Add a listener to filter input
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidInput(newValue)) {
                this.setText(oldValue);
            }
        });
    }

    private boolean isValidInput(String text) {
        // Allow digits and at most one decimal point
        return text.matches("\\d*\\.?\\d*") && text.length() <= limit;
    }
}
