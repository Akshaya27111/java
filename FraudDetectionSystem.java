 import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FraudDetectionSystem {

    // Data structure to store transaction history
    static class Transaction {
        String accountNumber;
        double amount;
        String date;
        String type; // Deposit or Withdrawal

        public Transaction(String accountNumber, double amount, String date, String type) {
            this.accountNumber = accountNumber;
            this.amount = amount;
            this.date = date;
            this.type = type;
        }

        @Override
        public String toString() {
            return String.format("Account: %s, Amount: %.2f, Date: %s, Type: %s", 
                                 accountNumber, amount, date, type);
        }
    }

    // List of transactions
    private static List<Transaction> transactions = new ArrayList<>();
    private static Set<String> flaggedAccounts = new HashSet<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Fraud Detection System =====");
            System.out.println("1. Add Transaction");
            System.out.println("2. Check for Fraud");
            System.out.println("3. View Flagged Accounts");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTransaction(scanner);
                    break;
                case 2:
                    detectFraud();
                    break;
                case 3:
                    viewFlaggedAccounts();
                    break;
                case 4:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void addTransaction(Scanner scanner) {
        System.out.println("\n===== Add Transaction =====");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter Transaction Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Transaction Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter Transaction Type (Deposit/Withdrawal): ");
        String type = scanner.nextLine();

        Transaction transaction = new Transaction(accountNumber, amount, date, type);
        transactions.add(transaction);
        System.out.println("Transaction added successfully.");
    }

    private static void detectFraud() {
        System.out.println("\n===== Fraud Detection =====");

        // Define fraud detection thresholds
        double largeTransactionThreshold = 10_000.0; // Large amount threshold
        int frequentTransactionLimit = 5;           // Frequent transactions per day limit

        // Map to count transactions per account per day
        Map<String, Map<String, Integer>> transactionFrequency = new HashMap<>();

        for (Transaction transaction : transactions) {
            // Check for large transactions
            if (transaction.amount > largeTransactionThreshold) {
                System.out.println("Fraud Alert: Large transaction detected!");
                System.out.println(transaction);
                flaggedAccounts.add(transaction.accountNumber);
            }

            // Check for frequent transactions on the same day
            transactionFrequency.putIfAbsent(transaction.accountNumber, new HashMap<>());
            Map<String, Integer> dailyTransactions = transactionFrequency.get(transaction.accountNumber);
            dailyTransactions.put(transaction.date, dailyTransactions.getOrDefault(transaction.date, 0) + 1);

            if (dailyTransactions.get(transaction.date) > frequentTransactionLimit) {
                System.out.println("Fraud Alert: Frequent transactions detected!");
                System.out.println(transaction);
                flaggedAccounts.add(transaction.accountNumber);
            }
        }

        if (flaggedAccounts.isEmpty()) {
            System.out.println("No fraudulent activities detected.");
        }
    }

    private static void viewFlaggedAccounts() {
        System.out.println("\n===== Flagged Accounts =====");
        if (flaggedAccounts.isEmpty()) {
            System.out.println("No accounts flagged for fraud.");
        } else {
            for (String account : flaggedAccounts) {
                System.out.println("Flagged Account: " + account);
            }
        }
    }
}
