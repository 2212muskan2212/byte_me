import java.io.*;
import java.util.ArrayList;

public class MenuFileManager {
    private static final String MENU_FILE = "src/menu.txt";

    // Write menu to file after CLI operations
    public static void writeMenuToFile(ArrayList<FoodItem> menu) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MENU_FILE))) {
            for (FoodItem item : menu) {
                writer.println(item.getName() + "," +
                        item.getCategory() + "," +
                        item.getPrice() + "," +
                        item.isAvailable());
            }
        } catch (IOException e) {
            System.out.println("Error writing menu to file: " + e.getMessage());
        }
    }

    // Read menu from file for GUI
    public static ArrayList<FoodItem> readMenuFromFile() {
        ArrayList<FoodItem> menu = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    FoodItem item = new FoodItem(parts[0], parts[1], (int) Double.parseDouble(parts[2]));
                    item.setAvailable(Boolean.parseBoolean(parts[3]));
                    menu.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading menu from file: " + e.getMessage());
        }
        return menu;
    }
}