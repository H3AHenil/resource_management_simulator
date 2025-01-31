package model;

import persistence.Writable;
import org.json.JSONObject;

import javax.swing.*;

// Represents player bank account
public class BankAccount implements Writable {
    private double balance;
    private double interestRate;

    // EFFECTS: creates new bank account for player with an initial balance of 0 and interest rate of 1.1%
    public BankAccount() {
        this.balance = 0.0;
        this.interestRate = 1.1;
    }

    // EFFECTS: return bank account balance amount
    public double getBalance() {
        return balance;
    }

    // EFFECTS: return interest rate
    public double getInterestRate() {
        return interestRate;
    }

    // REQUIRES: interest rate >= 0
    // MODIFIES: this
    // EFFECTS: set new interest rate
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    // REQUIRES: balance >= 0
    // MODIFIES: this
    // EFFECTS: adds additional amount to balance
    public void deposit(double amount) {
        balance += amount;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: decrease given amount to balance
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        }
    }

    // MODIFIES: this
    // EFFECTS: update bank account balance when called
    public void applyInterest() {
        balance += balance * interestRate / 100;
    }

    // REQUIRES: increment >= 0
    // MODIFIES: this
    // EFFECTS: increment bank account interest
    public void incrementInterest(double increment) {
        this.interestRate += increment;
    }

    // EFFECTS: returns bank account as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("balance", balance);
        json.put("interestRate", interestRate);
        return json;
    }
}
