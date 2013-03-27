package model;

public class Exam implements Comparable<Exam>{

private final int id;
	private String name;
	private byte creditpoints = 0;
	private Grade rating;
	

	public Exam(final int id) {
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
		return result;
	}
}
