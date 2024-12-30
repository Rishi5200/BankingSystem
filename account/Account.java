package com.src.account;
import com.src.exceptions.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Account {
    protected BigDecimal balance;  // Using BigDecimal for monetary values
    protected final Currency currency;   // To store currency type (USD, INR, etc.)
    protected final long accountNumber;  // Unique account number
    protected final String ifscCode;     // IFSC code of the branch
    private static final AtomicLong accountNumberGenerator = new AtomicLong(1000000000L); // Starting point for account numbers

    public Account(BigDecimal initialBalance, String ifscCode) {
        validateAmount(initialBalance);;
        this.accountNumber = accountNumberGenerator.getAndIncrement(); // Generate unique account number;
        this.balance = initialBalance;
        this.currency = Currency.getInstance("INR");
        this.ifscCode = ifscCode;
    }

    public void withdraw(BigDecimal amount) throws InsufficientBalanceException {
        // Ensure withdrawal is possible (balance - amount should be >= 0)
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
        } else {
            throw new InsufficientBalanceException("Cannot withdraw, balance cannot be less than 0!");
        }
    }

    // Method to deposit money
    public void deposit(BigDecimal amount) {
        try {
            validateAmount(amount);
            balance = balance.add(amount);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    // Getter for balance
    public BigDecimal getBalance() {
        return balance;
    }

    public String getFormattedBalance() {
        return currency.getSymbol() + " " + balance.toPlainString();
    }

    // Getter for currency
    public Currency getCurrency() {
        return currency;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber +
                "\nBalance: " + getFormattedBalance() +
                "\nIFSC Code: " + ifscCode;
    }
}