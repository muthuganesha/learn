package learning;

import java.math.BigDecimal;

public final class BankTests {
    private static int passedTests;

    public static void main(String[] args) {
        runTest("deposit and withdrawal update balance", BankTests::depositAndWithdrawUpdateBalance);
        runTest("negative deposits are rejected", BankTests::negativeDepositIsRejected);
        runTest("savings account prevents overdraft", BankTests::savingsAccountPreventsOverdraft);
        runTest("checking account respects overdraft limit", BankTests::checkingAccountRespectsOverdraftLimit);
        runTest("transfer updates both accounts", BankTests::transferUpdatesBothAccounts);
        runTest("duplicate account numbers are rejected", BankTests::duplicateAccountNumberIsRejected);
        System.out.println("All " + passedTests + " tests passed.");
    }

    private static void runTest(String testName, Runnable test) {
        try {
            test.run();
            passedTests++;
            System.out.println("PASS: " + testName);
        } catch (AssertionError | RuntimeException exception) {
            System.out.println("FAIL: " + testName + " - " + exception.getMessage());
            throw exception;
        }
    }

    private static void depositAndWithdrawUpdateBalance() {
        SavingsAccount account = new SavingsAccount("S-100", "Alice", new BigDecimal("100.00"));
        account.deposit(new BigDecimal("25.00"));
        account.withdraw(new BigDecimal("10.00"));
        assertAmount("115.00", account.getBalance());
    }

    private static void negativeDepositIsRejected() {
        SavingsAccount account = new SavingsAccount("S-101", "Bob", new BigDecimal("10.00"));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(new BigDecimal("-1.00")));
        assertAmount("10.00", account.getBalance());
    }

    private static void savingsAccountPreventsOverdraft() {
        SavingsAccount account = new SavingsAccount("S-102", "Casey", new BigDecimal("20.00"));
        assertThrows(IllegalStateException.class, () -> account.withdraw(new BigDecimal("21.00")));
        assertAmount("20.00", account.getBalance());
    }

    private static void checkingAccountRespectsOverdraftLimit() {
        CheckingAccount account = new CheckingAccount("C-100", "Drew", new BigDecimal("50.00"),
                new BigDecimal("25.00"));
        account.withdraw(new BigDecimal("60.00"));
        assertAmount("-10.00", account.getBalance());
        assertThrows(IllegalStateException.class, () -> account.withdraw(new BigDecimal("16.00")));
        assertAmount("-10.00", account.getBalance());
    }

    private static void transferUpdatesBothAccounts() {
        Bank bank = new Bank();
        bank.openSavingsAccount("S-103", "Elliot", new BigDecimal("100.00"));
        bank.openSavingsAccount("S-104", "Frankie", new BigDecimal("25.00"));
        bank.transfer("S-103", "S-104", new BigDecimal("40.00"));
        assertAmount("60.00", bank.findAccount("S-103").getBalance());
        assertAmount("65.00", bank.findAccount("S-104").getBalance());
    }

    private static void duplicateAccountNumberIsRejected() {
        Bank bank = new Bank();
        bank.openSavingsAccount("S-105", "Gray", BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class,
                () -> bank.openSavingsAccount("S-105", "Harper", BigDecimal.ZERO));
    }

    private static void assertAmount(String expected, BigDecimal actual) {
        if (new BigDecimal(expected).compareTo(actual) != 0) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private static void assertThrows(Class<? extends RuntimeException> expectedType, Runnable action) {
        try {
            action.run();
        } catch (RuntimeException exception) {
            if (expectedType.isInstance(exception)) {
                return;
            }
            throw new AssertionError("Expected " + expectedType.getSimpleName()
                    + " but got " + exception.getClass().getSimpleName(), exception);
        }
        throw new AssertionError("Expected " + expectedType.getSimpleName() + " to be thrown");
    }
}
