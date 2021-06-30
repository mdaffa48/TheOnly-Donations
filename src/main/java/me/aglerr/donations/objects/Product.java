package me.aglerr.donations.objects;

public class Product {

    private final String name;
    private final String displayName;
    private final double price;

    public Product(String name, String displayName, double price) {
        this.name = name;
        this.displayName = displayName;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPrice() {
        return price;
    }
}
