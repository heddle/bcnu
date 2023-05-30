package cnuphys.advisors.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;

public class ILCData extends DataModel {

	// attributes for student data
	private static final DataAttribute attributes[] = { DataManager.rowAtt, DataManager.lcAtt, DataManager.crnAtt,
			DataManager.subjectAtt, DataManager.courseAtt, DataManager.llcAtt, DataManager.notesAtt,
			DataManager.instructorAtt, DataManager.enrollmentAtt};

	public ILCData(String baseName) {
		super(baseName, attributes);
	}

	@Override
	protected void processData() {
		int lcIndex = getColumnIndex(DataManager.lcAtt);
		int crnIndex = getColumnIndex(DataManager.crnAtt);
		int subjectIndex = getColumnIndex(DataManager.subjectAtt);
		int courseIndex = getColumnIndex(DataManager.courseAtt);
		int llcIndex = getColumnIndex(DataManager.llcAtt);
		int notesIndex = getColumnIndex(DataManager.notesAtt);

		for (String s[] : _data) {
			String lcTitle = s[lcIndex];
			lcTitle = lcTitle.replace("(ILC)", "");

			String crn = s[crnIndex];
			String subject = s[subjectIndex];
			String course = s[courseIndex];
			String llc = s[llcIndex];
			if (llc == null) {
				llc = "";
			}

			String notes = s[notesIndex];
			notes = notes.replace("ILC:", "");

			_tableData.add(new ILCCourse(lcTitle, crn, subject, course, llc, notes));
		}
	}

	/**
	 * Get all the ILC courses in a list
	 *
	 * @return all the ILC courses
	 */
	public List<ILCCourse> getILCs() {

		ArrayList<ILCCourse> list = new ArrayList<>();
		for (ITabled itabled : _tableData) {
			ILCCourse course = (ILCCourse) itabled;
			list.add(course);
		}

		return list;
	}

	/**
	 * Is the given crn an ILC course?
	 * @param crn
	 * @return ILC course or nll
	 */
	public boolean isILC(String crn) {
		for (ILCCourse ilc : getILCs()) {
			if (ilc.crn.equals(crn)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the ilc course from the crn
	 * @param crn the crn
	 * @return the ILC course, or null.
	 */
	public ILCCourse getILCCourse(String crn) {
		for (ILCCourse ilc : getILCs()) {
			if (ilc.crn.equals(crn)) {
				return ilc;
			}
		}

		return null;
	}

	/**
	 * Get the ilc course from the advisor
	 * @param advisor the advisor
	 * @return the ILC course, or null.
	 */
	public ILCCourse getILCCourse(Advisor advisor) {
		for (ILCCourse ilc : getILCs()) {
			if (ilc.instructor == advisor) {
				return ilc;
			}
		}

		return null;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
	}

	/**
	 * Double clicked on a row
	 * @param row the 0-based row
	 * @param o the object at that location
	 */
	@Override
	protected void doubleClicked(int row, ITabled o) {

	}

}
