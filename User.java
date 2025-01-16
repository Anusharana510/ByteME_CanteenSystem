// Abstract class User that implements IUser interface
public abstract class User {
    // Private fields to store the user's name, email, and password
    private String name;
    private String email;
    private String password;

    // Constructor to initialize the User object with name, email, and password
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Method to handle user login, checking if the provided email and password match

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // Method to handle user logout, displaying a logout message
    public void logout() {
        System.out.println("Logging out...");
    }

    // Getter
    public String getName() {
        return name;
    }

    // Getter
    public String getEmail() {
        return email;
    }

    // Protected method to verify the user's password, accessible to subclasses
    protected boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    // View order history and also save it to a file
    public abstract void viewOrderHistory();

    public String getPassword() {
        return password;
    }
}
