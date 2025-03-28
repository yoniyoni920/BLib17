package base;

import controllers.BookControl;
import controllers.LoginControl;
import controllers.NotificationControl;
import controllers.SubscriberControl;
import entities.*;
import ocsf.server.ConnectionToClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Handles the decision of what to do with each Message incoming to the server
 * This decouples that from the LibraryServer, letting it focus on one thing, being a server.
 */
public class ClientMessageHandler {
    /* ------- Logic -------*/
    // This codes handles how actions work
    private Map<Action, BiFunction<Message, ConnectionToClient, Message>> actions;
    public static ClientMessageHandler instance;

    public static ClientMessageHandler getInstance() {
        if (instance == null) {
            instance = new ClientMessageHandler();
        }
        return instance;
    }

    // Define all actions here
    private ClientMessageHandler() {
        actions = new HashMap<>();
        setupActions();
    }

    /**
     * Reads a message from a client and decides what to do with it
     * @param msgFromClient The message to handle
     * @param client The client that sent the message
     * @return A reply to the message or error in case it can't be handled
     */
    public static Message handleMessage(Message msgFromClient, ConnectionToClient client) {
        Action actionName = msgFromClient.getAction();

        BiFunction<Message, ConnectionToClient, Message> actionFunc = getInstance().actions.get(actionName);
        if (actionFunc != null) {
            return actionFunc.apply(msgFromClient, client);
        }

        return msgFromClient.errorReply("Found no such action! " + msgFromClient.getAction());
    }

    /* ------- Actions -------*/

    /**
     * Method defining each action and reference to the method it will call
     */
    private void setupActions() {
        actions.put(Action.LOGIN, ClientMessageHandler::login);
        actions.put(Action.UPDATE_SUBSCRIBER, ClientMessageHandler::updateSubscriber);
        actions.put(Action.GET_BOOK_BY_ID, ClientMessageHandler::getBookById);
        actions.put(Action.GET_SUBSCRIBER_BY_ID, ClientMessageHandler::getSubscriberById);
        actions.put(Action.LEND_BOOK, ClientMessageHandler::lendBook);
        actions.put(Action.REGISTER, ClientMessageHandler::RegisterSubscriber);
        actions.put(Action.GET_BORROW_TIMES_REPORT, ClientMessageHandler::getBorrowTimesReport);
        actions.put(Action.GET_SUBSCRIBER_STATUS_REPORT, ClientMessageHandler::getSubscriberStatusReport);
        actions.put(Action.GET_REPORT_DATES, ClientMessageHandler::getReportDates);
        actions.put(Action.RETRIEVE_BORROWEDBOOKS, ClientMessageHandler::retrieveBorrowedBooks);
        actions.put(Action.SEARCH_BOOKS, ClientMessageHandler::searchBooks);
        actions.put(Action.ORDER_BOOK, ClientMessageHandler::orderBook);
        actions.put(Action.SEARCH_SUBSCRIBERS, ClientMessageHandler::searchSubscribers);
        actions.put(Action.RETURN_BOOK, ClientMessageHandler::returnBook);
        actions.put(Action.MARK_BOOK_COPY_AS_LOST, ClientMessageHandler::markBookCopyAsLost);
        actions.put(Action.EXTEND_BORROW_TIME, ClientMessageHandler::extendBorrowTime);
        actions.put(Action.RETRIEVE_NOTIFICATIONS, ClientMessageHandler::retrieveNotifications);
        actions.put(Action.UPDATE_NOTIFICATION_STATUS, ClientMessageHandler::updateNotificationStatus);
        actions.put(Action.GET_HISTORY, ClientMessageHandler::getSubscriberHistory);
    }

    /**
     * Function to handle output from book control after ordering book
     * @param msg
     * @param client
     * @return
     */
    public static Message orderBook(Message msg, ConnectionToClient client) {
        BookOrder bookOrder = (BookOrder) msg.getObject();
        Subscriber subscriber = SubscriberControl.getSubscriberBySubscriberId(bookOrder.getSubscriberId());
        if (subscriber == null || subscriber.isFrozen()) {
            return msg.errorReply("Subscriber is frozen or doesn't exist!");
        }
        if (BookControl.checkBookLendable(bookOrder.getBookId(), bookOrder.getSubscriberId()) != 0) {
            return msg.errorReply("Book is lendable, Can't be ordered!");
        }
        LocalDateTime orderable = BookControl.checkBookOrderable(bookOrder.getBookId());
        if (orderable == null) {
            bookOrder.setOrderId(-1);
            return msg.reply(bookOrder);
        } else {
            bookOrder.setOrderDate(orderable);
            bookOrder.setOrderId(0);
            if (BookControl.orderBook(bookOrder)) {
                return msg.reply(bookOrder);
            } else {
                return msg.errorReply("Failed to order book!");
            }
        }
    }

