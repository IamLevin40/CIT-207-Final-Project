package utils;

import java.text.DecimalFormat;

public class ProductInfo {
    private double quantity;
    private final double pricePerKg;
    private final double discount;
    private DecimalFormat df = new DecimalFormat("#.##");

    public ProductInfo(double quantity, double pricePerKg, double discount) {
        this.quantity = Double.parseDouble(df.format(quantity));
        this.pricePerKg = Double.parseDouble(df.format(pricePerKg));
        this.discount = Double.parseDouble(df.format(discount));
    }

    public double getQuantity() {
        return Double.parseDouble(df.format(quantity));
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPricePerKg() {
        return Double.parseDouble(df.format(pricePerKg));
    }

    public double getDiscount() {
        return Double.parseDouble(df.format(discount));
    }

    public double computePrice() {
        double price;
        if (discount > 0) {
            price = (pricePerKg * (0.01 * (100 - discount))) * quantity;
        } else {
            price = pricePerKg * quantity;
        }
        return Double.parseDouble(df.format(price));
    }
}