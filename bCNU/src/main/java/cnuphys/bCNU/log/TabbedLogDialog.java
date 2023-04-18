package cnuphys.bCNU.log;

import java.awt.Component;

import cnuphys.bCNU.dialog.SimpleDialog;

public class TabbedLogDialog extends SimpleDialog {
	
	public TabbedLogDialog() {
		super("", false, "close");
	}
	
	/**
	 * Override to create the component that goes in the center. Usually this is the
	 * "main" component.
	 * 
	 * @return the component that is placed in the center
	 */
	protected Component createCenterComponent() {
		return new LogTabbedPane(false);
	}


}
