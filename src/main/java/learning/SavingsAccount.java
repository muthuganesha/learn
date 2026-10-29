package learning;

import java.math.BigDecimal;

public class SavingsAccount extends BankAccount {
    public SavingsAccount(String accountNumber, String ownerName, BigDecimal openingBalance) {
        super(accountNumber, ownerName, openingBalance);
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }
}