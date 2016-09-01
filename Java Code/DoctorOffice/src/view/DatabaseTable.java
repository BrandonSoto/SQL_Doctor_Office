package view;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

/**
 * A convenience class for creating a JTable with certain attributes. 
 * Most importantly, it allows row selection, does not allow column selection.
 * Other attributes that this table has:
 * - a constant row height
 * - auto resizes all row columns
 * - allows dragging
 * - a table font
 * - single selection mode 
 * 
 * @author Brandon Soto
 */
@SuppressWarnings("serial")
public class DatabaseTable extends JTable {
	
	// constructors ////////////////////////////////////////////////////////////
	/**
	 * Creates a table that represents the given model and sets up the table
	 * with certain attributes. 
	 * @param model the model that this table will represent. 
	 */
	public DatabaseTable(final TableModel model) {
		super(model);
		
		setup();
	}
	
	/**
	 * Creates a table with no model, but still sets up the table with certain
	 * attributes. 
	 */
	public DatabaseTable() {
		super();
		
		setup();
	}
	
	// methods /////////////////////////////////////////////////////////////////

	/**
	 * Sets this table up by allowing row selection, not allowing column 
	 * selection, setting row height, setting resize mode, enabling drag
	 * enabled, sets the table's font, and sets the selection mode. 
	 */
	private void setup() {
		setRowSelectionAllowed(true);
		setColumnSelectionAllowed(false);
		setRowHeight(25);
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setDragEnabled(true);
		setFont(AbstractDatabasePanel.DATA_FONT);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

}
