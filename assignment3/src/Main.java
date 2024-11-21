import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<FoodItem> menu = new ArrayList<>();
    private static ArrayList<Customer> customers = new ArrayList<>();
    private static ArrayList<Admin> admins = new ArrayList<>();
    private static ArrayList<Order> orders = new ArrayList<>();

    public static void addOrder(Order order) {
        orders.add(order);
        OrderFileManager.writeOrdersToFile(orders);
    }

    public static void main(String[] args) {
        initializeMenu();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Welcome ---");
            System.out.println("1. Customer Signup");
            System.out.println("2. Customer Login");
            System.out.println("3. Admin Signup");
            System.out.println("4. Admin Login");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> customerSignup(scanner);
                case 2 -> customerLogin(scanner);
                case 3 -> adminSignup(scanner);
                case 4 -> adminLogin(scanner);
                case 5 -> { System.out.println("Bye"); return; }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void initializeMenu() {
        menu.add(new FoodItem("Burger", "Snacks", 50));
        menu.add(new FoodItem("Roll", "Snacks", 80));
        menu.add(new FoodItem("Dal makhani", "North Indian", 180));
        menu.add(new FoodItem("Tea", "Beverage", 20));
        menu.add(new FoodItem("Coffee", "Beverage", 35));
    }

    private static void customerSignup(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        customers.add(new Customer(username, password));
        System.out.println("Customer signed up successfully, now you can login!");
    }

    private static void customerLogin(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (Customer customer : customers) {
            if (customer.login(username, password)) {
                System.out.println("Welcome, " + username + "!");
                while (true) {
                    System.out.println("\n--- Customer Menu ---");
                    System.out.println("1. Browse Menu");
                    System.out.println("2. Cart Operations");
                    System.out.println("3. Order tracking");
                    System.out.println("4. Provide Review");
                    System.out.println("5. View Reviews");
                    System.out.println("6. Logout");
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1 -> customer.browseMenu(menu);
                        case 2 -> customer.manageCart(menu);
                        case 3 -> customer.orderTracking(menu);
                        case 4 -> customer.provideReview(menu, scanner);
                        case 5 -> customer.viewReviews(menu, scanner);
                        case 6 -> { System.out.println("Logging out"); return; }
                        default -> System.out.println("Invalid choice, try again.");
                    }
                }
            }
        }
        System.out.println("Invalid credentials.");
    }

    private static void adminSignup(Scanner scanner) {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        admins.add(new Admin(username, password));
        System.out.println("Admin signed up successfully, now you can login!");
    }

    private static void adminLogin(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (Admin admin : admins) {
            if (admin.login(username, password)) {
                System.out.println("Welcome, " + username + "!");
                while (true) {
                    System.out.println("\n--- Admin Menu ---");
                    System.out.println("1. Menu Management");
                    System.out.println("2. Order Management");
                    System.out.println("3. Report Generation");
                    System.out.println("4. Logout");
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1 -> admin.manageMenu(menu, orders);
                        case 2 -> admin.manageOrders(orders, scanner);
                        case 3 -> admin.generateSalesReport(orders);
                        case 4 -> {
                            System.out.println("Logging out...");
                            return;
                        }
                        default -> System.out.println("Invalid choice, try again.");
                    }
                }
            }
        }
        System.out.println("Invalid login credentials.");
    }
}