    /**
     * Function to handle output from book control after lending
     * @param msg
     * @param client
     * @return
     */
    public static Message lendBook(Message msg, ConnectionToClient client) {
        BookCopy bookCopy = (BookCopy)msg.getObject();

        if (bookCopy.getLendDate().isBefore(LocalDateTime.now().minusWeeks(2))) {
            return msg.errorReply("Lend date is not valid!");
        }

        if (bookCopy.getReturnDate().isBefore(bookCopy.getLendDate()) || bookCopy.getReturnDate().isAfter(bookCopy.getLendDate().plusWeeks(2))) {
            return msg.errorReply("Return date is not valid!");
        }

        Subscriber subscriber = SubscriberControl.getSubscriberBySubscriberId(bookCopy.getBorrowSubscriberId());
        if (subscriber == null || subscriber.isFrozen()) {
            return msg.errorReply("Subscriber is frozen or doesn't exist!");
        }

        if (BookControl.searchBookById(bookCopy.getBookId()) != null) {
            int availableCopy = BookControl.checkBookLendable(bookCopy.getBookId(), bookCopy.getBorrowSubscriberId());
            if (availableCopy != 0) {
                bookCopy.setCopyId(availableCopy);
                if (BookControl.lendBookToSubscriber(bookCopy)) {
                    return msg.reply(bookCopy);
                } else {
                    return msg.errorReply("Failed to lend book!");
                }
            } else {
                LocalDateTime orderableDate = BookControl.checkBookOrderable(bookCopy.getBookId());
                if (orderableDate != null) {
                    BookOrder bookOrder = new BookOrder(0, bookCopy.getBorrowSubscriberId(), bookCopy.getBookId(),
                            orderableDate, null);
                    return msg.reply(bookOrder);
                } else {
                    return msg.reply(null);
                }
            }
        } else {
            return msg.errorReply("Book doesn't exist!");
        }
    }

    /**
     * Handles getting book by id
     * @param msg
     * @param client
     * @return
     */
    public static Message getBookById(Message msg, ConnectionToClient client) {

        Integer bookId = null;
        try {
            bookId = Integer.parseInt(msg.getObject().toString());
        } catch (NumberFormatException e) {
            return msg.errorReply("Book ID is not valid!");
        }
        Book book = BookControl.searchBookById(bookId);
        if (book == null) {
            return msg.errorReply("Book not found!");
        } else {
            return msg.reply(book);
        }
    }

    /**
     * Handles getting subscriber by id
     * @param msg
     * @param client
     * @return
     */
    public static Message getSubscriberById(Message msg, ConnectionToClient client) {
        Integer subscriberId = null;
        try {
            subscriberId = Integer.parseInt(msg.getObject().toString());
        } catch (NumberFormatException e) {
            return msg.errorReply("Subscriber ID is not valid!");
        }
        Subscriber subscriber = SubscriberControl.getSubscriberById(subscriberId);
        if (subscriber == null) {
            return msg.errorReply("Subscriber not found!");
        }
        return msg.reply(subscriber);
    }

    /**
     * Handles logging subscribers or librarians
     * @param msg
     * @param client
     * @return
     */
    public static Message login(Message msg, ConnectionToClient client) {
        String[] args = (String[]) msg.getObject();
        User user = LoginControl.login(Integer.parseInt(args[0]), args[1]);
        if (user != null) {
            return msg.reply(user);
        }
        return msg.errorReply("Couldn't login user! ID may be wrong.");
    }

    /**
     * Handles updating info of a subscriber
     *
     * @param msg
     * @param client
     * @return Message
     */
    public static Message updateSubscriber(Message msg, ConnectionToClient client) {
        SubscriberControl.updateInfo((List<String>) msg.getObject());
        return msg.reply("Success");
    }

