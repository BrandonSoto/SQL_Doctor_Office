package view;

import java.awt.BorderLayout;
import java.sql.CallableStatement;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Database;
import model.DatabaseTableModel;
import model.SQLCall;

/**
 * Represents a panel that will display information on tests and the the patients
 * have undergone those tests. 
 * 
 * @author Brandon Soto
 */
@SuppressWarnings("serial")
public class PatientTestsPanel extends AbstractDatabasePanel {
	
	// constants ///////////////////////////////////////////////////////////////

	/** Stored procedure for getting a list of all DoctorOffice patients. */
	private static final SQLCall GET_ALL_PATIENTS = new SQLCall(
											"{call dbo.spGetAllPatients()}");
	
	/** Stored procedure for getting a specified patient's tests. */
	private static final SQLCall GET_PATIENT_TESTS = new SQLCall(
											"{call dbo.spGetPatientTests(?)}", 
											new String[]{"patientID"}, null);
	
	// constructors ////////////////////////////////////////////////////////////

	/**
	 * Creates an PatientTestsPanel that will model SQL calls for the 
	 * specified database. 
	 * @param theDatabase the database to use for SQL calls
	 */
	public PatientTestsPanel(final Database theDatabase) {
		super(theDatabase);
	}

	// methods /////////////////////////////////////////////////////////////////

	/**
	 * Sets up all of the PatientTestsPanel subcomponents and places 
	 * them in the OfficeVisitPanel. 
	 */
	@Override
	protected void setupPanel() {
		try {
			final JTable patientTable = new DatabaseTable(new DatabaseTableModel(
					myDatabase.getResults(GET_ALL_PATIENTS)));
			final JTable patTable = new DatabaseTable();
			
			final JPanel patientPanel = new TablePanel("SELECT PATIENT:", 
					patientTable);
			final JPanel testPanel = new TablePanel("PATIENT TESTS:", patTable);
			
			final ListSelectionModel patSelection = patientTable.getSelectionModel();
			patSelection.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					final int row = patientTable.getSelectedRow();
					final int patID = (int) patientTable.getValueAt(row, 0);
					
					try {
						
						final CallableStatement cs = myDatabase
								.prepareCall(GET_PATIENT_TESTS);
						cs.setInt(GET_PATIENT_TESTS.getInputs()[0], patID);
						
						patTable.setModel(new DatabaseTableModel(cs.executeQuery()));
						
					} catch (SQLException e1) {
						showErrorPanel("Error retrieving patient's tests. "
								+ "Try again later.");
					}
				}
			});
			
			add(patientPanel, BorderLayout.WEST);
			add(testPanel, BorderLayout.CENTER);
			
		} catch (SQLException e2) {
			showErrorPanel("Error retrieving patients. Try again later.");
		}
	}

} // end PatientTestPanel
