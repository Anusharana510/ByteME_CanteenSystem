import java.util.*;

public class Order {
    private static int orderCounter = 1; // Static variable for generating unique order IDs
    private String orderId; // Unique order ID
    private String customerName; // Customer's name
    private Map<FoodItem, Integer> items; // Map of food items and their quantities
    private String status; // Current status of the order
    private boolean refundProcessed; // Flag to check if refund has been processed
    private String specialRequest;// Special requests from the customer
    private boolean isVIP;

    // Constructor
    public Order(String customerName, String pending) {
        this.orderId = String.valueOf(orderCounter++);
        this.customerName = customerName; // Set the customer name
        this.items = new HashMap<>();
        this.status = "Pending";
        this.refundProcessed = false;
        this.specialRequest = "";
        this.isVIP = false;

    }

//    public static Order fromFileString(String orderData, Menu menu, List<Customer> customers) {
//        // Split the string by commas (or any delimiter you use)
//        String[] parts = orderData.split(",");
//
//        if (parts.length < 5) {
//            System.out.println("Invalid order format");
//            return null;
//        }
//
//        String orderId = parts[0];
//        String customerEmail = parts[1];
//        List<FoodItem> orderedItems = new ArrayList<>();
//
//        // Parse the ordered items (assuming they are stored in a way that can be separated by commas)
//        String[] items = parts[2].split(";");  // Assuming items are separated by semicolon
//        for (String itemName : items) {
//            FoodItem item = menu.getFoodItemByName(itemName);  // Fetch food item from menu
//            if (item != null) {
//                orderedItems.add(item);
//            }
//        }
//
//        double totalAmount = Double.parseDouble(parts[3]);
//        boolean isVIP = Boolean.parseBoolean(parts[4]);
//        String orderStatus = parts[5];  // Status of the order
//
//        // Find customer by email
//        Customer customer = null;
//        for (Customer c : customers) {
//            if (c.getEmail().equals(customerEmail)) {
//                customer = c;
//                break;
//            }
//        }
//
//        if (customer == null) {
//            System.out.println("Customer not found");
//            return null;
//        }
//
//        // Create and return the Order object
//        Order order = new Order(orderId, orderStatus);
//        return order;
//    }


    public void setVIP(boolean isVIP) {
        this.isVIP = isVIP;
    }

    // Getter for VIP status
    public boolean isVIP() {
        return isVIP;
    }
    public String toFileString() {
        // Convert the Order to a String that can be saved in a file
        // Adjust this based on your Order's fields
        return "OrderID: " + this.orderId + ", Customer: " + this.customerName + ", Items: " + this.items;
    }



    // Add item to the order
    public void addItem(FoodItem item, int quantity) {
        items.put(item, quantity);
    }

    public void addSpecialRequest(String request) {
        this.specialRequest = request;  // Set the request in the order
    }

    public String getSpecialRequest() {
        return specialRequest;  // Retrieve the request for later use
    }

    // Remove item from the order
    public void removeItem(FoodItem item) {
        items.remove(item);
    }

    // Update order status
    // Update order status dynamically
    public void updateStatus(String newStatus) {
        // Define valid statuses
        List<String> validStatuses = Arrays.asList("Pending", "Cancelled", "Denied", "Completed", "Refunded");

        // Check if the new status is valid
        if (!validStatuses.contains(newStatus)) {
            System.out.println("Invalid status: " + newStatus);
            return;
        }

        // Allow status to be updated to the new value
        this.status = newStatus;
    }


    public String getStatus() {
        return status;
    }


    public double calculateTotal() {
        if (items == null || items.isEmpty()) {
            System.out.println("No items in the order.");
            return 0.0; // Return 0 if there are no items
        }

        double total = 0;
        for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    // Process refund
    public void processRefund() {
        if (status.equals("Cancelled") && !refundProcessed) {
            refundProcessed = true;
            System.out.println("Refund processed for Order ID: " + orderId);
        } else if (refundProcessed) {
            System.out.println("Refund already processed for Order ID: " + orderId);
        } else {
            System.out.println("Refund cannot be processed unless the order is cancelled.");
        }
    }

    // Add special request
    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest; // Ensure this sets the request properly
    }


    // Cancel order
    public void cancelOrder() {
        if (status.equals("Pending")) {
            status = "Cancelled";
            System.out.println("Order ID: " + orderId + " has been cancelled.");
        } else {
            System.out.println("Order cannot be cancelled as it is already " + status + ".");
        }
    }

    // Override toString for order details
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Customer: ").append(customerName).append("\n");
        sb.append("VIP Status: ").append(isVIP ? "Yes" : "No").append("\n");
        sb.append("Items: \n");
        for (Map.Entry<FoodItem, Integer> entry : items.entrySet()) {
            sb.append(entry.getKey().getName()).append(" x ").append(entry.getValue()).append("\n");
        }
        sb.append("Total: ₹").append(calculateTotal()).append("\n");
        sb.append("Status: ").append(status).append("\n");
        if (specialRequest != null && !specialRequest.isEmpty()) {
            sb.append("Special Request: ").append(specialRequest).append("\n");
        } else {
            sb.append("No special requests added by the customer for this order.\n");
        }
        return sb.toString();
    }

    // Process refund
    public void refund() {
        if (status.equals("Cancelled")) {
            // Perform refund logic here (e.g., updating the database)
            System.out.println("Refund processed for Order ID: " + orderId);
            refundProcessed = true; // Mark the refund as processed
        } else {
            System.out.println("Refund cannot be processed. Order is not cancelled.");
        }
    }

    // Get order ID
    public String getOrderId() {
        return orderId;
    }

    // Modify item quantity
    public void modifyItemQuantity(FoodItem item, int newQuantity) {
        if (items.containsKey(item)) {
            if (newQuantity <= 0) {
                removeItem(item); // Remove item if quantity is 0 or less
            } else {
                items.put(item, newQuantity); // Update item quantity
            }
        } else {
            System.out.println("Item not found in the order.");
        }
    }

    // Get items in the order
    public Set<Map.Entry<FoodItem, Integer>> getItems() {
        return items.entrySet(); // Return a collection of food items and their quantities
    }

    // Update order status dynamically
    public void setStatus(String newStatus) {
        // Define valid statuses
        List<String> validStatuses = Arrays.asList("Pending", "Cancelled", "Denied", "Completed", "Refunded");

        // Check if the new status is valid
        if (!validStatuses.contains(newStatus)) {
            System.out.println("Invalid status: " + newStatus);
            return;
        }

        // Allow status to be updated to the new value
        this.status = newStatus;
        System.out.println("hello");
    }


    public String getOrderID() {
        return orderId;
    }

    public Collection<String> getItemName() {
        Collection<String> itemNames = new ArrayList<>(); // Create a collection to hold item names
        for (FoodItem item : items.keySet()) { // Iterate through the keys of the items map
            itemNames.add(item.getName()); // Add each item's name to the collection
        }
        return itemNames; // Return the collection of item names
    }
    public String getCustomerName() {
        return this.customerName;
    }
}
