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
		
	}

	
	public MainFrame getMainWindow() {
		
		if (mainWindow == null)
			mainWindow = new MainFrame(this);
		
		return mainWindow;
	}


	public Errorhandler getErrorhandler() {
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
