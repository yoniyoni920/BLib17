package entities;

/**
 * An ENUM contains all the subscriber's history actions and can be displayed for librarian and subscriber
 */
public enum HistoryAction {
    LOST_BOOK,
    BORROW_BOOK,
    LATE_RETURN,
    FREEZE_SUBSCRIBER,
    RETURN_BOOK,
    LOGIN_SUBSCRIBER,
    EXTEND_BORROW_SUBSCRIBER,
    EXTEND_BORROW_LIBRARIAN,
    ORDER_BOOK
}