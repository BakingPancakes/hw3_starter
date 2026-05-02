import controller.ExpenseTrackerController;
import org.tinylog.Logger;

/**
 * Runs the Expense Tracker app to allow users to add/remove daily transactions.
 */
public class ExpenseTrackerApp {

  public static void main(String[] args) {
    Logger.info("Starting Expense Tracker Application");
    
    // Create MVC components
	ExpenseTrackerController controller = new ExpenseTrackerController();
	
	Logger.info("Expense Tracker Application started successfully");
  }

}