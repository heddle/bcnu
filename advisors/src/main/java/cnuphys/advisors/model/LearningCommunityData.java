package cnuphys.advisors.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.table.CustomRenderer;

public class LearningCommunityData extends DataModel {


	// attributes for student data
	private static final DataAttribute attributes[] = { DataManager.rowAtt,
			DataManager.lcAtt, DataManager.enrollmentAtt, DataManager.crnAtt,
			DataManager.subjectAtt, DataManager.courseAtt, DataManager.llcAtt, DataManager.notesAtt,
			DataManager.instructorAtt};

	public LearningCommunityData(String baseName) {
		super(baseName, attributes);

		renderer = new CustomRenderer(this);

		for (int i = 0; i < getColumnCount(); i++) {
			_dataTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

	}

	/**
	 * Get a highlight text color for a given row and column
	 * @param row the 0-based row
	 * @param column the 0-based column
	 * @return the hightlight color, if null use default (black)
	 */
    @Override
	public Color getHighlightTextColor(int row, int column) {
		LearningCommunityCourse lc = getLearningCommunityCourseFromRow(row);
		return lc.ilc ? Color.red : Color.black;
	}

	/**
	 * Get the learning community at the given 0-based row
	 * @param row the row
	 * @return the lc at the given row
	 */
    public LearningCommunityCourse getLearningCommunityCourseFromRow(int row) {
    	return (LearningCommunityCourse)getFromRow(row);
    }

	/**
	 * Get all the ILC courses in a list
	 *
	 * @return all the LC courses courses
	 */
	public List<LearningCommunityCourse> getLearningCommunityCourses() {

		ArrayList<LearningCommunityCourse> list = new ArrayList<>();
		for (ITabled itabled : _tableData) {
			LearningCommunityCourse course = (LearningCommunityCourse) itabled;
			list.add(course);
		}

		return list;
	}


	@Override
	protected void processData() {
		int lcIndex = 0;
		int titleIndex = getColumnIndex(DataManager.lcAtt);
		int crnIndex = getColumnIndex(DataManager.crnAtt);
		int subjectIndex = getColumnIndex(DataManager.subjectAtt);
		int courseIndex = getColumnIndex(DataManager.courseAtt);
		int llcIndex = getColumnIndex(DataManager.llcAtt);
		int notesIndex = getColumnIndex(DataManager.notesAtt);

		for (String s[] : _data) {
			String lcStr = s[lcIndex].trim();

			if (lcStr.length() < 1) {
				continue;
			}

			int lcNum = Integer.parseInt(lcStr);
			String lcTitle = s[titleIndex];
			lcTitle = lcTitle.replace("(ILC)", "");

			String crn = s[crnIndex];
			String subject = s[subjectIndex];
			String course = s[courseIndex];
			String llc = s[llcIndex];
			if (llc == null) {
				llc = "";
			}

			String notes = s[notesIndex];

			_tableData.add(new LearningCommunityCourse(lcNum, lcTitle, crn, subject, course, llc, notes));
		}
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
