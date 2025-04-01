package cnuphys.advisors.model;

import java.awt.Rectangle;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.dialogs.AdvisorDialog;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;

public class HonorsAdvisors extends DataModel {

	private AdvisorDialog _dialog;

	//attributes for honors advisors data
	private static final DataAttribute honAdvAttributes[] = {DataManager.rowAtt, DataManager.idAtt, DataManager.lastNameAtt,
			DataManager.firstNameAtt, DataManager.directorAtt};


	public HonorsAdvisors(String baseName) {
		super(baseName, honAdvAttributes);
	}

	@Override
	protected void processData() {

		int idIndex = getColumnIndex(DataManager.idAtt);
		int lastIndex = getColumnIndex(DataManager.lastNameAtt);
		int firstIndex = getColumnIndex(DataManager.firstNameAtt);
		int directorIndex = getColumnIndex(DataManager.directorAtt);

		//dont create an actual model, just mark the corresponding advisor  as an honors advisor
		for (String s[] : _data) {
			String id = DataManager.fixId(s[idIndex]);
			String lastName = s[lastIndex];
			String firstName = s[firstIndex];
			String dirString = s[directorIndex];

			Advisor advisor = DataManager.getAdvisorData().getAdvisorFromId(id);
			if (advisor == null) {
				System.out.println(String.format("Did not match honors advisor [%s] %s, %s to any current advisor", id,
						lastName, firstName));
				continue;
			} else {
				advisor.setHonors();
			}


			if (dirString.contains("Y")) {
				DataManager.honorsDirector = advisor;
			}
		}

		//raw data not needed
		deleteRawData();

	}



	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
		}
	}



	/**
	 * Double clicked on a row
	 * @param row the 0-based row
	 * @param o the object at that location
	 */
	@Override
	protected void doubleClicked(int row, ITabled o) {
		Advisor advisor = (Advisor)o;

		Rectangle bounds = null;

		if (_dialog != null) {
			bounds = _dialog.getBounds();
			_dialog.setVisible(false);
		}

		_dialog = new AdvisorDialog(advisor);

		if (bounds != null) {
			_dialog.setBounds(bounds);
		}

		_dialog.setVisible(true);

	}

}
