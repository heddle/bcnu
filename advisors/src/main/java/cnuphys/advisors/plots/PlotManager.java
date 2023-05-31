package cnuphys.advisors.plots;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.enums.Department;
import cnuphys.advisors.enums.Major;
import cnuphys.advisors.graphics.AdvisorDisplay;
import cnuphys.advisors.graphics.BarPlot;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.model.AdvisorData;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.model.StudentData;

public class PlotManager {


	/**
	 * Number of advisors per number of majors bar plot
	 */
	public static void handleFCAsPerMajors() {
		String title = "FCAs per Major in Department";
		Department.clearFcaCounts();
		Department.clearMajorCounts();

		AdvisorData advisorData = DataManager.getAdvisorData();

		for (Advisor fca : advisorData.getAdvisors()) {
			Department.incrementFcaCount(fca.department);
		}

		StudentData studentData = DataManager.getStudentData();

		for (Student student : studentData.getStudents()) {
			Department department = student.major.getDepartment();
			Department.incrementMajorCount(department);
		}

		String categories[] = Department.getBaseNames();
		int majorCounts[] = Department.getMajorCounts();
		int fcaCounts[] = Department.getFcaCounts();

		double ratio[] = new double[majorCounts.length];

		for (int i = 0; i < ratio.length; i++) {

			if ((majorCounts[i] == 0) || (fcaCounts[i] == 0)) {
				ratio[i] = 0;
				continue;
			}

			ratio[i] = ((double)fcaCounts[i]/(double)majorCounts[i]);
		}


		BarPlot barPlot = new BarPlot(title, categories, ratio);
		AdvisorDisplay.getInstance().setContent(barPlot);

	}

	/**
	 * FCA by department bar plot
	 */
	public static void handleFCAByDept() {
		String title = "FCAs by Department";

		Department.clearFcaCounts();

		AdvisorData advisorData = DataManager.getAdvisorData();

		for (Advisor fca : advisorData.getAdvisors()) {
			Department.incrementFcaCount(fca.department);
		}

		String categories[] = Department.getBaseNames();
		int counts[] = Department.getFcaCounts();

		BarPlot barPlot = new BarPlot(title, categories, counts);
		AdvisorDisplay.getInstance().setContent(barPlot);
	}

	/**
	 * Students by department bar plot
	 */
	public static void handleStudentsByDepartment() {
		String title = "Students by Department";
		Department.clearMajorCounts();

		StudentData studentData = DataManager.getStudentData();

		for (Student student : studentData.getStudents()) {
			Department department = student.major.getDepartment();
			Department.incrementMajorCount(department);
		}
		String categories[] = Department.getBaseNames();
		int counts[] = Department.getMajorCounts();

		BarPlot barPlot = new BarPlot(title, categories, counts);
		AdvisorDisplay.getInstance().setContent(barPlot);
	}

	/**
	 * Students by major bar plot
	 */
	public static void handleStudentsByMajor() {
		String title = "Students by Major";

		Major.clearCounts();

		StudentData studentData = DataManager.getStudentData();

		List<ITabled> data = studentData.getData();

		for (ITabled itabled : data) {
			Student student = (Student)itabled;
			Major.incrementCount(student.major);
		}

		String categories[] = Major.getBaseNames();
		int counts[] = Major.getCounts();

		BarPlot barPlot = new BarPlot(title, categories, counts);
		AdvisorDisplay.getInstance().setContent(barPlot);
	}

	/**
	 * Number of unique majors
	 */
	public static void numberUniqueMajors() {
		int length = 22;
		String categories[] = new String[length];
		int counts[] = new int[length];

		for (int i = 0; i < length; i++) {
			categories[i] = "" + i;
			counts[i] = 0;
		}

		AdvisorData advisorData = DataManager.getAdvisorData();

		for (Advisor fca : advisorData.getAdvisors()) {
			int n = fca.numDiffMajorsAdvising();
			counts[n] += 1;
		}

		BarPlot barPlot = new BarPlot("Number of Unique Majors for FCA", categories, counts);
		AdvisorDisplay.getInstance().setContent(barPlot);
	}

}
