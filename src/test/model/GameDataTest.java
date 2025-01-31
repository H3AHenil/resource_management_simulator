package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameDataTest {
    private GameData gameData;

    @BeforeEach
    public void setUp() {
        gameData = new GameData(new Inventory(5), new BankAccount(), 1);
    }

    @Test
    public void testConstructor() {
        assertEquals(1, gameData.getDay());
        assertEquals(0.0, gameData.getBankAccount().getBalance());
        assertEquals(1.1, gameData.getBankAccount().getInterestRate());
        assertEquals(5, gameData.getInventory().getCapacity());
        assertEquals(0, gameData.getInventory().getItems().size());
    }
}
