package cnuphys.advisors.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Student;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.table.InputOutput;

public class Schedule extends DataModel {

	//attributes for schedule data
	private static final DataAttribute scheduleAttributes[] = {DataManager.crnAtt, 
			DataManager.courseAtt, DataManager.sectionAtt,
			DataManager.titleAtt, DataManager.hoursAtt,
			DataManager.llcAtt, DataManager.typeAtt,
			DataManager.daysAtt, DataManager.timeAtt,
			DataManager.locationAtt, DataManager.instructorAtt};

	
	public Schedule(String baseName) {
		super(baseName, scheduleAttributes);
	}



	@Override
	protected void processData() {
		int colCount = _header.length;
		InputOutput.debugPrintln("SCHEDULE row count: " + _data.size());
		InputOutput.debugPrintln("SCHEDULE col count: " + colCount);
		
		int crnIndex = getColumnIndex(DataManager.crnAtt);
		int courseIndex = getColumnIndex(DataManager.courseAtt);
		int sectionIndex = getColumnIndex(DataManager.sectionAtt);
		int titleIndex = getColumnIndex(DataManager.titleAtt);
		int hoursIndex = getColumnIndex(DataManager.hoursAtt);
		int llcIndex = getColumnIndex(DataManager.llcAtt);
		int typeIndex = getColumnIndex(DataManager.typeAtt);
		int daysIndex = getColumnIndex(DataManager.daysAtt);
		int timeIndex = getColumnIndex(DataManager.timeAtt);
		int locationIndex = getColumnIndex(DataManager.locationAtt);
		int instructorIndex = getColumnIndex(DataManager.instructorAtt);
		
		for (String s[] : _data) {
			String crn = s[crnIndex];
			String course = s[courseIndex];
			String section = s[sectionIndex];
			String title = s[titleIndex];
			String hours = s[hoursIndex];
			String llc = s[llcIndex];
			String type = s[typeIndex];
			String days = s[daysIndex];
			String time = s[timeIndex];
			String location = s[locationIndex];
			String instructor = s[instructorIndex];
			
			_tableData.add(new Course(crn, course, section, title, hours, llc, type, days, time, location, instructor));
		}

		//raw data not needed
		deleteRawData();

	}
	
	
	/**
	 * Get all the courses in a list
	 * @return all the students
	 */
    public List<Course> getCourses() {
    	
    	ArrayList<Course> list = new ArrayList<>();
		for (ITabled itabled : _tableData) {
			Course course = (Course) itabled;
			list.add(course);
		}

		return list;
    }
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
	}

}
