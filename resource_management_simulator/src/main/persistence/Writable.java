package persistence;

import org.json.JSONObject;

// Interface to allow model classes to return JSON objects
public interface Writable {
    // EFFECTS: returns implemented object as JSON object
    JSONObject toJson();
}
