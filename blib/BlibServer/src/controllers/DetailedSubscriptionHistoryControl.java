package controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import entities.DetailedSubscriptionHistory.HistoryItem;


public class DetailedSubscriptionHistoryControl {
	
	public static List<HistoryItem> retrieveActionsHistory() {
		List<HistoryItem> actionsHistory = new ArrayList<>();
		LocalDate now = LocalDate.of(2025, 12, 2);
		actionsHistory.add(new HistoryItem(1,"borrow",now));
		actionsHistory.add(new HistoryItem(2,"hghsdorrow",now));
		return actionsHistory;
	}
}
