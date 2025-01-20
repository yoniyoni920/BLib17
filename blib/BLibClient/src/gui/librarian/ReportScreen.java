package gui.librarian;

import base.Action;
import entities.Book;
import entities.BorrowReport;
import entities.Message;
import gui.AbstractScreen;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import services.ClientUtils;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;


/**
 * This class manages the report screen for librarians.
 * It provides functionalities to display and interact with various reports such as:
 * - Borrow times for books
 * - Subscriber status over time
 *
 * The class includes methods to:
 * - Initialize the screen with report dates and charts
 * - Search and select books for detailed borrow time reports
 * - Update charts based on selected dates and filters
 * - Handle user interactions with the report screen
 */
public class ReportScreen extends AbstractScreen implements Initializable {
    @FXML
    private LineChart<Integer, Integer> subscriberStatusChart;

    @FXML
    private NumberAxis yAxis;

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

    boolean hasLoadedBorrowTImes = false;
    /**
     * Initializes the report screen.
     * Loads available report dates, sets default selection, and initializes charts.
     *
     * @param location  the location used to resolve relative paths for the root object
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            setSubscriberStatusData();
        }
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
     * @throws Exception if an error occurs during the process
     */
    public void onChoseBook(ActionEvent event) throws Exception{
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
     * @throws Exception if an error occurs during the search process
     */
    public void searchBooks(KeyEvent event) throws Exception {
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
        Message msg = ClientUtils.sendMessage(Action.GET_BORROW_TIMES_REPORT, new Object[] { reportDate.getValue(), bookId });
        borrowTimesChart.getData().clear();

        if (!msg.isError()) {
            List<BorrowReport> reports = (List<BorrowReport>)msg.getObject();
            for (BorrowReport report : reports) {
                XYChart.Series<Integer, String> borrow = new XYChart.Series<>();

                int startD = report.getStartDate().getDayOfMonth();
                int endD = report.getReturnDate().getDayOfMonth();

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

                LocalDate lateDate = report.getLateReturnDate();
                if (lateDate != null) {
                    XYChart.Series<Integer, String> lateReturn = new XYChart.Series<>();

                    // Create XY for end->late borrow
                    XYChart.Data<Integer, String> lateStartData = new XYChart.Data<>(endD, yVal);
                    XYChart.Data<Integer, String> lateEndData = new XYChart.Data<>(lateDate.getDayOfMonth(), yVal);

                    // Add XY to series and series to chart
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
     * Updates the subscriber status chart with data for the selected date.
     */
    public void setSubscriberStatusData() {
        Message msg = ClientUtils.sendMessage(Action.GET_SUBSCRIBER_STATUS_REPORT, reportDate.getValue());

        subscriberStatusChart.getData().clear();

        if (!msg.isError()) {
            int[] days = (int[])msg.getObject();
            XYChart.Series<Integer, Integer> frozen = new XYChart.Series<>();

            int maxSubs = 0;

            for (int i = 0; i < days.length; i++) {
                frozen.getData().add(new XYChart.Data<>(i, days[i]));
                maxSubs = Math.max(maxSubs, days[i]);
            }

            yAxis.setUpperBound(maxSubs + 1);
            subscriberStatusChart.getData().add(frozen);
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
            setSubscriberStatusData();
        }
    }
}
