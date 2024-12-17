package utils;

import javafx.scene.image.ImageView;

public class ProductDisplay {
    private final int id;
    private final String cropName;
    private final double quantity;
    private final double actualPrice;
    private final double discountedPrice;
    private final ImageView imageView;
    private final String sellerId;

    public ProductDisplay(int id, String cropName, double quantity, double actualPrice, double discountedPrice,
            ImageView imageView,
            String sellerId) {
        this.id = id;
        this.cropName = cropName;
        this.quantity = quantity;
        this.actualPrice = actualPrice;
        this.discountedPrice = discountedPrice;
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
        return quantity;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
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