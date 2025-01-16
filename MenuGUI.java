import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MenuGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextArea menuDisplay;
    private Menu menu;

    public MenuGUI(Menu menu) {
        this.menu = menu;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        // Set up the frame
        frame = new JFrame("Menu Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        // Panel for components
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Display area for menu
        menuDisplay = new JTextArea(20, 50);
        menuDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(menuDisplay);

        // Buttons and input fields
        JPanel buttonPanel = new JPanel();
        JButton viewMenuButton = new JButton("View Menu");
        JButton addFoodItemButton = new JButton("Add Item");
        JButton searchByCategoryButton = new JButton("Search by Category");
        JButton updatePriceButton = new JButton("Update Price");

        buttonPanel.add(viewMenuButton);
        buttonPanel.add(addFoodItemButton);
        buttonPanel.add(searchByCategoryButton);
        buttonPanel.add(updatePriceButton);

        // Add action listeners to buttons
        viewMenuButton.addActionListener(e -> displayMenu());
        addFoodItemButton.addActionListener(e -> addFoodItem());
        searchByCategoryButton.addActionListener(e -> searchByCategory());
        updatePriceButton.addActionListener(e -> updatePrice());

        // Add components to the panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to the frame
        frame.add(panel);
        frame.setVisible(true);
    }

    private void displayMenu() {
        List<FoodItem> menuItems = menu.getMenuItems();
        StringBuilder display = new StringBuilder("Current Menu:\n");
        for (FoodItem item : menuItems) {
            display.append(item.toString()).append("\n");
        }
        menuDisplay.setText(display.toString());
    }

    private void addFoodItem() {
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JCheckBox availableCheckBox = new JCheckBox("Available");

        Object[] message = {
                "Name:", nameField,
                "Category:", categoryField,
                "Price:", priceField,
                "Available:", availableCheckBox
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add Food Item", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                String category = categoryField.getText();
                double price = Double.parseDouble(priceField.getText());
                boolean available = availableCheckBox.isSelected();

                FoodItem newItem = new FoodItem(name, category, price, available);
                menu.addFoodItem(newItem);
                JOptionPane.showMessageDialog(frame, "Food item added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input for price.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchByCategory() {
        String category = JOptionPane.showInputDialog(frame, "Enter category to search:");
        if (category != null && !category.isEmpty()) {
            List<FoodItem> results = menu.searchByCategory(category);
            if (results.isEmpty()) {
                menuDisplay.setText("No items found in category: " + category);
            } else {
                StringBuilder display = new StringBuilder("Items in category '" + category + "':\n");
                for (FoodItem item : results) {
                    display.append(item.toString()).append("\n");
                }
                menuDisplay.setText(display.toString());
            }
        }
    }

    private void updatePrice() {
        String name = JOptionPane.showInputDialog(frame, "Enter name of the item to update price:");
        if (name != null && !name.isEmpty()) {
            String priceInput = JOptionPane.showInputDialog(frame, "Enter new price:");
            try {
                double newPrice = Double.parseDouble(priceInput);
                menu.updatePrice(name, newPrice);
                JOptionPane.showMessageDialog(frame, "Price updated successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input for price.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        new MenuGUI(menu);
    }
}
