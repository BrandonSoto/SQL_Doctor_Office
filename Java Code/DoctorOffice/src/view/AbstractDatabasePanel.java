package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;

import model.Database;

/**
 * Represents an abstract panel that will display data from a given database. 
 * 
 * @author Brandon Soto
 */
@SuppressWarnings("serial")
public abstract class AbstractDatabasePanel extends JPanel {
	
	// constants ///////////////////////////////////////////////////////////////

	/** The font to use for labels. */
	public static final Font LABEL_FONT = new Font("Serif", Font.BOLD, 24);
	
	/** The font to use for data in tables. */
	public static final Font DATA_FONT = new Font("Serif", Font.BOLD, 18);
	
	/** The size of margins to place between components. */
	public static final int MARGIN = 15;
	
	// fields //////////////////////////////////////////////////////////////////

	/** The database that this panel will show data about. */
	protected Database myDatabase; 
	
	// constructors/////////////////////////////////////////////////////////////

	/**
	 * Creates a Panel that will contain a title and table underneath it. 
	 * @param theDatabase the SQL database that this panel should display 
	 * data on. 
	 */
	public AbstractDatabasePanel(final Database theDatabase) {
		super(new BorderLayout());
		
		myDatabase = theDatabase;
		
		setupPanel();
	}
	
	// methods//////////////////////////////////////////////////////////////////

	/** Sets this panel by creating and organizing its subcomponents. */
	protected abstract void setupPanel();
	
	/** 
	 * Replaces all components in this panel with a panel displaying an 
	 * error.
	 * @param errorText the text describing the error
	 */
	protected void showErrorPanel(final String errorText) {
		removeAll();
		add(new ErrorPanel(errorText));
		repaint();
		validate();
	}
	
	/** Removes all components from this panel and then reinitializes them. */
	protected void reinitialize() {
		removeAll();
		setupPanel();
	}
	
} // end of AbstractTablePanel
