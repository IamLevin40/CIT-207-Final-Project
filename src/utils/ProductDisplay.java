package utils;

import javafx.scene.image.ImageView;

public class ProductDisplay {
    private final int id;
    private final String cropId;
    private final double price;
    private final ImageView imageView;

    public ProductDisplay(int id, String cropId, double price, ImageView imageView) {
        this.id = id;
        this.cropId = cropId;
        this.price = price;
        this.imageView = imageView;
    }

    public int getId() {
        return id;
    }

    public String getCropId() {
        return cropId;
    }

    public double getPrice() {
        return price;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
