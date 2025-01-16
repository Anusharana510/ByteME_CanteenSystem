import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTesting {

    @Test
    public void testInvalidCustomerLogin() {
        // Initialize the system to load customers
        Byte_Me.initializeSystem();

        // Simulate invalid customer login attempts
        String invalidEmail = "wrong@customer.com";
        String invalidPassword = "wrongpassword";

        boolean loginSuccess = false;
        for (Customer customer : Byte_Me.customers) {
            if (customer.login(invalidEmail, invalidPassword)) {
                loginSuccess = true;
                break;
            }
        }

        // Assert that login is unsuccessful
        assertFalse(loginSuccess, "Customer login should fail for invalid credentials.");
    }

    @Test
    public void testInvalidAdminLogin() {
        // Initialize the system to load the admin
        Byte_Me.initializeSystem();

        // Simulate invalid admin login attempts
        String invalidEmail = "wrong@admin.com";
        String invalidPassword = "wrongpassword";

        boolean loginSuccess = Byte_Me.admin.login(invalidEmail, invalidPassword);

        // Assert that login is unsuccessful
        assertFalse(loginSuccess, "Admin login should fail for invalid credentials.");
    }

    @Test
    public void testValidCustomerLogin() {
        // Initialize the system to load customers
        Byte_Me.initializeSystem();

        // Use valid customer credentials (e.g., the first customer in the list)
        if (Byte_Me.customers.isEmpty()) {
            fail("No customers found to test valid login.");
        }

        String validEmail = Byte_Me.customers.get(0).getEmail();
        String validPassword = Byte_Me.customers.get(0).getPassword();

        boolean loginSuccess = false;
        for (Customer customer : Byte_Me.customers) {
            if (customer.login(validEmail, validPassword)) {
                loginSuccess = true;
                break;
            }
        }

        // Assert that login is successful
        assertTrue(loginSuccess, "Customer login should succeed for valid credentials.");
    }

    @Test
    public void testValidAdminLogin() {
        // Initialize the system to load the admin
        Byte_Me.initializeSystem();

        // Use valid admin credentials
        String validEmail = Byte_Me.admin.getEmail();
        String validPassword = Byte_Me.admin.getPassword();

        boolean loginSuccess = Byte_Me.admin.login(validEmail, validPassword);

        // Assert that login is successful
        assertTrue(loginSuccess, "Admin login should succeed for valid credentials.");
    }
}
