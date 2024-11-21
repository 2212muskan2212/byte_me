import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CanteenMenuGUI extends JFrame {
    private JTable menuTable;
    private DefaultTableModel tableModel;

    public CanteenMenuGUI() {
        // Frame setup
        setTitle("Byte Me Canteen");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with vertical layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Welcome heading with gradient
        JLabel welcomeLabel = createWelcomeLabel();

        // Create table model
        String[] columnNames = {"Name", "Category", "Price", "Availability"};
        tableModel = new DefaultTableModel(columnNames, 0);
        menuTable = createStyledTable();

        // Add table to scrollpane with custom styling
        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 0, 0),
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1)
        ));
        scrollPane.setBackground(Color.WHITE);

        // Create button panel
        JPanel buttonPanel = createButtonPanel();

        // Add components to main panel
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        // Create "View Pending Orders" button
        JButton pendingOrdersButton = new JButton("View Pending Orders");
        pendingOrdersButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pendingOrdersButton.setBackground(new Color(100, 50, 120)); // Deep purple
        pendingOrdersButton.setForeground(Color.WHITE);
        pendingOrdersButton.setFocusPainted(false);

        // Add action listener to open Pending Orders GUI
        pendingOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPendingOrdersGUI();
            }
        });

        buttonPanel.add(pendingOrdersButton);
        return buttonPanel;
    }

    private void openPendingOrdersGUI() {
        // Create and show the Pending Orders GUI
        SwingUtilities.invokeLater(() -> {
            PendingOrdersGUI pendingOrdersGUI = new PendingOrdersGUI();
            pendingOrdersGUI.setVisible(true);
        });
    }

    private JLabel createWelcomeLabel() {
        JLabel welcomeLabel = new JLabel("Welcome to Byte Me", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeLabel.setForeground(new Color(70, 30, 90)); // Deep purple color
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        return welcomeLabel;
    }

    private JTable createStyledTable() {
        JTable table = new JTable(tableModel);

        // Table styling
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(230, 230, 250)); // Light lavender selection

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(100, 50, 120)); // Deep purple header
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setPreferredSize(new Dimension(header.getWidth(), 50));

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Color for selected row
                if (isSelected) {
                    c.setBackground(new Color(230, 230, 250));
                    c.setForeground(Color.BLACK);
                } else {
                    // Alternating row colors
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(240, 240, 255)); // Very light blue
                    }
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });

        return table;
    }

    public void loadMenuItems() {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Read menu from file
        ArrayList<FoodItem> menu = MenuFileManager.readMenuFromFile();

        // Populate table
        for (FoodItem item : menu) {
            tableModel.addRow(new Object[]{
                    item.getName(),
                    item.getCategory(),
                    "Rs. " + String.format("%.2f", item.getPrice()),
                    item.isAvailable() ? "Available" : "Not Available"
            });
        }
    }

    public static void main(String[] args) {
        // Ensure GUI runs on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            CanteenMenuGUI gui = new CanteenMenuGUI();
            gui.loadMenuItems();
            gui.setVisible(true);
        });
    }
}