import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class OrderGUI extends JFrame {
    private Admin admin; // The admin managing the orders
    private List<Order> orders; // List of orders
    private JTextArea orderDisplayArea; // Display orders
    private JTextField orderIdField; // Input for order ID
    private JTextField statusField; // Input for new status
    private JTextArea specialRequestArea; // Input for special request handling

    public OrderGUI(Admin admin, List<Order> orders) {
        this.admin = admin;
        this.orders = orders;

        setTitle("Order Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Layout
        setLayout(new BorderLayout());

        // Text Area to display orders
        orderDisplayArea = new JTextArea(15, 50);
        orderDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderDisplayArea);
        add(scrollPane, BorderLayout.CENTER);

        // Panel for actions
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(4, 2));

        // Order ID input and buttons
        actionPanel.add(new JLabel("Order ID:"));
        orderIdField = new JTextField();
        actionPanel.add(orderIdField);

        actionPanel.add(new JLabel("New Status:"));
        statusField = new JTextField();
        actionPanel.add(statusField);

        actionPanel.add(new JLabel("Special Request:"));
        specialRequestArea = new JTextArea(2, 20);
        actionPanel.add(specialRequestArea);

        JButton updateStatusButton = new JButton("Update Status");
        actionPanel.add(updateStatusButton);

        JButton processRefundButton = new JButton("Process Refund");
        actionPanel.add(processRefundButton);

        JButton handleRequestButton = new JButton("Handle Special Request");
        actionPanel.add(handleRequestButton);

        add(actionPanel, BorderLayout.SOUTH);

        // Button Actions
        updateStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String orderId = orderIdField.getText();
                String newStatus = statusField.getText();
                admin.updateOrderStatus(orders,orderId);
                updateOrderDisplay();
            }
        });

        processRefundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String orderId = orderIdField.getText();
                admin.processRefund(orders, orderId);
                updateOrderDisplay();
            }
        });

        handleRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String orderId = orderIdField.getText();
                String request = specialRequestArea.getText();
                handleSpecialRequest(orderId, request);
                updateOrderDisplay();
            }
        });

        // Initial display of orders
        updateOrderDisplay();
    }

    // Update the orders displayed in the JTextArea
    private void updateOrderDisplay() {
        orderDisplayArea.setText("");
        for (Order order : orders) {
            orderDisplayArea.append(order.toString() + "\n\n");
        }
    }

    // Handle special request for the selected order
    private void handleSpecialRequest(String orderId, String request) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                order.setSpecialRequest(request);
                JOptionPane.showMessageDialog(this, "Special request updated for Order ID: " + orderId);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Order ID not found.");
    }

    public static void main(String[] args) {
        // Dummy data for testing
        Admin admin = new Admin("Admin", "admin@canteen.com", "admin123");
        List<Order> orders = List.of(
                new Order("John Doe", "Pending"),
                new Order("Jane Smith", "Pending")
        );

        // Add some items to the orders
        FoodItem pizza = new FoodItem("Pizza", "Main Course", 200, true);
        FoodItem burger = new FoodItem("Burger", "Snack", 150, true);
        orders.get(0).addItem(pizza, 2);
        orders.get(1).addItem(burger, 1);

        // Create and show the GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OrderGUI(admin, orders).setVisible(true);
            }
        });
    }
}
