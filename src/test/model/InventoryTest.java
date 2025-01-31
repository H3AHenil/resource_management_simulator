package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    private Inventory inventory;
    private CraftedItem item1;
    private CraftedItem item2;
    private CraftedItem item3;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory(10);
        item1 = new CraftedItem("Wood Desk", 100.0, 3);
        item2 = new CraftedItem("Wood Cabinet", 150.0, 5);
        item3 = new CraftedItem("Wood House", 15000.0, 20);
        inventory.addMaterial("Wood", 10);
    }

    @Test
    public void testAddItemAvailable() {
        assertTrue(inventory.addItem(item1));
        assertEquals(1, inventory.getItems().size());
        assertEquals(7, inventory.getCapacity());
    }

    @Test
    public void testAddItemMultiple() {
        assertTrue(inventory.addItem(item1));
        assertTrue(inventory.addItem(item2));
        assertEquals(2, inventory.getItems().size());
        assertEquals(2, inventory.getCapacity());
    }
    @Test
    public void testAddItemFail() {
        assertFalse(inventory.addItem(item3));
        assertEquals(0, inventory.getItems().size());
        assertEquals(10, inventory.getCapacity());
    }

    @Test
    public void testRemoveItem() {
        inventory.addItem(item1);
        assertTrue(inventory.removeItem(item1));
        assertEquals(0, inventory.getItems().size());
        assertEquals(10, inventory.getCapacity());
    }

    @Test
    public void testRemoveItemDoesNotExist() {
        inventory.addItem(item1);
        assertFalse(inventory.removeItem(item3));
        assertEquals(1, inventory.getItems().size());
        assertEquals(7, inventory.getCapacity());
    }

    @Test
    public void testAddMaterial() {
        assertTrue(inventory.addMaterial("Iron", 20));
        assertEquals(20, inventory.getMaterialQuantity("Iron"));
    }

    @Test
    public void testRemoveMaterial() {
        assertTrue(inventory.removeMaterial("Wood", 5));
        assertEquals(5, inventory.getMaterialQuantity("Wood"));
    }

    @Test
    public void testRemoveMaterialFail() {
        assertFalse(inventory.removeMaterial("Wood", 15));
        assertEquals(10, inventory.getMaterialQuantity("Wood"));
    }

    @Test
    public void testGetMaterialQuantity() {
        assertEquals(10, inventory.getMaterialQuantity("Wood"));
        inventory.addMaterial("Wood", 15);
        assertEquals(25, inventory.getMaterialQuantity("Wood"));

    }

    @Test
    public void testGetMaterials() {
        Map<String, Integer> materials = inventory.getMaterials();

        assertEquals(1, materials.size());
        assertEquals(10, materials.get("Wood"));
    }

    @Test
    public void testExpandCapacity() {
        inventory.expandCapacity(3);
        assertEquals(13, inventory.getEmptyCapacity());
    }

    @Test
    public void testClearItems() {
        inventory.addItem(item1);
        inventory.addItem(item2);
        inventory.clearItems();
        assertEquals(0, inventory.getItems().size());
    }

    @Test
    public void testResetCapacity() {
        inventory.addItem(item1);
        inventory.addItem(item2);
        inventory.resetCapacity();
        assertEquals(10, inventory.getCapacity());
    }

    @Test
    public void testSortItems() {
        inventory.addItem(item1);
        inventory.addItem(item3);
        inventory.addItem(item2);
        inventory.addItem(item2);
        inventory.sortItems();
        assertEquals(item2, inventory.getItems().get(0));
        assertEquals(item1, inventory.getItems().get(1));
    }
}
