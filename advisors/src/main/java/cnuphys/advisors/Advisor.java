package cnuphys.advisors;

import java.util.ArrayList;

import cnuphys.advisors.enums.Department;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.model.DataManager;

public class Advisor extends Person implements ITabled {

	/** list of students */
	public ArrayList<Student> advisees;

	/** the full name: last, first */
	public String name;

	/** the department */
	public Department department;

	/** the advisor's primary subject. might be same as depart. Or in come cases a major like music
	 * ASSIGNED FROM SCHEDULE! */
	public Major subject;

	/** in case we want to disable an advisor */
	public boolean acceptingCohort = true;

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

		advisees = new ArrayList<>();
	}

	/**
	 * Add an advisee to this advisor's collection
	 * @param student
	 * @param lockStudentWhenDone if true lock down student so can't be reassigned by algorithm
	 */
	public void addAdvisee(Student student, boolean lockStudentWhenDone) {

		//is the advisor locked?
		if (locked()) {
			String s = String.format("Trying to assign to locked advisor. Advisor: [%s] Student: [%s]", name, student.fullNameAndID());
			System.err.println(s);
			return;
		}

		//is the student locked?
		if (student.locked()) {
			String s = String.format("Trying to assign a locked student. Advisor: [%s] Student: [%s]", name, student.fullNameAndID());
			System.err.println(s);
			return;
		}

		//are we replacing an advisor?
		if (student.assigned()) {
			student.advisor.removeAdvisee(student);
		}

		advisees.remove(student);
		advisees.add(student);

		student.setLocked(lockStudentWhenDone);
		student.advisor = this;

		String s = String.format("Assignment made. Advisor: [%s] (%d) Student: [%s]", name, adviseeCount(), student.fullNameAndID());
		System.err.println(s);

	}

	/**
	 * Remove an advisee from the list of advisees
	 * @param student the student to remove
	 * @return true if it found the student in the list and removed the student
	 */
	public boolean removeAdvisee(Student student) {
		return advisees.remove(student);
	}

	/**
	 * full name (last, first) and department is a single string
	 * @return full name (last, first) and department is a single string
	 */
	public String nameAndDepartment() {
		return String.format("%s  [%s]", name, department);
	}

	/**
	 * The number of assigned advisees
	 * @return the number of assigned advisees
	 */
	public int adviseeCount() {
		return advisees.size();
	}

	/**
	 * Get the value at a given column, considering this as a row
	 */
	@Override
	public String getValueAt(int col) {


		switch (col) {
		case 1:
			return name;
		case 2:
			return department.name();
		case 3:
			return subject.name();
		case 4:
			return id;
		case 5:
			return "" + advisees.size();
		default:
			System.err.println("Bad column in Advisor getValueAt [" + col + "]");
			System.exit(0);

		}

		return null;
	}


}
