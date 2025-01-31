package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import ui.CraftCapitalApp;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    public void testReaderNonExistentFile() {
        try {
            JsonReader reader = new JsonReader("./data.noSuchFile.json");
            reader.loadGame();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }


    @Test
    public void testReaderEmptyGame() {
        try {
            JsonReader reader = new JsonReader("./data/testReaderEmptyCraftCapital.json");
            GameData gameData1 = reader.loadGame();
            assertEquals(0, gameData1.getInventory().getMaterials().size());
            assertEquals(0, gameData1.getInventory().getItems().size());
            assertEquals(0.0, gameData1.getBankAccount().getBalance());
            assertEquals(1, gameData1.getDay());
        } catch (IOException e) {
            fail("Exception should not been thrown");
        }
    }

    @Test
    public void testReaderGeneralFile() {
        try {
            JsonReader reader = new JsonReader("./data/testReaderGeneralCraftCapital.json");
            GameData gameData1 = reader.loadGame();

            assertEquals(250.0, gameData1.getBankAccount().getBalance());
            assertEquals(3, gameData1.getDay());
            assertEquals(2.6, gameData1.getBankAccount().getInterestRate());

            assertEquals(3, gameData1.getInventory().getMaterials().size());
            assertTrue(gameData1.getInventory().getMaterials().containsKey("Wood"));
            assertEquals(20, gameData1.getInventory().getMaterialQuantity("Wood"));

            CraftedItem item = gameData1.getInventory().getItems().get(0);
            assertEquals("'Live, Laugh, Love' Wooden Block", item.getName());
            assertEquals(5.0, item.getSalePrice());
            assertEquals("Fantastic", item.getQuality());
        } catch (IOException e) {
            fail("Couldn't read from the file");
        }
    }
}