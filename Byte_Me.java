import javax.swing.*;
import java.io.*;
import java.util.*;

public class Byte_Me {
    static List<Customer> customers = new ArrayList<>();
    static List<Order> orders = new ArrayList<>();
    public static Menu menu;  // Using Menu instance instead of a List of FoodItems
    static Admin admin;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Initialize the system with sample data
        initializeSystem();
        // Main menu loop for user interaction
        while (true) {
            System.out.println("\nWelcome to Byte Me! Food Ordering System");
            System.out.println("1. Register as Customer");
            System.out.println("2. Login as Customer");
            System.out.println("3. Login as Admin");
            System.out.println("4. Launch Menu GUI");
            System.out.println("5. Launch Order GUI");
            System.out.println("6. View Customer File");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    customerLogin();  // Call customer login
                    break;
                case 3:
                    adminLogin();  // Call admin login
                    break;
                case 4:
                    launchMenuGUI(); // Launch Menu GUI
                    break;
                case 5:
                    launchOrderGUI(); // Launch Order GUI
                    break;
                case 6:
                    viewCustomerList();
                    break;
                case 7:
                    System.out.println("Thank you for using Byte Me! Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Initialize the system with some sample data
    static void initializeSystem() {
        loadCustomersFromFile();
        loadOrderHistoryFromFile();
        // Create admin instance
        admin = new Admin("admin", "admin", "adminpass");

        // Initialize menu with default items
        menu = new Menu();

        // Add sample customers
        OrderHistory sharedOrderHistory = new OrderHistory();
        //customers.add(new Customer("Alice", "a", "a", sharedOrderHistory));
        //customers.add(new Customer("Bob", "b", "b", sharedOrderHistory));
    }

    private static void loadOrderHistoryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("OrderHistory.txt"))) {
            String line;
            Map<String, List<Order>> orderHistoryMap = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                // Parse each line of the file to reconstruct the order history
                String[] parts = line.split(":"); // Assuming a format like CustomerEmail:OrderDetails
                if (parts.length < 2) {
                    System.out.println("Invalid line format: " + line);
                    continue;
                }

                String customerEmail = parts[0].trim();
                String[] orderDetails = parts[1].split(";"); // Assuming orders are separated by semicolons

                List<Order> orders = new ArrayList<>();
                for (String orderDetail : orderDetails) {
                    String[] orderParts = orderDetail.split(","); // Assuming individual order fields are comma-separated
                    if (orderParts.length < 5) {
                        System.out.println("Incomplete order detail: " + orderDetail);
                        continue;
                    }

                    // Extract order details
                    String orderId = orderParts[0].trim();
                    String customerName = orderParts[1].trim();
                    String[] itemDetails = orderParts[2].split("\\|"); // Assuming items are separated by pipes
                    String status = orderParts[3].trim();
                    boolean isVIP = Boolean.parseBoolean(orderParts[4].trim());

                    // Construct items map
                    Map<FoodItem, Integer> itemsMap = new HashMap<>();
                    for (String itemDetail : itemDetails) {
                        String[] itemParts = itemDetail.split("\\*"); // Assuming format FoodItemName*Quantity
                        if (itemParts.length != 2) {
                            System.out.println("Invalid item detail: " + itemDetail);
                            continue;
                        }
                        String itemName = itemParts[0].trim();
                        int quantity = Integer.parseInt(itemParts[1].trim());
                        itemsMap.put(new FoodItem(itemName,FoodItem.getPrice(), FoodItem.isAvailable(), FoodItem.getCategory()), quantity);
                    }

                    // Create order and set its properties
                    Order order = new Order(customerName, status);
                    order.setVIP(isVIP);
                    for (Map.Entry<FoodItem, Integer> entry : itemsMap.entrySet()) {
                        order.addItem(entry.getKey(), entry.getValue());
                    }

                    orders.add(order); // Add the order to the list
                }

                // Add the orders list to the map for this customer
                orderHistoryMap.put(customerEmail, orders);
            }

