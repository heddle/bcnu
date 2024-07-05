package cnuphys.advisors.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;

public class ALCData extends DataModel {

	// attributes for student data
	private static final DataAttribute attributes[] = { DataManager.rowAtt, DataManager.lcAtt, DataManager.crnAtt,
			DataManager.subjectAtt, DataManager.courseAtt, 
			DataManager.instructorAtt, DataManager.enrollmentAtt};

	public ALCData(String baseName) {
		super(baseName, attributes);
	}

	@Override
	protected void processData() {
		int lcIndex = getColumnIndex(DataManager.lcAtt);
		int crnIndex = getColumnIndex(DataManager.crnAtt);
		int subjectIndex = getColumnIndex(DataManager.subjectAtt);
		int courseIndex = getColumnIndex(DataManager.courseAtt);

		for (String s[] : _data) {
			String lcTitle = s[lcIndex];
			lcTitle = lcTitle.replace("(ILC)", "");

			String crn = s[crnIndex];
			String subject = s[subjectIndex];
			String course = s[courseIndex];

			_tableData.add(new ALCCourse(lcTitle, crn, subject, course));
		}
		
		for (ITabled itabled : _tableData) {
			ALCCourse alc = (ALCCourse) itabled;
			Course course = DataManager.getSchedule().getCourseFromCRN(alc.crn);
			
			// should not be null
			if (course == null) {
				System.err.println("ALCData: course is null for alc crn: " + alc.crn);
			}
			
			course.isALC = true;
		}
		
		//raw data not needed
		deleteRawData();

		if (AdvisorAssign.DEBUG) {
			int i = 0;
			for (ITabled itabled : _tableData) {
				ALCCourse alc = (ALCCourse) itabled;
				String s = String.format("%-4d %s", (++i), alc.infoString());
				System.out.println(s);
			}
		}
		
		

	}

	/**
	 * Get all the ALC courses in a list
	 *
	 * @return all the ALC courses
	 */
	public List<ALCCourse> getALCs() {

		ArrayList<ALCCourse> list = new ArrayList<>();
		for (ITabled itabled : _tableData) {
			ALCCourse course = (ALCCourse) itabled;
			list.add(course);
		}

		return list;
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
