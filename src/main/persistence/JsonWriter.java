package persistence;

import org.json.JSONArray;
import org.json.JSONObject;
import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes JSON representation of craft capital application to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file not found
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of simulation to file
    public void write(GameData gameData) {
        JSONObject json = new JSONObject();
        json.put("inventory", gameData.getInventory().toJson());
        json.put("bankAccount", gameData.getBankAccount().toJson());
        json.put("day", gameData.getDay());
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
// Class modelled from 210 JsonSerializationDemo