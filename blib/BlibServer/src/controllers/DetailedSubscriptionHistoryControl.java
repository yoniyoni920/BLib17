package controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import entities.DetailedSubscriptionHistory.HistoryItem;


/**
 * Controls the retrieval of subscription history actions.
 * 
 * This class contains methods to retrieve and manage the history of actions 
 * related to subscriptions, such as borrowing.
 */
public class DetailedSubscriptionHistoryControl {
    /**
     * Retrieves a list of history actions, such as borrow events, for a subscription.
     * 
     * This method simulates retrieving actions history by adding sample data to the list.
     * The date for each action is set to December 2, 2025.
     * 
     * @return a List of HistoryItem objects representing the actions history
     */
	public static List<HistoryItem> retrieveActionsHistory() {
		List<HistoryItem> actionsHistory = new ArrayList<>();
        // Adding sample actions to the history
		LocalDate now = LocalDate.of(2025, 12, 2);
		actionsHistory.add(new HistoryItem(1,"borrow",now));
		actionsHistory.add(new HistoryItem(2,"hghsdorrow",now));
		return actionsHistory;
	}
}
