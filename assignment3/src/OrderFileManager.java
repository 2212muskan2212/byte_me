import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderFileManager {
    private static final String ORDER_FILE_PATH = "src/orders.txt";

    public static void writeOrdersToFile(ArrayList<Order> orders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE_PATH))) {
            for (Order order : orders) {
                // Write order details with delimiter
                writer.write(serializeOrder(order));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing orders to file: " + e.getMessage());
        }
    }

    public static ArrayList<Order> readOrdersFromFile(ArrayList<FoodItem> menu) {
        ArrayList<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Order order = deserializeOrder(line, menu);
                if (order != null) {
                    orders.add(order);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading orders from file: " + e.getMessage());
        }
        return orders;
    }

    private static String serializeOrder(Order order) {
        StringBuilder sb = new StringBuilder();
        // Serialize order details
        sb.append(order.getOrderTime()).append("|");
        sb.append(order.getStatus()).append("|");
        sb.append(order.getSpecialRequest()).append("|");

        // Serialize cart items
        for (CartItem cartItem : order.getItems()) {
            sb.append(cartItem.getFoodItem().getName()).append(",")
                    .append(cartItem.getQuantity()).append(";");
        }

        return sb.toString();
    }

    private static Order deserializeOrder(String line, ArrayList<FoodItem> menu) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) return null;

        LocalDateTime orderTime = LocalDateTime.parse(parts[0]);
        String status = parts[1];
        String specialRequest = parts[2];

        // Create a new order with deserialized details
        ArrayList<CartItem> cartItems = new ArrayList<>();
        String[] itemParts = parts[3].split(";");
        for (String itemDetail : itemParts) {
            String[] itemInfo = itemDetail.split(",");
            if (itemInfo.length == 2) {
                // Find corresponding food item from menu
                FoodItem foodItem = menu.stream()
                        .filter(f -> f.getName().equals(itemInfo[0]))
                        .findFirst()
                        .orElse(null);

                if (foodItem != null) {
                    CartItem cartItem = new CartItem(foodItem, Integer.parseInt(itemInfo[1]));
                    cartItems.add(cartItem);
                }
            }
        }

        Order order = new Order(cartItems, specialRequest);
        order.setStatus(status);
        return order;
    }
}