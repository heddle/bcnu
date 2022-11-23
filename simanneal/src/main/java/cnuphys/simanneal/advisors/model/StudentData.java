package cnuphys.simanneal.advisors.model;

import javax.swing.event.ListSelectionEvent;

import cnuphys.simanneal.advisors.Advisor;
import cnuphys.simanneal.advisors.Student;
import cnuphys.simanneal.advisors.io.DataModel;
import cnuphys.simanneal.advisors.table.InputOutput;

public class StudentData extends DataModel {
	
	//attributes for student data
	private static final DataAttribute studentAttributes[] = {DataManager.idAtt, DataManager.lastNameAtt, 
			DataManager.firstNameAtt, DataManager.plpAtt, DataManager.honrAtt, DataManager.pspAtt,
			DataManager.prelawAtt, DataManager.majorAtt};

	
	public StudentData(String baseName) {
		super(baseName, studentAttributes);
	}


	@Override
	protected void processData() {
		int colCount = _header.length;
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
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
	}



}
