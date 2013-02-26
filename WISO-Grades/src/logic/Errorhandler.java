package logic;

import javax.swing.JOptionPane;

import view.MainFrame;

/**
 * 
 * 
 * @author Cihan Öcal
 * <i> 25.02.2013 </i>
 */
public class Errorhandler {

	@SuppressWarnings("unused")
	private WISOGrades app;
	private MainFrame mainWindow;
	
	/**
	 * 
	 * Constructor
	 * @param app
	 * @author Cihan Öcal
	 * <i> 25.02.2013 </i>
	 */
	protected Errorhandler(WISOGrades app){
		this.app = app;
		mainWindow = app.getMainWindow();
	}
	
	/**
	 * show error <b>without</b> exiting program
	 * @param message
	 * @param exit
	 * @param exitcode
	 * @author Cihan Öcal
	 * <i> 25.02.2013 </i>
	 */
	public void showError(String message){
		JOptionPane.showMessageDialog(mainWindow,
			    message, "Fehler",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Show error <b>with</b> exitig program
	 * @param message
	 * @param exitcode
	 * @author Cihan Öcal
	 * <i> 25.02.2013 </i>
	 */
	public void showError(String message, int exitcode){
		String exitingString = "\nDas Programm wird jetzt beendet...";
		JOptionPane.showMessageDialog(mainWindow,
			    message + exitingString, "Fehler",
			    JOptionPane.ERROR_MESSAGE);
		System.exit(exitcode);
	}
}