            // Optionally, set this map to the OrderHistory instance
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrderHistory(orderHistoryMap);

        } catch (IOException e) {
            System.out.println("Error loading order history: " + e.getMessage());
        }
    }

    private static List<Order> sharedOrderHistory = new ArrayList<>();
    public static void saveOrdersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("OrderHistory.txt"))) {
            for (Order order : orders) {
                writer.write(order.toFileString());  // Call toFileString on each order
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }


    private static void loadCustomersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Expecting "name,email,password"
                if (parts.length == 3) {
                    customers.add(new Customer(parts[0], parts[1], parts[2], new OrderHistory()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }


    // Register a new customer
    private static void registerCustomer() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        Customer newCustomer = new Customer(name, email, password, new OrderHistory());
        customers.add(newCustomer);
        saveCustomerToFile(newCustomer); // Save the new customer to file
        System.out.println("Registration successful! You can now log in.");
    }

    // Save a new customer to the file
    private static void saveCustomerToFile(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customer.txt", true))) {
            writer.write(customer.toFileString()); // Use the method to get the string
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }



    // Customer login
    private static void customerLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (Customer customer : customers) {
            if (customer.login(email, password)) {
                customerMenu(customer);  // Open customer menu
                return;
            }
        }
        System.out.println("Invalid credentials. Please try again.");
    }

    // Customer menu
    private static void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\nCustomer Menu");
            System.out.println("1. View Menu");
            System.out.println("2. Search Menu");
            System.out.println("3. Filter by Category");
            System.out.println("4. Sort by Price");
            System.out.println("5. Place Order");
            System.out.println("6. View Total");
            System.out.println("7. View Order Status");
            System.out.println("8. Cancel Order");
            System.out.println("9. View Order History");
            System.out.println("10. Become VIP");
            System.out.println("11. Add review");
            System.out.println("12. View review");
            System.out.println("13. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    menu.viewMenu();  // Show current menu from Menu class
                    break;
                case 2:
                    System.out.print("Enter your search query: ");
                    String query = scanner.nextLine();
                    customer.searchMenu(query, menu); // Call searchMenu method
                    break;
                case 3:
                    System.out.print("Enter category to filter: ");
                    String category = scanner.nextLine();
                    customer.filterMenuByCategory(category, menu); // Call filterByCategory method
                    break;
                case 4:
                    customer.sortMenuByPrice(menu);  // Sort menu items by price
                    break;
                case 5:
                    customer.placeOrder(menu.getMenuItems(), customer.getName(), orders);
                    break;
                case 6:
                    customer.viewTotal();
                    break;
                case 7:
                    if (!orders.isEmpty()) {
                        Order orderToView = orders.get(0); // Or get the specific order you want
                        customer.viewOrderStatus(orderToView); // Pass single Order object
                    } else {
                        System.out.println("No orders to view.");
                    }
                    break;
                case 8:
                    if (!orders.isEmpty()) {
                        Order orderToCancel = orders.get(0); // Or specify which order to cancel
                        customer.cancelOrder(orderToCancel); // Pass single Order object
                    } else {
                        System.out.println("No orders to cancel.");
                    }
                    break;
                case 9:
                    customer.viewOrderHistory();  // View past orders
                    break;
                case 10:
                    handleVIPUpgrade(customer);
                    break;
                case 11: // Provide review for an item
                    System.out.print("Enter item name to review: ");
                    String reviewItemName = scanner.nextLine();

                    FoodItem itemToReview = menu.getFoodItemByName(reviewItemName); // Assuming a getItemByName method in Menu
                    if (itemToReview != null) {
                        System.out.print("Enter your rating (1-5): ");
                        int rating = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter your review text: ");
                        String reviewText = scanner.nextLine();

                        customer.provideReview(itemToReview, reviewText, rating, menu); // Call the provideReview method
                    } else {
                        System.out.println("Item not found in the menu.");
                    }
                    break;

                case 12: // View reviews for an item
                    System.out.print("Enter item name to view reviews: ");
                    String reviewViewItemName = scanner.nextLine();

                    FoodItem itemToView = menu.getFoodItemByName(reviewViewItemName); // Assuming a getItemByName method in Menu
                    if (itemToView != null) {
                        customer.viewReviews(itemToView, menu); // Call the viewReviews method
                    } else {
                        System.out.println("Item not found in the menu.");
                    }
                    break;

                case 13:
                    customer.logout();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // VIP upgrade for customer
    private static void handleVIPUpgrade(Customer customer) {
        customer.becomeVIP();
        System.out.println("Congratulations, you are now a VIP!");
    }

    // Method to sort orders, prioritizing VIP orders first
    public static void processOrders() {
        orders.sort((o1, o2) -> Boolean.compare(o2.isVIP(), o1.isVIP()));

        for (Order order : orders) {
            System.out.println("Processing Order: " + order);
        }
    }
    public static void launchMenuGUI() {
        SwingUtilities.invokeLater(() -> new MenuGUI(menu));
    }

    public static void launchOrderGUI() {
        SwingUtilities.invokeLater(() -> new OrderGUI(admin, orders).setVisible(true));
    }

    private static void viewCustomerList() {
        try (BufferedReader reader = new BufferedReader(new FileReader("customer.txt"))) {
            String line;
            System.out.println("\nCustomer List:");
            while ((line = reader.readLine()) != null) {
                // Print each customer entry from the file
                System.out.println("Reading line: " + line); // Debug statement
                String[] parts = line.split(",");
                System.out.println("Parts: " + parts.length); // Debug statement

                if (parts.length != 4) {
                    System.out.println("Skipping line due to unexpected format: " + line);
                    continue;
                }

                String name = parts[0];
                String email = parts[1];
                String password = parts[2];
                String vipStatus = parts[3]; // VIP or Regular

                System.out.println("Name: " + name + ", Email: " + email + ", VIP Status: " + vipStatus);
            }
        } catch (IOException e) {
            System.out.println("Error reading customer list: " + e.getMessage());
        }
    }



    // Admin login
    private static void adminLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (admin.login(email, password)) {
            adminMenu();  // Open admin menu
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    // Admin menu
    private static void adminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Manage Orders");
            System.out.println("2. Manage Menu");
            System.out.println("3. Check Daily Sales Report");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    admin.manageOrders(orders);
                    break;
                case 2:
                    admin.manageMenu(menu.getMenuItems(),orders);  // Use menu instance
                    break;
                case 3:
                    admin.dailySalesReport(orders);
                    break;
                case 4:
                    admin.logout();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
