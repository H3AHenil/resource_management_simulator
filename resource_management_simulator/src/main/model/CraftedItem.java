package model;

import org.json.JSONObject;
import persistence.Writable;

import java.nio.file.Paths;
import java.util.Random;

// Represents a crafted item with name, price, and size
public class CraftedItem implements Writable {
    private String name;
    private double baseSalePrice;
    private double salePrice;
    private int inventorySize;
    private String quality;

    // EFFECTS: constructs new crafted item with name, base price, inventory capacity, and randomized quality
    public CraftedItem(String name, double baseSalePrice, int inventorySize) {
        this.name = name;
        this.baseSalePrice = baseSalePrice;
        this.inventorySize = inventorySize;
        assessQuality();
    }

    // EFFECTS: constructor that allows setting quality directly
    public CraftedItem(String name, double baseSalePrice, int inventorySize, String quality) {
        this.name = name;
        this.baseSalePrice = baseSalePrice;
        this.inventorySize = inventorySize;
        this.quality = quality;
        adjustSalePrice();
    }

    // MODIFIES: this
    // EFFECTS: set quality of crafted item based off random set quality assessment
    private void assessQuality() {
        Random rand = new Random();
        int qualityScore = rand.nextInt(100);

        if (qualityScore < 30) {
            this.quality = "Poor";
        } else if (qualityScore < 70) {
            this.quality = "Good";
        } else {
            this.quality = "Fantastic";
        }
        adjustSalePrice();
    }

    // MODIFIES: this
    // EFFECTS: change sale value of crafted item based on quality
    private void adjustSalePrice() {
        if (quality == "Poor") {
            salePrice = baseSalePrice * 0.75;
        } else if (quality == "Good") {
            salePrice = baseSalePrice;
        } else if (quality == "Fantastic") {
            salePrice = baseSalePrice * 1.25;
        } else {
            salePrice = baseSalePrice;
        }
    }

    // EFFECTS: returns name
    public String getName() {
        return name;
    }

    // EFFECTS: returns sale value
    public double getSalePrice() {
        return salePrice;
    }

    // EFFECTS: returns inventory space it takes up
    public int getInventorySize() {
        return inventorySize;
    }

    // EFFECTS: returns crafted item quality
    public String getQuality() {
        return quality;
    }

    // EFFECTS: returns crafted item as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("baseSalePrice", baseSalePrice);
        json.put("inventorySize", inventorySize);
        json.put("quality", quality);
        return json;
    }
}
