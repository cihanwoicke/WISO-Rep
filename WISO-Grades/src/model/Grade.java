package model;

public enum Grade {
	ONE(1), ONE_THREE(1.3f), ONE_SEVEN(1.7f), 
	TWO(2f), TWO_THREE(2.3f), TWO_SEVEN(2.7f), 
	THREE(3f), THREE_THREE(3.3f), THREE_SEVEN(3.7f), 
	FOUR(4f), FIVE(5f), NaN(-1f);
	final float numericValue;
	
	Grade(float numericValue){
		this.numericValue = numericValue;
	}


	public float getNumericValue() {
		return numericValue;
	}
	
	public static Grade getGradeByNumericValue(float value){
		for (Grade i : Grade.values())
			if (i.getNumericValue() == value)
				return i;
		// nothing found
		System.err.println("Note ungültig nach Prüfungsordnung WISO '2013");
		return FIVE;
	}
}
