package base;

import controllers.BookCopyControl;
import controllers.BookControl;
import controllers.LoginControl;
import controllers.RegisterUser;
import controllers.SubscriberControl;
import entities.BookCopy;
import entities.Message;
import entities.Subscriber;
import entities.User;
import ocsf.server.ConnectionToClient;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

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

    /*
     * Reads a message from a client and decides what to do with it
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
        actions.put(Action.MARK_BOOK_COPY_AS_LOST, ClientMessageHandler::markBookCopyAsLost);
        actions.put(Action.EXTEND_BORROW_TIME , ClientMessageHandler :: extendBorrowTime);
    }

    public static Message orderBook(Message msg, ConnectionToClient client) {
        BookCopy bookCopy = (BookCopy) msg.getObject();
        Subscriber subscriber = SubscriberControl.getSubscriberById(bookCopy.getBorrowSubscriberId());
        if (subscriber == null || subscriber.isFrozen()) {
            return msg.errorReply("Subscriber is frozen or doesn't exist!");
        }
        BookCopy foundCopy = BookControl.checkBookOrderable(bookCopy.getBookId());
        if (foundCopy == null) {
            bookCopy.setOrderSubscriberId(-1);
            return msg.reply(bookCopy);
        } else {
            bookCopy.setCopyId(foundCopy.getCopyId());
            bookCopy.setReturnDate(foundCopy.getReturnDate());
            if (BookControl.orderBook(bookCopy)) {
                return msg.reply(bookCopy);
            }else{
                return msg.errorReply("Failed to order book!");
            }
        }
    }

    public static Message lendBook(Message msg, ConnectionToClient client) {
        BookCopy bookCopy = (BookCopy) msg.getObject();

        if (bookCopy.getLendDate().isBefore(LocalDate.now())) {
            return msg.errorReply("Lend date is not valid!");
        }

        if (bookCopy.getReturnDate().isBefore(bookCopy.getLendDate()) || bookCopy.getReturnDate().isAfter(bookCopy.getLendDate().plusWeeks(2))) {
            return msg.errorReply("Return date is not valid!");
        }

        Subscriber subscriber = SubscriberControl.getSubscriberById(bookCopy.getBorrowSubscriberId());
        if (subscriber == null || subscriber.isFrozen()) {
            return msg.errorReply("Subscriber is frozen or doesn't exist!");
        }

        if (BookControl.searchBookById(bookCopy.getBookId()) != null) {
            BookCopy foundCopy = BookControl.checkBookLendable(bookCopy.getBookId());
            if (foundCopy == null) {
                bookCopy.setBorrowSubscriberId(-1);
                foundCopy = BookControl.checkBookOrderable(bookCopy.getBookId());
                if (foundCopy == null) {
                    bookCopy.setBorrowSubscriberId(-1);
                    return msg.reply(bookCopy);
                }
                bookCopy.setCopyId(foundCopy.getCopyId());
                bookCopy.setReturnDate(foundCopy.getReturnDate());
                return msg.reply(bookCopy);
            }
            bookCopy.setCopyId(foundCopy.getCopyId());
            if (BookControl.lendBookToSubscriber(bookCopy)) {
                bookCopy.setCopyId(foundCopy.getCopyId());
                return msg.reply(bookCopy);
            } else {
                return msg.errorReply("Failed to lend book!");
            }
        } else {
            return msg.errorReply("Book doesn't exist!");
        }
    }

    /**
     * Handles getting book by id
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
     */
    public static Message login(Message msg, ConnectionToClient client) {
        String[] args = (String[])msg.getObject();
        User user = LoginControl.loginAction(args[0], args[1]);
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
        SubscriberControl.updateInfo((List<String>)msg.getObject());
        return msg.reply("Success");
    }

    public static Message RegisterSubscriber(Message msg, ConnectionToClient client) {
    	String[] args = (String[])msg.getObject();
    	msg = RegisterUser.registerAction(args[0], args[1],args[2],args[3],args[4]);
        return msg;
    }

    public static Message getBorrowTimesReport(Message msg, ConnectionToClient client) {
        Object[] params = (Object[])msg.getObject();
        return msg.reply(BookControl.getBorrowTimesReport((LocalDate)params[0], (Integer)params[1]));
    }

    public static Message getSubscriberStatusReport(Message msg, ConnectionToClient client) {
        return msg.reply(SubscriberControl.getSubscriberStatusReport((LocalDate)msg.getObject()));
    }

    public static Message getReportDates(Message msg, ConnectionToClient client) {
        return msg.reply(SubscriberControl.getReportDates());
    }

    //handles retrieving the borrowed books for a specific subscriber
    public static Message retrieveBorrowedBooks(Message msg , ConnectionToClient client) {
        List<BookCopy> borrowedBooks = BookControl.retrieveBorrowedBooks((int)msg.getObject());
        return msg.reply(borrowedBooks);
    }

    public static Message searchBooks(Message msg, ConnectionToClient client) {
        String[] searchInfo = (String[]) msg.getObject();
        return msg.reply(BookControl.searchBooks(searchInfo[0], searchInfo[1]));
    }

    public static Message searchSubscribers(Message msg, ConnectionToClient client) {
        String[] searchInfo = (String[])msg.getObject();
        return msg.reply(SubscriberControl.searchSubscribers(searchInfo[0], searchInfo[1]));
    }

    public static Message markBookCopyAsLost(Message msg, ConnectionToClient client) {
        return msg.reply(BookControl.markBookCopyAsLost((Integer)msg.getObject()));
    }

    public static Message extendBorrowTime(Message msg , ConnectionToClient client) {
    	boolean successfullyChanged = BookControl.extendBorrowTime((BookCopy )msg.getObject());
    	return msg.reply(successfullyChanged);
    }
}


