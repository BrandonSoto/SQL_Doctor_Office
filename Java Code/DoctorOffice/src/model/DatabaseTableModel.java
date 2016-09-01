package model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

/**
 * DBTableModel is a table model tailored for modeling a SQL database. 
 * @author Brandon Soto
 */
@SuppressWarnings("serial")
public final class DatabaseTableModel extends AbstractTableModel {
	
	// fields //////////////////////////////////////////////////////////////////

	/** Contains the names of the table's columns. */
    private String[] myColumnNames;
    
    /** Contains the data that fills a table. */
    private Object[][] myTableData;
    
    /** The query that was carried out. */
    private SQLCall myQuery;
    
    // constructors ////////////////////////////////////////////////////////////
    
    /**
     * Creates a TableModel that will represent the results from executing the given
     * query on the given database. 
     * @param theDB the database to run the given query on. 
     * @param theQuery the SQL query to be executed on the database. 
     * @throws SQLException if a database error occurs or the url is null 
     */
    public DatabaseTableModel(final ResultSet data) throws SQLException {
    	super();
    	
    	myColumnNames = new String[0];
    	myTableData = new Object[0][0];
    	
    	if (data != null) {
    		initializeColumnNames(data.getMetaData());
        	initializeData(data);
    	}
    }

    // methods /////////////////////////////////////////////////////////////////
    
    /**
     * @return the SQL query that this TableModel currently represents. 
     */
    public SQLCall getQuery() {
    	return myQuery;
    }

    /**  {@inheritDoc} */
    public int getColumnCount() {
        return myColumnNames.length;
    }

    /**  {@inheritDoc} */
    public int getRowCount() {
        return myTableData.length;
    }

    /**  {@inheritDoc} */
    public String getColumnName(int col) {
        return myColumnNames[col];
    }

    /**  {@inheritDoc} */
    public Object getValueAt(int row, int col) {
        return myTableData[row][col];
    }
    
    /**
     * Initializes this model's column names. 
     * @param metaData a ResultSet's meta data that contains column names. 
     * @throws SQLException if a database error occurs.
     */
    private void initializeColumnNames(final ResultSetMetaData metaData) 
    		throws SQLException {
		myColumnNames = new String[metaData.getColumnCount()];
		
		for (int i = 1; i <= myColumnNames.length; i++) { // NOTE: columns start at 1 NOT 0
			myColumnNames[i - 1] = metaData.getColumnLabel(i);
		}
		
    }
    
    /**
     * Fills the entire table with data. 
     * @param theResults contains the data to be put in the table. 
     * @throws SQLException if a database error occurs. 
     */
    private void initializeData(final ResultSet theResults) throws SQLException {
    	// have resultset point to last element (used to get # of rows)
    	if (theResults.last()) { 
    		final int rowCount = theResults.getRow();
    		final int colCount = getColumnCount();

        	myTableData = new Object[rowCount][colCount];
        	
        	theResults.beforeFirst(); // reset resultset to point back to first element

        	int k = 0;
        	while (theResults.next()) {
        		final Object[] rowData = new Object[colCount];
        		
        		// go through each of the current row's columns
    			for (int i = 0; i < colCount; i++) { 
    				rowData[i] = theResults.getObject(myColumnNames[i]);
    			}
    			
    			myTableData[k] = rowData;
    			
    			k++;
        	}
        	
    	}
    	
    }
    
} // end of class