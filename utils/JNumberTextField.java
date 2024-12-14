package utils;

import javax.swing.*;
import javax.swing.text.*;

public class JNumberTextField extends JTextField {
    private int limit;

    public JNumberTextField(int limit) {
        this.limit = limit;
        ((AbstractDocument) this.getDocument()).setDocumentFilter(new NumberDocumentFilter());
    }

    private class NumberDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (isValidInput(fb, string)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (isValidInput(fb, text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        private boolean isValidInput(FilterBypass fb, String text) throws BadLocationException {
            String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
            return newText.matches("\\d*") && newText.length() <= limit;
        }
    }
}