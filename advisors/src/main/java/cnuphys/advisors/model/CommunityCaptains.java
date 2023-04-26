package cnuphys.advisors.model;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Student;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.table.InputOutput;

public class CommunityCaptains extends DataModel {
	//attributes for student data
	private static final DataAttribute studentAttributes[] = {DataManager.idAtt, DataManager.lastNameAtt,
			DataManager.firstNameAtt};

	public CommunityCaptains(String baseName) {
		super(baseName, studentAttributes);
		
	}
	
	@Override
	protected void processData() {
		InputOutput.debugPrintln("CC row count: " + _data.size());

		int idIndex = getColumnIndex(DataManager.idAtt);
		int lastIndex = getColumnIndex(DataManager.lastNameAtt);
		int firstIndex = getColumnIndex(DataManager.firstNameAtt);
		
		//dont create an actual model, just mark the corresponding student  as a cc
		for (String s[] : _data) {
			String id = DataManager.fixId(s[idIndex]);
			id.replace("\"", "").trim();
    		String lastName = s[lastIndex];
			String firstName = s[firstIndex];
			
			Student student = DataManager.getStudentData().getStudentFromId(id);
			if (student == null) {
				System.err.println(String.format("Did not match community captain [%] %s, %s", id, lastName, firstName));
				System.exit(1);
			}
			
			student.communityCaptain = true;
		}

		//raw data not needed
		deleteRawData();

	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
	}


}
