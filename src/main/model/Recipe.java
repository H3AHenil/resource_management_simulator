package model;

import java.util.Map;

// Represents item recipes
public class Recipe {
    private String itemName;
    private String materialName;
    private int requiredQuantity;
    private double salePrice;
    private int inventorySize;

    // EFFECTS: constructs new recipe with craftable item name and required material, amount, sale price, and size
    public Recipe(String itemName, String materialName, int requiredQuantity, double salePrice, int inventorySize) {
        this.itemName = itemName;
        this.materialName = materialName;
        this.requiredQuantity = requiredQuantity;
        this.salePrice = salePrice;
        this.inventorySize = inventorySize;
    }

    // EFFECTS: returns item name
    public String getItemName() {
        return itemName;
    }

    // EFFECTS: returns materialName
    public String getMaterialName() {
        return materialName;
    }

    // EFFECTS: returns required quantity
    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    // EFFECTS: returns base sale price of item
    public double getSalePrice() {
        return salePrice;
    }

    // EFFECTS: returns inventory size of item
    public int getInventorySize() {
        return inventorySize;
    }
}
