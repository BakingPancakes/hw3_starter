package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.tinylog.Logger;

import model.CSVExporter;
import model.CSVImporter;
import model.ExpenseTrackerModel;
import model.InputValidation;
import model.Transaction;
import view.ExpenseTrackerView;

/**
 * Provides the application programming layer to support the
 * following interface: addTransaction, delete, import, export.
 * 
 * NOTE) Represents the Controller in the MVC architecture pattern.
 */
public class ExpenseTrackerController {
	private ExpenseTrackerModel model = new ExpenseTrackerModel();    
    private ExpenseTrackerView view = new ExpenseTrackerView(model);
    
    public ExpenseTrackerController() {
    	super();
    	
    	Logger.info("Initializing Expense Tracker Controller");
    	
    	// Hook up the view and controller
    	
        // Handle add transaction button clicks
        view.getDataPanelView().getAddTransactionBtn().addActionListener(e -> {
        	addTransaction();
        });
        
        // Handle "Delete" menu item clicks
        view.getDeleteMenuItem().addActionListener(e -> {
        	delete();
        });
        
        // Handle "Open File..." menu item clicks
        view.getOpenFileMenuItem().addActionListener(e -> {
        	openFile();
        });
        
        // Handle "Save" menu item clicks
        view.getSaveAsMenuItem().addActionListener(e -> {	  
        	saveAs();
        });
        
        // Handle "Analyze" button clicks
        view.getAnalysisPanelView().getAnalyzeButton().addActionListener(e -> {
        	performDataAnalysis();
        });
        
        // Initialize view
        view.setVisible(true);
    }
    
    public ExpenseTrackerModel getModel() {
    	// For testing purposes
    	return this.model;
    }
    
    public ExpenseTrackerView getView() {
    	// For testing purposes
    	return this.view;
    }
    
    public void addTransaction() { 
    	try {
    		Logger.debug("Attempting to add new transaction");
    		// Get transaction data from view
    		double amount = view.getDataPanelView().getAmount(); 
    		String category = view.getDataPanelView().getCategory();

    		// Create transaction object
    		Transaction t = new Transaction(amount, category);

    		// Call controller to add transaction
    		model.addTransaction(t);
    		Logger.info("Successfully added transaction: amount={}, category={}", amount, category);
    		view.refresh();
    	}
    	catch (NumberFormatException nfe) {
    		Logger.warn("Failed to add transaction: invalid amount format - {}", nfe.getMessage());
    		view.displayErrorMessage("The amount cannot be parsed as a double number.");
    	}
    	catch (IllegalArgumentException iae) {
    		Logger.warn("Failed to add transaction: {}", iae.getMessage());
    		view.displayErrorMessage(iae.getMessage());
    	}
    }
    
    public void delete() {
        int selectedTransactionID = view.getDataPanelView().getSelectedTransactionID();
        Logger.debug("Attempting to delete transaction with ID: {}", selectedTransactionID);
    	boolean removed = model.removeTransaction(selectedTransactionID);
    	if (! removed) {
    		Logger.warn("Failed to delete transaction: invalid ID {}", selectedTransactionID);
    		view.displayErrorMessage("A valid transaction was not selected to be removed.");
    	}
    	else {
    		Logger.info("Successfully deleted transaction with ID: {}", selectedTransactionID);
    		view.refresh();
    	}
    }
    
    public void openFile() {
    	String inputFileName = view.showFileChooser(true);
    	if (inputFileName != null) {   
    		Logger.info("Importing transactions from file: {}", inputFileName);
    		int transactionCount = model.getTransactions().size();
    		for (int i = 0; i < transactionCount; i++) {
    			model.removeTransaction(0);
    		}

    		try {
    			CSVImporter csvImporter = new CSVImporter();
    			List<Transaction> importedTransactionsList = csvImporter.importTransactions(inputFileName);
    			for (Transaction importedTransaction : importedTransactionsList) {				
    				model.addTransaction(importedTransaction);
    			}
    			Logger.info("Successfully imported {} transactions from {}", importedTransactionsList.size(), inputFileName);
    		}
    		catch (IOException ioe) {
    			Logger.error("Failed to import transactions from {}: {}", inputFileName, ioe.getMessage());
    			view.displayErrorMessage(ioe.getMessage());
    		}
    		view.refresh();
    	}
    	else {
    		Logger.debug("File import cancelled by user");
    	}
    }
    
    public void saveAs() {
    	String outputFileName = view.showFileChooser(false);
    	if (outputFileName != null) {
    		Logger.info("Exporting transactions to file: {}", outputFileName);
    		CSVExporter csvExporter = new CSVExporter();
    		String errorMessage = csvExporter.exportTransactions(model.getTransactions(), outputFileName);
    		if (errorMessage != null) {
    			Logger.error("Failed to export transactions to {}: {}", outputFileName, errorMessage);
    			view.displayErrorMessage(errorMessage);
    		}
    		else {
    			Logger.info("Successfully exported {} transactions to {}", model.getTransactions().size(), outputFileName);
    		}
    	}
    	else {
    		Logger.debug("File export cancelled by user");
    	}
    }
    
    public void performDataAnalysis() {
    	Logger.info("Performing data analysis on {} transactions", model.getTransactions().size());
    	view.getAnalysisPanelView().performDataAnalysis(model);
    	Logger.debug("Data analysis completed");
    }
}
