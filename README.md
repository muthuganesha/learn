# Java Banking Project

A small, in-memory console banking application for practicing Java classes, interfaces, inheritance, collections, and validation. It requires JDK 17 or newer and uses no external libraries.

## Run in Windows PowerShell

From the project folder:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$javaFiles = Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $javaFiles
java -cp out learning.Main
```

In VS Code, open `src/main/java/learning/Main.java` and use the Run or Debug action above `main`.

## Run test cases

Compile all production and test classes, then run the test console:

```powershell
$javaFiles = Get-ChildItem -Path .\src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $javaFiles
java -cp out learning.BankTests
```

Successful checks print `PASS` lines followed by `All 6 tests passed.` A failed check prints `FAIL` and stops at the first failure. In VS Code, choose **Run Banking Tests** from the Run and Debug menu.

## Interview practice loop

1. Add a class or interface under `src/main/java/learning`.
2. Add a focused test method in `src/test/java/learning/BankTests.java` and call it from `main`.
3. Run the test command above and use the failure output to debug your implementation.
4. Run `learning.Main` to try the behavior through the interactive banking menu.

## Features

- Open savings and checking accounts.
- List accounts and balances.
- Deposit, withdraw, and transfer money.
- View each account's transaction history.
- Reject invalid amounts, duplicate account numbers, unknown accounts, and overdrafts beyond a checking account's limit.

All data stays in memory and is cleared when the program exits. This is a learning project, not real banking software.

## How the design works

- `Account` is an interface describing what every account can do.
- `BankAccount` is an abstract class that implements shared account state and deposit/withdraw rules.
- `SavingsAccount` and `CheckingAccount` extend `BankAccount`; checking accounts override the withdrawal rule to support a configured overdraft limit.
- `Bank` stores accounts in a `Map` and coordinates lookups and transfers.
- `Transaction` is an immutable class used for account history.
- `Main` handles console input and calls the banking classes.
- `BigDecimal` represents money without floating-point rounding errors.

## Interview practice

1. Add a method that calculates monthly interest for a savings account.
2. Add an account search by owner name.
3. Explain why `Bank` stores the `Account` interface rather than a specific account class.
4. Add unit tests for invalid amounts, insufficient funds, and transfers between two accounts.
5. Discuss what should happen if a transfer fails after the withdrawal, and how to make the operation atomic.
