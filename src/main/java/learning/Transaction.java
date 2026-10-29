package learning;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private final String type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;
    private final LocalDateTime timestamp;

    public Transaction(String type, BigDecimal amount, BigDecimal balanceAfter, LocalDateTime timestamp) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}