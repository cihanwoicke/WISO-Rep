package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.WISOGrades;
import model.Area;
import model.CompleteExamList;
import model.Exam;
import model.Grade;

public class ResultFrame extends JFrame {

	@SuppressWarnings("unused")
	private final Color yellowTransp = new Color(255, 255, 0, 63);
	private final Color greyTransp = new Color(0,0,0,63);
	private final Color brightGreyTransp = new Color(0,0,0,15);
	private static final long serialVersionUID = 6264200744924413001L;
	private WISOGrades app;
	private JButton close;
	@SuppressWarnings("unused")
	private JButton excelButton; // TODO EXCEL-Export
	private JPanel contentPane;
	
	/* following lists are only needed for ui-colors of rects etc. */
	private List<JLabel> averageLabels = new ArrayList<JLabel>();
	private List<JLabel> areaLabels = new ArrayList<JLabel>();
	private List<JLabel> examGradeLabels = new ArrayList<JLabel>();
	private List<JLabel> firstExamLabelsOfArea = new ArrayList<JLabel>();
	
	private JLabel averageOverallLabel;
	private Font defaultFont = new Font("default", Font.PLAIN, 11);
	
	public ResultFrame(WISOGrades app, String userName) {
		this.app = app;
		setTitle("Notenübersicht von " + userName + " " +
				"(ohne Studium Integrale)");
		init();
	}
	
