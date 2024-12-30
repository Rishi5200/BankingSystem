package com.src.customer;

import com.src.account.Account;
import com.src.account.CurrentAccount;
import com.src.account.SavingAccount;
import com.src.exceptions.AccountAlreadyExistsException;
import com.src.exceptions.AccountNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {
    private final String customerId;
    private final Name name;

    public enum CustomerType {
        Retail, Corporate
    }
    private final CustomerType customerType; // Retail/Corporate

    private final String email;
    private final List<Account> accounts;

    public Customer(String customerId, String name, CustomerType customerType, String email) {
        if (customerId == null || customerId.isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (customerType == null) {
            throw new IllegalArgumentException("Customer type cannot be null");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        this.customerId = customerId;
        this.name = new Name(name);
        this.customerType = customerType;
        this.email = email;
        this.accounts = new ArrayList<>();

    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name.getFullName();
    }

    public String getEmail() {
        return email;
    }


    public Account getAccount(String accountType) {
        for (Account account : accounts) {
            if (accountType.equalsIgnoreCase("Saving") && account instanceof SavingAccount) {
                return account;
            } else if (accountType.equalsIgnoreCase("Current") && account instanceof CurrentAccount) {
                return account;
            }
        }
        return null;
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public void addAccount(Account account) throws AccountAlreadyExistsException {
        for (Account acc : accounts) {
            if (acc.getClass() == account.getClass()) {
                throw new AccountAlreadyExistsException(account.getClass().getSimpleName() + " already exists for customer " + this.name);
            }
        }
        accounts.add(account);
    }

    public boolean hasAccount(long accountNumber) throws AccountNotFoundException {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return true;
            }
        }
        throw new AccountNotFoundException("Customer " + this.name + " does not have an account with number " + accountNumber);
    }

    public Account returnAccount(long accountNumber) {
        try {
            hasAccount(accountNumber);
            for (Account account : accounts) {
                if (account.getAccountNumber() == accountNumber) {
                    return account;
                }
            }
        } catch (AccountNotFoundException e) {
            System.out.println(e);
        }
        return null;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer ID: ").append(customerId)
                .append("\nName: ").append(name.getFullName())
                .append("\nType: ").append(customerType)
                .append("\nEmail: ").append(email)
                .append("\nAccounts: ");
        if (accounts.isEmpty()) {
            sb.append("No accounts");
        } else {
            for (Account account : accounts) {
                sb.append("\n  - ").append(account.toString());
            }
        }
        return sb.toString();
    }


}