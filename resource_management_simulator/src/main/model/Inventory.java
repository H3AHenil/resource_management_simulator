package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

// Represents inventory that holds crafted items with a capacity
public class Inventory implements Writable {
    private int capacity;
    private int emptyCapacity;
    private List<CraftedItem> items;
    private Map<String, Integer> materials;

    // EFFECTS: Create new inventory with empty list of items, materials, and given capacity
    public Inventory(int emptyCapacity) {
        this.items = new ArrayList<>();
        this.materials = new HashMap<>();
        this.capacity = emptyCapacity;
        this.emptyCapacity = emptyCapacity;
    }

    // EFFECTS: return inventory capacity
    public int getCapacity() {
        return capacity;
    }

    // EFFECTS: return empty inventory capacity
    public int getEmptyCapacity() {
        return emptyCapacity;
    }

    // EFFECTS: return list of crafted items
    public List<CraftedItem> getItems() {
        return items;
    }

    // EFFECTS: sort items by price descending
    public void sortItems() {
        items.sort(Comparator.comparingDouble(CraftedItem::getSalePrice).reversed());
        EventLog.getInstance().logEvent(new Event("Sorted items in inventory by price descending"));
    }

    // EFFECTS: return the quantity of given material
    public int getMaterialQuantity(String materialName) {
        return materials.getOrDefault(materialName,0);
    }

    // EFFECTS: return material and their quantities
    public Map<String, Integer> getMaterials() {
        return materials;
    }

    // MODIFIES: this
    // EFFECTS: adds new item to list if capacity is available
    public boolean addItem(CraftedItem item) {
        if (this.capacity >= item.getInventorySize()) {
            items.add(item);
            capacity -= item.getInventorySize();
            EventLog.getInstance().logEvent(new Event("Added new item: " + item.getName()));
            return true;
        }
        EventLog.getInstance().logEvent(new Event("Capacity overload, failed to add new item: "
                + item.getName()));
        return false;
    }

    // MODIFIES: this
    // EFFECTS: removes item to list and adjust capacity
    public boolean removeItem(CraftedItem item) {
        if (items.contains(item)) {
            items.remove(item);
            capacity += item.getInventorySize();
            return true;
        }
        return false;
    }

    // REQUIRES: quantity > 0 and materialName has a non-zero length
    // MODIFIES: this
    // EFFECTS: add to quantity of specified of material
    public boolean addMaterial(String materialName, int quantity) {
        materials.put(materialName, materials.getOrDefault(materialName, 0) + quantity);
        return true;
    }

    // REQUIRES: quantity > 0 and materialName has a non-zero length
    // MODIFIES: this
    // EFFECTS: remove quantity of specified of material if available
    public boolean removeMaterial(String materialName, int quantity) {
        if (materials.getOrDefault(materialName, 0) >= quantity) {
            materials.put(materialName, materials.getOrDefault(materialName, 0) - quantity);
            return true;
        }
        return false;
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: increase capacity
    public void expandCapacity(int amount) {
        this.emptyCapacity += amount;
    }

    // MODIFIES: this
    // EFFECTS: empty inventory of items
    public void clearItems() {
        items.clear();
        EventLog.getInstance().logEvent(new Event("Cleared inventory by going to the next day"));
    }

    // MODIFIES: this
    // EFFECTS: reset capacity
    public void resetCapacity() {
        this.capacity = this.emptyCapacity;
    }

    // EFFECTS: returns inventory with all materials, items, and capacity as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray itemsJson = new JSONArray();
        JSONArray materialsJson = new JSONArray();

        for (CraftedItem item: items) {
            itemsJson.put(item.toJson());
        }
        json.put("craftedItems", itemsJson);

        for (Map.Entry<String, Integer> entry : materials.entrySet()) {
            JSONObject materialJson = new JSONObject();
            materialJson.put("name", entry.getKey());
            materialJson.put("quantity", entry.getValue());
            materialsJson.put(materialJson);
        }

        json.put("materials", materialsJson);

        json.put("capacity", capacity);

        return json;
    }
}
