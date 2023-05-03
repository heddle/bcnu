package cnuphys.advisors.graphics;

import java.util.List;

import cnuphys.advisors.Advisor;
import cnuphys.advisors.enums.Department;
import cnuphys.advisors.model.DataManager;
import cnuphys.bCNU.graphics.component.TextPaneScrollPane;

public class CatalogPane  extends TextPaneScrollPane {
	
	public CatalogPane() {
		super("Catalogs needed");
		append("                                                    \n", BLUE_TERMINAL);
		
		int total = 0;
		
		for (Department dept : Department.values()) {
			append(dept.name(), BLACK_SS_12_P);
			int departTot = 0;
			
			List<Advisor> advisors = DataManager.getAdvisorsForDepartment(dept);
			
			for (Advisor advisor : advisors) {
				int count = advisor.adviseeCount();
				String s = String.format("\n%s  [%d]", advisor.name, count);
				append(s, BLUE_SS_12_P);
				departTot += count;
				total += count;
				
			}
			append("\nDepartment Count: " + departTot + "\n", BLACK_SS_12_P);
			
			append("\n------------------------\n", GREEN_TERMINAL);
		}
		
		append("\nTotal catalogs: " + total, RED_TERMINAL);
	}

}
