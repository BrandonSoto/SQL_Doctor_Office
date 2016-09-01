package view;

import java.awt.EventQueue;

/** Starting point of the DoctorOffice program. */
public final class StartProgram {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> new DoctorDatabaseGUI().show());
	}

}
