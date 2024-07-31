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
		writeIndividualFiles();
	}
	
	private static void writeIndividualFiles() {
		List<Advisor> advisors = DataManager.getAdvisorData().getAdvisors();
		for (Advisor advisor : advisors) {
			writeAdvisorFile(advisor);
		}
	}
	
	private static void writeAdvisorFile(Advisor advisor) {
		File af = new File(_outputDir, removeNonLetters(advisor.name) + ".csv");
		af.delete();

		CSVWriter csvw = new CSVWriter(af.getAbsolutePath());
		writeHeader(csvw);
		
		Comparator<Student> scomp = new Comparator<>() {

			@Override
			public int compare(Student s1, Student s2) {
				int c1 = s1.lastName.compareTo(s2.lastName);
				return (c1 != 0) ? c1 : s1.firstName.compareTo(s2.firstName);
			}

		};


		List<Student> students = advisor.advisees;
		
		Collections.sort(students, scomp);

		String sArr[] = new String[23];
		for (Student student : students) {
			writeAssignment(csvw, advisor, student, sArr);
		}
		csvw.close();
	}
	
	   public static String removeNonLetters(String input) {
	        // Use a regular expression to replace all non-letter characters and whitespace with an empty string
	        String cleanedString = input.replaceAll("[^a-zA-Z]", "");
	        return cleanedString;
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

		String sArr[] = new String[23];
		for (Advisor advisor : advisors) {

			List<Student> students = advisor.advisees;
			Collections.sort(students, scomp);

			for (Student student : students) {
				writeAssignment(csvw, advisor, student, sArr);
			}
		}
		
		csvw.close();
	}


// write an individual assignment
	private static void writeAssignment(CSVWriter csvw, Advisor advisor, Student student, String sArr[]) {

		sArr[0] = "\"\t" + student.id + "\"";  //will get leading 0's
		sArr[1] = student.lastName;
		sArr[2] = student.firstName;
		sArr[3] = student.prefFirst;
		sArr[4] = student.sport;
		sArr[5] = student.alc() ? "ALC" : "";
		sArr[6] = student.plp() ? "PLP" : "";
		sArr[7] = student.honors() ? "HON": "";
		sArr[8] = student.prsc() ? "PRSC": "";
		sArr[9] = student.psp() ? "PSP" : "";
		sArr[10] = student.wind() ? "WIND" : "";
		sArr[11] = student.ccpt() ? "CCAP" : "";
		sArr[12] = student.major.name();
		sArr[13] = student.email;
		sArr[14] = student.prstr1;
		sArr[15] = student.prstr2;
		sArr[16] = student.prcity;
		sArr[17] = student.prstate;
		sArr[18] = student.przip;
		sArr[19] = "\"" + advisor.name + "\"";
		sArr[20] = "\"\t" + advisor.id + "\"";  //will get leading 0's
		sArr[21] = advisor.department.name();
		sArr[22] = student.reason.name();

		csvw.writeRow(sArr);
	}

	private static void writeHeader(CSVWriter csvw) {
		csvw.writeRow("ID", // 0
				"Last", // 1
				"FIRST", // 2
				"PREF_FIRST", // 3
				"SPORT", // 4
				"ALC", // 5
				"PLP", // 6
				"HONR", // 
				"PRSC", // 8
				"PSP", // 9
				"WIND", // 10
				"CCAP", // 11
				"MAJOR_1ST", // 12
				"EMAIL", // 13
				"PR_STR1", // 14
				"PR_STR2", // 15
				"PR_CITY", // 16
				"PR_STATE", // 17
				"PR_ZIP", // 18
				"ADVISOR", // 19
				"ADV_ID", // 20
				"ADV1_DEPT", // 21
				"REASON" // 22
		);
	}


}
