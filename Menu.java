import java.util.*;

public class Menu {
    private List<FoodItem> menuItems; // List to store menu items

    // Constructor to initialize the menu with default items
    public Menu() {
        menuItems = new ArrayList<>(Arrays.asList(
                new FoodItem("Burger", "Fast Food", 5.99, true),
                new FoodItem("Pasta", "Italian", 7.49, false),
                new FoodItem("Salad", "Healthy", 4.99, true),
                new FoodItem("Pizza", "Italian", 8.99, false),
                new FoodItem("Sushi", "Japanese", 12.99, true),
                new FoodItem("Fries", "Fast Food", 2.99, true),
                new FoodItem("Tacos", "Mexican", 3.49, true),
                new FoodItem("Burrito", "Mexican", 6.99, true),
                new FoodItem("Smoothie", "Beverages", 4.49, true),
                new FoodItem("Latte", "Beverages", 3.99, true)
        ));
    }

    // Method to view all items in the menu
    public void viewMenu() {
        System.out.println("\nCurrent Menu:");
        for (FoodItem item : menuItems) {
            System.out.println(item); // Print each item in the menu
        }
    }

    // Method to add a new food item to the menu
    public void addFoodItem(FoodItem item) {
        menuItems.add(item); // Add food item to menuItems list
        System.out.println(item.getName() + " has been added to the menu.");
    }

    // Method to add a review to a food item
    public void addReview(String itemName, String review) {
        FoodItem item = getFoodItemByName(itemName);
        if (item != null) {
            item.addReview(review);
            System.out.println("Review added for " + itemName);
        } else {
            System.out.println(itemName + " not found in the menu.");
        }
    }

    // Method to view all reviews for a food item
    public void viewItemReviews(String itemName) {
        FoodItem item = getFoodItemByName(itemName);
        if (item != null) {
            List<String> reviews = item.getReviews();
            System.out.println("Reviews for " + itemName + ":");
            if (reviews.isEmpty()) {
                System.out.println("No reviews yet.");
            } else {
                for (String review : reviews) {
                    System.out.println("- " + review);
                }
            }
        } else {
            System.out.println(itemName + " not found in the menu.");
        }
    }

    // Method to get all reviews for a specific food item by its name
    public List<String> getReviews(String itemName) {
        FoodItem item = getFoodItemByName(itemName);
        if (item != null) {
            return item.getReviews();
        } else {
            System.out.println(itemName + " not found in the menu.");
            return new ArrayList<>(); // Return empty list if item not found
        }
    }

    // Method to remove a food item from the menu by name
    public boolean removeFoodItem(String name) {
        boolean removed = menuItems.removeIf(item -> item.getName().equalsIgnoreCase(name));
        if (removed) {
            System.out.println(name + " has been removed from the menu.");
        } else {
            System.out.println(name + " not found in the menu.");
        }
        return removed;
    }

    // Method to update the price of a food item
    public void updatePrice(String name, double newPrice) {
        FoodItem item = getFoodItemByName(name);
        if (item != null) {
            item.setPrice(newPrice);
            System.out.println("Price of " + name + " updated to " + newPrice);
        } else {
            System.out.println(name + " not found in the menu.");
        }
    }

    public boolean updateAvailability(String itemName, boolean isAvailable) {
        FoodItem item = getFoodItemByName(itemName);
        if (item != null) {
            item.setAvailable(isAvailable);
            System.out.println("Availability of " + itemName + " updated to " + isAvailable);
            return true;
        } else {
            System.out.println(itemName + " not found in the menu.");
            return false;
        }
    }


    // Method to search for food items by category
    public List<FoodItem> searchByCategory(String category) {
        List<FoodItem> result = new ArrayList<>();
        for (FoodItem item : menuItems) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                result.add(item);
            }
        }
        return result;
    }

    // Method to sort food items by price
    public List<FoodItem> sortByPrice() {
        List<FoodItem> sortedList = new ArrayList<>(menuItems);
        sortedList.sort(Comparator.comparingDouble(foodItem -> foodItem.getPrice()));
        return sortedList;
    }

    // Method to search for food items by name (partial match)
    public List<FoodItem> searchItems(String query) {
        List<FoodItem> results = new ArrayList<>();
        for (FoodItem item : menuItems) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    // Method to retrieve a FoodItem by name
    public FoodItem getFoodItemByName(String name) {
        for (FoodItem item : menuItems) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    // Method to return the menu items (getter)
    public List<FoodItem> getMenuItems() {
        return menuItems;
    }

    public static void main(String[] args) {
        Menu menu = new Menu();

        // Display the initial menu
        menu.viewMenu();

        // Example operations
        System.out.println("\nAdding new item...");
        menu.addFoodItem(new FoodItem("Tea", "Beverages", 2.50, true));

        System.out.println("\nUpdating Pizza price...");
        menu.updatePrice("Pizza", 9.99);

        System.out.println("\nUpdating availability of Fries...");
        boolean updated = menu.updateAvailability("Fries", false);
        if (updated) {
            System.out.println("Fries availability updated.");
        }

        System.out.println("\nRemoving Sushi...");
        menu.removeFoodItem("Sushi");

        // Display updated menu
        System.out.println("\nUpdated Menu:");
        menu.viewMenu();

        System.out.println("\nItems in 'Italian' category:");
        List<FoodItem> italianItems = menu.searchByCategory("Italian");
        italianItems.forEach(System.out::println);

        System.out.println("\nSearching for items containing 'Smoothie':");
        List<FoodItem> searchResult = menu.searchItems("Smoothie");
        searchResult.forEach(System.out::println);

        System.out.println("\nItems sorted by price:");
        List<FoodItem> sortedItems = menu.sortByPrice();
        sortedItems.forEach(System.out::println);
    }


    public List<FoodItem> filterByCategory(String category) {
        List<FoodItem> filteredItems = new ArrayList<>();
        for (FoodItem item : menuItems) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                filteredItems.add(item);
            }
        }
        if (filteredItems.isEmpty()) {
            System.out.println("No items found in the '" + category + "' category.");
        }
        return filteredItems;
    }
    public String getcategory() {
        return getcategory();
    }



}
