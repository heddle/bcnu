package cnuphys.bCNU.simanneal.example.layout;

import java.awt.Component;

import cnuphys.bCNU.dialog.DialogUtilities;
import cnuphys.bCNU.dialog.SimpleDialog;
import cnuphys.bCNU.simanneal.example.ts.TSDialog;
import cnuphys.bCNU.simanneal.example.ts.TSPanel;
import cnuphys.bCNU.simanneal.example.ts.TSSimulation;

public class LayoutDialog extends SimpleDialog {

	public LayoutDialog() {
		super("Layout agorithm (Simulated Annealing)", false, "Close");
		DialogUtilities.centerDialog(this);
	}

	/**
	 * Override to create the component that goes in the center. Usually this is the
	 * "main" component.
	 * 
	 * @return the component that is placed in the center
	 */
	@Override
	protected Component createCenterComponent() {
		TSSimulation simulation = new TSSimulation();

		TSPanel tsPanel = new TSPanel(simulation);

		return tsPanel;
	}

	public static void main(String arg[]) {
		TSDialog dialog = new TSDialog();
		dialog.setVisible(true);
	}

}
