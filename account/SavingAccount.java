package com.src.account;
import java.math.BigDecimal;

public class SavingAccount extends Account {
    public static final BigDecimal MIN_BALANCE = new BigDecimal(1000);

    public SavingAccount(BigDecimal initialBalance, String ifscCode) {
        super(initialBalance, ifscCode);
    }

//    @Override
//    public void withdraw(double amount) {
////        if (balance - amount >= MIN_BALANCE) {
//        balance -= amount;
////        } else {
////            System.out.println("Cannot withdraw, minimum balance requirement not met!");
////        }
//    }

//    @Override
//    public void deposit(double amount) {
//        balance += amount;
//    }
}