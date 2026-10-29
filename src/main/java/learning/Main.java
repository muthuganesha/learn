package learning;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();

        try (Scanner scanner = new Scanner(System.in)) {
            runMenu(scanner, bank);
        }
    }

    private static void runMenu(Scanner scanner, Bank bank) {
        boolean running = true;

        while (running && scanner.hasNextLine()) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> openAccount(scanner, bank);
                    case "2" -> showAccounts(bank);
                    case "3" -> deposit(scanner, bank);
                    case "4" -> withdraw(scanner, bank);
                    case "5" -> transfer(scanner, bank);
                    case "6" -> showTransactions(scanner, bank);
                    case "0" -> running = false;
                    default -> System.out.println("Choose an option from 0 to 6.");
                }
            } catch (IllegalArgumentException | IllegalStateException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }

        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("\n=== Learning Bank ===");
        System.out.println("1. Open an account");
        System.out.println("2. List accounts");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Transfer");
        System.out.println("6. Show transaction history");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void openAccount(Scanner scanner, Bank bank) {
        System.out.println("1. Savings account");
        System.out.println("2. Checking account");
        String type = readRequired(scanner, "Account type: ");
        String accountNumber = readRequired(scanner, "Account number: ");
        String ownerName = readRequired(scanner, "Owner name: ");
        BigDecimal openingBalance = readMoney(scanner, "Opening balance: ");
        Account account;

        if (type.equals("1")) {
            account = bank.openSavingsAccount(accountNumber, ownerName, openingBalance);
        } else if (type.equals("2")) {
            BigDecimal overdraftLimit = readMoney(scanner, "Overdraft limit: ");
            account = bank.openCheckingAccount(accountNumber, ownerName, openingBalance, overdraftLimit);
        } else {
            throw new IllegalArgumentException("Account type must be 1 or 2.");
        }

        System.out.println("Opened " + account.getAccountType() + " account " + account.getAccountNumber() + ".");
    }

    private static void showAccounts(Bank bank) {
        if (bank.getAccounts().isEmpty()) {
            System.out.println("No accounts yet.");
            return;
        }

        for (Account account : bank.getAccounts()) {
            printAccount(account);
        }
    }

    private static void deposit(Scanner scanner, Bank bank) {
        Account account = bank.findAccount(readRequired(scanner, "Account number: "));
        account.deposit(readMoney(scanner, "Amount to deposit: "));
        System.out.println("Deposit complete. New balance: " + account.getBalance().toPlainString());
    }

    private static void withdraw(Scanner scanner, Bank bank) {
        Account account = bank.findAccount(readRequired(scanner, "Account number: "));
        account.withdraw(readMoney(scanner, "Amount to withdraw: "));
        System.out.println("Withdrawal complete. New balance: " + account.getBalance().toPlainString());
    }

    private static void transfer(Scanner scanner, Bank bank) {
        String fromAccount = readRequired(scanner, "From account number: ");
        String toAccount = readRequired(scanner, "To account number: ");
        BigDecimal amount = readMoney(scanner, "Amount to transfer: ");
        bank.transfer(fromAccount, toAccount, amount);
        System.out.println("Transfer complete.");
    }

    private static void showTransactions(Scanner scanner, Bank bank) {
        Account account = bank.findAccount(readRequired(scanner, "Account number: "));

        if (account.getTransactions().isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Transaction transaction : account.getTransactions()) {
            System.out.println(transaction.getTimestamp().format(formatter)
                    + " | " + transaction.getType()
                    + " | amount " + transaction.getAmount().toPlainString()
                    + " | balance " + transaction.getBalanceAfter().toPlainString());
        }
    }

    private static void printAccount(Account account) {
        System.out.println(account.getAccountType() + " | " + account.getAccountNumber()
                + " | " + account.getOwnerName()
                + " | balance " + account.getBalance().toPlainString());
    }

    private static String readRequired(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("This value cannot be empty.");
        }
        return value;
    }

    private static BigDecimal readMoney(Scanner scanner, String prompt) {
        try {
            return new BigDecimal(readRequired(scanner, prompt));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Enter a valid amount, for example 25.00.");
        }
    }
}
