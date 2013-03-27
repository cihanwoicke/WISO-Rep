package logic;

import model.CompleteAreaList;

import view.MainFrame;

public class WISOGrades {

	private CompleteAreaList exams;
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
	public CompleteAreaList getExams() {
		return exams;
	}


	public void setExamList(CompleteAreaList exams) {
		this.exams = exams;
	}

}
