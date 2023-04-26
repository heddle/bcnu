package cnuphys.advisors;

import cnuphys.advisors.enums.Major;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.model.DataManager;

public class Student implements ITabled{

	public String id;

	public String lastName;

	public String _firstName;

	public String plp;

	public String honr;

	public String psp;

	public String prelaw;

	public Major major;
	
	/** is this a community captain? */
	public boolean communityCaptain;


/**
 * A student 
 * @param id
 * @param lastName
 * @param firstName
 * @param plp
 * @param honr
 * @param psp
 * @param prelaw
 * @param maj
 */
	public Student(String id, String lastName, String firstName, String plp, String honr, String psp,
			String prelaw, String maj) {
		super();
		this.id = DataManager.fixId(id);
		this.lastName = lastName.replace("\"", "").trim();
		this._firstName = firstName.replace("\"", "").trim();
		this.plp = plp.replace("\"", "").trim().toUpperCase();
		this.honr = honr.replace("\"", "").trim().toUpperCase();
		this.psp = psp.replace("\"", "").trim().toUpperCase();
		this.prelaw = prelaw.replace("\"", "").trim().toUpperCase();
		String majorstr = maj.replace("\"", "").trim();

		major = Major.getValue(majorstr);
		if (major == null) {
			System.err.println("COULD not match major [" + majorstr + "]");
			System.exit(1);
		}
	}



	/**
	 * @return the _id
	 */
	public String getID() {
		return id;
	}

	/**
	 * Is this an honor student?
	 * @return true if an honor student
	 */
	public boolean isHonor() {
		return((honr != null) && honr.contains("HO"));
	}
	
	/**
	 * Is this a plp student?
	 * @return true if a plp student
	 */
	public boolean isPLP() {
		return((plp != null) && plp.contains("PLP"));
	}
	
	/**
	 * Is this a prelaw student?
	 * @return true if a prelaw student
	 */
	public boolean isPrelaw() {
		return((prelaw != null) && prelaw.contains("LW"));
	}


	/**
	 * Return the name and ID of the student in a string. This can be used
	 * for, among other things, sorting
	 * @return the name and ID of the student
	 */
	public String fullNameAndID() {
		return String.format("%s, %s [%s]", lastName, _firstName, id);
	}



	@Override
	public String getValueAt(int col) {
		if (col == 0) {
			return id;
		}
		else if (col == 1) {
			return lastName;
		}
		else if (col == 2) {
			return _firstName;
		}
		else if (col == 3) {
			return plp;
		}
		else if (col == 4) {
			return honr;
		}
		else if (col == 5) {
			return psp;
		}
		else if (col == 6) {
			return prelaw;
		}
		else if (col == 7) {
			return major.name();
		}
		else {
			System.err.println("Bad column in Student getValueAt [" + col + "]");
			System.exit(0);
		}

		return null;
	}

}
