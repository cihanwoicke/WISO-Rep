package model;
/**
 * 
 * @author Cihan
 * <i> 26.03.2013 </i>
 */
public class Student {

	private final String username;
	private final int prNr;
	
	/**
	 * @param username
	 * @param prNr
	 * @author Cihan
	 * <i> 26.03.2013 </i>
	 */
	public Student(String username, int prNr) {
		this.username = username;
		this.prNr = prNr;
	}

	/**
	 * @return
	 * @author Cihan
	 * <i> 26.03.2013 </i>
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return
	 * @author Cihan
	 * <i> 26.03.2013 </i>
	 */
	public int getPrNr() {
		return prNr;
	}
}
