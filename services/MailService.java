package com.src.services;

import com.src.account.SavingAccount;
import com.src.customer.Customer;
import java.util.ArrayList;

public class MailService {

    public static void sendEmail(String toemail, String sub, String msg) {
        // Simulating email sending by printing to the console
        System.out.println("Simulating email...");
        System.out.println("To: " + toemail);
        System.out.println("Subject: " + sub);
        System.out.println("Message: \n" + msg);
        System.out.println("Email sent successfully (simulated).");
    }

    public void sendLowBalanceWarning(Customer customer) {
        // Simulate sending an email to the customer
        String subject = "Warning: Low Balance Alert";
        String message = "Dear " + customer.getName() + ",\n\nYour Savings Account balance has fallen below the minimum required balance. " +
                "Kindly deposit funds to avoid penalties.\n\nThank you, XYZ Bank.";

        String toemail = customer.getEmail();

        sendEmail(toemail, subject, message);
    }

    public void sendMailsToLowBalanceCustomers(ArrayList<Customer> customersWithLowBalance) {
        // Simulate sending emails to all customers with low balances
        customersWithLowBalance.forEach(this::sendLowBalanceWarning);
    }
}
