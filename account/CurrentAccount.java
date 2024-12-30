package com.src.account;

import java.math.BigDecimal;

public class CurrentAccount extends Account {
    public CurrentAccount(BigDecimal initialBalance, String ifscCode) {
        super(initialBalance, ifscCode);
    }

//    @Override
//    public void withdraw(double amount) {
//        balance -= amount;
//    }

//    @Override
//    public void deposit(double amount) {
//        balance += amount;
//    }
}