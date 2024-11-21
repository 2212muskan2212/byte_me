import java.util.ArrayList;
import java.util.Scanner;

public class Admin {
    private String username;
    private String password;;


    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void manageMenu(ArrayList<FoodItem> menu, ArrayList<Order> orders) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Menu Management ---");
            System.out.println("1. Add new items");
            System.out.println("2. Update existing items");
            System.out.println("3. Remove items");
            System.out.println("4. Modify prices");
            System.out.println("5. Update availability");
            System.out.println("6. View all items");
            System.out.println("7. Exit to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> addItem(menu, scanner);
                case 2 -> updateItem(menu, scanner);
                case 3 -> removeItem(menu, orders, scanner);
                case 4 -> modifyPrice(menu, scanner);
                case 5 -> updateAvailability(menu, scanner);
                case 6 -> viewAllItems(menu);
                case 7 -> { return; }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void addItem(ArrayList<FoodItem> menu, Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter the name of the new item: ");
        String name = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter price: ");
        int price = scanner.nextInt();
        scanner.nextLine();

        menu.add(new FoodItem(name, category, price));
        MenuFileManager.writeMenuToFile(menu);
        System.out.println("Item added successfully!");
    }

    private void updateItem(ArrayList<FoodItem> menu, Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter the name of the item to update: ");
        String itemName = scanner.nextLine();
        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                System.out.print("Enter new name (leave blank to keep current): ");
                String newName = scanner.nextLine();
                if (!newName.isEmpty()) {
                    item.setName(newName);
                }

                System.out.print("Enter new category (leave blank to keep current): ");
                String newCategory = scanner.nextLine();
                if (!newCategory.isEmpty()) {
                    item.setCategory(newCategory);
                }
                System.out.print("Enter new price (enter -1 to keep current): ");
                int newPrice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (newPrice >= 0) {
                    item.setPrice(newPrice);
                }
                MenuFileManager.writeMenuToFile(menu);
                System.out.println("Item updated successfully!");
                return;
            }
        }
        System.out.println("Item not found in menu");
    }

    private void removeItem(ArrayList<FoodItem> menu, ArrayList<Order> orders, Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter the name of the item to remove: ");
        String itemName = scanner.nextLine();

        FoodItem itemToRemove = null;
        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove != null) {
            menu.remove(itemToRemove);
            MenuFileManager.writeMenuToFile(menu);
            System.out.println("Item removed successfully!");

            // Update any pending orders to denied
            for (Order order : orders) {
                if (order.getStatus().equalsIgnoreCase("Pending")) {
                    boolean itemFoundInOrder = false;

                    for (CartItem cartItem : order.getItems()) {
                        if (cartItem.getFoodItem().getName().equalsIgnoreCase(itemName)) {
                            itemFoundInOrder = true;
                            break;
                        }
                    }

                    if (itemFoundInOrder) {
                        MenuFileManager.writeMenuToFile(menu);
                        order.setStatus("Denied");
                    }
                }
            }
        } else {
            System.out.println("Item not found in menu.");
        }
    }


    private void modifyPrice(ArrayList<FoodItem> menu, Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter the name of the item to modify price: ");
        String itemName = scanner.nextLine();
        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                System.out.print("Enter new price: ");
                int newPrice = scanner.nextInt();
                scanner.nextLine();
                item.setPrice(newPrice);
                MenuFileManager.writeMenuToFile(menu);
                System.out.println("Price updated successfully!");
                return;
            }
        }
        System.out.println("Item not found in menu.");
    }

    private void updateAvailability(ArrayList<FoodItem> menu, Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter the name of the item to update availability: ");
        String itemName = scanner.nextLine();
        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                System.out.print("Is the item available? (true/false): ");
                boolean available = scanner.nextBoolean();
                scanner.nextLine();
                item.setAvailable(available);
                MenuFileManager.writeMenuToFile(menu);
                System.out.println("Availability updated!!");
                return;
            }
        }
        System.out.println("Item not found in menu.");
    }

    private void viewAllItems(ArrayList<FoodItem> menu) {
        System.out.println("\n--- All Menu Items ---");
        for (FoodItem item : menu) {
            System.out.println(item);
        }
    }

    public void manageOrders(ArrayList<Order> orders, Scanner scanner) {
        while (true) {
            System.out.println("\n--- Order Management ---");
            System.out.println("1. View pending orders");
            System.out.println("2. Update order status");
            System.out.println("3. Process refunds");
            System.out.println("4. View all order (spl req.)");
            System.out.println("5. Exit to menu management");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewPendingOrders(orders);
                case 2 -> updateOrderStatus(orders, scanner);
                case 3 -> processRefunds(orders, scanner);
                case 4 -> viewAllOrders(orders);
                case 5 -> { return; }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void viewPendingOrders(ArrayList<Order> orders) {
        System.out.println("\n--- Pending Orders ---");
        for (Order order : orders) {
            if (order.getStatus().equalsIgnoreCase("Pending")) {
                System.out.println(order);
            }
        }
    }

    private void updateOrderStatus(ArrayList<Order> orders, Scanner scanner) {
        System.out.print("Enter the order index to update (starting from 0): ");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index >= 0 && index < orders.size()) {
            Order order = orders.get(index);
            System.out.print("Enter the new status for the order: ");
            String newStatus = scanner.nextLine();

            order.setStatus(newStatus);
            OrderFileManager.writeOrdersToFile(orders);
            System.out.println("Order status updated to " + newStatus + ".");

            if (newStatus.equalsIgnoreCase("Canceled")) {
                System.out.println("Processing refund for canceled order...");
                order.completeOrder();
                System.out.println("Refund processed.");
            }
        } else {
            System.out.println("Invalid order index.");
        }
    }


    private void processRefunds(ArrayList<Order> orders, Scanner scanner) {
        System.out.print("Enter the order index to process refund (starting from 0): ");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index >= 0 && index < orders.size()) {
            Order order = orders.get(index);
            if (order.getStatus().equalsIgnoreCase("Canceled")) {
                System.out.println("Refund processed for order: " + order);
                order.completeOrder();
            } else {
                System.out.println("This order cannot be refunded.");
            }
        } else {
            System.out.println("Invalid order index.");
        }
    }

    public void generateSalesReport(ArrayList<Order> orders) {
        System.out.println("\n--- Sales Report ---");
        double totalSales = 0.0;

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        for (Order order : orders) {
            for (CartItem item : order.getItems()) {
                System.out.println("  Item: " + item.getFoodItem().getName() + ", Quantity: " + item.getQuantity() + ", Total Price: Rs." + item.getTotalPrice());
                totalSales += item.getTotalPrice();
            }
        }

        System.out.printf("Total Sales: Rs.%.2f%n", totalSales);
    }

    private void viewAllOrders(ArrayList<Order> orders) {
        for (Order order : orders) {
            System.out.println(order);
        }
    }
}
