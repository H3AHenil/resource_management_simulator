package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Random;

// Craft Capital Application
public class CraftCapitalApp extends JFrame implements ActionListener {
    private Inventory inventory;
    private BankAccount bankAccount;
    private List<Recipe> recipes;
    private GameData gameData;
    private int day;
    private int winCondition;
    private Random random;
    private Scanner sc;
    private static final String JSON_STORE = "./data/craftcapital.json";
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    private JPanel contentPane;
    private BankWindow bankWindow;
    private CraftingWindow craftingWindow;
    private InventoryWindow inventoryWindow;
    private ShopWindow shopWindow;
    private JFrame currentWindow;
    private JLabel lblMerchant;
    private JLabel lblWelcome;
    private JLabel lblDay;
    private JLabel lblInt;
    private JLabel lblUnits;
    private JLabel bookImage;
    private JLabel winScreen;
    private JLabel winText;
    private Font customFont;
    private JButton buttonCraft;
    private JButton buttonShop;
    private JButton buttonNextDay;
    private JButton buttonInventory;
    private JButton buttonBank;
    private JButton buttonSave;
    private JButton buttonLoad;
    private JButton buttonQuit;



    // EFFECTS: runs the CraftCapital application and initializes the reader and writer
    public CraftCapitalApp() throws FileNotFoundException {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        runCraftCapital();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void runCraftCapital() {
        boolean keepGoing = true;
        String command = null;
        System.out.println("Welcome new merchant!");

        init();

        createFrame();

        while (keepGoing) {
            displayMenu();
            command = sc.next();

            if (command.equals("q")) {
                System.out.println("Quitting game...");
                for (model.Event e : model.EventLog.getInstance()) {
                    System.out.println(e);
                }
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("Goodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("c")) {
            handleCrafting();
        } else if (command.equals("b")) {
            shop();
        } else if (command.equals("n")) {
            startNextDay();
        } else if (command.equals("m")) {
            showBankDetails();
        } else if (command.equals("i")) {
            showInventory();
        } else if (command.equals("s")) {
            saveData();
        } else if (command.equals("l")) {
            loadData();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes bank account, inventory, list of materials,list of recipes,
    //          day counter, $amount win condition, and reference to random
    private void init() {
        bankAccount = new BankAccount();
        inventory = new Inventory(5);
        recipes = new ArrayList<>();
        sc = new Scanner(System.in);
        listOfMaterials();
        listOfRecipes();
        day = 1;
        winCondition = 100; // PLACEHOLDER (Original goal was $1m, but game needs better balancing)
        random = new Random();
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("data/MinecraftFont.otf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            System.out.println("font error");
        }
        bankWindow = new BankWindow();
        bankWindow.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: initializes list of materials
    private void listOfMaterials() {
        inventory.addMaterial("Wood", 10);
        inventory.addMaterial("Stone", 0);
        inventory.addMaterial("Iron", 0);
    }

    // MODIFIES: this
    // EFFECTS: initializes list of recipes
    private void listOfRecipes() {
        recipes.add(new Recipe("'Live, Laugh, Love' Wooden Block", "Wood", 3, 5, 2));
        recipes.add(new Recipe("Wooden Carriage", "Wood", 20, 25.0, 4));
        recipes.add(new Recipe("Satanic Stone Sculpture", "Stone", 5, 20, 2));
        recipes.add(new Recipe("Garden Pathway Stones", "Stone", 30, 60, 8));
        recipes.add(new Recipe("Iron Cage", "Iron", 20, 90, 10));
        recipes.add(new Recipe("Iron Spatula", "Iron", 2, 30, 1));
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("Select from:");
        System.out.println("c -> Craft an item:");
        System.out.println("b -> Buy materials and capacity");
        System.out.println("n -> Proceed to the next day");
        System.out.println("i -> See inventory");
        System.out.println("m -> See bank account balance");
        System.out.println("s -> Save simulation to file");
        System.out.println("l -> Load simulation from file");
        System.out.println("q -> Quit");
    }

    // MODIFIES: this
    // EFFECTS: takes user choice and handles crafting mechanism
    private void handleCrafting() {
        displayRecipes();

        int choice = sc.nextInt() - 1;
        if (choice < 0 || choice >= recipes.size()) {
            System.out.println("Invalid selection");
            return;
        }

        Recipe selectedRecipe = recipes.get(choice);
        String materialName = selectedRecipe.getMaterialName();
        int materialQuantity = selectedRecipe.getRequiredQuantity();
        String itemName = selectedRecipe.getItemName();
        double salePrice = selectedRecipe.getSalePrice();

        if (inventory.getMaterialQuantity(materialName) >= materialQuantity) {
            CraftedItem craftedItem = new CraftedItem(itemName, salePrice, selectedRecipe.getInventorySize());
            if (inventory.addItem(craftedItem)) {
                System.out.print("Crafted " + craftedItem.getName() + " of " + craftedItem.getQuality());
                System.out.println(" quality successfully.");
                inventory.removeMaterial(materialName, materialQuantity);
            } else {
                System.out.println("Not enough space in inventory, buy more capacity!");
            }
        } else {
            System.out.println("Not enough " + materialName + " to craft " + itemName + ".");
        }
    }

    // EFFECTS: displays list of recipes to user
    private void displayRecipes() {
        System.out.println("Choose an item to craft: ");

        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            System.out.print((i + 1) + " -> " + recipe.getItemName() + " (Requires: " + recipe.getRequiredQuantity());
            System.out.print(" " + recipe.getMaterialName() + ", Market Value: " + recipe.getSalePrice());
            System.out.println(", Inventory size of: " + recipe.getInventorySize() + ")");
        }
    }

    // EFFECTS: displays list of material options to user and takes user input
    //          calls method to handle transactions
    private void shop() {
        System.out.println("Welcome to the Shop! Select an option: ");
        System.out.println("1 -> Expand inventory ($15 per space)");
        System.out.println("2 -> Buy Wood ($3 per unit)");
        System.out.println("3 -> Buy Stone ($5 per unit)");
        System.out.println("4 -> Buy Iron ($15 per unit)");

        int option = sc.nextInt();
        System.out.println("Quantity? ");
        int amount = sc.nextInt();

        handleTransaction(option, amount);
    }

    // MODIFIES: this
    // EFFECTS: process transaction
    private void handleTransaction(int option, int amount) {
        if (option == 1 && bankAccount.getBalance() >= 15 * amount) {
            bankAccount.withdraw(15 * amount);
            inventory.expandCapacity(amount);
            System.out.println("Inventory capacity expanded by " + amount);
        } else if (option == 2 && bankAccount.getBalance() >= 3 * amount) {
            bankAccount.withdraw(3 * amount);
            inventory.addMaterial("Wood", amount);
            System.out.println("Purchased " + amount + " units of wood.");
        } else if (option == 3 && bankAccount.getBalance() >= 5 * amount) {
            bankAccount.withdraw(5 * amount);
            inventory.addMaterial("Stone", amount);
            System.out.println("Purchased " + amount + " units of wood.");
        } else if (option == 4 && bankAccount.getBalance() >= 15 * amount) {
            bankAccount.withdraw(3 * amount);
            inventory.addMaterial("Iron", amount);
            System.out.println("Purchased " + amount + " units of wood.");
        } else {
            System.out.println("Inventory funds or invalid input");
        }
    }

    // MODIFIES: this
    // EFFECTS: increments day counter
    //          update bank account
    //          add random amount of materials for next day
    //          sell all crafted items and reset inventory
    //          calls method for win condition
    //          display actions to user
    private void startNextDay() {
        if (currentWindow != null) {
            currentWindow.dispose();
        }
        incrementDay();
        bankAccount.applyInterest();
        bankAccount.incrementInterest(0.5);

        int newWood = random.nextInt(15);
        int newStone = random.nextInt(10);
        int newIron = random.nextInt(5);
        inventory.addMaterial("Wood", newWood);
        inventory.addMaterial("Stone", newStone);
        inventory.addMaterial("Iron", newIron);

        double totalMoneyMade = 0;
        for (CraftedItem item : inventory.getItems()) {
            totalMoneyMade += item.getSalePrice();
        }
        inventory.resetCapacity();
        inventory.clearItems();
        bankAccount.deposit(totalMoneyMade);

        isGameWon();

        updateLbl(newWood, newStone, newIron);
        bankWindow.updateLblText(bankAccount.getBalance());
    }

    // MODIFIES: this
    // EFFECTS: check if win condition is met, if so print message and exit app
    private void isGameWon() {
        if (bankAccount.getBalance() >= winCondition) {
            String message = "Congratulations, you've made more than $" + winCondition + " in " + day + " days!";
            System.out.println("Congratulations, you've made more than $" + winCondition + " in " + day + " days!");
            //System.exit(0);
            createWinScreen(message);
        }
    }

    // REQUIRES: day >= 0
    // MODIFIES: this
    // EFFECTS: set new day
    public void setDay(int day) {
        this.day = day;
    }

    // REQUIRES: day >= 0
    // MODIFIES: this
    // EFFECTS: increment day
    public void incrementDay() {
        this.day += 1;
    }

    // EFFECTS: return day
    public int getDay() {
        return day;
    }

    // EFFECTS: prints bank account balance
    private void showBankDetails() {
        String formatBalance = String.format("%.2f", bankAccount.getBalance());
        System.out.println("Bank Account Balance: " + formatBalance);
    }

    // EFFECTS: prints list of crafted items, materials, and inventory capacity
    private void showInventory() {
        System.out.println("CraftedItems: ");
        for (CraftedItem item : inventory.getItems()) {
            System.out.print("- " + item.getName() + ", of Quality: " + item.getQuality());
            System.out.println(" (Sale Price: " + item.getSalePrice() + ")");
        }

        System.out.println("Materials: ");
        inventory.getMaterials().forEach((material, quantity) ->
                System.out.println("- " + material + ": " + quantity));

        System.out.println("Inventory Capacity: " + inventory.getCapacity());
    }

    // MODIFIES: this
    // EFFECTS: loads simulation from file
    private void loadData() {
        if (currentWindow != null) {
            currentWindow.dispose();
        }

        try {
            gameData = jsonReader.loadGame();
            this.inventory = gameData.getInventory();
            this.bankAccount = gameData.getBankAccount();
            setDay(gameData.getDay());
            updateLbl();
            System.out.println("Loaded game data to " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Failed to read game data from: " + JSON_STORE);
        }
    }

    // EFFECTS: saves simulation to file
    private void saveData() {
        try {
            jsonWriter.open();
            jsonWriter.write(new GameData(inventory, bankAccount, getDay()));
            jsonWriter.close();
            System.out.println("Saved simulation to file: " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: contentPane
    // EFFECTS: creates frame with desired attributes
    private void createFrame() {
        setTitle("Craft Capital");
        setBounds(0, 0, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
        contentPane.setVisible(true);
        contentPane.setBackground(new java.awt.Color(240, 231, 216));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        createLblMerchant();
        createLblWelcome();
        createLblDay();
        createLblInterest();
        createLblUnits();
        createBookImage();
        createButtons();

    }

    // EFFECTS: creates each menu button with correct positioning and text
    private void createButtons() {
        buttonCraft = new JButton("Craft");
        addButton(buttonCraft, 50, 10, 200, 50);

        buttonShop = new JButton("Shop");
        addButton(buttonShop, 50, 80, 200, 50);

        buttonNextDay = new JButton("Next Day");
        addButton(buttonNextDay, 50, 150, 200, 50);

        buttonInventory = new JButton("Inventory");
        addButton(buttonInventory, 50, 220, 200, 50);

        buttonBank = new JButton("Bank Account");
        addButton(buttonBank, 50, 290, 200, 50);

        buttonSave = new JButton("Save");
        addButton(buttonSave, 50, 360, 200, 50);

        buttonLoad = new JButton("Load");
        addButton(buttonLoad, 50, 430, 200, 50);

        buttonQuit = new JButton("Quit");
        addButton(buttonQuit, 50, 500, 200, 50);

        contentPane.revalidate();
        contentPane.repaint();
    }

    // EFFECTS: creates a button with correct attributes
    private void addButton(JButton button, int x, int y, int w, int h) {
        button.setPreferredSize(new Dimension(200, 50));
        Border lineBorder = BorderFactory.createLineBorder(new Color(110,70,22), 3);
        Border shadow = new MatteBorder(0, 0, 3, 3, Color.BLACK);
        CompoundBorder compoundBorder = new CompoundBorder(shadow, lineBorder);
        button.setBorder(compoundBorder);
        button.setBounds(x, y, w, h);
        button.setFont(customFont.deriveFont(25f));
        button.setBackground(new Color(231, 216, 193));
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(this);

        contentPane.add(button);
    }

    // EFFECTS: creates label for greeting and adds it to contentPane
    private void createLblMerchant() {
        lblMerchant = new JLabel("Esteemed Merchant,");
        lblMerchant.setBounds(360, 80, 800, 20);
        lblMerchant.setHorizontalAlignment(JTextField.LEFT);
        lblMerchant.setFont(customFont.deriveFont(19f));
        contentPane.add(lblMerchant);
    }

    // EFFECTS: creates label for welcome message and adds it to contentPane
    private void createLblWelcome() {
        lblWelcome = new JLabel("Welcome to Craft Capital!");
        lblWelcome.setBounds(360, 105, 800, 20);
        lblWelcome.setHorizontalAlignment(JTextField.LEFT);
        lblWelcome.setFont(customFont.deriveFont(19f));
        contentPane.add(lblWelcome);
    }

    // EFFECTS: creates label for day number and adds it to contentPane
    private void createLblDay() {
        lblDay = new JLabel("Day " + getDay() + " begins!");
        lblDay.setBounds(360, 155, 800, 20);
        lblDay.setHorizontalAlignment(JTextField.LEFT);
        lblDay.setFont(customFont.deriveFont(19f));
        contentPane.add(lblDay);
    }

    // EFFECTS: creates label for added interest and adds it to contentPane
    private void createLblInterest() {
        lblInt = new JLabel("");
        lblInt.setBounds(360, 180, 800, 20);
        lblInt.setHorizontalAlignment(JTextField.LEFT);
        lblInt.setFont(customFont.deriveFont(19f));
        contentPane.add(lblInt);
    }

    // EFFECTS: creates label for units of materials added and adds it to contentPane
    private void createLblUnits() {
        lblUnits = new JLabel("");
        lblUnits.setBounds(360, 230, 800, 60);
        lblUnits.setHorizontalAlignment(JTextField.LEFT);
        lblUnits.setFont(customFont.deriveFont(18f));
        contentPane.add(lblUnits);
    }

    // EFFECTS: creates background book image and adds it to contentPane
    private void createBookImage() {
        bookImage = new JLabel(new ImageIcon("data/bookImage.png"));
        bookImage.setBounds(290, 30, 450, 510);
        contentPane.add(bookImage);
    }

    // EFFECTS: creates win screen image and adds it contentPane
    private void createWinScreen(String message) {
        winText = new JLabel(message);
        winText.setBounds(50, 270, 800, 50);
        winText.setFont(customFont.deriveFont(25F));
        winText.setForeground(Color.WHITE);
        contentPane.add(winText);

        winScreen = new JLabel((new ImageIcon("data/winScreenImg.png")));
        winScreen.setBounds(0, 0, 800, 600);
        contentPane.add(winScreen);

        disableMenu();

    }

    // EFFECTS: disables view for menu components
    private void disableMenu() {
        bookImage.setVisible(false);
        buttonCraft.setVisible(false);
        buttonSave.setVisible(false);
        buttonLoad.setVisible(false);
        buttonSave.setVisible(false);
        buttonShop.setVisible(false);
        buttonInventory.setVisible(false);
        buttonBank.setVisible(false);
        buttonNextDay.setVisible(false);
        lblMerchant.setVisible(false);
        lblUnits.setVisible(false);
        lblWelcome.setVisible(false);
        lblInt.setVisible(false);
        lblDay.setVisible(false);
    }

    // EFFECTS: updates required labels when called as necessary and updates GUI
    private void updateLbl(int newWood, int newStone, int newIron) {
        lblDay.setText("Day " + getDay() + " begins!");
        lblInt.setText("Interest rate increased to: " + bankAccount.getInterestRate() + "%");
        lblUnits.setText("<html>Added " + newWood + " units of wood, <br>" + newStone + " units of Stone, and<br>"
                + newIron + " " + "units of Iron to the inventory.</html>");

        contentPane.revalidate();
        contentPane.repaint();
    }

    // EFFECTS: updates required labels when called as necessary and updates GUI
    private void updateLbl() {
        lblDay.setText("Day " + getDay() + " begins!");
        lblInt.setText("");
        lblUnits.setText("");

        contentPane.revalidate();
        contentPane.repaint();
    }

    // MODIFIES: this
    // EFFECTS: processes user command when button is clicked
    public void actionPerformed(ActionEvent action) {
        if (action.getSource() == buttonCraft) {
            openCraftingWindow();
        } else if (action.getSource() == buttonShop) {
            openShopWindow();
        } else if (action.getSource() == buttonNextDay) {
            startNextDay();
        } else if (action.getSource() == buttonInventory) {
            showInventory();
            openInventoryWindow();
        } else if (action.getSource() == buttonBank) {
            showBankDetails();
            openBankAccountWindow();
        } else if (action.getSource() == buttonSave) {
            saveData();
        } else if (action.getSource() == buttonLoad) {
            loadData();
        } else if (action.getSource() == buttonQuit) {
            for (model.Event e : model.EventLog.getInstance()) {
                System.out.println(e);
            }
            System.exit(0);
        }
    }

    // MODIFIES: bankWindow
    // EFFECTS: disables currently opened window, and opens bankWindow
    public void openBankAccountWindow() {
        if (currentWindow != null) {
            currentWindow.dispose();
        }
        bankWindow.updateLblText(bankAccount.getBalance());
        bankWindow.setVisible(true);
        currentWindow = bankWindow;
    }

    // MODIFIES: craftingWindow
    // EFFECTS: disables currently opened window, and opens craftingWindow
    public void openCraftingWindow() {
        if (currentWindow != null) {
            currentWindow.dispose();
        }
        craftingWindow = new CraftingWindow(inventory, recipes);
        craftingWindow.setVisible(true);
        currentWindow = craftingWindow;
    }

    // MODIFIES: shopWindow
    // EFFECTS: disables currently opened window, and opens shopWindow
    public void openShopWindow() {
        if (currentWindow != null) {
            currentWindow.dispose();
        }
        shopWindow = new ShopWindow(inventory, bankAccount);
        shopWindow.setVisible(true);
        currentWindow = shopWindow;
    }

    // MODIFIES: inventoryWindow
    // EFFECTS: disables currently opened window, and opens inventoryWindow
    public void openInventoryWindow() {
        if (currentWindow != null) {
            currentWindow.dispose();
        }
        inventoryWindow = new InventoryWindow(inventory);
        inventoryWindow.setVisible(true);
        currentWindow = inventoryWindow;
    }
}