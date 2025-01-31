package ui;

import model.CraftedItem;
import model.Inventory;
import model.Recipe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

// Creates new JFrame GUI to function as crafting menu
public class CraftingWindow extends JFrame implements  ActionListener {
    private Inventory inventory;
    private List<Recipe> recipes;
    private JComboBox<String> recipeComboBox;
    private JButton buttonCraft;
    private JLabel lblFeedback;
    private Font customFont;

    // EFFECTS: creates crafting window with references to inventory and list of recipes
    public CraftingWindow(Inventory inventory, List<Recipe> recipes) {
        this.inventory = inventory;
        this.recipes = recipes;
        createFont();
        createFrame();
    }

    // MODIFIES: this, recipeComboBox, buttonCraft, lblFeedback
    // EFFECTS: sets attributes for GUI frame, including button and label
    private void createFrame() {
        setTitle("Craft Menu");
        setSize(1000,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new FlowLayout());
        getContentPane().setBackground(new java.awt.Color(240, 231, 216));

        recipeComboBox = new JComboBox<>();
        for (Recipe recipe : recipes) {
            String a = recipe.getItemName() + " (Requires: " + recipe.getRequiredQuantity();
            String b = " " + recipe.getMaterialName() + ", Market Value: " + recipe.getSalePrice();
            String c = ", Inventory size of: " + recipe.getInventorySize() + ")";
            String curRecipe = a + b + c;
            recipeComboBox.addItem(curRecipe);
        }
        add(recipeComboBox);

        buttonCraft = new JButton("Craft");
        buttonCraft.addActionListener(this);
        add(buttonCraft);

        lblFeedback = new JLabel("Select item to craft.");
        lblFeedback.setFont(customFont.deriveFont(14f));
        add(lblFeedback);
    }

    // MODIFIES: this
    // EFFECTS: processes user command when buttonCraft is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonCraft) {
            craft();
        }
    }

    // MODIFIES: inventory, lblFeedback
    // EFFECTS: handles logic and action for crafting items
    private void craft() {
        Recipe selectedRecipe = recipes.get(recipeComboBox.getSelectedIndex());

        String materialName = selectedRecipe.getMaterialName();
        int requiredQuantity  = selectedRecipe.getRequiredQuantity();
        String itemName = selectedRecipe.getItemName();
        double itemPrice = selectedRecipe.getSalePrice();
        int size = selectedRecipe.getInventorySize();

        if (inventory.getMaterialQuantity(materialName) >= requiredQuantity) {
            CraftedItem item = new CraftedItem(itemName, itemPrice, size);
            if (inventory.addItem(item)) {
                lblFeedback.setText("Crafted a " + item.getQuality() + " " + item.getName());
                inventory.removeMaterial(materialName, requiredQuantity);
            } else {
                lblFeedback.setText("Not enough space in inventory, buy more capacity!");
            }
        } else {
            lblFeedback.setText("Not enough " + materialName + " to craft " + itemName + ".");
        }
    }

    // MODIFIES: customFont
    // EFFECTS: creates font and registers it
    private void createFont() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("data/MinecraftFont.otf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            System.out.println("font error");
        }
    }
}
