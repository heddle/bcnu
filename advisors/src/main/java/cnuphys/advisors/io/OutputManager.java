package cnuphys.advisors.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Student;
import cnuphys.advisors.enums.Department;
import cnuphys.advisors.model.DataManager;
import cnuphys.advisors.table.InputOutput;
import cnuphys.bCNU.util.CSVWriter;

public class OutputManager {

	//the output folder
	private static File _outputDir;

	/**
	 * We are done with the assignments. Write the results
	 */
	public static void outputResults() {
		File dataDir = InputOutput.getDataDir();
		_outputDir = new File(dataDir, "output");
		_outputDir.mkdir();

		writeAssignmentsFile();
	}


	//write the all important assignments file
	private static void writeAssignmentsFile() {
		File asf = new File(_outputDir, "assignments.csv");
		asf.delete();

		CSVWriter csvw = new CSVWriter(asf.getAbsolutePath());
		writeHeader(csvw);

		Comparator<Advisor> acomp = new Comparator<>() {

			@Override
			public int compare(Advisor a1, Advisor a2) {
				return a1.name.compareTo(a2.name);
			}

		};


		List<Advisor> advisors = DataManager.getAdvisorData().getAdvisors();
		Collections.sort(advisors, acomp);

		Comparator<Student> scomp = new Comparator<>() {

			@Override
			public int compare(Student s1, Student s2) {
				int c1 = s1.lastName.compareTo(s2.lastName);
				return (c1 != 0) ? c1 : s1.firstName.compareTo(s2.firstName);
			}

		};

		String sArr[] = new String[15];
		for (Advisor advisor : advisors) {

			List<Student> students = advisor.advisees;
			Collections.sort(students, scomp);

			for (Student student : students) {
				writeAssignment(csvw, advisor, student, sArr);
			}
		}
	}


// write an individual assignment
	private static void writeAssignment(CSVWriter csvw, Advisor advisor, Student student, String sArr[]) {

		sArr[0] = "\"\t" + student.id + "\"";  //will get leading 0's
		sArr[1] = student.lastName;
		sArr[2] = student.firstName;
		sArr[3] = student.alc() ? "ALC" : "";
		sArr[4] = student.plp() ? "PLP" : "";
		sArr[5] = student.honors() ? "HON": "";
		sArr[6] = student.prsc() ? "PRSC": "";
		sArr[7] = student.psp() ? "PSP" : "";
		sArr[8] = student.wind() ? "WIND" : "";
		sArr[9] = student.ccpt() ? "CCAP" : "";
		sArr[10] = student.major.name();
		sArr[11] = "\"" + advisor.name + "\"";
		sArr[12] = "\"\t" + advisor.id + "\"";  //will get leading 0's
		sArr[13] = advisor.department.name();
		sArr[14] = student.reason.name();

		csvw.writeRow(sArr);
	}

	private static void writeHeader(CSVWriter csvw) {
		csvw.writeRow("ID", // 1
				"Last", // 2
				"FIRST", // 3
				"ALC", // 4
				"PLP", // 5
				"HONR", // 6
				"PRSC", // 7
				"PSP", // 8
				"WIND", // 9
				"CCAP", // 10
				"MAJOR_1ST", // 11
				"ADVISOR", // 12
				"ADV_ID", // 13
				"ADV1_DEPT", // 14
				"REASON" // 15
		);
	}


}
