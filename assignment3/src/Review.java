import java.util.Date;

public class Review {
    private String customerUsername;
    private String comment;
    private int rating;  // Rating out of 5
    private Date date;

    public Review(String customerUsername, String comment, int rating) {
        this.customerUsername = customerUsername;
        this.comment = comment;
        this.rating = rating;
        this.date = new Date();
    }

    @Override
    public String toString() {
        return "Rating: " + rating + "/5\n" + "Comment: " + comment + "\n" + "By: " + customerUsername + " on " + date;
    }
}
