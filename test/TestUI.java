package test;

import javax.swing.*;
import java.awt.*;

public class TestUI {
    public static void main(String[] args) {
        // Create main frame
        JFrame frame = new JFrame("Mobile View Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 720); // Size resembling a mobile screen

        // Create header
        JPanel header = new JPanel();
        header.setBackground(Color.LIGHT_GRAY);
        header.setPreferredSize(new Dimension(400, 50));
        JLabel headerLabel = new JLabel("App Header");
        header.add(headerLabel);

        // Create footer
        JPanel footer = new JPanel();
        footer.setBackground(Color.LIGHT_GRAY);
        footer.setPreferredSize(new Dimension(400, 50));
        JButton homeButton = new JButton("Home");
        JButton settingsButton = new JButton("Settings");
        footer.add(homeButton);
        footer.add(settingsButton);

        // Create the main content panel with a grid layout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 2 columns, with gaps
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add items to the content panel
        for (int i = 1; i <= 16; i++) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            itemPanel.setBackground(Color.WHITE);

            // Placeholder image
            JLabel imageLabel = new JLabel();
            imageLabel.setIcon(new ImageIcon(new ImageIcon("carrot.jpg")
                    .getImage().getScaledInstance(160, 128, Image.SCALE_SMOOTH)));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Item details
            JLabel nameLabel = new JLabel("Item " + i);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel priceLabel = new JLabel("Price: $10");
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel quantityLabel = new JLabel("Qty: 5");
            quantityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add components to the item panel
            itemPanel.add(imageLabel);
            itemPanel.add(Box.createVerticalStrut(5)); // Spacer
            itemPanel.add(nameLabel);
            itemPanel.add(priceLabel);
            itemPanel.add(quantityLabel);

            // Add item panel to content panel
            contentPanel.add(itemPanel);
        }

        // Wrap the content panel with JScrollPane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0)); // Hide vertical scrollbar
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0)); // Hide horizontal scrollbar

        // Enable scrolling via dragging (similar to touch apps)
        scrollPane.getViewport().addChangeListener(e -> scrollPane.repaint());

        // Set the main frame layout
        frame.setLayout(new BorderLayout());
        frame.add(header, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);

        // Show the frame
        frame.setVisible(true);
    }
}
