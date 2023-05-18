package cnuphys.advisors.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.dialogs.AdvisorDialog;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.table.CustomRenderer;
import cnuphys.bCNU.util.Fonts;

/**
 * Contains all the advisor data
 * @author heddle
 *
 */
public class AdvisorData extends DataModel {
	
	private AdvisorDialog _dialog;
	
	// attributes for advisor data
	private static final DataAttribute advisorAttributes[] = {
			DataManager.rowAtt, DataManager.advisorAtt, DataManager.departmentNameAtt,
			DataManager.subjectAtt, DataManager.emailAtt,
			DataManager.idAtt, DataManager.numAdviseeAtt, DataManager.numMajorAtt };

	/**
	 *
	 * @param baseName
	 */
	public AdvisorData(String baseName) {
		super(baseName, advisorAttributes);
		createCustomRenderer();
	}

	/**
	 * private constructor used to make a submodel
	 * @param baseModel
	 * @param bits
	 */
	private AdvisorData(AdvisorData baseModel, int bits) {
		super(baseModel, bits);
		createCustomRenderer();
	}

	//create a custom renderer
	private void createCustomRenderer() {
		renderer = new CustomRenderer(this);

		for (int i = 0; i < getColumnCount(); i++) {
			_dataTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
	}

	/**
	 * Create a submodel using a filter
	 */
	public AdvisorData subModel(int bits) {
		return new AdvisorData(this, bits);
	}

	@Override
	public int count() {
		int count = super.count();

		//subtract for advisors not accepting cohort
		for (Advisor advisor : getAdvisors()) {
			if (!advisor.acceptingCohort) {
				count--;
			}
		}

		return count;
	}

	/**
	 * Get the number of assigned students from advisor lists
	 * @return the number of assigned students
	 */
	public int getAssignedStudentCount() {
		int count = 0;
		for (Advisor advisor : getAdvisors()) {
			if (advisor.acceptingCohort) {
				count += advisor.advisees.size();
			}
		}
		return count;
	}

	@Override
	protected void processData() {
		int colCount = _header.length;

		int nameIndex = getColumnIndex(DataManager.advisorAtt);
		int idIndex = getColumnIndex(DataManager.idAtt);
		int deptIndex = getColumnIndex(DataManager.departmentNameAtt);
		int emailIndex = getColumnIndex(DataManager.emailAtt);


		for (String s[] : _data) {
			String name = s[nameIndex];
			String id = s[idIndex];
			String dept = s[deptIndex];
			String email = s[emailIndex];

			_tableData.add(new Advisor(name, id, dept, email));
		}

		//raw data not needed
		deleteRawData();

		if (AdvisorAssign.DEBUG) {
			int i = 0;
			for (ITabled itabled : _tableData) {
				Advisor advisor = (Advisor) itabled;
				String s = String.format("%-4d %s", (++i), advisor.nameAndDepartment());
				System.out.println(s);
			}
		}
	}

	/**
	 * Check whether full name corresponds to a core advisor
	 * @param Id the Id  to match
	 * @return the matching Advisor object, or null
	 */
	public Advisor isCoreAdvisor(String id)  {

		List<Advisor> advisors = getAdvisors();


		for (Advisor advisor : advisors) {
			if (advisor.id.equals(id)) {
				return advisor;
			}
		}


		return null;
	}


	/**
	 * Get all the advisors in a list
	 * @return all the advisors
	 */
    public List<Advisor> getAdvisors() {

    	ArrayList<Advisor> list = new ArrayList<>();
		for (ITabled itabled : _tableData) {
			Advisor advisor = (Advisor) itabled;
			list.add(advisor);
		}

		return list;
    }

	/**
	 * Get a highlight font for a given row and column
	 * @param row the 0-based row
	 * @param column the 0-based column
	 * @return the highlight color, if null use medium font
	 */
	@Override
	public Font getHighlightFont(int row, int column) {
		Advisor advisor = getAdvisorFromRow(row);
		return (advisor.locked()) ? Fonts.mediumItalicFont : Fonts.mediumFont;
	}


	/**
	 * Get a highlight text color for a given row and column
	 *
	 * @param row    the 0-based row
	 * @param column the 0-based column
	 * @return the hightlight color, if null use default (black)
	 */
	@Override
	public Color getHighlightTextColor(int row, int column) {
		Advisor advisor = getAdvisorFromRow(row);

		if ((column == 1) && advisor.locked()) {
			return Color.gray;
		} else {
			return (advisor.ilc()) ? Color.red : Color.black;
		}
	}


	/**
	 * Get the advisor at the given 0-based row
	 * @param row the row
	 * @return the advisor at the given row
	 */
    public Advisor getAdvisorFromRow(int row) {
    	return (Advisor)getFromRow(row);
    }

    /**
     * Get and advisor from a faculty Id
     * @param if the faculty id
     * @return the matching advisor or null
     */
    public Advisor getAdvisorFromId(String id) {
    	if (id == null) {
    		System.err.println("null id passed to getAdvisorFromId");
    		return null;
    	}
    	id = id.trim();

    	 List<Advisor> advisors = getAdvisors();
    	 for (Advisor advisor : advisors) {
    		 if (id.equals(advisor.id)) {
    			 return advisor;
    		 }
    	 }

    	return null;
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
		System.err.println("Double clicked on advisor: " + advisor.name);
		
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
