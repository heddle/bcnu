package cnuphys.advisors;

import java.util.ArrayList;
import java.util.List;

import cnuphys.advisors.enums.Department;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.model.Course;
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

	/** If this is diff from the advisor's subject, is is the prefeered "other" major of advisoors,
	 * e.g. Ryan Fisher and psych"
	 */
	public Major preferred2ndMajor;

	/** in case we want to disable an advisor */
	public boolean acceptingCohort = true;

	/** email address */
	public String email;

	/** advisor's schedule */
	public List<Course> schedule = new ArrayList<>();

	/**
	 * Create an advisor
	 * @param name full name: last, first
	 * @param id faculty id
	 * @param dept academic department
	 */
	public Advisor(String name, String id, String dept, String email) {
		this.name = name.replace("\"", "");
		this.id = DataManager.fixId(id);
		this.email = email;

		String deptstr = dept.replace("\"", "");

		//try to match to the enum
		department = Department.getValue(deptstr);

		if (department == null) {
			System.err.println("\nCOULD not match department [" + deptstr + "]");
			System.exit(1);
		}

		//default the subject nased of department name
		subject = Major.getValue(department.name());

		if (subject == null) {
			System.err.println("\nCOULD not subject to department [" + department.name() + "]");
			System.exit(1);
		}

		this.set(Person.MUSICTHEATER, (subject == Major.MUSIC) || (subject == Major.THEA));


		advisees = new ArrayList<>();
	}


	/**
	 * For a given target, this tells us how many slots are available.
	 * @param target the target number of advisees
	 * @return the number of slots, or 0 if negative
	 */
	public int slots(int target) {
		return Math.max(0, target - adviseeCount());
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
			System.out.println(s);
			return;
		}

		//is the student locked?
		if (student.locked()) {
			String s = String.format("Trying to assign a locked student. Advisor: [%s] Student: [%s]", name, student.fullNameAndID());
			System.out.println(s);
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

//		String s = String.format("Assignment made. Advisor: [%s] (%d) Student: [%s]", name, adviseeCount(), student.fullNameAndID());
//		System.out.println(s);

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


	//takes families into account if those flags set
	private boolean listContainsMajor(List<Major> majors , Major testMajor) {

		for (Major major : majors) {
			if (testMajor.isInMajorFamily(major)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Compute the number of differentmajors taking families into account
	 * if those options are set.
	 *
	 * @return the number of majors
	 */
	public int numDiffMajorsAdvising() {
		List<Major> majors = new ArrayList<>();

		for (Student student : advisees) {
			if (!listContainsMajor(majors, student.major)) {
				majors.add(student.major);
			}
		}

		return majors.size();

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
			return email;
		case 5:
			return id;
		case 6:
			return "" + advisees.size();
		case 7:
			return "" + numDiffMajorsAdvising();
		default:
			System.out.println("Bad column in Advisor getValueAt [" + col + "]");
			System.exit(0);

		}

		return null;
	}


}
