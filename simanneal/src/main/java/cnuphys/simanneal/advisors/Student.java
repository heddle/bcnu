package cnuphys.simanneal.advisors;

import cnuphys.simanneal.advisors.enums.Major;
import cnuphys.simanneal.advisors.io.ITabled;

public class Student implements ITabled{
	
	private String _id;
	
	private String _lastName;
	
	private String _firstName;
	
	private String _plp;
	
	private String _honr;
	
	private String _psp;
	
	private String _prelaw;
	
	public Major major;
	
	
	
	public Student(String id, String lastName, String firstName, String plp, String honr, String psp,
			String prelaw, String maj) {
		super();
		this._id = id.replace("\"", "");
		this._lastName = lastName.replace("\"", "");
		this._firstName = firstName.replace("\"", "");
		this._plp = plp.replace("\"", "");
		this._honr = honr.replace("\"", "");
		this._psp = psp.replace("\"", "");
		this._prelaw = prelaw.replace("\"", "");
		String majorstr = maj.replace("\"", "");
		
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
		return _id;
	}



	/**
	 * @param _id the _id to set
	 */
	public void setID(String _id) {
		this._id = _id;
	}



	/**
	 * @return the _lastName
	 */
	public String getLastName() {
		return _lastName;
	}



	/**
	 * @param _lastName the _lastName to set
	 */
	public void setLastName(String _lastName) {
		this._lastName = _lastName;
	}



	/**
	 * @return the _firstName
	 */
	public String getFirstName() {
		return _firstName;
	}



	/**
	 * @param _firstName the _firstName to set
	 */
	public void setFirstName(String _firstName) {
		this._firstName = _firstName;
	}



	/**
	 * @return the _plp
	 */
	public String getPlp() {
		return _plp;
	}



	/**
	 * @param _plp the _plp to set
	 */
	public void setPlp(String _plp) {
		this._plp = _plp;
	}



	/**
	 * @return the _honr
	 */
	public String getHonr() {
		return _honr;
	}



	/**
	 * @param _honr the _honr to set
	 */
	public void setHonr(String _honr) {
		this._honr = _honr;
	}



	/**
	 * @return the _psp
	 */
	public String getPsp() {
		return _psp;
	}



	/**
	 * @param _psp the _psp to set
	 */
	public void setPsp(String _psp) {
		this._psp = _psp;
	}



	/**
	 * @return the _prelaw
	 */
	public String getPrelaw() {
		return _prelaw;
	}



	/**
	 * @param _prelaw the _prelaw to set
	 */
	public void setPrelaw(String _prelaw) {
		this._prelaw = _prelaw;
	}



	/**
	 * @return the _major
	 */
	public Major getMajor() {
		return major;
	}



	/**
	 * @param _major the _major to set
	 */
	public void setMajor(Major _major) {
		this.major = _major;
	}



	/**
	 * Return the name and ID of the student in a string. This can be used
	 * for, among other things, sorting
	 * @return the name and ID of the student
	 */
	public String fullNameAndID() {
		return String.format("%s, %s [%s]", _lastName, _firstName, _id);
	}



	@Override
	public String getValueAt(int col) {
		if (col == 0) {
			return _id;
		}
		else if (col == 1) {
			return _lastName;
		}
		else if (col == 2) {
			return _firstName;
		}
		else if (col == 3) {
			return _plp;
		}
		else if (col == 4) {
			return _honr;
		}
		else if (col == 5) {
			return _psp;
		}
		else if (col == 6) {
			return _prelaw;
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
