package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {
    private BankAccount account;

    @BeforeEach
    public void setUp() {
        account = new BankAccount();
    }

    @Test
    public void testConstructor() {
        assertEquals(0.0, account.getBalance());
        assertEquals(1.1, account.getInterestRate());
    }

    @Test
    public void testDeposit() {
        account.deposit(100.0);
        assertEquals(100.0, account.getBalance());
    }

    @Test
    public void testWithdraw() {
        account.deposit(260.0);
        account.withdraw(150.0);
        assertEquals(110.0, account.getBalance());
    }

    @Test
    public void testWithdrawMoreThanBalance() {
        account.deposit(50.0);
        account.withdraw(100.0);
        assertEquals(50.0, account.getBalance());
    }

    @Test
    public void testApplyInterest() {
        account.deposit(100.0);
        account.applyInterest();
        assertEquals(101.1, account.getBalance());
    }

    @Test
    public void testSetInterestRate() {
        account.setInterestRate(5.0);
        account.deposit(100.0);
        account.applyInterest();
        assertEquals(105.0, account.getBalance());
    }

    @Test
    public void testIncrementInterest() {
        account.setInterestRate(5.0);
        account.incrementInterest(5.0);
        account.deposit(100.0);
        account.applyInterest();
        assertEquals(110.0, account.getBalance());
    }
}
