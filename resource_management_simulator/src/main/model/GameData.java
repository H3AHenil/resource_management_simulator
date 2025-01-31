package model;

// Represents the application data with inventory, bankAccount, and day number
public class GameData {
    private Inventory inventory;
    private BankAccount bankAccount;
    private int day;

    // EFFECTS: constructs new instance of game data with inventory, bankAccount, and day number
    public GameData(Inventory inventory, BankAccount bankAccount, int day) {
        this.inventory = inventory;
        this.bankAccount = bankAccount;
        this.day = day;
    }

    // EFFECTS: returns inventory
    public Inventory getInventory() {
        return inventory;
    }

    // EFFECTS: returns bankAccount
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    // EFFECTS: returns day number
    public int getDay() {
        return day;
    }
}
