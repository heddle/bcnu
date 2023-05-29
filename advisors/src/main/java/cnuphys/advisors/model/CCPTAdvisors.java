package cnuphys.advisors.model;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.Person;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;

public class CCPTAdvisors extends DataModel  {


	//attributes for honors advisors data
	private static final DataAttribute Attributes[] = {DataManager.rowAtt, DataManager.idAtt, DataManager.lastNameAtt,
			DataManager.firstNameAtt};


	public CCPTAdvisors(String baseName) {
		super(baseName, Attributes);
	}

	@Override
	protected void processData() {
		int idIndex = getColumnIndex(DataManager.idAtt);
		int lastIndex = getColumnIndex(DataManager.lastNameAtt);
		int firstIndex = getColumnIndex(DataManager.firstNameAtt);

		//don't create an actual model, just mark the corresponding advisor as a community captains advisor
		for (String s[] : _data) {
			String id = DataManager.fixId(s[idIndex]);
			String lastName = s[lastIndex];
			String firstName = s[firstIndex];

			Advisor advisor = DataManager.getAdvisorData().getAdvisorFromId(id);
			if (advisor == null) {
				System.out.println(String.format("Did not match ccpt advisor [%s] %s, %s to any current advisor", id,
						lastName, firstName));
			} else {
				advisor.set(Person.CCPT);
			}
		}

		//raw data not needed
		deleteRawData();

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
