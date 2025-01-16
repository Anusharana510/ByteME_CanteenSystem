import java.util.List;
import java.util.ArrayList;

public class FoodItem {
    private String name;        // Name of the food item
    private static String category;    // Category of the food item
    private static double price;       // Price of the food item
    private static boolean available;  // Availability status
    private int salesCount;     // Sales count of the item
    private List<String> reviews; // Reviews for the food item
    private String id;          // Unique identifier for the food item

    // Constructor to initialize a food item with all attributes
    public FoodItem(String name, String category, double price, boolean available) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
        this.reviews = new ArrayList<>();
        this.salesCount = 0; // Initially no sales
        this.id = generateUniqueId(); // Generate unique ID
    }

    // Constructor to initialize food item with optional category parameter
    public FoodItem(String name, double price, boolean available, String category) {
        this(name, category, price, available);
    }



    public String FoodItem(String foodName) {
        return foodName;
    }

    // Method to generate a unique ID for each food item
    private String generateUniqueId() {
        // You can implement your own logic for generating unique IDs (e.g., UUID, counter-based)
        return "ID" + System.currentTimeMillis(); // Simple unique ID based on current time
    }

    // Method to add a review
    public void addReview(String review) {
        reviews.add(review);
        System.out.println("Review added for " + name + ": " + review);
    }

    // Method to retrieve all reviews for the food item
    public List<String> getReviews() {
        return reviews;
    }

    // Getters
    public String getName() { return name; }
    public static String getCategory() { return category; }
    public static double getPrice() { return price; }
    public static boolean isAvailable() { return available ; }
    public int getSalesCount() { return salesCount; }
    public String getId() { return id; }

    // Setters
    public void setPrice(double price) { this.price = price; }
    public void setAvailable(boolean available) { this.available = available; }

    // Increment sales count
    public void incrementSalesCount(int quantity) {
        this.salesCount += quantity;
    }

    // Overriding toString to provide a string representation of the food item
    @Override
    public String toString() {
        return name + " (" + category + ") - ₹" + price + (available ? " [Available]" : " [Unavailable]");
    }
}
