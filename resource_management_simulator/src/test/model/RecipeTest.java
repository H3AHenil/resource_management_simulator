package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {
    private Recipe recipe;
    private String materialName = "Wood";
    private int requiredQuantity = 3;
    private double salePrice = 100.0;
    private int inventorySize = 5;

    @BeforeEach
    public void setUp() {
        recipe = new Recipe("Wooden Table", materialName, requiredQuantity, salePrice, inventorySize);
    }

    @Test
    public void testGetItemName() {
        assertEquals("Wooden Table", recipe.getItemName());
    }

    @Test
    public void testGetMaterialName() {
        assertEquals(materialName, recipe.getMaterialName());
    }

    @Test
    public void testRequiredQuantity() {
        assertEquals(requiredQuantity, recipe.getRequiredQuantity());
    }

    @Test
    public void testGetInventorySize() {
        assertEquals(inventorySize, recipe.getInventorySize());
    }

    @Test
    public void testGetSalePrice() {
        assertEquals(salePrice, recipe.getSalePrice());
    }
}
