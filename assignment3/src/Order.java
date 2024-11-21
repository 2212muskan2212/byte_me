import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    private final ArrayList<CartItem> items;
    private final LocalDateTime orderTime;
    private String status;
    private String specialRequest;

    public Order(ArrayList<CartItem> items, String specialRequest) {
        this.items = items;
        this.orderTime = LocalDateTime.now();
        this.status = "Pending";
        this.specialRequest = specialRequest;

    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }


    public ArrayList<CartItem> getItems() {
        return items;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void cancelOrder() {
        this.status = "Canceled";
    }

    public void completeOrder() {
        this.status = "Completed";
    }

    @Override
    public String toString() {
        return "Order{" +
                "items=" + items +
                ", orderTime=" + orderTime +
                ", status='" + status + '\'' +
                ", specialRequest='" + specialRequest + '\'' + // Include specialRequest
                '}';
    }
}
