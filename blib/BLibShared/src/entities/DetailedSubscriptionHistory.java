package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class DetailedSubscriptionHistory implements Serializable{
	
	public static class HistoryItem implements Serializable {
		private int id ;
		private String action ;
		private LocalDate actionDate ;
		
		public HistoryItem(int id,String action, LocalDate actionDate) {
			this.id = id;
			this.action = action ;
			this.actionDate = actionDate ;
		}
		
		@Override
		public String toString() {
			return id + " " + action + " " + actionDate;
		}
	}
	
	private List<HistoryItem> actionsHistory;
	
	public DetailedSubscriptionHistory() {
		actionsHistory = new ArrayList<>() ;
	}
	
	public void setActionsHistory(List<HistoryItem> actionsHistory) {
		this.actionsHistory = actionsHistory;
	}
	
	public List<HistoryItem> getActionsHistory() {
		return actionsHistory;
	}
}
