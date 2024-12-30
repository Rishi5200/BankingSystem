package com.src.services;

import com.src.account.Account;
import com.src.customer.Customer;
import com.src.exceptions.AccountNotFoundException;
import com.src.exceptions.InsufficientBalanceException;
import java.math.BigDecimal;
import java.util.logging.Logger;

public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = Logger.getLogger(TransactionServiceImpl.class.getName());

    @Override
    public void deposit(Account account, BigDecimal amount) {
        account.deposit(amount);
        logger.info("Deposited: " + amount + ". New balance: " + account.getBalance());
    }

    @Override
    public void withdraw(Account account, BigDecimal amount) {
        try {
            account.withdraw(amount);
            logger.info("Withdrawn: " + amount + ". New balance: " + account.getBalance());
        }
        catch (InsufficientBalanceException e){
            System.out.println(e.getMessage());
            logger.warning("Transaction is not completed.");
        }
    }

    @Override
    public void performNEFT(Account fromAccount, Account toAccount, BigDecimal amount) {
        try {
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            logger.info("NEFT Transfer of " + amount + " completed. From account new balance: " + fromAccount.getBalance() + ". To account new balance: " + toAccount.getBalance());
        }
        catch (InsufficientBalanceException e){
            System.out.println(e.getMessage());
            logger.warning("Transaction is not completed.");
        }
    }

    public void depositToCustomer(Customer customer, long accountNumber, BigDecimal amount) {
        try {
            customer.hasAccount(accountNumber);
            deposit(customer.returnAccount(accountNumber), amount);
        } catch (AccountNotFoundException e){
            logger.warning("Invalid account type or account not found.");
            System.out.println(e.getMessage());
        }
    }

    public void withdrawFromCustomer(Customer customer, long accountNumber, BigDecimal amount) {
        try {
            customer.hasAccount(accountNumber);
            withdraw(customer.returnAccount(accountNumber), amount);
        } catch (AccountNotFoundException e) {
            logger.warning("Invalid account type or account not found.");
            System.out.println(e.getMessage());;
        }
    }

    public void performNEFTBetweenCustomers(Customer fromCustomer, long fromAccountNumber, Customer toCustomer, long toAccountNumber, BigDecimal amount) {
        try {
            fromCustomer.hasAccount(fromAccountNumber);
            toCustomer.hasAccount(toAccountNumber);
            Account fromAccount = fromCustomer.returnAccount(fromAccountNumber);
            Account toAccount = toCustomer.returnAccount(toAccountNumber);

            performNEFT(fromAccount, toAccount, amount);

        } catch (AccountNotFoundException e) {
            logger.warning("Invalid account type or account not found.");
            System.out.println(e.getMessage());;
        }
    }

}