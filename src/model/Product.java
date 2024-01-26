package model;

public class Product {

    private int productId;
    private String productName;
    private Brand productBrand;
    private Category productCategory;
    private double price;
    private double discountRate;
    private int stockQuantity;
    private String internalStorage;
    private String screenSize;
    private String ramCapacity;
    private String color;
    private String batteryCapacity;

    public Product(int productId, String productName, Category productCategory, double price,double discountRate, int stockQuantity, Brand productBrand, String internalStorage, String screenSize, String ramCapacity, String color, String batteryCapacity) {
        this.productId = productId;
        this.productName = productName;
        this.productBrand = productBrand;
        this.productCategory = productCategory;
        this.price = price;
        this.discountRate = discountRate;
        this.stockQuantity = stockQuantity;
        this.internalStorage = internalStorage;
        this.screenSize = screenSize;
        this.ramCapacity = ramCapacity;
        this.color = color;
        this.batteryCapacity = batteryCapacity;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Brand getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(Brand productBrand) {
        this.productBrand = productBrand;
    }

    public Category getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Category productCategory) {
        this.productCategory = productCategory;
    }
}
