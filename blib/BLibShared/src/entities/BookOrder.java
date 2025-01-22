package entities;

import java.io.Serializable;
import java.time.LocalDate;

public class BookOrder implements Serializable {
    private int orderId;
    private int subscriberId;
    private int bookId;
    private LocalDate orderDate;
    private LocalDate untilDate;

    public BookOrder(int orderId, int subscriberId, int bookId, LocalDate orderDate, LocalDate untilDate) {
        this.orderId = orderId;
        this.subscriberId = subscriberId;
        this.bookId = bookId;
        this.orderDate = orderDate;
        this.untilDate = untilDate;
    }

    public LocalDate getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(LocalDate untilDate) {
        this.untilDate = untilDate;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
