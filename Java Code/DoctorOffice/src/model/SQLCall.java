package model;

/**
 * A class that models a SQL call. It includes the call string, any inputs, 
 * and any outputs. 
 * 
 * @author Brandon Soto
 */
public class SQLCall {
	
	/** A call string for executing SQL call. */
	private final String myCallString;
	
	/** All input names for this SQL call. */
	private final String[] myInputs;
	
	/** All output names for this SQL call. */
	private final String[] myOutputs;
	
	/**
	 * Creates a SQLCall with the given call string, input names, and output
	 * names. 
	 * @param callStr the string that can be used to execute this SQL call
	 * @param inputs all input names for this SQL call
	 * @param outputs all output names for this SQL call
	 */
	public SQLCall(final String callStr, final String[] inputs, 
			final String[] outputs) {
		myCallString = callStr;
		myInputs = inputs; 
		myOutputs = outputs; 
	}
	
	/**
	 * Creates a SQLCall with the given call string. This SQLCall will have 0
	 * inputs and 0 outputs.
	 * @param callStr the string that can be used to execute this SQL call
	 */
	public SQLCall(final String callStr) {
		this(callStr, new String[0], new String[0]);
	}
	
	/**
	 * @return this SQLCall's call string 
	 */
	public String getCallString() {
		return myCallString;
	}
	
	/**
	 * @return all of the input names for this SQL call
	 */
	public String[] getInputs() {
		return myInputs;
	}
	
	/**
	 * @return all of the output names for this SQL call
	 */
	public String[] getOutputs() {
		return myOutputs; 
	}
}
