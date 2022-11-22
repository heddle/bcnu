package cnuphys.simanneal.advisors;

public class Student {
	
	//the usual CNU ID
	private String ID = "?";
	
	//last name
	private String LAST = "?";
	
	//first name
	private String FIRST = "?";
	
	//first major
	private String MAJOR_1st = "?";
	

	
	
	public Student(String id, String lastName, String firstName) {
		ID = id;
		LAST = lastName;
		FIRST = firstName;
	}
	
	/**
	 * Return the name and ID of the student in a string. This can be used
	 * for, among other things, sorting
	 * @return the name and ID of the student
	 */
	public String fullNameAndID() {
		return String.format("%s, %s, [%s]", LAST, FIRST, ID);
	}

}
