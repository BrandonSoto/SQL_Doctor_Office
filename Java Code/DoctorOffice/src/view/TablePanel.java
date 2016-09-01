package view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * Represents a panel that contains a table and a label for that table. 
 * 
 * @author Brandon Soto
 */
@SuppressWarnings("serial")
public final class TablePanel extends JPanel {
	
	// constructors ////////////////////////////////////////////////////////////

	/**
	 * Creates a TablePanel with the given label text and table. 
	 * @param labelText the text to display on the table label
	 * @param table the table to be displayed on this panel
	 */
	public TablePanel(final String labelText, final JTable table) {
		super(new BorderLayout());
		
		setup(labelText, table);
	}
	
	// methods /////////////////////////////////////////////////////////////////

	/**
	 * Sets this panel up to contain a label and a table. 
	 * @param labelText the text to be put on the label
	 * @param table the table to be placed in the container
	 * @param call the SQL call that should be used as the table's model 
	 * @return a JPanel that contains a label and a table
	 */
	private void setup(final String labelText, final JTable table) {
		final JLabel label = new JLabel(labelText);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(AbstractDatabasePanel.LABEL_FONT);
		
		add(label, BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);
		add(Box.createHorizontalStrut(AbstractDatabasePanel.MARGIN), BorderLayout.EAST);
		add(Box.createHorizontalStrut(AbstractDatabasePanel.MARGIN), BorderLayout.WEST);
		setBackground(Color.LIGHT_GRAY);
	}
}
