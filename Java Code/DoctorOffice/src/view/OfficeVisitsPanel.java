package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import model.Database;
import model.DatabaseTableModel;
import model.SQLCall;

/**
 * Represents a panel that will display information on office visits.
 * The user can see all office visits and can also sort the visits by date. 
 * 
 * @author Brandon Soto
 */
@SuppressWarnings("serial")
public class OfficeVisitsPanel extends AbstractDatabasePanel {
	
	// constants ///////////////////////////////////////////////////////////////
	
	/** Stored procedure for getting the office visits on a given date. */
	private static final SQLCall GET_VISITS_ON_DAY = new SQLCall(
										"{call dbo.spGetDayConsultations(?)}", 
										new String[]{"date"}, null);
	
	/** A view that gets ALL office visits. */
	private static final SQLCall GET_ALL_VISITS = new SQLCall(
											"SELECT * FROM dbo.vwDocPatient");
	
	/** The number of options to display for the Day combo box. */
	private static final int NUMBER_OF_DAYS = 31;
	
	/** The number of years to display for the Year combo box. */
	private static final int NUMBER_OF_YEARS = 15;
	
	/** The starting year for the Year combo box. */
	private static final int START_YEAR = 2009;
	
	/** All of the months for the Months combo box. */
	private static final String[] MONTHS = {"January", "February", "March", 
		"April", "May", "June", "July", "August", "September", "October", 
		"November", "December"};
	
	// constructors ////////////////////////////////////////////////////////////

	/**
	 * Creates an OfficeVisitsPanel that will model SQL calls for the specified 
	 * database. 
	 * @param theDatabase the database to use for SQL calls
	 */
	public OfficeVisitsPanel(final Database theDatabase) {
		super(theDatabase);
	}
	
	// methods /////////////////////////////////////////////////////////////////

	/** 
	 * Sets up all of the OfficeVisitsPanel's subcomponents and places them in 
	 * the OfficeVisitPanel. 
	 */
	protected void setupPanel() {
		final JTable officeVisits = new DatabaseTable();

		final JPanel officeVisitPanel = new TablePanel("OFFICE VISITS:", officeVisits); 
		final JPanel dateSearchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		dateSearchPanel.setBorder(BorderFactory.createEtchedBorder());
		
		final JLabel monthLabel = new JLabel("Month: ");
		final JLabel dayLabel = new JLabel("Day: ");
		final JLabel yearLabel = new JLabel("Year: ");
		
		final JComboBox<String> monthOptions = new JComboBox<String>(MONTHS);
		final JComboBox<Integer> dayOptions = new JComboBox<Integer>(getDayOptions());
		final JComboBox<Integer> yearOptions = new JComboBox<Integer>(getYearOptions());
		
		final JButton allVisitsButton = new JButton("See All Visits");
		allVisitsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					officeVisits.setModel(new DatabaseTableModel(
											OfficeVisitsPanel.this.myDatabase
											.getResults(GET_ALL_VISITS)));
				} catch (SQLException e) {
					showErrorPanel("Error retrieving all office visits. Try again later.");
				}
			}
		});
		
		final JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final int month = monthOptions.getSelectedIndex();
				final int year = (int) yearOptions.getSelectedItem();
				final int day = (int) dayOptions.getSelectedItem();
				
				final Calendar date = new Calendar.Builder()
											.setDate(year, month, day).build();
				
				try {
					final CallableStatement cs = myDatabase.prepareCall(GET_VISITS_ON_DAY);
					cs.setDate(GET_VISITS_ON_DAY.getInputs()[0], 
										new java.sql.Date(date.getTimeInMillis()));
					
					officeVisits.setModel(new DatabaseTableModel(cs.executeQuery()));
					
				} catch (SQLException e1) {
					showErrorPanel("Error retrieving office visits. Try again later.");
				}
				
				
			}
		});
		
		addComponentsToPanel(dateSearchPanel, monthLabel, monthOptions, dayLabel, 
									dayOptions, yearLabel, yearOptions, 
									Box.createHorizontalStrut(MARGIN), searchButton);
		
		final JPanel topContainer = new JPanel();
		topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
		addComponentsToPanel(topContainer, dateSearchPanel, 
								Box.createVerticalStrut(MARGIN), allVisitsButton,
								Box.createVerticalStrut(MARGIN));
		
		add(topContainer, BorderLayout.NORTH);
		add(officeVisitPanel, BorderLayout.CENTER);
	}
	
	/** 
	 * @return an integer array that lists the days in a month
	 */
	private Integer[] getDayOptions() {
		final Integer[] days = new Integer[NUMBER_OF_DAYS];
		
		for (int i = 0; i < NUMBER_OF_DAYS; i++) {
			days[i] = i + 1;
		}
		
		return days; 
	}
	
	/**
	 * @return an integer that lists available year options 
	 */
	private Integer[] getYearOptions() {
		final Integer[] years = new Integer[NUMBER_OF_YEARS];
		
		for (int i = 0; i < NUMBER_OF_YEARS; i++) {
			years[i] = START_YEAR + i;
		}
		
		return years; 
	}
	
	/**
	 * Adds all of the passed components to the given container. 
	 * @param container the component to have elements added into
	 * @param components the GUI components to be added to the container
	 */
	private void addComponentsToPanel(final JPanel container, 
											final Component... components) {
		for (final Component comp : components) {
			container.add(comp);
		}
	}
	
}
