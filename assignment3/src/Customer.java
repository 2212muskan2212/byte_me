import java.util.*;

public class Customer {
    private String username;
    private String password;
    private ArrayList<CartItem> cart = new ArrayList<>();
    private ArrayList<Order> orderHistory = new ArrayList<>();

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void browseMenu(ArrayList<FoodItem> menu) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Browse Menu ---");
            System.out.println("1. View all items");
            System.out.println("2. Search by name");
            System.out.println("3. Filter by category");
            System.out.println("4. Sort by price");
            System.out.println("5. Exit to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewAllItems(menu);
                case 2 -> searchByName(menu, scanner);
                case 3 -> filterByCategory(menu, scanner);
                case 4 -> sortByPrice(menu);
                case 5 -> { return; }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void viewAllItems(ArrayList<FoodItem> menu) {
        System.out.println("\n--- All Menu Items ---");
        for (FoodItem item : menu) {
            System.out.println(item);
        }
    }

    private void searchByName(ArrayList<FoodItem> menu, Scanner scanner) {
        System.out.print("Enter the name of the item: ");
        String name = scanner.nextLine();
        boolean found = false;
        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(name)) {
                System.out.println("Found: " + item);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items found with the name \"" + name + "\".");
        }
    }

    private void filterByCategory(ArrayList<FoodItem> menu, Scanner scanner) {
        System.out.print("Enter category to filter: ");
        String category = scanner.nextLine();
        System.out.println("\n--- Filtered Items ---");
        boolean found = false;
        for (FoodItem item : menu) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                System.out.println(item);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items found in the category \"" + category + "\".");
        }
    }

    private void sortByPrice(ArrayList<FoodItem> menu) {
        TreeMap<Double, List<FoodItem>> priceMap = new TreeMap<>();

        for (FoodItem item : menu) {
            priceMap.computeIfAbsent(item.getPrice(), k -> new ArrayList<>()).add(item);
        }

        System.out.println("\n--- Sorted by Price ---");
        for (Map.Entry<Double, List<FoodItem>> entry : priceMap.entrySet()) {
            for (FoodItem item : entry.getValue()) {
                System.out.println(item);
            }
        }
    }

    //Cart operations
    public void manageCart(ArrayList<FoodItem> menu) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Cart Operations ---");
            System.out.println("1. Add items");
            System.out.println("2. Modify quantities");
            System.out.println("3. Remove items");
            System.out.println("4. View total");
            System.out.println("5. Checkout");
            System.out.println("6. Exit to main menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addItemToCart(menu, scanner);
                case 2 -> modifyItemQuantity(scanner);
                case 3 -> removeItemFromCart(scanner);
                case 4 -> viewCartTotal();
                case 5 -> checkout();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void addItemToCart(ArrayList<FoodItem> menu, Scanner scanner) {
        System.out.println("\n--- Add Item to Cart ---");
        System.out.print("Enter the name of the item: ");
        String itemName = scanner.nextLine();
        FoodItem selectedItem = null;

        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                selectedItem = item;
                break;
            }
        }

        if (selectedItem != null) {
            if (!selectedItem.isAvailable()) {
                System.out.println("Sorry, " + itemName + " is currently not available.");
                return;
            }

            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            final FoodItem finalSelectedItem = selectedItem;

            CartItem existingItem = cart.stream()
                    .filter(ci -> ci.getFoodItem().equals(finalSelectedItem))
                    .findFirst().orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                cart.add(new CartItem(selectedItem, quantity));
            }
            System.out.println(quantity + " " + itemName + "(s) added to cart.");
        } else {
            System.out.println("Item not found in menu.");
        }
    }


    private void modifyItemQuantity(Scanner scanner) {
        System.out.println("\n--- Modify Item Quantity ---");
        System.out.print("Enter the name of the item: ");
        String itemName = scanner.nextLine();

        for (CartItem cartItem : cart) {
            if (cartItem.getFoodItem().getName().equalsIgnoreCase(itemName)) {
                System.out.print("Enter new quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();

                if (quantity > 0) {
                    cartItem.setQuantity(quantity);
                    System.out.println("Quantity updated to " + quantity);
                } else {
                    cart.remove(cartItem);
                    System.out.println("Item removed from cart.");
                }
                return;
            }
        }
        System.out.println("Item not found in cart.");
    }

    private void removeItemFromCart(Scanner scanner) {
        System.out.println("\n--- Remove Item from Cart ---");
        System.out.print("Enter the name of the item: ");
        String itemName = scanner.nextLine();

        cart.removeIf(cartItem -> cartItem.getFoodItem().getName().equalsIgnoreCase(itemName));
        System.out.println("Item removed from cart, if it existed.");
    }

    private void viewCartTotal() {
        double total = cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
        System.out.println("\n--- Cart Total ---");
        for (CartItem item : cart) {
            System.out.println(item);
        }
        System.out.println("Total: Rs." + total);
    }

    private void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Add items before checking out.");
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter any special requests (e.g., 'extra spicy', 'no onions'): ");
            String specialRequest = scanner.nextLine();

            Order order = new Order(new ArrayList<>(cart), specialRequest); // Pass specialRequest to Order
            orderHistory.add(order);
            viewCartTotal();
            Main.addOrder(order);
            System.out.println("Proceeding to checkout. Thank you for your order!");
            cart.clear();
        }
    }


    //order tracking
    public void orderTracking(ArrayList<FoodItem> menu) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Order Tracking ---");
            System.out.println("1. View Order Status");
            System.out.println("2. Cancel Order");
            System.out.println("3. View Order History");
            System.out.println("4. Exit to Cart Operations");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewOrderStatus();
                case 2 -> cancelOrder(scanner);
                case 3 -> viewOrderHistory();
                case 4 -> { return; }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void viewOrderStatus() {
        if (orderHistory.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            for (Order order : orderHistory) {
                System.out.println(order);
            }
        }
    }

    private void cancelOrder(Scanner scanner) {
        System.out.print("Enter order index to cancel (starting from 0): ");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index >= 0 && index < orderHistory.size()) {
            Order order = orderHistory.get(index);
            order.cancelOrder();
            System.out.println("Order canceled: " + order);
        } else {
            System.out.println("Invalid order index.");
        }
    }

    private void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("No order history found.");
        } else {
            for (int i = 0; i < orderHistory.size(); i++) {
                System.out.println(i + ": " + orderHistory.get(i));
            }
        }
    }

    //Item reviews
    public void provideReview(ArrayList<FoodItem> menu, Scanner scanner) {
        System.out.print("Enter the name of the item to review: ");
        String itemName = scanner.nextLine();
        FoodItem selectedItem = null;

        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                selectedItem = item;
                break;
            }
        }

        if (selectedItem != null) {
            System.out.print("Enter your rating (1-5): ");
            int rating = scanner.nextInt();
            scanner.nextLine();

            if (rating < 1 || rating > 5) {
                System.out.println("Invalid rating. Please enter a rating between 1 and 5.");
                return;
            }

            System.out.print("Enter your comment: ");
            String comment = scanner.nextLine();

            Review review = new Review(this.username, comment, rating);
            selectedItem.addReview(review);
            System.out.println("Thank you for your review!");
        } else {
            System.out.println("Item not found in the menu.");
        }
    }

    public void viewReviews(ArrayList<FoodItem> menu, Scanner scanner) {
        System.out.print("Enter the name of the item to view reviews: ");
        String itemName = scanner.nextLine();
        FoodItem selectedItem = null;

        for (FoodItem item : menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                selectedItem = item;
                break;
            }
        }

        if (selectedItem != null) {
            ArrayList<Review> reviews = selectedItem.getReviews();
            if (reviews.isEmpty()) {
                System.out.println("No reviews available for " + itemName);
            } else {
                System.out.println("\n--- Reviews for " + itemName + " ---");
                for (Review review : reviews) {
                    System.out.println(review);
                }
            }
        } else {
            System.out.println("Item not found in the menu.");
        }
    }

}
