import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Customer extends User {
    private final OrderHistory orderHistory;
    private Order currentOrder;
    private boolean isVIP;

    public Customer(String name, String email, String password, OrderHistory orderHistory) {
        super(name, email, password);
        this.currentOrder = new Order(name, "Pending");
        this.orderHistory = orderHistory;
        this.isVIP = false;
        // Use the shared OrderHistory
    }



    public void becomeVIP() {
        if (isVIP) {
            System.out.println("You are already a VIP!");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Pay ₹10 to become a VIP (yes/no): ");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("yes")) {
            this.isVIP = true;
            System.out.println("Congratulations! You are now a VIP. Your orders will be prioritized.");
        } else {
            System.out.println("Upgrade to VIP canceled.");
        }
    }
    // Method to browse the menu
    public void browseMenu(Menu menu) {
        menu.viewMenu();
    }
    public void saveOrderHistory(FileHandler fileHandler) {
        fileHandler.saveOrderHistory(getName(), (List<Order>) currentOrder);
    }


    // Method to add an item to the cart
    public void addToCart(FoodItem item, int quantity) {
        currentOrder.addItem(item, quantity);
    }

    // Method to modify item quantity in the cart
    public void modifyCart(FoodItem item, int newQuantity) {
        currentOrder.modifyItemQuantity(item, newQuantity);
    }

    // Method to remove an item from the cart
    public void removeFromCart(FoodItem item) {
        currentOrder.removeItem(item);
    }



    // Method to view the status of a specific order
    public void viewOrderStatus(Order order) {
        String status = order.getStatus();  // Assuming the `Order` class has a `getStatus` method
        if (status == null || status.isEmpty()) {
            System.out.println("Order Status: Pending");  // Default status
        } else {
            System.out.println("Order Status: " + status);
        }
    }


    // Method to cancel a specific order
    public void cancelOrder(Order order) {
        order.cancelOrder();
        System.out.println("Order cancelled: " + order);
    }

//    public void viewOrderHistory() {
//        if (orderHistory.getOrderHistory(getName()).isEmpty()) {
//            System.out.println("No past orders found.");
//            return;
//        }
//        else {
//            System.out.println("Order History:");
//            // Loop through each order in the order history for this customer
//            for (Order order : orderHistory.getOrderHistory(getName())) {
//                System.out.println("Order ID: " + order.getOrderID());
//                System.out.println("Status: " + order.getStatus());
//                System.out.println("Items:");
//
//                // Display each item and quantity in this order
//                for (Map.Entry<FoodItem, Integer> entry : order.getItems()) {
//                    FoodItem item = entry.getKey();
//                    int quantity = entry.getValue();
//                    System.out.println(" - " + item.getName() + " x " + quantity);
//                }
//            }
//        }
//    }

    // Method to search for items in the menu
    public void searchMenu(String query, Menu menu) {
        List<FoodItem> results = menu.searchItems(query);
        if (results.isEmpty()) {
            System.out.println("No items found matching your query.");
        } else {
            System.out.println("Search results:");
            for (FoodItem item : results) {
                System.out.println(item);
            }
        }
    }

    public void filterMenuByCategory(String category, Menu menu) {
        List<FoodItem> filteredItems = menu.filterByCategory(category);
        if (filteredItems.isEmpty()) {
            System.out.println("No items found in this category.");
        } else {
            System.out.println("Items in category " + category + ":");
            for (FoodItem item : filteredItems) {
                System.out.println(item);
            }
        }
    }

    // Method to sort the menu items by price
    public void sortMenuByPrice(Menu menu) {
        List<FoodItem> sortedItems = menu.sortByPrice();
        System.out.println("Menu sorted by price.");
        for (FoodItem item : sortedItems) {
            System.out.println(item);
        }
    }
    public void placeOrder(List<FoodItem> menuItems, String customerName, List<Order> orders) {
        // Create a new Order instance with the customer's name
        Order currentOrder = new Order(customerName, "Pending"); // Pass the customer name to the order

        if (isVIP) {
            currentOrder.setVIP(true); // Mark the order as VIP if the customer is a VIP
        }

        Scanner scanner = new Scanner(System.in);
        boolean addingItems = true;

        // Adding items to the order
        while (addingItems) {
            System.out.println("Current Menu:");
            for (FoodItem item : menuItems) {
                System.out.println(item.getName() + " - ₹" + item.getPrice());
            }

            System.out.print("Enter the food item you want to add (or type 'done' to finish): ");
            String itemName = scanner.nextLine();

            if (itemName.equalsIgnoreCase("done")) {
                addingItems = false; // Exit the loop when done
                continue;
            }

            FoodItem selectedItem = null;
            for (FoodItem item : menuItems) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    selectedItem = item;
                    break;
                }
            }

            if (selectedItem != null) {
                // Check if the item is available
                if (!selectedItem.isAvailable()) {
                    System.out.println("Sorry, " + selectedItem.getName() + " is out of stock and cannot be added to your order.");
                    continue; // Skip adding this item
                }

                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                currentOrder.addItem(selectedItem, quantity); // Add the item to the order
                System.out.println(quantity + " x " + selectedItem.getName() + " added to your order.");
            } else {
                System.out.println("Item not found. Please try again.");
            }

            System.out.print("Do you want to add another item? (yes/no): ");
            String anotherItem = scanner.nextLine();
            if (anotherItem.equalsIgnoreCase("no")) {
                addingItems = false; // Exit loop
            }
        }

        // Allow user to modify item quantities or remove items
        boolean modifyingOrder = true;
        while (modifyingOrder) {
            System.out.println("Your current order:");
            for (Map.Entry<FoodItem, Integer> entry : currentOrder.getItems()) {
                System.out.println(entry.getKey().getName() + " x " + entry.getValue());
            }



            System.out.print("Would you like to modify the order? (yes/no): ");
            String modifyChoice = scanner.nextLine();

            if (modifyChoice.equalsIgnoreCase("no")) {
                modifyingOrder = false;
                continue;
            }

            System.out.print("Enter the item name to modify/remove: ");
            String itemToModify = scanner.nextLine();

            FoodItem itemToModifyObj = null;
            for (Map.Entry<FoodItem, Integer> entry : currentOrder.getItems()) {
                if (entry.getKey().getName().equalsIgnoreCase(itemToModify)) {
                    itemToModifyObj = entry.getKey();
                    break;
                }
            }

            if (itemToModifyObj != null) {
                System.out.print("Enter new quantity (or 0 to remove): ");
                int newQuantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (newQuantity == 0) {
                    currentOrder.removeItem(itemToModifyObj); // Remove the item
                    System.out.println(itemToModify + " has been removed from your order.");
                } else {
                    currentOrder.modifyItemQuantity(itemToModifyObj, newQuantity); // Modify the quantity
                    System.out.println("Updated " + itemToModify + " to " + newQuantity + ".");
                }
            } else {
                System.out.println("Item not found in your order.");
            }
        }

        // Allow special request
        System.out.print("Do you have any special requests? (yes/no): ");
        String requestChoice = scanner.nextLine();
        if (requestChoice.equalsIgnoreCase("yes")) {
            System.out.print("Enter your special request: ");
            String specialRequest = scanner.nextLine();
            currentOrder.setSpecialRequest(specialRequest); // Add special request
        }

        // Add the finalized order to the list of orders
        orders.add(currentOrder);
        orderHistory.addOrder(customerName, currentOrder);
        System.out.println("Your order has been placed:\n" + currentOrder);
        Byte_Me.saveOrdersToFile();
    }

    // Method for customers to leave a review for an item they ordered
    public void provideReview(FoodItem item, String reviewText, int rating, Menu menu) {
        // Check if the customer has ordered the item before
        if (orderHistory.hasOrdered(item.getName())) { // Assuming orderHistory can check if the customer ordered the item
            Review review = new Review(getName(), reviewText, rating); // Assuming a Review class exists
            menu.addReview(item.getName(), String.valueOf(review)); // Add review to the item in the menu
            System.out.println("Thank you for your review on " + item.getName() + "!");
        } else {
            System.out.println("You can only review items you have ordered.");
        }
    }

    // Method to view reviews of a specific item
    public void viewReviews(FoodItem item, Menu menu) {
        List<String> reviews = menu.getReviews(item.getName()); // Assuming getReviews returns List<String> for simplicity

        if (reviews == null || reviews.isEmpty()) {
            System.out.println("No reviews available for " + item.getName() + ".");
        } else {
            System.out.println("Reviews for " + item.getName() + ":");
            for (String review : reviews) {
                System.out.println(review);
                System.out.println("---");
            }
        }
    }


    public void saveOrderHistoryToFile() {
        String filename = getName() + "_OrderHistory.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            List<Order> orders = orderHistory.getOrderHistory(getName());
            if (orders.isEmpty()) {
                writer.write("No past orders found.\n");
                return;
            }
            for (Order order : orders) {
                writer.write("Order ID: " + order.getOrderID() + "\n");
                writer.write("Status: " + order.getStatus() + "\n");
                writer.write("Items:\n");
                for (Map.Entry<FoodItem, Integer> entry : order.getItems()) {
                    FoodItem item = entry.getKey();
                    int quantity = entry.getValue();
                    writer.write(" - " + item.getName() + " x " + quantity + " (₹" + item.getPrice() + " each)\n");
                }
                writer.write("Total: ₹" + order.calculateTotal() + "\n");
                writer.write("Special Request: " + (order.getSpecialRequest() == null ? "None" : order.getSpecialRequest()) + "\n");
                writer.write("---\n");
            }
            System.out.println("Order history saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving order history: " + e.getMessage());
        }
    }

    // Load order history from a file
    public void loadOrderHistoryFromFile() {
        String filename = getName() + "_OrderHistory.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            System.out.println("Loading order history from " + filename + "...");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No saved order history found.");
        } catch (IOException e) {
            System.out.println("Error reading order history: " + e.getMessage());
        }
    }

    // View order history and also save it to a file
    @Override
    public void viewOrderHistory() {
        if (orderHistory.getOrderHistory(getName()).isEmpty()) {
            System.out.println("No past orders found.");
            return;
        }
        System.out.println("Order History:");
        for (Order order : orderHistory.getOrderHistory(getName())) {
            System.out.println("Order ID: " + order.getOrderID());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Items:");
            for (Map.Entry<FoodItem, Integer> entry : order.getItems()) {
                FoodItem item = entry.getKey();
                int quantity = entry.getValue();
                System.out.println(" - " + item.getName() + " x " + quantity);
            }
            System.out.println("Total: ₹" + order.calculateTotal());
            System.out.println("Special Request: " + (order.getSpecialRequest() == null ? "None" : order.getSpecialRequest()));
            System.out.println("---");
        }
        saveOrderHistoryToFile();
    }

    public double viewTotal() {
        if (currentOrder != null) {
            double total = currentOrder.calculateTotal();
            System.out.println("Total price of current order: ₹" + total);
            return total;
        } else {
            System.out.println("No current order available.");
            return 0.0;
        }
    }
    public String toFileString() {
        return getName() + "," + getEmail() + "," + getPassword() + "," + (isVIP ? "VIP" : "Regular");
    }

    public boolean isVIP() {
        return isVIP;
    }




}