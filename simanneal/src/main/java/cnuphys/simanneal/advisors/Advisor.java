package cnuphys.simanneal.advisors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cnuphys.simanneal.advisors.enums.Department;
import cnuphys.simanneal.advisors.io.ITabled;

public class Advisor implements ITabled {
	
	private ArrayList<Student> _advisees;
	
	/** the first name */
	public String lastName;
	
	/** the last name */
	public String firstName;
	
	/** the department */
	public Department department;
	
	//used for sorting students
	private static Comparator<Student> _comparator;
	
	static {
		_comparator = new Comparator<Student>() {

			@Override
			public int compare(Student o1, Student o2) {
				return o1.fullNameAndID().compareTo(o2.fullNameAndID());
			}
			
		};
	}
 
	/**
	 * Create an advisor 
	 * @param last last name
	 * @param first first name
	 * @param dept department
	 */
	public Advisor(String last, String first, String dept) {
		lastName = last.replace("\"", "");;
		firstName = first.replace("\"", "");;
		String deptstr = dept.replace("\"", "");
		
		department = Department.getValue(deptstr);
		
		if (department == null) {
			System.err.println("COULD not match department [" + deptstr + "]");
			System.exit(1);
		}		
		_advisees = new ArrayList<>();
		
		
	}
	
	/**
	 * Add an advisee to this advisor's collection
	 * @param student
	 */
	public void addAdvisee(Student student) {
		int index = Collections.binarySearch(_advisees, student, _comparator);
		
		if (index >= 0) { // duplicate
			System.err.println("[WARNING]");
			return;
		} else {
			index = -(index + 1); // now the insertion point.
		}
		

	}
	
	/**
	 * Remove an advisee from the list of advisees
	 * @param student the student to remove
	 * @return true if it found the student in the list and removed the student
	 */
	public boolean removeAdvisee(Student student) {
		return _advisees.remove(student);
	}
	
	/**
	 * full name (last, first) and department is a single string
	 * @return full name (last, first) and department is a single string
	 */
	public String nameAndDepartment() {
		return String.format("%s, %s  [%s]", lastName, firstName, department);
	}
	
	/**
	 * Get the value at a given column, considering this as a row
	 */
	@Override
	public String getValueAt(int col) {
		if (col == 0) {
			return lastName;
		}
		else if (col == 1) {
			return firstName;
		}
		else if (col == 2) {
			return department.name();
		}
		else {
			System.err.println("Bad column in Advisor getValueAt [" + col + "]");
			System.exit(0);
		}
		
		return null;
	}
	

}
