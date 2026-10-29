package learning;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class BankAccount implements Account {
    private final String accountNumber;
    private final String ownerName;
    private final List<Transaction> transactions = new ArrayList<>();
    private BigDecimal balance;

    protected BankAccount(String accountNumber, String ownerName, BigDecimal openingBalance) {
        this.accountNumber = requireText(accountNumber, "Account number");
        this.ownerName = requireText(ownerName, "Owner name");
        this.balance = normalizeMoney(openingBalance);
        if (balance.signum() < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }
        if (balance.signum() > 0) {
            recordTransaction("OPENING", balance);
        }
    }

    @Override
    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void deposit(BigDecimal amount) {
        BigDecimal validAmount = requirePositiveAmount(amount);
        balance = balance.add(validAmount);
        recordTransaction("DEPOSIT", validAmount);
    }

    @Override
    public void withdraw(BigDecimal amount) {
        BigDecimal validAmount = requirePositiveAmount(amount);
        if (!canWithdraw(validAmount)) {
            throw new IllegalStateException("Insufficient funds.");
        }
        balance = balance.subtract(validAmount);
        recordTransaction("WITHDRAWAL", validAmount);
    }

    @Override
    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    protected boolean canWithdraw(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }

    protected final BigDecimal normalizeMoney(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required.");
        }
        try {
            return amount.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("Amounts can have at most two decimal places.");
        }
    }

    private BigDecimal requirePositiveAmount(BigDecimal amount) {
        BigDecimal normalizedAmount = normalizeMoney(amount);
        if (normalizedAmount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        return normalizedAmount;
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    private void recordTransaction(String type, BigDecimal amount) {
        transactions.add(new Transaction(type, amount, balance, LocalDateTime.now()));
    }
}