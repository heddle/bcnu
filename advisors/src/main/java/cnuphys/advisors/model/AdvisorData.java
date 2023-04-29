package cnuphys.advisors.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.IFilter;
import cnuphys.advisors.frame.AdvisorAssign;
import cnuphys.advisors.io.DataModel;
import cnuphys.advisors.io.ITabled;
import cnuphys.advisors.log.LogManager;
import cnuphys.advisors.table.CustomRenderer;
import cnuphys.advisors.table.InputOutput;

/**
 * Contains all the advisor data
 * @author heddle
 *
 */
public class AdvisorData extends DataModel {

	// attributes for advisor data
	private static final DataAttribute advisorAttributes[] = { 
			DataManager.advisorAtt, DataManager.departmentNameAtt,
			DataManager.subjectAtt,
			DataManager.idAtt, DataManager.numAdviseeAtt };

	/**
	 * 
	 * @param baseName
	 */
	public AdvisorData(String baseName) {
		super(baseName, advisorAttributes);
		
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
	private AdvisorData(AdvisorData baseModel, IFilter filter) {
		super(baseModel, filter);
	}
	
	/**
	 * Create a submodel using a filter
	 */
	public AdvisorData subModel(IFilter filter) {
		return new AdvisorData(this, filter);
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
		InputOutput.debugPrintln("ADVISOR row count: " + _data.size());
		InputOutput.debugPrintln("ADVISOR col count: " + colCount);

		int nameIndex = getColumnIndex(DataManager.advisorAtt);
		int idIndex = getColumnIndex(DataManager.idAtt);
		int deptIndex = getColumnIndex(DataManager.departmentNameAtt);


		InputOutput.debugPrintln(String.format("Column Indices (name, depart) = (%d, %d)", nameIndex, deptIndex));

		for (String s[] : _data) {
			String name = s[nameIndex];
			String id = s[idIndex];
			String dept = s[deptIndex];

			_tableData.add(new Advisor(name, id, dept));
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
	 * Get a highlight text color for a given row and column
	 * @param row the 0-based row
	 * @param column the 0-based column
	 * @return the hightlight color, if null use default (black)
	 */
    @Override
	public Color getHighlightTextColor(int row, int column) {
		Advisor advisor = getAdvisorFromRow(row);
		return (advisor.hasILC) ? Color.red : Color.black;
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
    		LogManager.error("null id passed to getAdvisorFromId");
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
			System.err.println("Selected Rows: ");

			for (int row : getSelectedRows()) {
				System.err.println("  " + row);
			}

			System.err.println();
		}
	}


}
