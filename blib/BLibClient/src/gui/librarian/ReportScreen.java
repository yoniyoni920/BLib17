package gui.librarian;

import base.Action;
import entities.*;
import gui.AbstractScreen;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import services.ClientUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * This class manages the report screen for librarians.
 * It provides functionalities to display and interact with various reports such as:
 * - Borrow times for books
 * - Subscriber status over time
 * <br>
 * The class includes methods to:
 * - Initialize the screen with report dates and charts
 * - Search and select books for detailed borrow time reports
 * - Update charts based on selected dates and filters
 * - Handle user interactions with the report screen
 */
public class ReportScreen extends AbstractScreen {
    @FXML
    private LineChart<Integer, String> subscriberStatusChart;

    @FXML
    private NumberAxis borrowYAxis;

    @FXML
    private NumberAxis subscriberYAxis;

    @FXML
    private LineChart<Integer, String> borrowTimesChart;

    @FXML
    private TextField searchBook;

    @FXML
    private ContextMenu searchBooksContextMenu;

    @FXML
    private Tab borrowTimesTab;

    @FXML
    private Tab subscriberStatusTab;

    @FXML
    private ChoiceBox<LocalDate> reportDate;

    @FXML
    private Button clearFilter;

    @FXML private Button clearSubscriberFilter;

    @FXML private TextField searchSubscribers;

    @FXML
    private ContextMenu searchSubscribersContextMenu;

    boolean hasLoadedBorrowTImes = false;

