import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class PendingOrdersGUI extends JFrame {
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public PendingOrdersGUI() {
        // Frame setup
        setTitle("Byte Me Canteen - Pending Orders");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Pending Orders heading
        JLabel headingLabel = createHeadingLabel();

        // Create table model
        String[] columnNames = {
                "Order Time",
                "Status",
                "Special Request",
                "Items",
                "Total Quantity",
                "Total Price"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        ordersTable = createStyledTable();

        // Add table to scrollpane
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 0),
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1)
        ));

        // Add components to main panel
        mainPanel.add(headingLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    private JLabel createHeadingLabel() {
        JLabel headingLabel = new JLabel("Pending Orders", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        headingLabel.setForeground(new Color(70, 30, 90));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        return headingLabel;
    }

    private JTable createStyledTable() {
        JTable table = new JTable(tableModel) {
            // Override to make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Table styling
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(230, 230, 250));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(100, 50, 120));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setPreferredSize(new Dimension(header.getWidth(), 50));

        return table;
    }

    public void loadPendingOrders() {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Read menu and orders
        ArrayList<FoodItem> menu = MenuFileManager.readMenuFromFile();
        ArrayList<Order> orders = OrderFileManager.readOrdersFromFile(menu);

        // Populate table with pending orders
        for (Order order : orders) {
            if (order.getStatus().equalsIgnoreCase("Pending")) {
                // Prepare items string
                StringBuilder itemsStr = new StringBuilder();
                int totalQuantity = 0;
                double totalPrice = 0;

                for (CartItem item : order.getItems()) {
                    itemsStr.append(item.getFoodItem().getName())
                            .append(" (")
                            .append(item.getQuantity())
                            .append("), ");
                    totalQuantity += item.getQuantity();
                    totalPrice += item.getTotalPrice();
                }

                // Remove trailing comma and space
                if (itemsStr.length() > 2) {
                    itemsStr.setLength(itemsStr.length() - 2);
                }

                // Add row to table
                tableModel.addRow(new Object[]{
                        order.getOrderTime().toString(),
                        order.getStatus(),
                        order.getSpecialRequest(),
                        itemsStr.toString(),
                        totalQuantity,
                        String.format("Rs. %.2f", totalPrice)
                });
            }
        }
    }

    public static void main(String[] args) {
        // Ensure GUI runs on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            PendingOrdersGUI gui = new PendingOrdersGUI();
            gui.loadPendingOrders();
            gui.setVisible(true);
        });
    }
}