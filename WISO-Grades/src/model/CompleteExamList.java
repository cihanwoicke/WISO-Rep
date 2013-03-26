package model;

import java.util.TreeSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CompleteExamList extends LinkedList<Exam> {

	private static final long serialVersionUID = 6768416802026411605L;
	private Student student = null; // Student that belongs to these exams
	
	public Set<Area> getAllAreas(){
		Set<Area> allAreas = new TreeSet<Area>();
		for (Exam exam : this){
			allAreas.add(exam.getArea());
		}
		return allAreas;
	}
	
	public List<Exam> getExamsOfArea(Area area){
		List<Exam> examsOfArea = new LinkedList<Exam>();
	
		for (Exam exam : this){
			if (exam.getArea().equals(area)){
				examsOfArea.add(exam);
			}
		}
		
		return examsOfArea;
	}
	
	public int getAreaQty(){
		return getAllAreas().size();
	}
	
	
	public void print(){
	
		Set<Area> allAreas = getAllAreas();
		
		for (Area area : allAreas){
			System.out.println("AREA: " + area.getCompleteName());
			for (Exam exam : this){
				if (exam.getArea().equals(area)){
					System.out.println("              " +
							exam.getId() + " " + exam.getName() + " " +
							exam.getCreditpoints() + "CP; Note: " + 
							exam.getRating().toString());
				}
			}
		}
	}

	public double getAverage(Area area){
		
		if (area.getName().equalsIgnoreCase("Studium Integrale")){
			return 0;
		}
		
		double sumCP = 0;
		double sumWeightedGrades = 0;
		for (Exam exam : getExamsOfArea(area)){
			Grade grade = exam.getRating();
			if (grade != Grade.FIVE){
				
				byte cp = exam.getCreditpoints();
				sumCP += exam.getCreditpoints();
				sumWeightedGrades += (cp * grade.getNumericValue());
			}
		}
	
		if (sumCP != 0)
			return (Math.floor(
					Math.round((sumWeightedGrades / sumCP) * 1000)/1000d * 10 ) / 10d );
		else
			return 0;
		
	}
	
	public double getAverageOverall(boolean twoFractionalDigits){
		
		double accuracy = twoFractionalDigits ? 100 : 10;
		int cp;
		int sumCP = 0;
		double sumWeightedAreaGrades = 0d;
		
		for (Area area : getAllAreas()){
			
			/* Studium Integrale does not affect Overall Average */
			if (area.getName().equalsIgnoreCase("Studium Integrale")){
				continue;
			}
			
			// else
			cp = getCpOfArea(area); 
			sumWeightedAreaGrades += (getAverage(area) * cp);
			sumCP += cp;
		}
				
		if (sumCP != 0){
			return (Math.floor(
					Math.round((sumWeightedAreaGrades / sumCP) * 1000)/1000d * accuracy) / accuracy);
		} else
			return 0f;
	}
	
	private short getCpOfArea(Area area){
		short cp = 0;
		
		for (Exam exam : getExamsOfArea(area)){
			
			if (exam.getRating() != Grade.FIVE)
				cp += exam.getCreditpoints();
		}
		
		return cp;
	}
	
	/**
	 * 
	 * @return
	 * @author Cihan Öcal
	 * <i> 26.02.2013 </i>
	 */
	public short getSumCP(){
		
		short sumCP = 0;
		for (Exam exam : this){
			if (!exam.getRating().equals(Grade.FIVE)){ 
				sumCP += exam.getCreditpoints();
			}
		}
		
		return sumCP;
	}
	
	public byte getSumMP(){
		
		byte malP = 0;
		for (Exam exam : this){
			if (exam.getRating().equals(Grade.FIVE)){ 
				malP -= exam.getCreditpoints();
			}
		}
		
		return malP;
	}

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
	
	public Student getStudent(){
		return student;
	}
}
