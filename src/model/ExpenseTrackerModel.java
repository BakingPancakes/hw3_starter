package model;

import java.util.ArrayList;
import java.util.List;

import org.tinylog.Logger;

/**
 * Represents the data model as a list of transactions.
 * 
 * NOTE) Represents the Model in the MVC architecture pattern.
 */
public class ExpenseTrackerModel {

	private List<Transaction> transactions = new ArrayList<>();
	
	public ExpenseTrackerModel() {
		super();
		Logger.info("Expense Tracker Model initialized");
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void addTransaction(Transaction t) {
		Logger.debug("Adding transaction: amount={}, category={}", t.getAmount(), t.getCategory());
		transactions.add(t);
		Logger.debug("Transaction added. Total transactions: {}", transactions.size());
	}

	public boolean removeTransaction(int transactionID) {
  	  // Perform input validation
  	  if ((transactionID < 0) || (transactionID > this.getTransactions().size() - 1)) {
  		  Logger.warn("Invalid transaction ID for removal: {} (valid range: 0-{})", transactionID, this.getTransactions().size() - 1);
  		  return false;
  	  }
  	  else {
  		  Transaction removed = transactions.remove(transactionID);
  		  Logger.debug("Removed transaction: amount={}, category={}. Remaining transactions: {}", 
  		              removed.getAmount(), removed.getCategory(), transactions.size());
  		  return true;
  	  }
	}

	public double computeTransactionsTotalCost() {
		double totalCost=0;
		for(Transaction t : transactions) {
			totalCost+=t.getAmount();
		}
		return totalCost;
	}
}
