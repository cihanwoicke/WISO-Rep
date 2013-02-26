package model;

public class Exam implements Comparable<Exam>{

	private final Area area;
	private final int id;
	private String name;
	private byte creditpoints = 0;
	private Grade rating;
	

	public Exam(final Area area, final int id) {
		this.area = area;
		this.id = id;
	}
	
	public byte getCreditpoints() {
		return creditpoints;
	}

	public void setCreditpoints(byte creditpoints) {
		this.creditpoints = creditpoints;
	}

	public Grade getRating() {
		return rating;
	}

	public void setRating(Grade rating) {
		this.rating = rating;
	}

	public Area getArea() {
		return area;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	public int compareTo(Exam o) {
		int result = ((Integer)getId()).compareTo(o.getId());
		result += 100* getArea().getCompleteName().compareTo(o.getArea().getCompleteName());
		return result;
	}
}
