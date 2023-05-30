package cnuphys.advisors.model;

import java.awt.Rectangle;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.dialogs.AdvisorDialog;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;

public class PSPAdvisors extends DataModel {

	private AdvisorDialog _dialog;
	
	//attributes for honors advisors data
	private static final DataAttribute pspAdvAttributes[] = {DataManager.rowAtt, DataManager.idAtt, DataManager.lastNameAtt,
			DataManager.firstNameAtt};


	public PSPAdvisors(String baseName) {
		super(baseName, pspAdvAttributes);
	}

	
	@Override
	protected void processData() {

		int idIndex = getColumnIndex(DataManager.idAtt);
		int lastIndex = getColumnIndex(DataManager.lastNameAtt);
		int firstIndex = getColumnIndex(DataManager.firstNameAtt);

		//dont create an actual model, just mark the corresponding advisor  as an honors advisor
		for (String s[] : _data) {
			String id = DataManager.fixId(s[idIndex]);
			String lastName = s[lastIndex];
			String firstName = s[firstIndex];

			Advisor advisor = DataManager.getAdvisorData().getAdvisorFromId(id);
			if (advisor == null) {
				System.out.println(String.format("Did not match psp advisor [%s] %s, %s to any current advisor", id,
						lastName, firstName));
				continue;
			} else {
				advisor.setPSP();
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