	private void init(){
		contentPane = new JPanel();
		setLocationByPlatform(true);
		setContentPane(contentPane);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		contentPane.setLayout(new GridBagLayout());
		
		int row = 0;
		Insets spaceRight = new Insets(0, 0, 0, 5);
		GridBagConstraints c0 = new GridBagConstraints();
		c0.gridx = 0;
		c0.gridy = row;
		c0.anchor = GridBagConstraints.LINE_START;
		c0.insets = spaceRight;
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 1;
		c1.gridy = row;
		c1.anchor = GridBagConstraints.LINE_START;
		c1.insets = spaceRight;
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 2;
		c2.gridy = row;
		c2.anchor = GridBagConstraints.LINE_START;
		c2.insets = spaceRight;
		GridBagConstraints c3 = new GridBagConstraints();
		c3.gridx = 3;
		c3.gridy = row;
		c3.anchor = GridBagConstraints.LINE_END;
		c3.insets = new Insets(0, 0, 0, 10);
		GridBagConstraints c4 = new GridBagConstraints();
		c4.gridx = 4;
		c4.gridy = row;
		c4.anchor = GridBagConstraints.LINE_END;
		c4.insets = spaceRight;
		
		CompleteExamList exams = app.getExams();
		
		for (Area area : exams.getAllAreas()){
			
			c0.gridy = row;
			c1.gridy = row;
			c2.gridy = row;
			c3.gridy = row;
			c4.gridy = row;
			
			JLabel areaLabel = new JLabel(area.getCompleteName()); 
			contentPane.add(areaLabel, c0);
			areaLabels.add(areaLabel);
			
			row++;
			
			c0.gridy = row;
			c1.gridy = row;
			c2.gridy = row;
			c3.gridy = row;
			c4.gridy = row;
		
			/**
			 * Integer to determine whether current exam is the
			 * first in its area to draw first line grey. 
			 */
			int posOfExamInArea = -1;  
			
			for (Exam exam : exams){

				if (exam.getArea().equals(area)){
					
					contentPane.add(new JLabel(""));
					
					JLabel idLabel = new JLabel(String.valueOf(exam.getId()));
					idLabel.setHorizontalAlignment(JLabel.RIGHT);
					idLabel.setFont(defaultFont);
					contentPane.add(idLabel, c1);
					
					JLabel nameLabel = new JLabel(exam.getName());
					nameLabel.setHorizontalAlignment(JLabel.RIGHT);
					nameLabel.setFont(defaultFont);
					contentPane.add(nameLabel, c2);
					
					JLabel cpLabel = new JLabel(String.valueOf(exam.getCreditpoints())
										.concat(" CP"));
					cpLabel.setHorizontalAlignment(JLabel.RIGHT);
					cpLabel.setFont(defaultFont);
					contentPane.add(cpLabel, c3);
					
					JLabel gradeLabel = new JLabel(String.valueOf(
							Math.round(exam.getRating().getNumericValue() * 10f) / 10f));
					gradeLabel.setHorizontalAlignment(JLabel.RIGHT);
					gradeLabel.setFont(defaultFont);
					contentPane.add(gradeLabel, c4);
					examGradeLabels.add(gradeLabel);
					
					if (++posOfExamInArea == 0){
						firstExamLabelsOfArea.add(gradeLabel);
					}
					
					
					if (exam.getRating().equals(Grade.FIVE)){
						idLabel.setForeground(Color.RED);
						nameLabel.setForeground(Color.RED);
						cpLabel.setForeground(Color.RED);
						gradeLabel.setForeground(Color.RED);
					}
					
					row++;
					c0.gridy = row;
					c1.gridy = row;
					c2.gridy = row;
					c3.gridy = row;
					c4.gridy = row;
				}
			}
			
			/* All exams of One area are processed
			 * so show the average grade of area:
			 */
			final JLabel averageLabel = new JLabel("Durchschnitt in " + area.getName() +
					": " + exams.getAverage(area));
			contentPane.add(averageLabel, c4);
			averageLabels.add(averageLabel);
			
			row++;
		}

		Insets bottomInset = new Insets(30, 0, 0, 5);
		
		/* 
		 * All areas are processed
		 * so show sum maluspoints:
		 */
		byte malus = exams.getSumMP();
		JLabel mpLabel = new JLabel("Maluspunkte: " + malus);
		mpLabel.setForeground(Color.RED);
		c1.gridy = row;
		c1.insets = bottomInset;
		contentPane.add(mpLabel, c1);
		
		/* 
		 * All areas are processed
		 * so show achieved creditpoints:
		 */
		JLabel cpLabel = new JLabel("Erreichte CreditPoints: " + exams.getSumCP());
		c3.gridy = row;
		c3.insets = bottomInset;
		contentPane.add(cpLabel, c3);
		
		/* All areas are processed
		 * so show overall average:
		 */		
		double averageDouble = exams.getAverageOverall(true);
		double averageSingle = Math.floor(averageDouble * 10) / 10d;
		c4.gridy = row;
		c4.insets = bottomInset;
		
		averageOverallLabel = new JLabel("Gesamtdurchschnitt: " +
					": " + averageSingle+ 
					" (" +averageDouble+ ")");
			contentPane.add(averageOverallLabel, c4);

		
		
		close = new JButton("schließen");
		close.addActionListener(new ActionListener() {
			@Override//Gees prog
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.setBackground(Color.WHITE);
		
		
		pack();
		
		setResizable(false);
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		
		int width = 0;
		int height;
		int x; 
		int y;
				
		final int offsetTop = getInsets().top;
		final int offsetBottom = getInsets().bottom;
		final int offsetLeft = getInsets().left;
		@SuppressWarnings("unused")
		final int offsetRight = getInsets().right;
		
//		/* Highlight averages */
//		g.setColor(yellowTransp);
//		
//		// get biggest width of all averageLabels
//		for (JLabel averageLabel : averageLabels){
//			width = Math.max(width, averageLabel.getWidth());
//		}
//		width += 10; // makes it longer and moves it a bit to the left
//		x = getWidth() - width - offsetLeft - offsetRight;		
//		
//		for (JLabel averageLabel : averageLabels){
//			y = averageLabel.getY() + offsetTop;
//			height = averageLabel.getHeight();
//			g.fillRect(x, y, width, height);
//		}
		
		/* Highlight areas */
		width = getWidth();
		x = offsetLeft;
		g.setColor(greyTransp);
		for (JLabel areaLabel : areaLabels){
			y = areaLabel.getY() + offsetTop;
			height = areaLabel.getHeight();
			g.fillRect(x, y, width, height);
		}
		
		/* Alternate grey and white for examlist */
		boolean firstLineGray = true;
		boolean grey = firstLineGray;
		g.setColor(brightGreyTransp);
		
		for (JLabel gradeLabel : examGradeLabels){
			if (firstExamLabelsOfArea.contains(gradeLabel)){
				grey = firstLineGray ;
			}
			if (grey){
				gradeLabel.getY();
				g.fillRect(offsetLeft, offsetTop + gradeLabel.getY(), 
						getWidth(), gradeLabel.getHeight());
			}
			grey = !grey;
		}
		
				
		/* Make a black bar at bottom*/
		width = getWidth();
		height = averageOverallLabel.getHeight();
		x = offsetLeft;
		y = getHeight()-(height + offsetTop) + offsetBottom;
		
		g.setColor(Color.BLACK);
		g.fillRect(x, y , width, height/2);
	}
}
