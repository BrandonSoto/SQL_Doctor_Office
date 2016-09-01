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
 * Represents a panel that will display information on prescriptions and the
 * patients that use them.
 * 
 * @author Brandon Soto
 */
@SuppressWarnings("serial")
public final class PatientPrescriptionsPanel extends AbstractDatabasePanel {
	
	// constants ///////////////////////////////////////////////////////////////

	/** 
	 * Stored procedure for showing a list of patients that are prescribed the 
	 * specified medicine. 
	 */
	private static final SQLCall GET_PRESCRIPTION_USERS = new SQLCall(
										"{call dbo.spGetPrescriptionUsers(?)}", 
										new String[]{"prescriptionID"}, null);
	
	/** Stored procedure for getting all prescriptions. */
	private static final SQLCall GET_ALL_PRESCRIPTIONS = new SQLCall(
												"{call dbo.spGetAllMedicines}");

	// constructors ////////////////////////////////////////////////////////////

	/**
	 * Creates an PatientPrescriptionsPanel that will model SQL calls for the 
	 * specified database. 
	 * @param theDatabase the database to use for SQL calls
	 */
	public PatientPrescriptionsPanel(final Database theDatabase) {
		super(theDatabase);
	}
	
	// methods /////////////////////////////////////////////////////////////////

	/** 
	 * Sets up all of the PatientPrescriptionsPanel subcomponents and places 
	 * them in the OfficeVisitPanel. 
	 */
	protected void setupPanel() {
		try {
			final JTable prescriptionTable = new DatabaseTable(
								new DatabaseTableModel(myDatabase
										.getResults(GET_ALL_PRESCRIPTIONS)));
			final JTable patientTable = new DatabaseTable();
			
			final JPanel presPanel = new TablePanel("SELECT PRESCRIPTION:", 
										prescriptionTable);
			final JPanel patPanel = new TablePanel("PATIENTS TAKING PRESCRIPTION:", 
					patientTable);
			
			final ListSelectionModel presSelection = prescriptionTable.getSelectionModel();
			presSelection.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					final int row = prescriptionTable.getSelectedRow();
					final int presID = (int) prescriptionTable.getValueAt(row, 0);
					
					try {
						final CallableStatement cs = myDatabase.prepareCall(GET_PRESCRIPTION_USERS);
						cs.setInt(GET_PRESCRIPTION_USERS.getInputs()[0], presID);
						
						patientTable.setModel(new DatabaseTableModel(cs.executeQuery()));
					} catch (SQLException e1) {
						showErrorPanel("Error retrieving prescription users. "
								+ "Try again later.");
					}
				}
			});
			
			add(presPanel, BorderLayout.WEST);
			add(patPanel, BorderLayout.CENTER);
			
		} catch (SQLException e2) {
			showErrorPanel("Error retrieving prescriptions. Try again later.");
		}
		
		
	}
	
}
