package cnuphys.advisors.graphics;

import java.util.List;

import cnuphys.advisors.enums.Department;
import cnuphys.advisors.enums.Major;
import cnuphys.bCNU.graphics.component.TextPaneScrollPane;

public class DepartmentMajorPane extends TextPaneScrollPane {
	
	
	public DepartmentMajorPane() {
		super("Departments and their majors");
		
		for (Department dept : Department.values()) {
			append(dept.name() + ":  ", BLUE_TERMINAL);
			
			
			List<Major> majors = dept.getMajors();
			
			for (int i = 0; i < majors.size(); i++) {
				Major major = majors.get(i);
				append(major.name(), BLACK_SS_12_P);
				
				if (i < (majors.size()-1) ) {
					append(", ", BLACK_SS_12_P);
				}
				else {
					append("   ", BLACK_SS_12_P);
				}
			}
			
			append("\n", BLUE_SS_12_P);

		}
		
		
	}
	

}
