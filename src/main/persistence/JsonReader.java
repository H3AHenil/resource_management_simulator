package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader to that reads simulation from JSON data stored in the file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from the source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads source file as string and returns it
    public String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: loads simulation data from file for the gameData with bank, inventory, and day number
    // and returns the GameData object; throws IOException if an error occurs reading data from file
    public GameData loadGame()
            throws IOException {
        String data = readFile(source);
        JSONObject jsonObject = new JSONObject(data);

        BankAccount bankAccount = parseBank(jsonObject);
        Inventory inventory = parseInventory(jsonObject);
        int day = parseDay(jsonObject);

        return new GameData(inventory,bankAccount,day);
    }

    // MODIFIES: gameData
    // EFFECTS: parses bank account information from JSON object and returns it
    private BankAccount parseBank(JSONObject jsonObject) {
        JSONObject bankAccountJson = jsonObject.getJSONObject("bankAccount");
        double balance = bankAccountJson.getDouble("balance");
        double interestRate = bankAccountJson.getDouble("interestRate");
        BankAccount bankAccount = new BankAccount();
        bankAccount.deposit(balance);
        bankAccount.setInterestRate(interestRate);

        return bankAccount;
    }

    // MODIFIES: gameData
    // EFFECTS: parses materials, items, and capacity from JSON object and returns inventory
    private Inventory parseInventory(JSONObject jsonObject) {
        Inventory inventory = new Inventory(5);
        JSONObject inventoryJson = jsonObject.getJSONObject("inventory");
        JSONArray materialsJson = inventoryJson.getJSONArray("materials");
        JSONArray itemsJson = inventoryJson.getJSONArray("craftedItems");

        int capacity = inventoryJson.getInt("capacity");
        inventory.resetCapacity();
        inventory.expandCapacity(capacity);

        for (int i = 0; i < materialsJson.length(); i++) {
            JSONObject materialJson = materialsJson.getJSONObject(i);
            String name = materialJson.getString("name");
            int quantity = materialJson.getInt("quantity");
            inventory.addMaterial(name, quantity);
        }

        for (int i = 0; i < itemsJson.length(); i++) {
            JSONObject itemJson = itemsJson.getJSONObject(i);
            String name = itemJson.getString("name");
            double baseSalePrice = itemJson.getDouble("baseSalePrice");
            int inventorySize = itemJson.getInt("inventorySize");
            String quality = itemJson.getString("quality");

            CraftedItem craftedItem = new CraftedItem(name, baseSalePrice, inventorySize, quality);
            inventory.addItem(craftedItem);
        }
        return inventory;
    }

    // MODIFIES: gameData
    // EFFECTS: parses day number from JSON object and returns it
    private int parseDay(JSONObject jsonObject) {
        return jsonObject.getInt("day");
    }
}
// Class modelled from 210 JsonSerializationDemo