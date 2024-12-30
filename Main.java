package com.src;

import com.src.account.CurrentAccount;
import com.src.account.SavingAccount;
import com.src.bank.Bank;
import com.src.customer.Customer;
// import com.src.services.TransactionServiceImpl;
import com.src.exceptions.AccountAlreadyExistsException;
import com.src.exceptions.CustomerAlreadyExistsException;
import com.src.services.MailService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Bank bank = new Bank();
    // TransactionServiceImpl transactionService = new TransactionServiceImpl();
    MailService mailService = new MailService();
    Scanner scanner = new Scanner(System.in);
    boolean exit = false;

    while (!exit) {
      System.out.println("\n--- XYZ Bank System ---");
      System.out.println("1) Add Customer");
      System.out.println("2) Create account for a Customer with some balance amount");
      System.out.println("3) Perform NEFT operation and display updated balance");
      System.out.println("4) Perform deposit and withdrawal operations and display updated balance after transactions");
      System.out.println("5) Display the Branch and IFSC Code List");
      System.out.println("6) Search for a customer based on customer ID");
      System.out.println("7) Display list of customers who do not maintain the minimum balance");
      System.out.println("8) Send mail to customers who do not maintain the minimum balance");
      System.out.println("9) Exit menu");
      System.out.print("Select an option: ");

      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume newline

      switch (choice) {
        case 1:
          addCustomer(bank, scanner);
          break;

        case 2:
          createAccount(bank, scanner);
          break;

        case 3:
          NEFTTransaction(bank, scanner);
          break;

        case 4:
          depositWithdrawTransaction(bank, scanner);
          break;

        case 5:
          bank.displayBranches();
          break;

        case 6:
          System.out.println("Enter Customer ID to search:");
          String customerId = scanner.nextLine();
          Customer customer = bank.searchCustomerById(customerId);

          if (customer != null) {
            System.out.println(customer);
          } else {
            System.out.println("Customer not found.");
          }
          break;

        case 7:
          bank.displayCustomersWithLowBalance();
          break;

        case 8:
          ArrayList<Customer> customersWithLowBalance = bank.getCustomersWithLowBalance();
          mailService.sendMailsToLowBalanceCustomers(customersWithLowBalance);
          System.out.println("Warning emails sent to customers with low balance.");
          break;

        case 9:
          exit = true;
          System.out.println("Exiting system.");
          break;

        default:
          System.out.println("Invalid option.");
      }
    }
  }

  // Implementation of Case Study requirements

  // 1
  public static void addCustomer(Bank bank, Scanner scanner) {
    System.out.println("Enter Customer Id:");
    String customerId = scanner.nextLine();
    System.out.println("Enter Customer Name:");
    String name = scanner.nextLine();
    System.out.println("Enter Customer Type (R for Retail, C for Corporate):");
    char typeChar = scanner.next().charAt(0);
    Customer.CustomerType type = (typeChar == 'R' || typeChar == 'r') ? Customer.CustomerType.Retail
        : Customer.CustomerType.Corporate;
    System.out.println("Enter Customer Email:");
    scanner.nextLine(); // Consume leftover newline
    String email = scanner.nextLine();

    // int customerId = getNextCustomerId();
    try {
      Customer newCustomer = new Customer(customerId, name, type, email);
      try {
        bank.addCustomer(newCustomer);
        System.out.println(newCustomer);
      } catch (CustomerAlreadyExistsException e) {
        System.out.println(e.getMessage());
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

  }

  // 2
  public static void createAccount(Bank bank, Scanner scanner) {
    System.out.println("Enter Customer ID:");
    String custId = scanner.nextLine();
    Customer customer = bank.searchCustomerById(custId);

    if (customer != null) {
      System.out.println("Enter account type (S for Saving, C for Current):");
      char AccountTypeChar = scanner.next().charAt(0);
      String AccountType = (AccountTypeChar == 'S' || AccountTypeChar == 's') ? "Saving" : "Current";

      String ifscCode;
      scanner.nextLine();
      while (true) {
        System.out.println("Enter IFSC code:");
        ifscCode = scanner.nextLine();
        // Validate IFSC code
        if (bank.isValidIFSCCode(ifscCode)) {
          break; // Valid IFSC code entered
        }
      }

      if (AccountType.equals("Saving")) {
        System.out.println("Enter initial balance for Saving Account:");
        // scanner.nextLine();
        String savingBalance = scanner.nextLine();
        BigDecimal bSavingBalance = new BigDecimal(savingBalance);
        SavingAccount savingAccount = new SavingAccount(bSavingBalance, ifscCode);
        try {
          customer.addAccount(savingAccount);
          System.out.println(savingAccount);
        } catch (AccountAlreadyExistsException e) {
          System.out.println(e.getMessage());
        }
      } else {
        System.out.println("Enter initial balance for Current Account:");
        // scanner.nextLine();
        String currentBalance = scanner.nextLine();
        BigDecimal bCurrentBalance = new BigDecimal(currentBalance);
        CurrentAccount currentAccount = new CurrentAccount(bCurrentBalance, ifscCode);
        try {
          customer.addAccount(currentAccount);
          System.out.println(currentAccount);
        } catch (AccountAlreadyExistsException e) {
          System.out.println(e.getMessage());
        }
      }
    } else {
      System.out.println("Customer not found.");
    }
  }

  // 3
  public static void NEFTTransaction(Bank bank, Scanner scanner) {
    System.out.println("Enter source Customer ID for NEFT:");
    String fromCustomerId = scanner.nextLine();
    Customer fromCustomer = bank.searchCustomerById(fromCustomerId);

    System.out.println("Enter target Customer ID for NEFT:");
    String toCustomerId = scanner.nextLine();
    Customer toCustomer = bank.searchCustomerById(toCustomerId);

    if (fromCustomer != null && toCustomer != null) {
      System.out.println("Enter source account number: ");
      long fromAccountNumber = scanner.nextLong();
      System.out.println("Enter target account number: ");
      long toAccountNumber = scanner.nextLong();

      System.out.println("Enter NEFT amount:");
      scanner.nextLine();
      String neftAmount = scanner.nextLine();
      BigDecimal bNeftAmount = new BigDecimal(neftAmount);

      bank.transactionService.performNEFTBetweenCustomers(fromCustomer, fromAccountNumber, toCustomer, toAccountNumber,
          bNeftAmount);
      // System.out.println("NEFT Transaction completed.");

      // Check if the minimum balance is breached after withdrawal
      bank.checkAndSendLowBalanceWarning(fromCustomer, fromAccountNumber);
    } else {
      System.out.println("Invalid customer ID.");
    }
  }

  // 4
  public static void depositWithdrawTransaction(Bank bank, Scanner scanner) {
    System.out.println("Enter Customer ID for transaction:");
    String customerId = scanner.nextLine();
    Customer customer = bank.searchCustomerById(customerId);

    if (customer != null) {
      // Get account type
      // System.out.println("Enter account type (S for Saving, C for Current):");
      // char AccountTypeChar = scanner.next().charAt(0);
      // String accountType = (AccountTypeChar == 'S' || AccountTypeChar == 's') ?
      // "Saving" : "Current";
      System.out.println("Enter Account number: ");
      long accountNumber = scanner.nextLong();

      // Get Deposit Withdraw action
      System.out.println("Enter operation (D for Deposit, W for Withdraw):");
      char operationChar = scanner.next().charAt(0);
      String operation = (operationChar == 'D' || operationChar == 'd') ? "Deposit" : "Withdraw";
      System.out.println("Enter amount:");
      // double amount = scanner.nextDouble();
      scanner.nextLine();
      String amount = scanner.nextLine();
      BigDecimal bAmount = new BigDecimal(amount);

      if (operation.equalsIgnoreCase("Deposit")) {
        bank.transactionService.depositToCustomer(customer, accountNumber, bAmount);
      } else if (operation.equalsIgnoreCase("Withdraw")) {
        bank.transactionService.withdrawFromCustomer(customer, accountNumber, bAmount);

        // Check if the minimum balance is breached after withdrawal
        bank.checkAndSendLowBalanceWarning(customer, accountNumber);
      }
      System.out.println("Transaction completed.");
    } else {
      System.out.println("Customer not found.");
    }
  }

}