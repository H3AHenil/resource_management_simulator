package ui;

import javax.swing.*;
import model.BankAccount;
import model.Inventory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

// Creates new JFrame GUI to function as shop menu
public class ShopWindow extends JFrame implements ActionListener {
    private Inventory inventory;
    private BankAccount bankAccount;
    private JComboBox<String> itemSelector;
    private JTextField quantityField;
    private JButton buttonBuy;
    private JLabel lblFeedback;
    private Font customFont;

    // EFFECTS: creates shop window with references to inventory and bankAccount
    public ShopWindow(Inventory inventory, BankAccount bankAccount) {
        this.inventory = inventory;
        this.bankAccount = bankAccount;
        init();
        createFrame();
    }

    // MODIFIES: this
    // EFFECTS: creates GUI frame with desired attributes
    private void createFrame() {
        setTitle("Shop");
        setResizable(false);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        setSize(300,200);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getContentPane().setBackground(new java.awt.Color(240, 231, 216));

        add(itemSelector);
        add(quantityField);
        add(buttonBuy);
        add(lblFeedback);

        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes fields itemSelector, font, quantityField, buttonBuy, and lblFeedback
    private void init() {
        itemSelector = new JComboBox<>(new String[]{
                "Expand Inventory",
                "Buy Wood",
                "Buy Stone",
                "Buy Iron"});
        createFont();
        quantityField = new JTextField(10);
        buttonBuy = new JButton("Buy");
        buttonBuy.addActionListener(this);
        lblFeedback = new JLabel("Current Balance: $" + String.format("%.2f", bankAccount.getBalance()));
        lblFeedback.setFont(customFont.deriveFont(14f));
    }

    // MODIFIES: this
    // EFFECTS: processes user command when buttonBuy is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonBuy) {
            handleTransaction();
        }
    }

    // MODIFIES: bankAccount, inventory, lblFeedback
    // EFFECTS: handles transaction to buy item/capacity and updates lblFeedback
    private void handleTransaction() {
        String selectedItem = (String) itemSelector.getSelectedItem();
        int amount = getAmount();

        if (selectedItem.equals("Expand Inventory") && bankAccount.getBalance() >= 15 * amount) {
            bankAccount.withdraw(15 * amount);
            inventory.expandCapacity(amount);
            lblFeedback.setText("Inventory capacity expanded by " + amount);
        } else if (selectedItem.equals("Buy Wood") && bankAccount.getBalance() >= 3 * amount) {
            bankAccount.withdraw(3 * amount);
            inventory.addMaterial("Wood", amount);
            lblFeedback.setText("Purchased " + amount + " units of wood.");
        } else if (selectedItem.equals("Buy Stone") && bankAccount.getBalance() >= 5 * amount) {
            bankAccount.withdraw(5 * amount);
            inventory.addMaterial("Stone", amount);
            lblFeedback.setText("Purchased " + amount + " units of stone.");
        } else if (selectedItem.equals("Buy Iron") && bankAccount.getBalance() >= 15 * amount) {
            bankAccount.withdraw(15 * amount);
            inventory.addMaterial("Iron", amount);
            lblFeedback.setText("Purchased " + amount + " units of iron.");
        } else {
            lblFeedback.setText("Inventory funds or invalid input");
        }
    }

    // EFFECTS: gets value from text field
    private int getAmount() {
        try {
            int amount = Integer.parseInt(quantityField.getText());
            if (amount <= 0) {
                lblFeedback.setText("Amount must be positive.");
                return 0;
            }
            return amount;
        } catch (NumberFormatException e) {
            lblFeedback.setText("Please enter a valid amount.");
            return 0;
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
