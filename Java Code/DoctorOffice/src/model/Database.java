package model;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/** 
 * A class that models a SQL database. A client is able to execute queries and 
 * receive the results. 
 * (NOTE: all exceptions must be handled by the client.) 
 * 
 * @author Brandon Soto
 */
public final class Database {
	
	/** URL string for accessing the AdventureWorks database. */
	public static final String ADVENTURE_WORKS = 
				"jdbc:sqlserver://localhost;databaseName=AdventureWorks2012;"
				+ "integratedSecurity=true;allowMultiQueries=true";
		
	/** URL string for accessing the DoctorOffice database. */
	public static final String DOCTOR_OFFICE = 
						"jdbc:sqlserver://localhost;databaseName=DoctorOffice;"
						+ "integratedSecurity=true;allowMultiQueries=true";
	
	/** The connection to the database. */
	private Connection myConnection;
	
	/** The URL for the database. */
	private String myURL; 
	
	/**
	 * Creates a database from the given URL. 
	 * @param url the url of the desired database 
	 * @throws SQLException if a database error occurs or the url is null 
	 */
	public Database(final String url) throws SQLException  {
		myURL = url;
		myConnection = DriverManager.getConnection(myURL);
	}
	
	/**
	 * Prepares and returns a CallableStatement with the given SQL call. 
	 * @param theCall the SQL call that should be prepared
	 * @return a CallableStatement that can be customized and executed at the client's whim
	 * @throws SQLException if a database error occurs
	 */
	public CallableStatement prepareCall(final String theCall) throws SQLException {
		final CallableStatement cd = myConnection.prepareCall(theCall, 
											ResultSet.TYPE_SCROLL_INSENSITIVE, 
											ResultSet.CONCUR_READ_ONLY);
		cd.setEscapeProcessing(true);
		cd.setQueryTimeout(10);
		
		return cd;
	}
	
	/** 
	 * Returns the results from a given SQL query. 
	 * NOTE: the returned resultset is typescroll insensitive and is read only
	 * @param theQuery the SQL query that should be executed
	 * @return the results for the specified SQL query
	 * @throws SQLException if a database access error occurs or if there is a closed connection
	 */
	public ResultSet getResults(final SQLCall theCall) throws SQLException {
		return prepareCall(theCall.getCallString()).executeQuery();
	}
	
	/**
	 * Prepares and returns a CallableStatement with the given SQL call. 
	 * @param theCall the SQL call that should be prepared
	 * @return a CallableStatement that can be customized and executed at the client's whim
	 * @throws SQLException if a database error occurs
	 */
	public CallableStatement prepareCall(final SQLCall theCall) throws SQLException {
		return prepareCall(theCall.getCallString());
	}
	
} // end of class
