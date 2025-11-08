import java.util.*;

class User {
    private String userId;
    private String pin;
    private double balance;
    private List<String> transactionHistory;

    public User(String userId, String pin, double balance) {
        this.userId = userId.toLowerCase().trim();
        this.pin = pin.trim();
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() { return userId; }
    public String getPin() { return pin; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(String.format("Deposited ₹%.2f", amount));
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.add(String.format("Withdrawn ₹%.2f", amount));
            return true;
        }
        return false;
    }

    public boolean transfer(User receiver, double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            receiver.balance += amount;
            transactionHistory.add(String.format("Transferred ₹%.2f to %s", amount, receiver.getUserId()));
            receiver.transactionHistory.add(String.format("Received ₹%.2f from %s", amount, userId));
            return true;
        }
        return false;
    }

    public void showTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("\nNo transactions yet.");
        } else {
            System.out.println("\n--- Transaction History ---");
            for (String t : transactionHistory) {
                System.out.println(t);
            }
        }
    }
}

class ATM {
    private Scanner scanner;
    private User currentUser;
    private Map<String, User> users;

    public ATM() {
        scanner = new Scanner(System.in);
        users = new HashMap<>();
    }

    public void start() {
        System.out.println("===== Welcome to ATM Interface =====");
        while (true) {
            System.out.println("\n1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createAccount();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Thank you for visiting!");
                    return;
                }
                default -> System.out.println("Invalid option! Please choose 1, 2, or 3.");
            }
        }
    }

    private void createAccount() {
        System.out.print("Enter desired User ID: ");
        String userId = scanner.nextLine().trim().toLowerCase();

        if (users.containsKey(userId)) {
            System.out.println("This User ID already exists. Please try a different one.");
            return;
        }

        System.out.print("Set a 4-digit PIN: ");
        String pin = scanner.nextLine().trim();

        if (pin.length() != 4 || !pin.matches("\\d{4}")) {
            System.out.println("Invalid PIN! Please enter exactly 4 digits.");
            return;
        }

        System.out.print("Enter initial deposit amount: ");
        double balance = 0;
        try {
            balance = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Account not created.");
            return;
        }

        User newUser = new User(userId, pin, balance);
        users.put(userId, newUser);
        System.out.println("Account created successfully! You can now log in using your credentials.");
    }

    private void login() {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine().trim().toLowerCase();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine().trim();

        if (users.containsKey(userId)) {
            User user = users.get(userId);
            if (user.getPin().equals(pin)) {
                currentUser = user;
                System.out.println("Login successful. Welcome, " + currentUser.getUserId() + "!");
                showMenu();
                return;
            }
        }
        System.out.println("Invalid credentials. Please try again.");
    }

    private void showMenu() {
        while (true) {
            System.out.println("\n===== ATM Menu =====");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Check Balance");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> currentUser.showTransactionHistory();
                case "2" -> withdraw();
                case "3" -> deposit();
                case "4" -> transfer();
                case "5" -> System.out.printf("Your current balance: ₹%.2f%n", currentUser.getBalance());
                case "6" -> {
                    System.out.println("Logging out...");
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private void withdraw() {
        System.out.print("Enter amount to withdraw: ₹");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (currentUser.withdraw(amount)) {
                System.out.printf("Withdrawal successful! New balance: ₹%.2f%n", currentUser.getBalance());
            } else {
                System.out.println("Insufficient balance or invalid amount.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    private void deposit() {
        System.out.print("Enter amount to deposit: ₹");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            currentUser.deposit(amount);
            System.out.printf("Deposit successful! New balance: ₹%.2f%n", currentUser.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    private void transfer() {
        System.out.print("Enter receiver User ID: ");
        String receiverId = scanner.nextLine().trim().toLowerCase();

        if (!users.containsKey(receiverId)) {
            System.out.println("Receiver not found!");
            return;
        }

        System.out.print("Enter amount to transfer: ₹");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            User receiver = users.get(receiverId);

            if (currentUser.transfer(receiver, amount)) {
                System.out.printf("Transfer successful! New balance: ₹%.2f%n", currentUser.getBalance());
            } else {
                System.out.println("Transfer failed. Insufficient balance or invalid amount.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }
}

public class ATMInterface {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}
