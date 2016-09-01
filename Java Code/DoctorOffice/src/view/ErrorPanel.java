package view;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Represents a panel that contains red text that informs the user
 * about an error. 
 * 
 * @author Brandon Soto
 */
@SuppressWarnings("serial")
public class ErrorPanel extends JPanel {
	
	// constructors ////////////////////////////////////////////////////////////
	
	/**
	 * Creates an ErrorPanel that will display the given text. 
	 * @param errorText the text that describes the error
	 */
	public ErrorPanel(final String errorText) {
		super(new FlowLayout(FlowLayout.CENTER));
		
		setup(errorText);
	}
	
	// methods /////////////////////////////////////////////////////////////////

	/**
	 * Creates the label to be displayed and adds it to this panel. 
	 * @param errorText the text that describes the error
	 */
	private void setup(final String errorText) {
		final JLabel errorLabel = new JLabel(errorText);
		errorLabel.setFont(AbstractDatabasePanel.LABEL_FONT);
		errorLabel.setForeground(Color.RED);
		
		add(errorLabel);
	}

}
