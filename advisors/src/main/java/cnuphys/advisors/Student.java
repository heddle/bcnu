package cnuphys.advisors;

import java.util.ArrayList;
import java.util.List;

import cnuphys.advisors.enums.EReason;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.model.ALCCourse;
import cnuphys.advisors.model.Course;
import cnuphys.advisors.model.DataManager;

public class Student extends Person implements ITabled {

	/** learning community as a string */
	public String lc;

	/** the student's last name */
	public String lastName;

	/** the student's first name */
	public String firstName;

	/** the student's major */
	public Major major;

	/** the assigned advisor */
	public Advisor advisor;

	/** block from banner */
	public String bannerBlock;

	/** the student's preferred first name */
	public String prefFirst;

	/** the student's email */
	public String email;

	/** the student's sport */
	public String sport;

	/** part of student address */
	public String prstr1;

	/** part of student address */
	public String prstr2;

	/** part of student address */
	public String prcity;

	/** part of student address */
	public String prstate;

	/** part of student address */
	public String przip;

	/** thereason for the assignment */
	public EReason reason = EReason.NONE;

	/** Student schedule only of classes taught by an FCA */
	public List<Course> schedule = new ArrayList<>();


	public Student(String id, String lastName, String firstName, String alc, String plp, String honr, String prsc,
			String psp, String wind, String ccap, String maj, String bannerBlock,
			String prefFirst, String email, String sport, String prstr1, String prstr2, String prcity, String prstate,
			String przip) {
		super();

		this.id = DataManager.fixId(id);
		this.lastName = lastName.replace("\"", "").trim();
		this.firstName = firstName.replace("\"", "").trim();
		this.set(Person.ALC, checkString(alc, "ALC"));
		this.set(Person.PLP, checkString(plp, "PLP"));
		this.setHonors(checkString(honr, "HO"));
		this.set(Person.PRESSCHOLAR, checkString(prsc, "PRS"));
		this.set(Person.PREMEDSCHOLAR, checkString(psp, "PSP"));
		this.set(Person.WIND, checkString(wind, "WIN"));
		this.set(Person.CCPT, checkString(ccap, "CCAP"));
		this.bannerBlock = bannerBlock;

		this.prefFirst = prefFirst;
		this.email = email;
		this.sport = sport;
		this.prstr1 = prstr1;
		this.prstr2 = prstr2;
		this.prcity = prcity;
		this.prstate = prstate;
	    this.przip = przip;



		String majorstr = maj.replace("\"", "").trim();

		major = Major.getValue(majorstr);
		if (major == null) {
			System.out.println("COULD not match major [" + majorstr + "]");
			System.exit(1);
		}

		this.set(Person.MUSICTHEATER, (major == Major.MUSIC) || (major == Major.THEA));
		this.set(Person.PREBUS, major.isPreBusiness());
		set(Person.ENGR, major.isEngineering());

	}

	//check a string for a pattern
	private boolean checkString(String s, String patt) {
		return s.replace("\"", "").trim().toUpperCase().contains(patt.toUpperCase());
	}

	public boolean hasCourseWithAdvisor() {
		if (advisor != null) {
			for (Course course : schedule) {

				if (course.honors() && (course.id.equals(DataManager.honorsDirector.id))) {
					continue;
				}

				if (advisor.id.equals(course.id)) {
					return true;
				}
			}
		}

		return false;
	}

	public Course courseWithThisAdvisor(Advisor advisor) {
		for (Course course : schedule) {
			if (advisor.id.equals(course.id)) {
				return course;
			}
		}
		return null;
	}


	/**
	 * Add a course to the student's schedule
	 * @param course the course to add
	 */
	public void addCourse(Course course) {
		schedule.remove(course);
		schedule.add(course);
	}


	/**
	 * @return the _id
	 */
	public String getID() {
		return id;
	}

	/**
	 * Is this student assigned an advisor?
	 * @return true if student has an advisor
	 */
	public boolean assigned() {
		return (advisor != null);
	}


	/**
	 * Return the name and ID of the student in a string. This can be used
	 * for, among other things, sorting
	 * @return the name and ID of the student
	 */
	public String fullNameAndID() {
		return String.format("%s, %s [%s]", lastName, firstName, id);
	}

	/**
	 * Is the student a presidential scholar?
	 * @return true if the student a presidential scholar
	 */
	public boolean prsc() {
		return check(Person.PRESSCHOLAR);
	}

	/**
	 * Is the student in PLP?
	 * @return true if the student is in plp
	 */
	public boolean plp() {
		return check(Person.PLP);
	}

	/**
	 * Is the student a premed scholar?
	 * @return true if the student is a premed scholar
	 */
	public boolean psp() {
		return check(Person.PREMEDSCHOLAR);
	}

	/**
	 * Is the student prebus?
	 * @return true if the student is prelaw
	 */
	public boolean prebus() {
		return check(Person.PREBUS);
	}

	/**
	 * Is the student a wind ensemble scholar?
	 * @return true if the student is a wind ensemble scholar
	 */
	public boolean wind() {
		return check(Person.WIND);
	}

	/**
	 * Is the student a community captain?
	 * @return true if the student is a community captain
	 */
	public boolean ccpt() {
		return check(Person.CCPT);
	}


	@Override
	public String getValueAt(int col) {
		if (col == 1) {
			return id;
		}
		else if (col == 2) {
			return lastName;
		}
		else if (col == 3) {
			return firstName;
		}
		else if (col == 4) {
			return alc() ? "ALC" : "";
		}
		else if (col == 5) {
			return plp() ? "PLP" : "";
		}
		else if (col == 6) {
			return honors() ? "HON": "";
		}
		else if (col == 7) {
			return prsc() ? "PRSC": "";
		}
		else if (col == 8) {
			return psp() ? "PSP" : "";
		}
		else if (col == 9) {
			return prebus() ? "PREBUS" : "";
		}
		else if (col == 10) {
			return wind() ? "WIND" : "";
		}
		else if (col == 11) {
			return ccpt() ? "CCAP" : "";
		}
		else if (col == 12) {
			return major.name();
		}
		else if (col == 13) {
			return bannerBlock;
		}
		else if (col == 14) {
			return advisor == null ? "---" : advisor.name;
		}
		else if (col == 15) {
			return reason == EReason.NONE ? "---" : reason.name();
		}
		else if (col == 16) {
			return hasCourseWithAdvisor() ? "Y" : "";
		}

		else {
			System.out.println("Bad column in Student getValueAt [" + col + "]");
			System.exit(0);
		}

		return null;
	}

	/**
	 * Is the student in an ALC's learning community?
	 *
	 * @return true if the student is in an ALC
	 */
	public boolean inALC_LC(ALCCourse alc) {

		if (!alc()) {
			return false;
		}

		int alcNum = extractNumber(alc.lcNum);
		if (alcNum <= 0) {
			return false;
		}

		int lcnum = extractNumber(bannerBlock);
		return alcNum == lcnum;
	}

	private int extractNumber(String input) {
	        // Use a regular expression to replace all non-numeric characters with an empty string
	        String numericString = input.replaceAll("[^0-9]", "");

	        // If the numericString is empty, return 0
	        if (numericString.isEmpty()) {
	            return 0;
	        }

	        // Convert the numeric string to an integer
	        return Integer.parseInt(numericString);
	    }


}
