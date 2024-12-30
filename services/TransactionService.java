package com.src.services;

import com.src.account.Account;
import java.math.BigDecimal;

public interface TransactionService {
    void deposit(Account account, BigDecimal amount);
    void withdraw(Account account, BigDecimal amount);
    void performNEFT(Account fromAccount, Account toAccount, BigDecimal amount);
}