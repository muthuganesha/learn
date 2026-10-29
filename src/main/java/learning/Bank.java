package learning;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    public SavingsAccount openSavingsAccount(String accountNumber, String ownerName, BigDecimal openingBalance) {
        ensureAccountNumberAvailable(accountNumber);
        SavingsAccount account = new SavingsAccount(accountNumber, ownerName, openingBalance);
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    public CheckingAccount openCheckingAccount(String accountNumber, String ownerName, BigDecimal openingBalance,
            BigDecimal overdraftLimit) {
        ensureAccountNumberAvailable(accountNumber);
        CheckingAccount account = new CheckingAccount(accountNumber, ownerName, openingBalance, overdraftLimit);
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    public Account findAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account;
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Choose two different accounts for a transfer.");
        }

        Account source = findAccount(fromAccountNumber);
        Account destination = findAccount(toAccountNumber);
        source.withdraw(amount);
        destination.deposit(amount);
    }

    private void ensureAccountNumberAvailable(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required.");
        }
        if (accounts.containsKey(accountNumber.trim())) {
            throw new IllegalArgumentException("Account number already exists.");
        }
    }
}