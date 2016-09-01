package view;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import model.Database;

/**
 * A GUI that will show data on the DoctorOffice SQL database. 
 * 
 * @author Brandon Soto
 */
public final class DoctorDatabaseGUI {
	
	// constants ///////////////////////////////////////////////////////////////

	/** The title of the program. */
	private static final String PROGRAM_TITLE = "Doctor's Office";

	// fields //////////////////////////////////////////////////////////////////

	/** The GUI's main frame. */
	private final JFrame myMainFrame;
	
	/** The table that will display data from the database. */
	private Database myDatabase;
	
	/** The current panel displayed in the center of the main frame. */
	private JPanel myCurrDisplayedPanel;
	
	// constructors ////////////////////////////////////////////////////////////

	/**
	 * Creates a DoctorDatabaseGUI. 
	 */
	public DoctorDatabaseGUI() {
		myMainFrame = new JFrame();
		myDatabase = null;
		
		try {
			myDatabase = new Database(Database.DOCTOR_OFFICE);
			setUpMainFrame();
			
		} catch (SQLException e) {
			myMainFrame.add(new ErrorPanel("A database error occurred. Please restart the program"));
			
			myMainFrame.pack();
			myMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} 
		
	}
	
	// methods /////////////////////////////////////////////////////////////////

	/**
	 * Sets up the main frame and its components. 
	 */
	private void setUpMainFrame() {
		myMainFrame.setLayout(new BorderLayout());
		
		final JPanel navPanel = getNavPanel();
		
		setupCurrentPanel(navPanel);
		
		// add panels to main frame		
		myMainFrame.add(navPanel, BorderLayout.EAST);
		myMainFrame.add(new JPanel(), BorderLayout.NORTH);
		myMainFrame.add(new JPanel(), BorderLayout.WEST);
		myMainFrame.add(new JPanel(), BorderLayout.SOUTH);
		myMainFrame.add(myCurrDisplayedPanel, BorderLayout.CENTER);
		
		// set location and size of GUI
		myMainFrame.pack();
		myMainFrame.setMinimumSize(myMainFrame.getSize());
		myMainFrame.setSize(1000, 1000); // change later
		
		myMainFrame.setLocationRelativeTo(null);
		myMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myMainFrame.setTitle(PROGRAM_TITLE);
	}
	
	/**
	 * Sets the GUI's current panel to the panel associated with the first navigation button. 
	 * @param navPanel the panel containing all navigation buttons
	 */
	private void setupCurrentPanel(final JPanel navPanel) {
		final JButton firstNavButton = (JButton) navPanel.getComponent(0);
		firstNavButton.setEnabled(false);
		
		final NavButtonActionListener listener = (NavButtonActionListener) firstNavButton.getActionListeners()[0];
		
		myCurrDisplayedPanel = listener.getAssociatedPanel();
	}
	
	/**
	 * @return this GUI's navigation panel. 
	 */
	private JPanel getNavPanel() {
		final TitledBorder navBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder());
		navBorder.setTitle("Navigation");
		navBorder.setTitleFont(new Font("Serif", Font.BOLD, 20));
		navBorder.setTitleJustification(TitledBorder.CENTER);
		
		final JPanel navPanel = new JPanel();
		navPanel.setBorder(navBorder);
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
		
		int index = 0; 
		
		for (final JButton navButton : getNavButtons()) {
			navPanel.add(navButton, index++);
		}
		
		return navPanel; 
	}
	
	/**
	 * @return all of the GUI's navigation buttons. 
	 */
	private JButton[] getNavButtons() {
		return new JButton[]{
				createNavButton("See Doctor's Patients", new ADoctorPatientsPanel(myDatabase)),
				createNavButton("See Office Visits", new OfficeVisitsPanel(myDatabase)),
				createNavButton("See Prescriptions and Prescription Users", new PatientPrescriptionsPanel(myDatabase)),
				createNavButton("See a Patient's Tests", new PatientTestsPanel(myDatabase))
		};
	}
	
	/**
	 * Creates and returns a JButton with the specified text and enabled setting. 
	 * When clicked, the button will cause associatedPanel to become visible in the GUI. 
	 * @param text the text to display on the button
	 * @param associatedPanel the panel that should become visible when this
	 *  button is clicked
	 * @param isEnabled true if this button should be enabled. Otherwise false
	 * @return a JButton with the specified text and enabled setting. When 
	 * clicked, the button will cause associatedPanel to become visible in the GUI
	 */
	private JButton createNavButton(final String text, final AbstractDatabasePanel associatedPanel) {
		final JButton navButton = new JButton(text);
		navButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		navButton.addActionListener(new NavButtonActionListener(associatedPanel));
		
		return navButton; 
	}
	
	/**
	 * Makes the GUI visible.
	 */
	public void show() {
		myMainFrame.setVisible(true);
	}
	
	/**
	 * Represents an action listener for one of the GUI's navigation buttons. 
	 * 
	 * @author Brandon Soto
	 */
	private final class NavButtonActionListener implements ActionListener {
		
		/** The panel that should become visible when this action is performed. */
		private AbstractDatabasePanel myAssociatedPanel;
		
		/**
		 * Creates an action listener with the given associated panel. 
		 * @param associatedPanel the panel that should become visible when this
		 * action is performed. 
		 */
		public NavButtonActionListener(final AbstractDatabasePanel associatedPanel) {
			myAssociatedPanel = associatedPanel;
		}
		
		@Override
		public void actionPerformed(final ActionEvent e) {
			final JButton navButton = (JButton) e.getSource();
			final JPanel navPanel = (JPanel) navButton.getParent();
			
			for (final Component c : navPanel.getComponents()) {
				c.setEnabled(true);
			}
			
			myMainFrame.remove(myCurrDisplayedPanel);
			
			// reinitialize just in case the panel experienced an error before
			myAssociatedPanel.reinitialize(); 
			
			myCurrDisplayedPanel = myAssociatedPanel;
			
			myMainFrame.add(myCurrDisplayedPanel, BorderLayout.CENTER);
			myMainFrame.repaint();
			myMainFrame.validate();
			
			navButton.setEnabled(false);
		}
		
		/**
		 * @return the panel that should become visible when the button is clicked. 
		 */
		public AbstractDatabasePanel getAssociatedPanel() {
			return myAssociatedPanel;
		}
		
	} // end of NavButtonActionListener 
	
} // end of DatabaseGUI

