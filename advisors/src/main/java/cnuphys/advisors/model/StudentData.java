package cnuphys.advisors.model;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Student;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.table.CustomRenderer;
import cnuphys.advisors.table.InputOutput;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.X11Colors;

public class StudentData extends DataModel {

	//attributes for student data
	private static final DataAttribute studentAttributes[] = {DataManager.idAtt, DataManager.lastNameAtt,
			DataManager.firstNameAtt, DataManager.plpAtt, DataManager.honrAtt, DataManager.pspAtt,
			DataManager.prelawAtt, DataManager.majorAtt};


	public StudentData(String baseName) {
		super(baseName, studentAttributes);
		
		renderer = new CustomRenderer(this);
		
		for (int i = 0; i < getColumnCount(); i++) {
			_dataTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

	}


	@Override
	protected void processData() {
		InputOutput.debugPrintln("STUDENT row count: " + _data.size());

		int idIndex = getColumnIndex(DataManager.idAtt);
		int lastIndex = getColumnIndex(DataManager.lastNameAtt);
		int firstIndex = getColumnIndex(DataManager.firstNameAtt);
		int plpIndex = getColumnIndex(DataManager.plpAtt);
		int honrIndex = getColumnIndex(DataManager.honrAtt);
		int pspIndex = getColumnIndex(DataManager.pspAtt);
		int prelawIndex = getColumnIndex(DataManager.prelawAtt);
		int majorIndex = getColumnIndex(DataManager.majorAtt);

		for (String s[] : _data) {
			String id = s[idIndex];
    		String lastName = s[lastIndex];
			String firstName = s[firstIndex];
			String plp = s[plpIndex];
			String honr = s[honrIndex];
			String psp = s[pspIndex];
			String prelaw = s[prelawIndex];
			String major = s[majorIndex];

			_tableData.add(new Student(id, lastName, firstName, plp, honr, psp, prelaw, major));
		}

		//raw data not needed
		deleteRawData();

	}
	
	/**
	 * Get a highlight background color for a given row and column
	 * @param row the 0-based row
	 * @param column the 0-based column
	 * @return the highlight backgroundcolor, if null use default (black)
	 */
    @Override
	public Color getHighlightBackgroundColor(int row, int column) {
		Student student = getStudentFromRow(row);
		return (student.isHonor()) ? X11Colors.getX11Color("alice blue") : Color.white;
	}
    
	/**
	 * Get a highlight font for a given row and column
	 * @param row the 0-based row
	 * @param column the 0-based column
	 * @return the highlight color, if null use medium font
	 */
	public Font getHighlightFont(int row, int column) {
		Student student = getStudentFromRow(row);
		return (student.isPLP()) ? Fonts.mediumBoldFont : Fonts.mediumFont;
	}


    
 	/**
 	 * Get the student at the given 0-based row
 	 * @param row the row
 	 * @return the student at the given row
 	 */   
     public Student getStudentFromRow(int row) {
     	return (Student)getFromRow(row);
     }
     

	/**
	 * Get all the students in a list
	 * @return all the students
	 */
    public List<Student> getStudents() {

    	ArrayList<Student> list = new ArrayList<>();
		for (ITabled itabled : _tableData) {
			Student student = (Student) itabled;
			list.add(student);
		}

		return list;
    }


	@Override
	public void valueChanged(ListSelectionEvent e) {
	}



}
