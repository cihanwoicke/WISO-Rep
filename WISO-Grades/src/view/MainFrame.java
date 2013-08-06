/**
 * @author Cihan Öcal
 * <i> 25.02.2013 </i>
 */
package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import logic.HTMLConnector;
import logic.WISOGrades;
import model.WISOColors;

/**
 * 
 * @author Cihan Öcal
 * <i> 25.02.2013 </i>
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1307184780911584505L;
	private final String loginCommand = "loginViaHTTP";
	private JTextField nameField;
	private Document nameDocument;
	private JPasswordField passwordField;
	private Document passwordDocument;
	
	private String username = "unbekannt";
	private String password;
	private JButton button;
	private JPanel contentPane;
	private JPanel upperPanel = new JPanel();
	private JPanel bigPanel = new JPanel();
	private JPanel lowerPanel = new JPanel();
	private JPanel messagePanel; // only shown when fetching data in progress.
	private boolean finished = true; // indicator whether data fetching is in progress
	private JLabel messageLabel;
	private String messageText = "Ihre Anfrage wird bearbeitet";
	
	private MyKeyListener myKeyListener;
	private MyDocumentListener myDocumentListener;
	private WISOGrades app;
	public MainFrame(WISOGrades app) {
		this.app = app;
		setTitle("WISO-Notenberechnung (inoffiziell)");
	}
	
	public void constructUi() {
		
		contentPane = new JPanel();
		
		setLocationByPlatform(true);
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.setLayout(new BorderLayout(10,10));
		contentPane.setBackground(WISOColors.BACKGROUND);
		bigPanel.setLayout(new GridLayout(2,2));
		bigPanel.setBackground(WISOColors.BACKGROUND);
		upperPanel.setBackground(WISOColors.BACKGROUND);
		lowerPanel.setBackground(WISOColors.BACKGROUND);
		
		
		JLabel infoLabel = new JLabel();
		infoLabel.setText("Bitte melden Sie sich mit Ihrem S-Mail-Account an.");
		infoLabel.setFont(new Font("default", Font.BOLD, 12));
		
		JLabel nameLabel = new JLabel();
		nameLabel.setText("Benutzername ");
		nameLabel.setFont(new Font("default", Font.PLAIN, 11));
		
		nameField = new JTextField();
		nameField.setForeground(WISOColors.DARKGREENTEXT);
		nameField.setText("dduck");
		
		nameField.setColumns(12);
		nameField.setFont(new Font("default", Font.PLAIN, 11));
		nameField.addKeyListener((KeyListener) getMyKeyListener());
		nameField.setActionCommand(loginCommand);
		nameDocument = nameField.getDocument();
		nameDocument.addDocumentListener(getMyDocumentListener());
		
		JLabel passwordLabel = new JLabel();
		passwordLabel.setText("Passwort ");
		passwordLabel.setFont(new Font("default", Font.PLAIN, 11));
		
		passwordField= new JPasswordField();
		passwordField.setForeground(WISOColors.DARKGREENTEXT);
		passwordField.setFont(new Font("default", Font.PLAIN, 11));
		passwordField.addKeyListener((KeyListener) getMyKeyListener());
		passwordField.setColumns(14);
		passwordDocument = passwordField.getDocument();
		passwordDocument.addDocumentListener(getMyDocumentListener());
		
		
		
		button = new JButton("Noten laden");
		button.setActionCommand(loginCommand);
		button.addActionListener((ActionListener) getMyKeyListener());
		button.setEnabled(false);

		
		upperPanel.add(infoLabel);
		bigPanel.add(nameLabel);
		bigPanel.add(nameField);
		nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		bigPanel.add(passwordLabel);
		bigPanel.add(passwordField);
		lowerPanel.add(button);

		
		nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		contentPane.add(upperPanel, BorderLayout.PAGE_START);
		contentPane.add(bigPanel, BorderLayout.CENTER);
		contentPane.add(lowerPanel, BorderLayout.PAGE_END);
		pack();
		setVisible(true);
		setResizable(false);
		
		nameField.selectAll();
		
	}
	
	public void showInputFields() {
		bigPanel.setVisible(true);
		lowerPanel.setVisible(true);
		messagePanel.setVisible(false);

	}
	
	private void hideInputFields() {
		bigPanel.setVisible(false);
		lowerPanel.setVisible(false);
	}
	

	private Component getMessagePanel() {
		if (messagePanel == null){
			messageLabel = new JLabel(messageText);
			messagePanel = new JPanel();
			messagePanel.setBackground(WISOColors.BACKGROUND);
			messagePanel.add(messageLabel);
		}
		return messagePanel;
	}

	private MyKeyListener getMyKeyListener() {
		if (myKeyListener == null)
			myKeyListener = new MyKeyListener();
		return myKeyListener;
	}
	
	private MyDocumentListener getMyDocumentListener(){
		if (myDocumentListener == null)
			myDocumentListener = new MyDocumentListener();
		return myDocumentListener;
	}

	private boolean requiredFieldsFilled() {
		return (!nameField.getText().equals("")
				&&
				!String.valueOf(passwordField.getPassword()).equals(""));
				
	}

	
	

	/**
	 * This starts a thread! <br />
	 * Shows a message to the user, that his exams are being fetched
	 * 
	 * @author Cihan Öcal
	 * <i> 25.02.2013 </i>
	 */
	public void showProgressMessage() {
		
		hideInputFields();
		contentPane.add(getMessagePanel());
		getMessagePanel().setVisible(true);
		
		/*
		 * Show progress
		 */
		Thread thread = new Thread(){
			
			public void run(){
				
				setFinished(false);
				
				try {
					byte b = 0;
					while (!isFinished()){
						String dots = "";
						for (byte a = 1; a <= b; a++){
							dots = dots.concat(".");
						}
						messageLabel.setText(messageText.concat(dots));
						if (++b == 4)
							b = 0;
						
						sleep(666);
					}
				} catch (InterruptedException e) {
					String message ="Schwerwiegender Fehler. ";
					app.getErrorhandler().showError(message, 2);
				}
			}
		};
		thread.start();
	}


	/**
	 * Call this method to indicate, that fetching data 
	 * or doing some other logic is finished.
	 * @param fin
	 * @author Cihan Öcal
	 * <i> 26.02.2013 </i>
	 */
	public void setFinished(boolean fin) {

		finished = fin;

		if (isFinished()) {
			showInputFields();

			if (app.getExams() != null) {
				app.getExams().print();
				new ResultFrame(app);
			}

		}
	}
	
	/**
	 * Resets name and password field.
	 * 
	 * @author Cihan Öcal
	 * <i> 26.02.2013 </i>
	 */
	public void resetFields() {
		nameField.setText("");
		passwordField.setText("");
	}

	public boolean isFinished() {
		return finished;
	}

	/**
	 * Main method to start grabbing the exam results via HTTP. It also sets 
	 * fields <i>username</i> and <i>password</i>.
	 * the Once grabbing
	 * succeeded, method setFinished(boolean) will be called
	 * 
	 * @author Cihan Öcal <i> 25.02.2013 </i>
	 */
	public void startHTTPQuery() {
		username = nameField.getText();
		password = String.valueOf(passwordField.getPassword());
		
		showProgressMessage();
		
		Thread connector = new Thread(new HTMLConnector(app, username, password));
		
		password = null;
		
		connector.start(); // loads examList
	}

	
	/* --------------------- Inner Classes -----------------------*/
	
	/**
	 * Inner listener-class for mainFrame.
	 * 
	 * @author Cihan Öcal
	 * <i> 25.02.2013 </i>
	 */
	private class MyKeyListener extends KeyAdapter implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			String cmd = event.getActionCommand();
			if (cmd.equals(loginCommand) && requiredFieldsFilled()){
				startHTTPQuery();
			}
			
		}
		
		@Override
		public void keyTyped(KeyEvent key){
			if (key.getKeyChar() == KeyEvent.VK_ENTER && requiredFieldsFilled()){
				startHTTPQuery();
			}
		}
		
	}
	
	/**
	 * Checks whether two Fields are empty or not.
	 * if both are empty, login-button will be disabled 
	 * 
	 * @author Cihan
	 * <i> 06.08.2013 </i>
	 */
	private class MyDocumentListener implements DocumentListener{

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			disableOrEnableButton();
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			disableOrEnableButton();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			disableOrEnableButton();
		}
		
		private void disableOrEnableButton(){
			if (passwordDocument.getLength() == 0
					|| nameDocument.getLength() == 0) 
				button.setEnabled(false);
			else
				button.setEnabled(true);
		}
	}

	
	
}
