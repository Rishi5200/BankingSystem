package com.src.bank;

import java.util.Hashtable;

import com.src.account.Account;
import com.src.customer.Customer;
import java.util.ArrayList;
import com.src.exceptions.AccountNotFoundException;
import com.src.exceptions.CustomerAlreadyExistsException;
import com.src.account.SavingAccount;
import com.src.services.TransactionServiceImpl;
import com.src.services.MailService;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class Bank {
    private Hashtable<String, String> branches;
    private ArrayList<Customer> customers;
    public TransactionServiceImpl transactionService;
    private MailService mailService;

    private static final Logger logger = Logger.getLogger(TransactionServiceImpl.class.getName());



    public Bank() {
        customers = new ArrayList<>();
        branches = new Hashtable<>();
        // nextCustomerId = 1;
        this.transactionService = new TransactionServiceImpl();
        this.mailService = new MailService();

        branches.put("IFSC001", "Mumbai");
        branches.put("IFSC002", "Delhi");
        branches.put("IFSC003", "Pune");
        branches.put("IFSC004", "Chandigarh");
        branches.put("IFSC005", "Bengaluru");
    }

    public void displayBranches() {
        System.out.println("Bank Branches and IFSC Codes:");
        branches.forEach((ifscCode, branchName) -> System.out.println(ifscCode + " - " + branchName));
    }

    public String getBranchName(String ifscCode) {
        return branches.get(ifscCode);
    }

    public boolean isValidIFSCCode(String ifscCode) {
        if  (branches.containsKey(ifscCode)) {
            return true;
        }
        System.out.println("Invalid IFSC code. Please enter a valid IFSC code from the list below:");
        displayBranches(); // Show available IFSC codes
        return false;
    }


    public void addCustomer(Customer customer) throws CustomerAlreadyExistsException {
        // Check if the customer already exists
        if (customers.stream().anyMatch(c -> c.getCustomerId().equalsIgnoreCase(customer.getCustomerId()))) {
            throw new CustomerAlreadyExistsException("Customer with ID " + customer.getCustomerId() + " already exists.");
        }
        else {
            customers.add(customer);
            System.out.println("Customer added: " + customer.getName());
        }
    }

    public Customer searchCustomerById(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }


    public ArrayList<Customer> getCustomers() {
        return customers;
    }


    // Function to get the list of customers with low balance
    public ArrayList<Customer> getCustomersWithLowBalance() {
        return customers.stream()
                .filter(c -> c.getAccount("Saving") != null
                        && c.getAccount("Saving").getBalance().compareTo(SavingAccount.MIN_BALANCE) < 0)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Function to print the list of customers with low balance
    public void displayCustomersWithLowBalance() {
        ArrayList<Customer> lowBalanceCustomers = getCustomersWithLowBalance();
        System.out.println("Customers with low balance:");
        lowBalanceCustomers.forEach(System.out::println);
    }



    public void checkAndSendLowBalanceWarning(Customer customer, long accountNumber)  {
        try{
            customer.hasAccount(accountNumber);
            Account account = customer.returnAccount(accountNumber);
            String accountType = account.getClass().getSimpleName();
            if (accountType.equalsIgnoreCase("SavingAccount") &&
                    account.getBalance().compareTo(SavingAccount.MIN_BALANCE) < 0) {
                mailService.sendLowBalanceWarning(customer);
                System.out.println("Warning email sent to " +customer.getName() + " due to low balance.");
                logger.warning("Warning email sent to " +customer.getName() + " due to low balance.");
            }
//            else {
//                System.out.println(accountType + " " + accountType.equalsIgnoreCase("Saving"));
//                System.out.println(account.getBalance() + " " + SavingAccount.MIN_BALANCE + " " + (account.getBalance().compareTo(SavingAccount.MIN_BALANCE) < 0) );
//                System.out.println("No low balance.");
//            }
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}