package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents materials to craft with
public class Material {
    private String name;
    private int quantity;

    // EFFECTS: creates new material with name and initial given quantity
    public Material(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // EFFECTS: returns name
    public String getName() {
        return name;
    }

    // EFFECTS: returns quantity of material
    public int getQuantity() {
        return quantity;
    }

    // REQUIRES: value >= 0
    // MODIFIES: this
    // EFFECTS: increase material quantity by given number
    public void addQuantity(int value) {
        if (value > 0) {
            this.quantity += value;
        }
    }

    // REQUIRES: value >= 0
    // MODIFIES: this
    // EFFECTS: decrease material quantity by given number
    public void consumeQuantity(int value) {
        if (this.quantity > value) {
            this.quantity -= value;
        }
    }
}
