package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CompleteAreaList extends ArrayList<Area> {

	private static final long serialVersionUID = 6768416802026411605L;
	private Student student = null; // Student that belongs to these exams
	
	
	/* ------------------- GETTER -------------------*/ 
	
	public List<Exam> getExamsOfArea(Area area){
		
		List<Exam> examsOfArea = area.getAllExams();
		return examsOfArea;
	}
	
	public double getAverageOverall(boolean twoFractionalDigits){
		
		int accuracy = twoFractionalDigits ? 2 : 1;
		BigDecimal cp;
		BigDecimal sumCP = BigDecimal.ZERO;
		BigDecimal sumWeightedAreaGrades = BigDecimal.ZERO;
		
		for (Area area : this){
			
			// else
			cp = new BigDecimal(area.getSumCpForAverage()); 
			BigDecimal area_average = new BigDecimal(area.getAverage()); 
			sumWeightedAreaGrades = sumWeightedAreaGrades.add(area_average.multiply(cp));
			sumCP = sumCP.add(cp);
		}
				
		if (sumCP != BigDecimal.ZERO){
		
			BigDecimal result = sumWeightedAreaGrades.divide(sumCP, 6, RoundingMode.HALF_UP);
			result = result.setScale(accuracy, RoundingMode.DOWN);
			return result.doubleValue();
		
		} else
			return 0f;
	}
	
	/**
	 * 
	 * @return
	 * @author Cihan Öcal
	 * <i> 26.02.2013 </i>
	 */
	public short getSumCP(){
		
		short sumCP = 0;
		for (Area area : this){
			for (Exam exam : area.getAllExams()){
				if (exam.getCreditpoints() > 0){ 
					sumCP += exam.getCreditpoints();
				}
			}
		}
		
		return sumCP;
	}
	
	public byte getSumMP(){
		
		byte malP = 0;
		for (Area area : this){
			for (Exam exam : area.getAllExams()){
				if (exam.getCreditpoints() < 0){ 
					malP -= exam.getCreditpoints();
				}
			}
		}
		
		return malP;
	}

	public Student getStudent(){
		return student;
	}

	/* ------------------- SETTER -------------------*/ 

	/**
	 * @param student
	 * @author Cihan
	 * <i> 26.03.2013 </i>
	 */
	public void setStudent(Student student) {
		if (this.student !=null){
			System.err.println("Student " + student.getUsername() +
					" überschreibt Studenten " +
					this.student.getUsername());
		}
		this.student = student;
	}


	public void print(){
	
		for (Area area : this){
			System.out.println("AREA: " + area.getCompleteName());
			for (Exam exam : area.getAllExams()){
				System.out.println("              " +
						exam.getId() + " " + exam.getName() + " " +
						exam.getCreditpoints() + "CP; Note: " + 
						exam.getRating().toString());
			
			}
		}
	}
}
