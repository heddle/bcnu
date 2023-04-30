package cnuphys.advisors.model;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.IFilter;
import cnuphys.advisors.Student;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.table.CustomRenderer;
import cnuphys.advisors.table.InputOutput;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.X11Colors;

/**
 * All the students
 * @author heddle
 *
 */
public class StudentData extends DataModel {

	//attributes for student data
	private static final DataAttribute studentAttributes[] = { DataManager.rowAtt, DataManager.idAtt, DataManager.lastNameAtt,
			DataManager.firstNameAtt, DataManager.ilcAtt, DataManager.plpAtt, DataManager.honrAtt, DataManager.prscAtt,
			DataManager.pspAtt, DataManager.prelawAtt, DataManager.windAtt, DataManager.ccapAtt, DataManager.majorAtt,
			DataManager.advisorAtt };

	public StudentData(String baseName) {
		super(baseName, studentAttributes);
		
		renderer = new CustomRenderer(this);
		
		for (int i = 0; i < getColumnCount(); i++) {
			_dataTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
	}
	
	/**
	 * private constructor used to make a submodel
	 * @param baseModel
	 * @param filter
	 */
	private StudentData(StudentData baseModel, IFilter filter) {
		super(baseModel, filter);
	}
	
	/**
	 * Create a submodel using a filter
	 */
	public StudentData subModel(IFilter filter) {
		return new StudentData(this, filter);
	}



	@Override
	protected void processData() {
		InputOutput.debugPrintln("STUDENT row count: " + _data.size());

		int idIndex = getColumnIndex(DataManager.idAtt);
		int lastIndex = getColumnIndex(DataManager.lastNameAtt);
		int firstIndex = getColumnIndex(DataManager.firstNameAtt);
		int prscIndex = getColumnIndex(DataManager.prscAtt);
		int windIndex = getColumnIndex(DataManager.windAtt);
		int ccapIndex = getColumnIndex(DataManager.ccapAtt);
		
		
		int plpIndex = getColumnIndex(DataManager.plpAtt);
		int honrIndex = getColumnIndex(DataManager.honrAtt);
		int pspIndex = getColumnIndex(DataManager.pspAtt);
		int prelawIndex = getColumnIndex(DataManager.prelawAtt);
		int majorIndex = getColumnIndex(DataManager.majorAtt);

		for (String s[] : _data) {
			String id = s[idIndex];
    		String lastName = s[lastIndex];
			String firstName = s[firstIndex];
			
			String prsc = s[prscIndex];
			String wind = s[windIndex];
			String ccap = s[ccapIndex];

			
			String plp = s[plpIndex];
			String honr = s[honrIndex];
			String psp = s[pspIndex];
			String prelaw = s[prelawIndex];
			String major = s[majorIndex];

			_tableData.add(new Student(id, lastName, firstName, plp, honr, prsc, psp, prelaw, wind, ccap, major));
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
		return (student.honor) ? X11Colors.getX11Color("alice blue") : Color.white;
	}
    
	/**
	 * Get a highlight font for a given row and column
	 * @param row the 0-based row
	 * @param column the 0-based column
	 * @return the highlight color, if null use medium font
	 */
	public Font getHighlightFont(int row, int column) {
		Student student = getStudentFromRow(row);
		return (student.plp) ? Fonts.mediumBoldFont : Fonts.mediumFont;
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
	 * Get the student with matching Id
	 * 
	 * @param id the id to match
	 * @return the student with matching id or null
	 */
	public Student getStudentFromId(String id) {

		for (Student student : getStudents()) {
			if (student.id.equals(id)) {
				return student;
			}
		}
		return null;
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
