package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialTest {
    private Material wood;

    @BeforeEach
    public void setUp() {
        wood = new Material("Wood", 10);
    }

    @Test
    public void testAddQuantity() {
        wood.addQuantity(5);
        assertEquals(15, wood.getQuantity());
    }

    @Test
    public void testConsumeQuantity() {
        wood.consumeQuantity(3);
        assertEquals(7, wood.getQuantity());
    }

    @Test
    public void testConsumeQuantityMoreThanAvailable() {
        wood.consumeQuantity(15);
        assertTrue(wood.getQuantity() > 0);
    }

    @Test
    public void testAddQuantityNegative() {
        wood.addQuantity(-5);
        assertEquals(10, wood.getQuantity());
    }

    @Test
    public void testGetName() {
        assertEquals("Wood", wood.getName());
    }
}