    /**
     * Handles output message from login control
     * @param msg
     * @param client
     * @return
     */
    public static Message RegisterSubscriber(Message msg, ConnectionToClient client) {
        String[] args = (String[]) msg.getObject();
        User user = LoginControl.register(args[0], args[1], args[2], args[3], args[4]);
        if (user != null) {
            return msg.reply(user);
        } else {
            return msg.errorReply("Couldn't register user!");
        }
    }

    /**
     * Handles borrow time reports output from book control
     * @param msg
     * @param client
     * @return
     */
    public static Message getBorrowTimesReport(Message msg, ConnectionToClient client) {
        Object[] params = (Object[]) msg.getObject();
        return msg.reply(BookControl.getBorrowTimesReport((LocalDate) params[0], (Integer)params[1]));
    }

    /**
     * Handles subscribe status report output from subscriber control
     * @param msg
     * @param client
     * @return
     */
    public static Message getSubscriberStatusReport(Message msg, ConnectionToClient client) {
        Object[] params = (Object[]) msg.getObject();
        return msg.reply(SubscriberControl.getSubscriberStatusReport((LocalDate)params[0], (Integer)params[1]));
    }

    /**
     * Gets report dates from subscriber control
     * @param msg
     * @param client
     * @return
     */
    public static Message getReportDates(Message msg, ConnectionToClient client) {
        return msg.reply(SubscriberControl.getReportDates());
    }

    /**
     * handles retrieving the borrowed books for a specific subscriber
     * @param msg
     * @param client
     * @return
     */
    public static Message retrieveBorrowedBooks(Message msg, ConnectionToClient client) {
        List<BookCopy> borrowedBooks = BookControl.retrieveBorrowedBooks((int) msg.getObject());
        return msg.reply(borrowedBooks);
    }

    /**
     * Gets books to display depending on search and search type.
     * @param msg
     * @param client
     * @return
     */
    public static Message searchBooks(Message msg, ConnectionToClient client) {
        String[] searchInfo = (String[]) msg.getObject();
        return msg.reply(BookControl.searchBooks(searchInfo[0], searchInfo[1]));
    }

    /**
     * Calls the book control to return the book
     * @param msg
     * @param client
     * @return
     */
    public static Message returnBook(Message msg, ConnectionToClient client) {
        int bookCopyId = (Integer) msg.getObject();
        BookControl.returnBook(bookCopyId);
        return msg.reply("Success");
    }

    /**
     * Gets subscriber/s from subscriber control
     * @param msg
     * @param client
     * @return
     */
    public static Message searchSubscribers(Message msg, ConnectionToClient client) {
        String[] searchInfo = (String[]) msg.getObject();
        return msg.reply(SubscriberControl.searchSubscribers(searchInfo[0], searchInfo[1]));
    }

    /**
     * Marks copy as lost
     * @param msg
     * @param client
     * @return
     */
    public static Message markBookCopyAsLost(Message msg, ConnectionToClient client) {
        return msg.reply(BookControl.markBookCopyAsLost((Integer) msg.getObject()));
    }

    /**
     * Extends the borrow time of a book
     * @param msg
     * @param client
     * @return
     */
    public static Message extendBorrowTime(Message msg, ConnectionToClient client) {
        if (BookControl.extendBorrowTime((BookCopy)msg.getObject(), msg.getUser())) {
            return msg.successReply();
        } else {
            return msg.errorReply("Couldn't extend borrow time!");
        }
    }

    /**
     * Updates notifications for librarian
     * @param msg
     * @param client
     * @return
     */
    public static Message updateNotificationStatus(Message msg, ConnectionToClient client) {
        @SuppressWarnings("unchecked")
        boolean successfullyUpdated = NotificationControl.updateNotificationStatus((List<Notification>) msg.getObject());
        if (!successfullyUpdated) {
            msg.setError(true);
        }
        return msg.reply(successfullyUpdated);
    }

    /**
     * Retrieves notification from notification control
     * @param msg
     * @param client
     * @return
     */
    public static Message retrieveNotifications(Message msg, ConnectionToClient client) {
        List<Notification> notifications = NotificationControl.retrieveNotifications();
        if (notifications == null) {
            msg.setError(true);
        }
        return msg.reply(notifications);
    }

    /**
     * @param msg
     * @param client
     * @return The history of the subscriber
     */
    public static Message getSubscriberHistory(Message msg, ConnectionToClient client) {
        List<HistoryEntry> history = SubscriberControl.getSubscriberHistory((int)msg.getObject());
        return msg.reply(history);
    }
}


