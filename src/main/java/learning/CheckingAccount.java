package learning;

import java.math.BigDecimal;

public class CheckingAccount extends BankAccount {
    private final BigDecimal overdraftLimit;

    public CheckingAccount(String accountNumber, String ownerName, BigDecimal openingBalance,
            BigDecimal overdraftLimit) {
        super(accountNumber, ownerName, openingBalance);
        this.overdraftLimit = normalizeMoney(overdraftLimit);
        if (this.overdraftLimit.signum() < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        }
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }

    @Override
    protected boolean canWithdraw(BigDecimal amount) {
        return getBalance().add(overdraftLimit).compareTo(amount) >= 0;
    }
}