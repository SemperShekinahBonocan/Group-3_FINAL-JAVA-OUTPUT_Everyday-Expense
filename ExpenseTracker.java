import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Expense {
    private double amount;
    private String type;
    private String category;
    private LocalDate date;

    public Expense(double amount, String type, String category, LocalDate date) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return String.format("%s - %.2f (%s) on %s", category, amount, type, date);
    }
}

public class ExpenseTracker {
    private static List<Expense> expenses = new ArrayList<>();
    private static double savings = 0;
    private static Scanner scanner = new Scanner(System.in);

    private static final List<String> PAYMENT_TYPES = Arrays.asList(
        "Cash", "Check", "Wire Transfer", "Credit Card", "Debit Card", "E-Wallet"
    );

    private static final List<String> EXPENSE_CATEGORIES = Arrays.asList(
        "Housing", "Utilities", "Transportation", "Food", "Healthcare", 
        "Insurance", "Gas", "Miscellaneous"
    );

    public static void main(String[] args) {
        System.out.println("Welcome to Everyday Expense. Please input the number of your choice.");
        while (true) {
            System.out.println("\n1. Add Expense");
            System.out.println("2. Update Savings");
            System.out.println("3. View Statistics");
            System.out.println("4. Display Expenses");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: 
                    addExpense();
                    break;
                case 2: 
                    updateSavings();
                    break;
                case 3: 
                    displayStats();
                    break;
                case 4: 
                    displayExpenses();
                    break;
                case 5: 
                    System.out.println("Exiting...");
                    System.out.println("Thank you for choosing Everyday Expense.");
                    return;
                default: 
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addExpense() {
        String type = selectFromList("payment type", PAYMENT_TYPES);
        String category = selectFromList("category", EXPENSE_CATEGORIES);

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        Expense expense = new Expense(amount, type, category, LocalDate.now());
        expenses.add(expense);
        System.out.println("Expense added successfully.");
    }

    private static String selectFromList(String itemName, List<String> options) {
        System.out.println("Select " + itemName + ":");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
        
        int selection;
        do {
            System.out.print("Enter the number of your choice: ");
            selection = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } while (selection < 1 || selection > options.size());

        return options.get(selection - 1);
    }

    private static void updateSavings() {
        System.out.print("Enter new savings amount: ");
        savings = scanner.nextDouble();
        System.out.println("Savings updated successfully.");
    }

    private static void displayStats() {
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        LocalDate today = LocalDate.now();
        double dailyExpenses = expenses.stream()
                .filter(e -> e.getDate().equals(today))
                .mapToDouble(Expense::getAmount)
                .sum();
        double monthlyExpenses = expenses.stream()
                .filter(e -> e.getDate().getMonth() == today.getMonth() && e.getDate().getYear() == today.getYear())
                .mapToDouble(Expense::getAmount)
                .sum();

        System.out.println("\nStatistics:");
        System.out.printf("Total Expenses: %.2f\n", totalExpenses);
        System.out.printf("Daily Expenses: %.2f\n", dailyExpenses);
        System.out.printf("Monthly Expenses: %.2f\n", monthlyExpenses);
        System.out.printf("Current Savings: %.2f\n", savings);
    }

    private static void displayExpenses() {
        System.out.println("\nExpense List:");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = expenses.size() - 1; i >= 0; i--) {
            Expense expense = expenses.get(i);
            System.out.printf("%s - %.2f (%s) on %s\n", 
                expense.getCategory(), expense.getAmount(), expense.getType(), 
                expense.getDate().format(formatter));
        }
    }
}