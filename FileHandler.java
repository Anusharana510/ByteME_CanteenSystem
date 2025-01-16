import java.io.*;
import java.util.*;

public class FileHandler {
    private final String filePath;


    // Constructor to initialize the file path
    public FileHandler(String filePath) {
        this.filePath = filePath;
    }
    FileHandler fileHandler = new FileHandler("customer.txt");


    // Method to save order history to a file
    public void saveOrderHistory(String customerName, List<Order> orders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("Customer: " + customerName);
            writer.newLine();
            for (Order order : orders) {
                writer.write("Order ID: " + order.getOrderID());
                writer.newLine();
                writer.write("Status: " + order.getStatus());
                writer.newLine();
                writer.write("Items:");
                writer.newLine();
                for (Map.Entry<FoodItem, Integer> entry : order.getItems()) {
                    FoodItem item = entry.getKey();
                    int quantity = entry.getValue();
                    writer.write(" - " + item.getName() + " x " + quantity + " (₹" + item.getPrice() + ")");
                    writer.newLine();
                }
                writer.write("Total: ₹" + order.calculateTotal());
                writer.newLine();
                writer.write("----");
                writer.newLine();
            }
            System.out.println("Order history saved for customer: " + customerName);
        } catch (IOException e) {
            System.err.println("Error saving order history: " + e.getMessage());
        }
    }

    // Method to load order history from a file
    public Map<String, List<String>> loadOrderHistory() {
        Map<String, List<String>> orderHistories = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentCustomer = null;
            List<String> orders = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Customer: ")) {
                    if (currentCustomer != null) {
                        orderHistories.put(currentCustomer, new ArrayList<>(orders));
                        orders.clear();
                    }
                    currentCustomer = line.substring(10); // Extract customer name
                } else if (line.equals("----")) {
                    // Do nothing, used as a separator between orders
                } else {
                    orders.add(line);
                }
            }

            // Add the last customer orders if any
            if (currentCustomer != null) {
                orderHistories.put(currentCustomer, orders);
            }

        } catch (IOException e) {
            System.err.println("Error loading order history: " + e.getMessage());
        }

        return orderHistories;
    }
}
