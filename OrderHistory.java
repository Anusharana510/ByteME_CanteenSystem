import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class OrderHistory implements Serializable{
    private Map<String, List<Order>> orderHistory = new HashMap<>();
    private List<String> orderedItems; // Initialize this to keep track of ordered items

    // Constructor to initialize orderedItems
    public OrderHistory() {
        this.orderedItems = new ArrayList<>(); // Initialize the list
    }

    public void setOrderHistory(Map<String, List<Order>> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public Map<String, List<Order>> getOrderHistory() {
        return this.orderHistory;
    }
//    public static OrderHistory fromFileString(String data) {
//        OrderHistory history = new OrderHistory();
//        String[] orderData = data.split("\n");
//        for (String orderStr : orderData) {
//            if (!orderStr.trim().isEmpty()) {
//                history.addOrder(,Order.fromFileString(orderStr, Byte_Me.menu, Byte_Me.customers));
//            }
//        }
//        return history;
//    }

    // Record an order and add the item to the ordered items list
    // Overloaded method to record multiple orders
    public void recordOrder(Collection<String> itemNames) {
        for (String itemName : itemNames) {
            orderedItems.add(itemName); // Add each item to ordered items
        }
    }

    // Method to check if a specific item was ordered
    public boolean hasOrdered(String itemName) {
        return orderedItems.contains(itemName); // Check if item has been ordered
    }

    // Add an order to the customer's order history
    public void addOrder(String customerName, Order order) {
        orderHistory.computeIfAbsent(customerName, k -> new ArrayList<>()).add(order);

        // Extract food item names from the order and pass them to recordOrder
        Collection<String> itemNames = new ArrayList<>();
        for (Map.Entry<FoodItem, Integer> entry : order.getItems()) {
            itemNames.add(entry.getKey().getName()); // Add the food item's name
        }

        recordOrder(itemNames); // Pass the item names to recordOrder
    }




    // Retrieve the entire order history for a customer
    public List<Order> getOrderHistory(String customerName) {
        return orderHistory.getOrDefault(customerName, Collections.emptyList()); // Return orders or empty list
    }

    // Display the order history for a customer
    public void displayOrderHistory(String customerName) {
        List<Order> orders = getOrderHistory(customerName);
        if (orders.isEmpty()) {
            System.out.println("No past orders found for customer: " + customerName);
        } else {
            System.out.println("Order history for " + customerName + ":");
            for (Order order : orders) {
                System.out.println(order); // Assumes Order class has a meaningful toString() method
            }
        }
    }

    // Admin method to display all orders for all customers
    public void displayAllOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("No orders have been placed.");
        } else {
            for (String customerName : orderHistory.keySet()) {
                System.out.println("Orders for " + customerName + ":");
                displayOrderHistory(customerName); // Display each customer's order history
            }
        }
    }

    // Admin method to get all pending orders for all customers
    public List<Order> getAllPendingOrders() {
        List<Order> pendingOrders = new ArrayList<>();
        for (List<Order> orders : orderHistory.values()) {
            for (Order order : orders) {
                if (order.getStatus().equals("Pending")) {
                    pendingOrders.add(order); // Add pending orders to the list
                }
            }
        }
        return pendingOrders; // Return list of pending orders
    }


    public void saveOrderHistoryToFile(String customerName) {
        List<Order> orders = getOrderHistory(customerName);
        String fileName = customerName + "_OrderHistory.txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            if (orders.isEmpty()) {
                writer.write("No orders found for " + customerName + ".\n");
            } else {
                writer.write("Order history for " + customerName + ":\n");
                for (Order order : orders) {
                    writer.write(order.toString() + "\n");
                }
            }
            System.out.println("Order history saved for " + customerName + " in " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }


}
