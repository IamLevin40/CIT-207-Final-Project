package utils;

import java.text.DecimalFormat;

import javafx.scene.image.ImageView;

public class ProductDisplay {
    private final int id;
    private final String cropName;
    private final double quantity;
    private final double pricePerKg;
    private final int discount;
    private final double actualPrice;
    private final double discountedPrice;
    private final ImageView imageView;
    private final String sellerId;
    private DecimalFormat df = new DecimalFormat("#.##");

    public ProductDisplay(int id, String cropName, double quantity, double pricePerKg, int discount, double actualPrice,
            double discountedPrice, ImageView imageView, String sellerId) {
        this.id = id;
        this.cropName = cropName;
        this.quantity = Double.parseDouble(df.format(quantity));
        this.pricePerKg = Double.parseDouble(df.format(pricePerKg));
        this.discount = Integer.parseInt(df.format(discount));
        this.actualPrice = Double.parseDouble(df.format(actualPrice));
        this.discountedPrice = Double.parseDouble(df.format(discountedPrice));
        this.imageView = imageView;
        this.sellerId = sellerId;
    }

    public int getId() {
        return id;
    }

    public String getCropName() {
        return cropName;
    }

    public double getQuantity() {
        return Double.parseDouble(df.format(quantity));
    }

    public double getPricePerKg() {
        return Double.parseDouble(df.format(pricePerKg));
    }

    public int getDiscount() {
        return Integer.parseInt(df.format(discount));
    }

    public double getActualPrice() {
        return Double.parseDouble(df.format(actualPrice));
    }

    public double getDiscountedPrice() {
        return Double.parseDouble(df.format(discountedPrice));
    }

    public ImageView getImage() {
        return imageView;
    }

    public ImageView getImageView(int width, int height) {
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    public String getSellerId() {
        return sellerId;
    }
}