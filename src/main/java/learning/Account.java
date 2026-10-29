package learning;

import java.math.BigDecimal;
import java.util.List;

public interface Account {
    String getAccountNumber();

    

    String getOwnerName();

    String getAccountType();

    BigDecimal getBalance();

    void deposit(BigDecimal amount);

    void withdraw(BigDecimal amount);

    List<Transaction> getTransactions();
}