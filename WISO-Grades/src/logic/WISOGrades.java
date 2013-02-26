package logic;

import model.CompleteExamList;

import view.MainFrame;

public class WISOGrades {

	private CompleteExamList exams;
	private MainFrame mainWindow;
	private Errorhandler errorhandler;
	
	/**
	 * Constructor
	 * @author Cihan Öcal
	 * <i> 25.02.2013 </i>
	 */
	public WISOGrades() {
		start();
	}
	
	
	/**
	 * 
	 * @param args
	 * @author Cihan Öcal
	 */
	public void start() {
		
		getMainWindow().constructUi();
		
		
//		byte maxAttempts = 3;
//		byte qtyAttempts = 0;
//		// Ask for userData until it is successfully saved and 
//		// until user is successfully logged in
//		while (++qtyAttempts <= maxAttempts && (!readUserData() || !login()));
//		
//		if (qtyAttempts > maxAttempts){
//			System.err.println("3 fehlerhafte Eingaben. Bitte starten Sie das Programm erneut.");
//			System.exit(1);
//		}
		
//		Elements[] gradeTables = getGradeTables();
//		/* 
//		 * List at position 0 contains headers
//		 * List at position 1 contains tables
//		 */
//		
//		if (gradeTables[0] == null || gradeTables[1] == null){
//			System.err.println("Es konnte keine Notenliste gefunden werden.");
//			System.err.println("Das Programm wird jetzt beendet...");
//			System.exit(2);
//		}
//		// else (gradeTables != null)
//		convertGradeTables(gradeTables);
		
	}

	
	public MainFrame getMainWindow() {
		
		if (mainWindow == null)
			mainWindow = new MainFrame(this);
		
		return mainWindow;
	}


	public Errorhandler getErrorhandler() {
		// TODO Auto-generated method stub
		if (errorhandler == null)
			errorhandler = new Errorhandler(this);
		return errorhandler;
	}


	/**
	 * 
	 * @return
	 * @author Cihan Öcal
	 * <i> 25.02.2013 </i>
	 */
	public CompleteExamList getExams() {
		return exams;
	}


	public void setExamList(CompleteExamList exams) {
		this.exams = exams;
	}

}
