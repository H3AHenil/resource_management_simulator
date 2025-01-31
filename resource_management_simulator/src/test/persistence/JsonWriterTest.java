package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import ui.CraftCapitalApp;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("Expected IOException");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmptyGame() {
        try {
            BankAccount bankAccount = new BankAccount();
            Inventory inventory = new Inventory(5);
            int day = 1;
            GameData gameData = new GameData(inventory, bankAccount, day);

            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCraftCapital.json");
            writer.open();
            writer.write(gameData);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCraftCapital.json");
            GameData gameData1 = reader.loadGame();
            assertEquals(0, gameData1.getBankAccount().getBalance());
            assertEquals(0, gameData1.getInventory().getItems().size());
            assertEquals(0, gameData1.getInventory().getMaterials().size());
        } catch (IOException e) {
            fail("Exception should not been thrown");
        }
    }

    @Test
    public void testWriterGeneralGame() {
        try {
            BankAccount bankAccount = new BankAccount();
            bankAccount.setInterestRate(1.6);
            bankAccount.deposit(100.0);

            Inventory inventory = new Inventory(5);
            inventory.addItem(new CraftedItem("Wood Door", 100.0, 3));
            inventory.addItem(new CraftedItem("Satanic Stone Sculpture", 20.0, 1));
            inventory.addMaterial("Wood", 20);

            int day = 5;
            GameData gameData = new GameData(inventory, bankAccount, day);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralCraftCapital.json");
            writer.open();
            writer.write(gameData);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralCraftCapital.json");
            GameData gameData1 = reader.loadGame();
            assertEquals(100,  gameData1.getBankAccount().getBalance());
            assertEquals(2, gameData1.getInventory().getItems().size());
            assertEquals("Wood Door", gameData1.getInventory().getItems().get(0).getName());
            assertEquals("Satanic Stone Sculpture", gameData1.getInventory().getItems().get(1).getName());
            assertTrue(gameData1.getInventory().getMaterials().containsKey("Wood"));
            assertEquals(20, gameData1.getInventory().getMaterialQuantity("Wood"));

        } catch (IOException e) {
            fail("Exception should not been thrown");
        }
    }
}