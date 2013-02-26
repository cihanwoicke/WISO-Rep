package model;

import java.util.TreeSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CompleteExamList extends LinkedList<Exam> {

	private static final long serialVersionUID = 6768416802026411605L;
	
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
							exam.getRating().getNumericValue());
				}
			}
		}
	}

	public double getAverage(Area area){
		double sumCP = 0;
		double sumWeightedGrades = 0l;
		for (Exam exam : getExamsOfArea(area)){
			Grade grade = exam.getRating();
			if (grade != Grade.FIVE){
				
				byte cp = exam.getCreditpoints();
				sumCP += exam.getCreditpoints();
				sumWeightedGrades += (cp * grade.numericValue);
			}
		}
	
		if (sumCP != 0)
			return (Math.floor((sumWeightedGrades / sumCP) * 10) / 10d );
		else
			return 0;
		
	}
	
	public double getAverageOverall(boolean twoFractionalDigits){
		
		double accuracy = twoFractionalDigits ? 100 : 10;
		int cp;
		int sumCP = 0;
		double sumWeightedAreaGrades = 0d;
		
		for (Area area : getAllAreas()){
			cp = getCpOfArea(area); 
			sumWeightedAreaGrades += (getAverage(area) * cp);
			sumCP += cp;
		}
				
		if (sumCP != 0){
			return (Math.floor((sumWeightedAreaGrades / sumCP) * accuracy) / accuracy);
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
}