package ui;

import model.Inventory;
import model.CraftedItem;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Comparator;
import java.util.List;

// Creates new JFrame GUI to show inventory
public class InventoryWindow extends JFrame implements ActionListener {
    private Inventory inventory;
    private JTextArea inventoryText;
    private JButton buttonSort;
    private Font  customFont;

    // EFFECTS: creates new inventory and GUI frame
    public InventoryWindow(Inventory inventory) {
        this.inventory = inventory;
        createFrame();
    }

    // MODIFIES: this, inventoryText
    // EFFECTS: sets attributes for GUI frame
    private void createFrame() {
        setTitle("Inventory");
        setSize(500,300);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(new java.awt.Color(240, 231, 216));
        createFont();

        inventoryText = new JTextArea();
        inventoryText.setFont(customFont.deriveFont(14f));
        inventoryText.setEditable(false);
        inventoryText.setBackground(new java.awt.Color(240, 231, 216));
        JScrollPane scrollPane = new JScrollPane(inventoryText);
        add(scrollPane, BorderLayout.CENTER);

        createButton();

        updateInventoryText();
    }

    // MODIFIES: buttonSort
    // EFFECTS: creates visual component of button for sorting items
    private void createButton() {
        buttonSort = new JButton("Sort by Price Descending");

        Border lineBorder = BorderFactory.createLineBorder(new Color(110,70,22), 3);
        Border shadow = new MatteBorder(0, 0, 3, 3, Color.BLACK);
        CompoundBorder compoundBorder = new CompoundBorder(shadow, lineBorder);
        buttonSort.setBorder(compoundBorder);
        buttonSort.setBackground(new Color(231, 216, 193));
        buttonSort.setOpaque(true);
        buttonSort.setFont(customFont.deriveFont(14f));
        buttonSort.addActionListener(this);
        add(buttonSort, BorderLayout.SOUTH);
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

    // MODIFIES: inventoryText
    // EFFECTS: updates text area to display window
    private void updateInventoryText() {
        StringBuilder displayedText = new StringBuilder("Materials:\n");
        inventory.getMaterials().forEach((material, quantity) ->
                displayedText.append(material).append(": ").append(quantity).append("\n"));

        displayedText.append("\nCrafted Items:\n");
        for (CraftedItem item : inventory.getItems()) {
            displayedText.append(item.getName()).append(", Price: $")
                    .append(item.getSalePrice()).append(", Quality: ")
                    .append(item.getQuality()).append("\n");
        }
        inventoryText.setText(displayedText.toString());
    }

    // MODIFIES: items
    // EFFECTS: updates text area to display window
    private void sortItems() {
        inventory.sortItems();
        updateInventoryText();
    }

    // MODIFIES: this
    // EFFECTS: processes user command when buttonSort is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonSort) {
            sortItems();
        }
    }
}
