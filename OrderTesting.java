import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private Admin admin;
    private Menu menu;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        admin = new Admin("Admin", "admin@example.com", "password123");
        menu = new Menu();
        orders = new ArrayList<>();

        // Adding items to the menu
        menu.addFoodItem(new FoodItem("Burger", "Fast Food", 5.99, true));
        menu.addFoodItem(new FoodItem("Pizza", "Fast Food", 8.99, false));
        menu.addFoodItem(new FoodItem("Pasta", "Italian", 6.99, false)); // Out-of-stock item
    }

    @Test
    void testAddOrderWithAvailableItem() {
        // Attempting to order an available item
        FoodItem burger = menu.getFoodItemByName("Burger");
        assertNotNull(burger);
        assertTrue(burger.isAvailable(), "Burger should be available");

        Order order = new Order("Order2", "Pending");
        order.addItem(burger, 2); // Adding an available item
        order.setStatus("Pending");

        // Verify the order is processed
        assertEquals("Pending", order.getStatus(), "Order should be marked as pending for available items");
        assertTrue(order.getItems().stream()
                        .anyMatch(entry -> entry.getKey().equals(burger)),
                "Order should contain the burger item");
    }

    @Test
    void testUpdateItemAvailability() {
        // Ensure Pasta is initially out-of-stock
        FoodItem pasta = menu.getFoodItemByName("Pasta");
        assertNotNull(pasta);
        assertFalse(pasta.isAvailable(), "Pasta should initially be marked as unavailable");

        // Admin updates an out-of-stock item to available
        boolean updated = menu.updateAvailability("Pasta", true); // Directly call the method

        // Verify the update was successful
        assertTrue(updated, "The update method should return true for a valid item");
        assertTrue(pasta.isAvailable(), "Pasta should now be marked as available");
    }


    @Test
    void testAddOrderWithUnavailableItem() {
        // Adding items to the menu
        FoodItem burger = menu.getFoodItemByName("Burger");
        FoodItem pizza = menu.getFoodItemByName("Pizza");
        FoodItem pasta = menu.getFoodItemByName("Pasta"); //Out-of-stock item

        // Ensure the items exist in the menu
        assertNotNull(burger, "Burger should be available in the menu");
        assertNotNull(pizza, "Pizza should be available in the menu");
        assertNotNull(pasta, "Pasta should be available in the menu");

        // Creating an order with both available and unavailable items
        Order order = new Order("Customer3", "Pending");
        order.addItem(burger, 1); // Available item
        order.addItem(pizza, 1);  // Available item
        order.addItem(pasta, 1);  // Unavailable item

        // Check for unavailable items in the order
        List<FoodItem> unavailableItems = new ArrayList<>();
        for (Map.Entry<FoodItem, Integer> entry : order.getItems()) {
            if (!entry.getKey().isAvailable()) {
                unavailableItems.add(entry.getKey());
            }
        }

        // Assert that unavailable items are detected
        assertFalse(unavailableItems.isEmpty(), "There should be at least one unavailable item in the order");
        assertTrue(unavailableItems.contains(pasta), "The unavailable item should be Pasta");

        // Admin sets the order status based on item availability
        if (unavailableItems.isEmpty()) {
            order.setStatus("Pending");
        } else {
            order.setStatus("Denied");
        }

        // Verify the order status is updated correctly
        assertEquals("Denied", order.getStatus(), "Order should be marked as 'Denied' due to unavailable items");
    }
}
