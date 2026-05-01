package view;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.ExpenseTrackerModel;
import model.Transaction;

import java.awt.*;
import java.util.List; 

/**
 * Provides a visualization of a list of transactions along with
 * the ability to add/remove transactions.
 * 
 * NOTE: Represents the View in the MVC architecture pattern.
 */
public class ExpenseTrackerView extends JFrame {

  private ExpenseTrackerModel model;
  
  private JTabbedPane tabbedPanel;
  private DataPanelView dataPanelView;
  private AnalysisPanelView analysisPanelView;
  
  private JMenuItem fileOpenFileMenuItem;
  private JMenuItem fileSaveAsMenuItem;
  private JMenuItem editDeleteMenuItem;

  public JTabbedPane getTabbedPanel() {
	  return this.tabbedPanel;
  }
  
  public DataPanelView getDataPanelView() {
	  return this.dataPanelView;
  }
  
  public AnalysisPanelView getAnalysisPanelView() {
	  return this.analysisPanelView;
  }
  
  public JMenuItem getOpenFileMenuItem() {
	  return fileOpenFileMenuItem;
  }
  
  public JMenuItem getSaveAsMenuItem() {
	  return fileSaveAsMenuItem;
  }
  
  public JMenuItem getDeleteMenuItem() {
	  return editDeleteMenuItem;
  }

  public ExpenseTrackerView(ExpenseTrackerModel model) {
    setTitle("Expense Tracker"); // Set title
 
    this.model = model;
    
    // Create the top menu bar
    JMenuBar topMenuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    topMenuBar.add(fileMenu);
    this.fileOpenFileMenuItem = new JMenuItem("Open File...");
    fileMenu.add(this.fileOpenFileMenuItem);
    this.fileSaveAsMenuItem = new JMenuItem("Save As...");
    fileMenu.add(this.fileSaveAsMenuItem);
    JMenu editMenu = new JMenu("Edit");
    topMenuBar.add(editMenu);
    this.editDeleteMenuItem = new JMenuItem("Delete");
    editMenu.add(editDeleteMenuItem);
    setJMenuBar(topMenuBar);
  
    // Layout components
    tabbedPanel = new JTabbedPane();
    dataPanelView = new DataPanelView();
    dataPanelView.getAccessibleContext().setAccessibleName("Transaction Data panel");
    dataPanelView.getAccessibleContext().setAccessibleDescription("View the transactions saved to the current session. Add transaction according with appropriate amount and category.");
    analysisPanelView = new AnalysisPanelView();
    analysisPanelView.getAccessibleContext().setAccessibleName("Transaction analysis panel");
    analysisPanelView.getAccessibleContext().setAccessibleDescription("Generate an overview of transactions by selecting a time period and clicking analyze.");
    tabbedPanel.add("Data", dataPanelView);
    tabbedPanel.add("Analyis", analysisPanelView);
    add(tabbedPanel);
  
    // Set frame properties
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    refresh();
    setVisible(true);
  
  }
  
  public String showFileChooser(boolean isOpenFile) {
	JFileChooser chooser = new JFileChooser();
	int result = -1;
	if (isOpenFile) {
		result = chooser.showOpenDialog(this);
	}
	else {
		result = chooser.showSaveDialog(this);
	}
	if (result == JFileChooser.APPROVE_OPTION) {
	  String path = chooser.getSelectedFile().getAbsolutePath();
	  if (!path.toLowerCase().endsWith(".csv")) {
	    path = path + ".csv";
	  }
	  return path;
	}
	return null;
  }
  
  public void displayErrorMessage(String message) {
    this.getAccessibleContext().setAccessibleName("Error: invalid input.");
    this.getAccessibleContext().setAccessibleDescription(message);
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
  } 

  public void refresh() {  
    // Pass to views
    dataPanelView.refreshTable(model);
    analysisPanelView.setVisible(model);
  }

  // Other view methods
}
