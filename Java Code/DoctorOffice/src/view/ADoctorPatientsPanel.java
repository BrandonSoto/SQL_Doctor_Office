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
 * A panel that shows all doctors and their respective patients. 
 * 
 * @author Brandon Soto
 */
@SuppressWarnings("serial")
public class ADoctorPatientsPanel extends AbstractDatabasePanel {
	
	// constants ///////////////////////////////////////////////////////////////

	/** Stored procedure for getting the list of doctors. */
	private static final SQLCall GET_ALL_DOCTORS = new SQLCall(
												"{call dbo.spGetDoctors()}");
	
	/** Stored procedure for getting a specified doctor's patients. */
	private static final SQLCall GET_DOCTOR_PATIENTS = new SQLCall(
			"{call dbo.spGetDocPatients(?)}", new String[]{"doctorID"}, null);
	
	// constructors ////////////////////////////////////////////////////////////

	/**
	 * Creates ADoctorPatientsPanel. 
	 * @param theDatabase the SQL database that this panel will show data on. 
	 */
	public ADoctorPatientsPanel(Database theDatabase) {
		super(theDatabase);
	}
	
	// methods /////////////////////////////////////////////////////////////////

	/**
	 * Places a table of doctors on the left of this panel and a table of 
	 * patients on the right of this panel. 
	 */
	protected void setupPanel() {
		try {
			final JTable doctorTable = new DatabaseTable(new DatabaseTableModel(
										myDatabase.getResults(GET_ALL_DOCTORS)));
			final JTable patientTable = new DatabaseTable();
			
			final JPanel docPanel = new TablePanel("SELECT DOCTOR:", doctorTable);
			final JPanel patPanel = new TablePanel("PATIENTS:", patientTable);
			
			final ListSelectionModel docSelection = doctorTable.getSelectionModel();
			docSelection.addListSelectionListener(new ListSelectionListener() {
				
				@Override
				public void valueChanged(ListSelectionEvent e) {
					final int row = doctorTable.getSelectedRow();
					final int docID = (int) doctorTable.getValueAt(row, 0);
						
					try {
						final CallableStatement cs = myDatabase.prepareCall(GET_DOCTOR_PATIENTS);
						cs.setInt(GET_DOCTOR_PATIENTS.getInputs()[0], docID);
						
						patientTable.setModel(new DatabaseTableModel(cs.executeQuery()));
					} catch (SQLException e1) {
						showErrorPanel("Error retrieving patients. Try agian later.");
					}
					
					
				}
			});
			
			add(docPanel, BorderLayout.WEST);
			add(patPanel, BorderLayout.CENTER);
			
		} catch (SQLException e2) {
			showErrorPanel("Error retrieving doctors. Try again later");
		}
		
	}
	
}
