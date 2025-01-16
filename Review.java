public class Review {
    private final String customerName;
    private final String text;
    private final int rating;

    public Review(String customerName, String text, int rating) {
        this.customerName = customerName;
        this.text = text;
        this.rating = rating;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }
}
