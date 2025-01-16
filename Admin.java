import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin extends User {
    private Menu menu;

    public Admin(String name, String email, String password) {
        super(name, email, password);
        this.menu = new Menu();
    }

    @Override
    public void viewOrderHistory() {

    }

    // Method to manage the menu
    public void manageMenu(List<FoodItem> menuItems, List<Order> orders) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Menu Management:");
            System.out.println("1. Add new items");
            System.out.println("2. Update existing items");
            System.out.println("3. Remove items");
            System.out.println("4. Modify prices");
            System.out.println("5. Update availability");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addNewItem(scanner);
                case 2 -> updateExistingItem(scanner);
                case 3 -> removeItem(scanner, orders); // Pass scanner and menuItems
                case 4 -> modifyPrice(scanner);
                case 5 -> updateAvailability(scanner);
                case 0 -> System.out.println("Exiting menu management.");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void addNewItem(Scanner scanner) {
        System.out.print("Enter food item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter food item category: ");
        String category = scanner.nextLine(); // Make sure to ask for category first
        System.out.print("Enter food item price: ");
        double price = scanner.nextDouble();
        System.out.print("Is the item available (true/false): ");
        boolean available = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline

        // Use the constructor that initializes all fields
        FoodItem newItem = new FoodItem(name, category, price, available);
        menu.addFoodItem(newItem);
        System.out.println("New item added: " + newItem);
    }

    private void updateExistingItem(Scanner scanner) {
        System.out.print("Enter the name of the item to update: ");
        String name = scanner.nextLine();
        FoodItem item = menu.getFoodItemByName(name);
        if (item != null) {
            System.out.print("Enter new price: ");
            double newPrice = scanner.nextDouble();
            System.out.print("Is the item available (true/false): ");
            boolean available = scanner.nextBoolean();
            scanner.nextLine(); // Consume newline

            menu.updatePrice(name, newPrice);
            menu.updateAvailability(name, available);
            System.out.println("Item updated: " + name);
        } else {
            System.out.println("Item not found.");
        }
    }

    void removeItem(Scanner scanner, List<Order> orders) {
        System.out.print("Enter the name of the item to remove: ");
        String name = scanner.nextLine();

        FoodItem itemToRemove = menu.getFoodItemByName(name); // Find the food item by name

        if (itemToRemove != null) {
            // Update status of all pending orders containing the removed item
            for (Order order : orders) {
                if (order.getItems().stream().anyMatch(entry -> entry.getKey().getName().equals(itemToRemove.getName()))) {
                    order.setStatus("Denied"); // Update status to 'Denied'
                }
            }

            // Now remove the food item from the menu
            menu.removeFoodItem(itemToRemove.getName()); // Use the name of the item
            System.out.println("Item removed: " + name);
        } else {
            System.out.println("Item not found.");
        }
    }

    private void modifyPrice(Scanner scanner) {
        System.out.print("Enter the name of the item to modify price: ");
        String name = scanner.nextLine();
        FoodItem item = menu.getFoodItemByName(name);
        if (item != null) {
            System.out.print("Enter new price: ");
            double newPrice = scanner.nextDouble();
            menu.updatePrice(name, newPrice);
            System.out.println("Price updated for: " + name);
        } else {
            System.out.println("Item not found.");
        }
    }

    void updateAvailability(Scanner scanner) {
        System.out.print("Enter the name of the item to update availability: ");
        String name = scanner.nextLine(); // Read the name of the item
        FoodItem item = menu.getFoodItemByName(name); // Fetch the item
        if (item != null) {
            System.out.print("Is the item available (true/false): ");
            if (scanner.hasNextBoolean()) { // Check if the input is a boolean
                boolean available = scanner.nextBoolean(); // Read availability
                menu.updateAvailability(name, available);
                System.out.println("Availability updated for: " + name);
            } else {
                System.out.println("Invalid input. Please enter true or false.");
                scanner.next(); // Consume invalid input
            }
        } else {
            System.out.println("Item not found.");
        }
        scanner.nextLine(); // Consume any leftover newline
    }


    // Order Management Methods
    public void manageOrders(List<Order> orders) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Order Management:");
            System.out.println("1. View pending orders");
            System.out.println("2. Update order status");
            System.out.println("3. Process refunds");
            System.out.println("4. Handle special requests");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewPendingOrders(orders);
                case 2 -> updateOrderStatus(orders, String.valueOf(scanner));
                case 3 -> processRefund(orders, String.valueOf(scanner));
                case 4 -> handleSpecialRequests(orders, scanner);
                case 0 -> System.out.println("Exiting order management.");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void viewPendingOrders(List<Order> orders) {
        System.out.println("Pending Orders:");
        for (Order order : orders) {
            if (order.getStatus().equalsIgnoreCase("Pending")) {
                System.out.println(order);
            }
        }
    }

    void updateOrderStatus(List<Order> orders, String scanner) {
        System.out.print("Enter the order ID to update: ");
        String orderId = scanner;
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                System.out.print("Enter new status: ");
                String newStatus = scanner;
                order.setStatus(newStatus);
                System.out.println("Order status updated: " + order);
                return;
            }
        }
        System.out.println("Order not found.");
    }

    public void processRefund(List<Order> orders, String scanner) {
        System.out.print("Enter the order ID to process a refund: ");
        String orderId = scanner;
        boolean orderFound = false;

        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                double refundAmount = order.calculateTotal(); // Assuming calculateTotal() returns the total cost of the order
                order.setStatus("Refunded");
                System.out.println("Refund initiated for order: " + orderId + ". Amount refunded: $" + refundAmount);
                orderFound = true; // Set the flag when order is found
                break; // Exit the loop once the order is found
            }
        }

        if (!orderFound) {
            System.out.println("Order not found or not eligible for a refund.");
        }
    }

    // Method to handle special requests
    private void handleSpecialRequests(List<Order> orders, Scanner scanner) {
        System.out.print("Enter the order ID to handle special requests: ");
        String orderId = scanner.nextLine();

        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                String request = order.getSpecialRequest();
                if (request == null || request.isEmpty()) {
                    System.out.println("No special requests added by the customer for this order.");
                } else {
                    System.out.println("Special request from customer: " + request);
                    System.out.print("Do you want to mark this request as acknowledged? (yes/no): ");
                    String confirmation = scanner.nextLine();

                    if (confirmation.equalsIgnoreCase("yes")) {
                        order.setSpecialRequest("Acknowledged: " + request); // Update the request as acknowledged
                        System.out.println("Special request has been marked as acknowledged for order: " + order.getOrderId());
                    } else {
                        System.out.println("Special request not acknowledged.");
                    }
                }
                return; // Exit once the order is found and processed
            }
        }
        System.out.println("Order not found."); // Message if order ID doesn't match any existing order
    }

    // Report Generation Method
    public void dailySalesReport(List<Order> orders) {
        double totalSales = 0.0; // To hold total sales from sold items
        double totalRefunds = 0.0; // To hold total refunded amounts
        System.out.println("Daily Sales Report:");

        // Calculate sales from sold food items
        for (FoodItem item : menu.getMenuItems()) { // Assuming menu.getMenuItems() returns a List<FoodItem>
            if (item.getSalesCount() > 0) { // Accessing getSalesCount() directly from FoodItem class
                double itemSales = item.getPrice() * item.getSalesCount();
                totalSales += itemSales; // Add to total sales
                System.out.println(item.getName() + ": $" + itemSales);
            }
        }

        // Calculate total refunds
        for (Order order : orders) {
            if (order.getStatus().equals("Refunded")) {
                totalRefunds += order.calculateTotal(); // Assuming calculateTotal() calculates total of the order
            }
        }

        System.out.println("Total Sales: $" + totalSales);
        System.out.println("Total Refunds: $" + totalRefunds);
        System.out.println("Net Earnings: $" + (totalSales - totalRefunds));
    }

    public void cancelOrder(Order order) {
        order.cancelOrder();  // Calling cancelOrder from Order class
        System.out.println("Order cancelled: " + order);
    }

    public List<Order> getPendingOrders() {
        List<Order> pendingOrders = new ArrayList<>();
        for (Order order : Byte_Me.orders) {
            if ("Pending".equals(order.getStatus())) {
                pendingOrders.add(order);
            }
        }
        return pendingOrders;
    }
    public void addOrder(Order order) {
        Byte_Me.orders.add(order);
    }
}
