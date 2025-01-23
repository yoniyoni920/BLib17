package gui.librarian;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import base.Action;
import entities.Message;
import entities.Notification;
import entities.Subscriber;
import gui.AbstractScreen;
import gui.SubscriberCardScreen;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import services.ClientUtils;
import services.InterfaceUtils;

/**
 * NotificationsScreen is a JavaFX controller class responsible for managing
 * the display of notifications for the librarian interface. It displays new
 * and all notifications in separate TableViews, allowing the user to interact
 * with the data and perform actions like viewing subscriber details.
 *
 * This class extends {@code AbstractScreen} to integrate with the application's
 * screen management system.
 */
public class NotificationsScreen extends AbstractScreen {

    /**
     * TableView to display new notifications.
     */
    @FXML
    private TableView<Notification> newNotifications;

    /**
     * TableView to display all notifications.
     */
    @FXML
    private TableView<Notification> allNotifications;

    /**
     * Method executed when the screen starts. It initializes the data and configures the TableViews.
     */
    @Override
    public void openScreen(Object... args) {
        loadData();
        prepareTableView();
    }

    /**
     * Loads notification data from the server and populates the TableViews.
     * 
     * <p>
     * Notifications are retrieved from the server using the {@code RETRIEVE_NOTIFICATIONS}
     * action. New notifications are added to the {@code newNotifications} TableView,
     * while all notifications are added to the {@code allNotifications} TableView.
     * </p>
     */
    @SuppressWarnings("unchecked")
    private void loadData() {
        Message msgFromServer = ClientUtils.sendMessage(Action.RETRIEVE_NOTIFICATIONS, null);
        
        // If couldn't retrieve the notifications
        if (msgFromServer.isError()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Couldn't retrieve the notifications");
            alert.showAndWait();
            screenManager.closeScreen();
            return;
        }
        
        // If notifications were retrieved successfully
        List<Notification> list = (List<Notification>) msgFromServer.getObject();
        allNotifications.getItems().addAll(list);
        for (Notification n : list) {
            if (n.getIsNew()) {
                newNotifications.getItems().add(n);
            }
        }
    }

    /**
     * Configures the TableViews by adding columns for subscriber, message, and date.
     * 
     * <p>
     * Each column is customized to display specific attributes of the Notification objects,
     * including a button in the "From" column that allows the user to view details about
     * the subscriber.
     * </p>
     */
    private void prepareTableView() {
        TableColumn<Notification, String> messageColumnAll = new TableColumn<>("Message");
        messageColumnAll.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMessage()));
        messageColumnAll.prefWidthProperty().bind(newNotifications.widthProperty().multiply(0.65));
        
        TableColumn<Notification, String> messageColumnNew = new TableColumn<>("Message");
        messageColumnNew.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMessage()));
        messageColumnNew.prefWidthProperty().bind(newNotifications.widthProperty().multiply(0.65));

        TableColumn<Notification, String> dateColumnAll = new TableColumn<>("Date");
        dateColumnAll.setCellValueFactory(cell -> new SimpleStringProperty(
            InterfaceUtils.formatDate(cell.getValue().getDate())
        ));
        dateColumnAll.prefWidthProperty().bind(newNotifications.widthProperty().multiply(0.15));
        
        TableColumn<Notification, String> dateColumnNew = new TableColumn<>("Date");
        dateColumnNew.setCellValueFactory(cell -> new SimpleStringProperty(
                InterfaceUtils.formatDate(cell.getValue().getDate())
        ));
        dateColumnNew.prefWidthProperty().bind(newNotifications.widthProperty().multiply(0.15));

        TableColumn<Notification, Button> subscriberCardColumnAll = getSubscriberCardColumn();
        TableColumn<Notification, Button> subscriberCardColumnNew = getSubscriberCardColumn();

        allNotifications.getColumns().addAll(subscriberCardColumnAll, messageColumnAll, dateColumnAll);
        newNotifications.getColumns().addAll(subscriberCardColumnNew, messageColumnNew, dateColumnNew);
    }

    /**
     * Creates a TableColumn for displaying subscriber information.
     * 
     * <p>
     * Each cell in this column contains a button that shows the subscriber's name
     * and ID. Clicking the button opens the subscriber's detailed information screen.
     * </p>
     * 
     * @return A configured TableColumn with buttons for subscriber details.
     */
    private TableColumn<Notification, Button> getSubscriberCardColumn() {
        TableColumn<Notification, Button> subscriberColumn = new TableColumn<>("From");
        subscriberColumn.prefWidthProperty().bind(newNotifications.widthProperty().multiply(0.17));

        subscriberColumn.setCellFactory(col -> new TableCell<Notification, Button>() {
            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Notification notification = (Notification) getTableRow().getItem();
                    Button subscriberCardBtn = new Button(notification.getSubscriberName() + "(" + notification.getSubscriberId() + ")");
                    subscriberCardBtn.setOnAction(event -> {
                        try {
                            openSubscriberCardScreen(notification.getSubscriberId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    subscriberCardBtn.setPrefWidth(col.getPrefWidth());
                    subscriberCardBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                    setGraphic(subscriberCardBtn);
                    setText(null);
                }
            }
        });

        return subscriberColumn;
    }

    /**
     * Opens the SubscriberCardScreen for a specific subscriber.
     * 
     * <p>
     * The subscriber details are fetched from the server using the {@code GET_SUBSCRIBER_BY_ID}
     * action. The data is then passed to the SubscriberCardScreen.
     * </p>
     * 
     * @param subscriberId The ID of the subscriber to view.
     * @throws IOException If an error occurs while opening the screen.
     */
    public void openSubscriberCardScreen(int subscriberId) throws IOException {
        Subscriber subscriber = (Subscriber) ClientUtils.sendMessage(Action.GET_SUBSCRIBER_BY_ID, subscriberId).getObject();
        SubscriberCardScreen screen = (SubscriberCardScreen) screenManager.openScreen("SubscriberCardScreen", "Subscriber Card");
        screen.setData(subscriber, false);
    }

    /**
     * Updates the status of all new notifications to "seen" on the server.
     * 
     * <p>
     * This method sends a request to the server to mark all new notifications as seen.
     * </p>
     */
    private void updateMessagesStatus() {
        if (!newNotifications.getItems().isEmpty()) {
            List<Notification> notifications = new ArrayList<>(newNotifications.getItems());
            Message msg = ClientUtils.sendMessage(Action.UPDATE_NOTIFICATION_STATUS, notifications);
            if (msg.isError()) {
                System.out.println("Error in updating the new messages status to seen");
            }
        }
    }

    /**
     * Closes the current screen and updates notification statuses.
     * 
     * <p>
     * If there are any new notifications in the {@code newNotifications} TableView,
     * their status is updated on the server using the {@code UPDATE_NOTIFICATION_STATUS}
     * action.
     * </p>
     * 
     * @param event The ActionEvent triggered by the user.
     */
    public void closeWindow(ActionEvent event) {
        updateMessagesStatus();
        newNotifications.getItems().clear();
        allNotifications.getItems().clear();
        screenManager.closeScreen();
    }
}
