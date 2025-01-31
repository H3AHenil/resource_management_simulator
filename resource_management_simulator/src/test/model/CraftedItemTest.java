package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CraftedItemTest {
    private CraftedItem item;

    @BeforeEach
    public void setUp() {
        item = new CraftedItem("Wood Door", 100.0, 3);
    }

    @Test
    public void testConstructor() {
        assertEquals(3, item.getInventorySize());
        assertEquals("Wood Door", item.getName());
        assertTrue(item.getSalePrice() >= 75.0 && item.getSalePrice() <= 125.0);
        assertTrue(item.getQuality() == "Poor" ||  item.getQuality() == "Good"|| item.getQuality() == "Fantastic");
    }

    @Test
    public void testQualityAsessment() {
        if (item.getQuality().equals("Poor")) {
            assertEquals(100.0 * 0.75, item.getSalePrice());
        } else if (item.getQuality().equals("Good")) {
            assertEquals(100.0, item.getSalePrice());
        } else if (item.getQuality().equals("Fantastic")) {
            assertEquals(100.0 * 1.25, item.getSalePrice());
        }
    }

}