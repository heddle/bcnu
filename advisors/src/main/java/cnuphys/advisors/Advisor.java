package cnuphys.advisors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cnuphys.advisors.enums.Department;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.model.DataManager;

public class Advisor implements ITabled {

	private ArrayList<Student> _advisees;

	/** the full name: last, first */
	public String name;

	/** the department */
	public Department department;
	
	/** the advisor's primary subject. might be same as depart. Or in come cases a major like music
	 * ASSIGNED FROM SCHEDULE! */
	public Major subject;
	
	/** faculty ID */
	public String id;
	
	/** does the instructor have an ILC */
	public boolean hasILC;

	//used for sorting students
	private static Comparator<Student> _comparator;

	static {
		_comparator = new Comparator<>() {

			@Override
			public int compare(Student o1, Student o2) {
				return o1.fullNameAndID().compareTo(o2.fullNameAndID());
			}

		};
	}

	/**
	 * Create an advisor
	 * @param name full name: last, first
	 * @param id faculty id
	 * @param dept academic department
	 */
	public Advisor(String name, String id, String dept) {
		this.name = name.replace("\"", "");
		this.id = DataManager.fixId(id);

		String deptstr = dept.replace("\"", "");

		//try to match to the enum
		department = Department.getValue(deptstr);

		if (department == null) {
			System.err.println("COULD not match department [" + deptstr + "]");
			System.exit(1);
		}
		
		//default the subject nased of department name
		subject = Major.getValue(department.name());
		
		if (subject == null) {
			System.err.println("COULD not subject to department [" + department.name() + "]");
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
		return String.format("%s  [%s]", name, department);
	}

	/**
	 * Get the value at a given column, considering this as a row
	 */
	@Override
	public String getValueAt(int col) {
		
		
		switch (col) {
		case 0:
			return name;
		case 1:
			return department.name();
		case 2:
			return subject.name();
		case 3:
			return id;
		case 4:
			return "" + _advisees.size();
		default:
			System.err.println("Bad column in Advisor getValueAt [" + col + "]");
			System.exit(0);

		}
		
		return null;
	}


}
