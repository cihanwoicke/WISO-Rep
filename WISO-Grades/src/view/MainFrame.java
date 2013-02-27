/**
 * @author Cihan Öcal
 * <i> 25.02.2013 </i>
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import logic.HTMLConnector;
import logic.WISOGrades;

/**
 * 
 * @author Cihan Öcal
 * <i> 25.02.2013 </i>
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1307184780911584505L;
	private final String CMD1 = "loginViaHTTP";
	private JTextField nameField;
	private JPasswordField passwordField;
	private JButton button;
	private JPanel contentPane;
	private JPanel upperPanel = new JPanel();
	private JPanel bigPanel = new JPanel();
	private JPanel lowerPanel = new JPanel();
	private JPanel messagePanel; // only shown when fetching data in Progess.
	private final Color DARKGREEN = new Color(0 << 16 | (100 << 8) | 0 << 0); // R G B
	private boolean finished = true; // indicator whether data fetching is in progress
	private JLabel messageLabel;
	private String messageText = "Ihre Anfrage wird bearbeitet";
	
	private MyListener myListener;
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
		contentPane.setBackground(Color.WHITE);
		bigPanel.setLayout(new GridLayout(2,2));
		bigPanel.setBackground(Color.WHITE);
		upperPanel.setBackground(Color.WHITE);
		lowerPanel.setBackground(Color.WHITE);
		
		
		JLabel infoLabel = new JLabel();
		infoLabel.setText("Bitte melden Sie sich mit Ihrem S-Mail-Account an.");
		infoLabel.setFont(new Font("default", Font.BOLD, 12));
		
		JLabel nameLabel = new JLabel();
		nameLabel.setText("Benutzername ");
		nameLabel.setFont(new Font("default", Font.PLAIN, 11));
		
		nameField = new JTextField();
		nameField.setForeground(DARKGREEN);
		nameField.setText("dduck");
		nameField.setColumns(12);
		nameField.setFont(new Font("default", Font.PLAIN, 11));
		nameField.addKeyListener((KeyListener) getMyListener());
		nameField.setActionCommand(CMD1);
		
		JLabel passwordLabel = new JLabel();
		passwordLabel.setText("Passwort ");
		passwordLabel.setFont(new Font("default", Font.PLAIN, 11));
		
		passwordField= new JPasswordField();
		passwordField.setForeground(DARKGREEN);
		passwordField.setFont(new Font("default", Font.PLAIN, 11));
		passwordField.addKeyListener((KeyListener) getMyListener());
		passwordField.setColumns(14);
		passwordField.setActionCommand(CMD1);
		
		button = new JButton("Noten laden");
		button.setActionCommand(CMD1);
		button.addActionListener((ActionListener) getMyListener());
//		button.setEnabled(false);

		
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
		
	}
	
	public void showInputFields(){
		bigPanel.setVisible(true);
		lowerPanel.setVisible(true);
		messagePanel.setVisible(false);

	}
	
	private void hideInputFields(){
		bigPanel.setVisible(false);
		lowerPanel.setVisible(false);
	}
	

	private Component getMessagePanel() {
		if (messagePanel == null){
			messageLabel = new JLabel(messageText);
			messagePanel = new JPanel();
			messagePanel.setBackground(Color.WHITE);
			messagePanel.add(messageLabel);
		}
		return messagePanel;
	}

	private MyListener getMyListener() {
		if (myListener == null)
			myListener = new MyListener();
		return myListener;
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
	public void setFinished(boolean fin){
		
		finished = fin;
		
		if (isFinished()){
			showInputFields();
			
			if (app.getExams() != null){
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
	public void resetFields(){
		
		nameField.setText("");
		passwordField.setText("");

	}

	public boolean isFinished(){
		return finished;
	}

	/**
	 * Main method to start grabbing the examresults via HTTP. Once grabbing
	 * succeeded, method setFinished(boolean) will be called
	 * 
	 * @author Cihan Öcal <i> 25.02.2013 </i>
	 */
	public void startHTTPQuery() {
		String username = nameField.getText();
		String password = String.valueOf(passwordField.getPassword());
		
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
	private class MyListener extends KeyAdapter implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			String cmd = event.getActionCommand();
			if (cmd.equals(CMD1) && requiredFieldsFilled()){
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

	
	
}