    /**
     * Open the report screen.
     * Loads available report dates, sets default selection, and initializes charts.
     */
    @Override
    public void openScreen(Object... args) {
        Message msg = ClientUtils.sendMessage(Action.GET_REPORT_DATES);
        if (!msg.isError()) {
            List<LocalDate> dates = (List<LocalDate>)msg.getObject();
            for (LocalDate date : dates) {
                reportDate.getItems().add(date);
                if (date.equals(LocalDate.now().minusMonths(1).withDayOfMonth(1))) {
                    reportDate.setValue(date);
                }
            }
        }

        reportDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? date.format(DateTimeFormatter.ofPattern("MMMM yyyy")) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return LocalDate.parse(string + " 1", DateTimeFormatter.ofPattern("MMMM yyyy d"));
            }
        });

        if (!(reportDate.getValue() == null)) {
            setSubscriber(null);
        }
    }

    /**
     * Clears the search filter for subscribers and resets the chart.
     *
     * @param event the ActionEvent triggered by the clear filter button
     */
    public void clearSubscriberFilter(ActionEvent event) {
        setSubscriber(null);
        clearSubscriberFilter.setDisable(true);
        searchSubscribers.setText("");
    }

    /**
     * Clears the search filter for books and resets the chart.
     *
     * @param event the ActionEvent triggered by the clear filter button
     */
    public void clearFilter(ActionEvent event) {
        setBook(null);
        clearFilter.setDisable(true);
        searchBook.setText("");
    }
    /**
     * Handles the selection of a book from the context menu.
     * Updates the chart with the selected book's borrow times.
     *
     * @param event the ActionEvent triggered by the book selection
     */
    public void onChoseBook(ActionEvent event) {
        MenuItem item = (MenuItem)event.getTarget();
        if (item != null) {
            setBook(((Book)item.getUserData()).getId());
            clearFilter.setDisable(false);
        }
    }
    /**
     * Searches for books based on user input and displays results in a context menu.
     *
     * @param event the KeyEvent triggered by typing in the search field
     */
    public void searchBooks(KeyEvent event) {
        String search = searchBook.getText();

        if (!search.isEmpty()) {
            Message msg = ClientUtils.sendMessage(Action.SEARCH_BOOKS, new String[] { search, "title" });
            if (!msg.isError()) {
                List<Book> books = (List<Book>)msg.getObject();

                ObservableList<MenuItem> list = searchBooksContextMenu.getItems();
                list.clear();

                for (Book book : books) {
                    MenuItem item = new MenuItem(book.getTitle());
                    list.add(item);
                    item.setUserData(book);
                }

                if (!searchBooksContextMenu.isShowing()) {
                    Bounds bounds = searchBook.localToScreen(searchBook.getBoundsInLocal());
                    searchBooksContextMenu.show(searchBook, bounds.getMinX(), bounds.getMaxY());
                }
            }
        }
    }

    /**
     * Updates the borrow times chart with data for the selected book and date.
     *
     * @param bookId the ID of the book to display borrow times for, or null for all books
     */
    public void setBook(Integer bookId) {
        LocalDate reportLocalDate = reportDate.getValue();
        Message msg = ClientUtils.sendMessage(Action.GET_BORROW_TIMES_REPORT, new Object[] { reportLocalDate, bookId });
        borrowTimesChart.getData().clear();

        borrowYAxis.setUpperBound(reportLocalDate.getMonth().maxLength()+1);

        if (!msg.isError()) {
            List<BorrowReport> reports = (List<BorrowReport>)msg.getObject();
            for (BorrowReport report : reports) {
                XYChart.Series<Integer, String> borrow = new XYChart.Series<>();

                LocalDate date = report.getStartDate();
                LocalDate endDate = report.getReturnDate();

                // Handles things that occur outside the report's month
                int startD = date.getDayOfMonth();
                if (!date.getMonth().equals(reportLocalDate.getMonth())) {
                    startD = 0;
                }
                int endD = endDate.getDayOfMonth();
                if (!endDate.getMonth().equals(reportLocalDate.getMonth())) {
                    endD = 32; // Doesn't matter, just so it looks like it passes through the chart
                }

                String yVal =  report.getBook().getTitle() + " (Copy " + report.getBookCopyId() + ")";

                // Create XY for start->end borrow
                XYChart.Data<Integer, String> startData = new XYChart.Data<>(startD, yVal);
                XYChart.Data<Integer, String> endData = new XYChart.Data<>(endD, yVal);

                // Add XY to series and series to chart
                borrow.getData().addAll(startData, endData);
                borrowTimesChart.getData().add(borrow);

                // Set styles
                startData.getNode().setStyle("-fx-background-color: #04b0bd");
                endData.getNode().setStyle("-fx-background-color: #04b0bd");
                borrow.getNode().setStyle("-fx-stroke: #04b0bd;");

                if (report.isLate()) {
                    LocalDate lateDate = report.getLateReturnDate();

                    int lateEndD = lateDate != null ? lateDate.getDayOfMonth() : 32;
                    if (lateDate != null && !lateDate.getMonth().equals(reportLocalDate.getMonth())) {
                        lateEndD = 32;
                    }

                    // Create XY for end->late borrow
                    XYChart.Data<Integer, String> lateStartData = new XYChart.Data<>(endD, yVal);
                    XYChart.Data<Integer, String> lateEndData = new XYChart.Data<>(lateEndD, yVal);

                    // Add XY to series and series to chart
                    XYChart.Series<Integer, String> lateReturn = new XYChart.Series<>();
                    lateReturn.getData().addAll(lateStartData, lateEndData);
                    borrowTimesChart.getData().add(lateReturn);

                    // Set styles
                    lateStartData.getNode().setStyle("-fx-background-color: red");
                    lateEndData.getNode().setStyle("-fx-background-color: red");
                    lateReturn.getNode().setStyle("-fx-stroke: red;");
                }
            }
        }
    }

    /**
     * This method searches for subscribers based on the input in the search field.
     * It sends the search query to the server and populates the context menu with results.
     *
     * @param event The KeyEvent triggered by the user's input.
     * @throws Exception If there is an error processing the search query.
     */
    public void searchSubscribers(KeyEvent event) throws Exception {
        String search = searchSubscribers.getText();

        if (!search.isEmpty()) {
            Message msg = ClientUtils.sendMessage(Action.SEARCH_SUBSCRIBERS, new String[] { search, "first_name" });
            if (!msg.isError()) {
                List<Subscriber> subs = (List<Subscriber>)msg.getObject();

                ObservableList<MenuItem> list = searchSubscribersContextMenu.getItems();
                list.clear();

                for (Subscriber sub : subs) {
                    MenuItem item = new MenuItem(sub.getName() + " (" + sub.getId() + ")");
                    list.add(item);
                    item.setUserData(sub);
                }

                if (!searchSubscribersContextMenu.isShowing()) {
                    Bounds bounds = searchSubscribers.localToScreen(searchSubscribers.getBoundsInLocal());
                    searchSubscribersContextMenu.show(searchSubscribers, bounds.getMinX(), bounds.getMaxY());
                }
            }
        }
    }

    /**
     * Handles the selection of a subscriber from the context menu.
     * Updates the chart with the selected subscriber's status report.
     *
     * @param event the ActionEvent triggered by the book selection
     * @throws Exception if an error occurs during the process
     */
    public void onChoseSubscriber(ActionEvent event) throws Exception {
        MenuItem item = (MenuItem)event.getTarget();
        if (item != null) {
            setSubscriber(((Subscriber)item.getUserData()).getId());
            clearSubscriberFilter.setDisable(false);
        }
    }

    /**
     * Updates the subscriber status chart with data for the selected subscriber and date.
     *
     * @param subscriberId the ID of the subscriber to display subscriber status for, or null for all subscribers
     */
    public void setSubscriber(Integer subscriberId) {
        LocalDate reportLocalDate = reportDate.getValue();
        Message msg = ClientUtils.sendMessage(Action.GET_SUBSCRIBER_STATUS_REPORT, new Object[] { reportLocalDate, subscriberId });
        subscriberStatusChart.getData().clear();

        subscriberYAxis.setUpperBound(reportLocalDate.getMonth().maxLength()+1);

        if (!msg.isError()) {
            List<SubscriberStatusReport> reports = (List<SubscriberStatusReport>)msg.getObject();

            System.out.println(reports.size());


            for (SubscriberStatusReport report : reports) {
                XYChart.Series<Integer, String> freeze = new XYChart.Series<>();

                LocalDate date = report.getDate();
                LocalDate endDate = report.getEndDate();

                int startD = report.getDate().getDayOfMonth();
                if (!date.getMonth().equals(reportLocalDate.getMonth())) {
                    startD = 0;
                }

                int endD = endDate.getDayOfMonth();
                if (!endDate.getMonth().equals(reportLocalDate.getMonth())) {
                    endD = 32; // Doesn't matter, just so it looks like it passes through the chart
                }

                String yVal = report.getName() + " (" + report.getUserIdId() + ")";

                // Create XY for start->end freeze
                XYChart.Data<Integer, String> startData = new XYChart.Data<>(startD, yVal);
                XYChart.Data<Integer, String> endData = new XYChart.Data<>(endD, yVal);

                // Add XY to series and series to chart
                freeze.getData().addAll(startData, endData);
                subscriberStatusChart.getData().add(freeze);

                // Set styles
                startData.getNode().setStyle("-fx-background-color: red");
                endData.getNode().setStyle("-fx-background-color: red");
                freeze.getNode().setStyle("-fx-stroke: red;");


            }
        }
    }

    /**
     * Handles tab selection changes.
     * Loads borrow times data if the borrow times tab is selected for the first time.
     *
     * @param event the Event triggered by tab selection
     */
    public void onSelectionChanged(Event event) {
        if (borrowTimesTab != null && borrowTimesTab.isSelected() && !hasLoadedBorrowTImes) {
            setBook(null);
            hasLoadedBorrowTImes = true;
        }
    }
    /**
     * Handles the selection of a report date.
     * Updates the corresponding chart based on the selected tab and date.
     *
     * @param event the ActionEvent triggered by selecting a date
     */
    public void onChoseDate(ActionEvent event) {
        if (borrowTimesTab.isSelected()) {
            setBook(null);
        } else {
            setSubscriber(null);
        }
    }
}
