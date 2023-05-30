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

		writeCatalogFile();
		writeAssignmentsFile();
	}

	//write catalog requirements by dept and advisor
	private static void writeCatalogFile() {
		File catFile = new File(_outputDir, "catalogs.txt");
		catFile.delete();

		PrintWriter pw;

		try {
			pw = new PrintWriter(catFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		pw.println("Catalog Distribution");



		int total = 0;

		for (Department dept : Department.values()) {
			pw.println("\n" + dept.name());
			int departTot = 0;

			List<Advisor> advisors = DataManager.getAdvisorsForDepartment(dept);

			for (Advisor advisor : advisors) {
				int count = advisor.adviseeCount();
				String s = String.format("  %s  [%d]", advisor.name, count);
				pw.println(s);
				departTot += count;
				total += count;

			}
			pw.println("Department Count: " + departTot);
		}

		pw.println("\nTotal catalogs: " + total);

		pw.flush();
		pw.close();
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

		String sArr[] = new String[18];
		for (Advisor advisor : advisors) {

			List<Student> students = advisor.advisees;
			Collections.sort(students, scomp);

			for (Student student : students) {
				writeAssignment(csvw, advisor, student, sArr);
			}
		}
	}



	private static void writeAssignment(CSVWriter csvw, Advisor advisor, Student student, String sArr[]) {

		sArr[0] = student.id;
		sArr[1] = student.lastName;
		sArr[2] = student.firstName;
		sArr[3] = student.ilc() ? "ILC" : "";
		sArr[4] = "L" + student.lcNum;
		sArr[5] = student.plp() ? "PLP" : "";
		sArr[6] = student.honors() ? "HON": "";
		sArr[7] = student.prsc() ? "PRSC": "";
		sArr[8] = student.psp() ? "PSP" : "";
		sArr[9] = student.prelaw() ? "PLW" : "";
		sArr[10] = student.wind() ? "WIND" : "";
		sArr[11] = student.ccpt() ? "CCAP" : "";
		sArr[12] = student.btmg() ? "BTMG" : "";
		sArr[13] = student.major.name();
		sArr[14] = "\"" + advisor.name + "\"";
		sArr[15] = "FCA";
		sArr[16] = advisor.department.name();
		sArr[17] = advisor.email;

		csvw.writeRow(sArr);
	}

	private static void writeHeader(CSVWriter csvw) {
		csvw.writeRow("ID", // 1
				"Last", // 2
				"FIRST", // 3
				"ILC", // 4
				"LC", // 5
				"PLP", // 6
				"HONR", // 7
				"PRSC", // 8
				"PSP", // 9
				"PRELAW", // 10
				"WIND", // 11
				"CCAP", // 12
				"BTMG", // 13
				"MAJOR_1ST", // 14
				"ADVISOR", // 15
				"ADV_TYPE", // 16
				"ADV1_DEPT", // 17
				"ADVISOR_EMAIL" // 18
		);
	}


}
