package model;

public class Area implements Comparable<Area>{
	private final String name;
	private int id;

	public Area(final String name) {
		int i = 0;
		while (Character.isDigit(name.charAt(i))){
			i++;
		}
		
		try{
			id = Integer.parseInt(name.substring(0, i));
		} catch(NumberFormatException e){
			id = -1;
			e.printStackTrace();
			System.err.println("Konnte ID von " + name + " nicht auslesen!");
		}
		this.name = name.substring(i + 1);
	}
	
	/*
	 * Overridden for having no multiple entries in sets.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj){
		if (obj instanceof Area){
			if (this.getCompleteName().equals(((Area) obj).getCompleteName()))
				return true;
			else
				return false;
		} else
			return super.equals(obj);
		
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	public String getCompleteName(){
		return String.valueOf(getId()).concat(" ").concat(name);
	}

	@Override
	public int compareTo(Area targetArea) {
		return getCompleteName().compareTo(targetArea.getCompleteName());
	}

}
