package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookOrder implements Serializable {
    private int orderId;
    private int subscriberId;
    private int bookId;
    private LocalDateTime orderDate;
    private LocalDateTime untilDate;

    public BookOrder(int orderId, int subscriberId, int bookId, LocalDateTime orderDate, LocalDateTime untilDate) {
        this.orderId = orderId;
        this.subscriberId = subscriberId;
        this.bookId = bookId;
        this.orderDate = orderDate;
        this.untilDate = untilDate;
    }

    public BookOrder(int subscriberId, int bookId) {
        this.subscriberId = subscriberId;
        this.bookId = bookId;
    }

    public LocalDateTime getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(LocalDateTime untilDate) {
        this.untilDate = untilDate;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
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
