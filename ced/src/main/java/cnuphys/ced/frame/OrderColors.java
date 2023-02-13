package cnuphys.ced.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cnuphys.bCNU.dialog.ColorLabel;
import cnuphys.bCNU.dialog.IColorChangeListener;
import cnuphys.bCNU.dialog.SimpleDialog;
import cnuphys.bCNU.util.X11Colors;

public class OrderColors extends SimpleDialog implements IColorChangeListener {
	
	private static Color _orderColors[] = {
			X11Colors.getX11Color("cadet blue"),
			X11Colors.getX11Color("dark red"),
			X11Colors.getX11Color("dark green"),
			X11Colors.getX11Color("coral"),
			X11Colors.getX11Color("alice blue"),
			X11Colors.getX11Color("orange"),
			X11Colors.getX11Color("gray"),
			X11Colors.getX11Color("dark orange"),
			X11Colors.getX11Color("olive green"),
			X11Colors.getX11Color("auqua marine"),
			X11Colors.getX11Color("dark violet"),
			X11Colors.getX11Color("fuchsia"),
			X11Colors.getX11Color("forest green"),
	};
	
	public OrderColors() {
		super("Color Selection", false, "Close");
	}

	@Override
	protected Component createNorthComponent() {
		return new JLabel("DC Cell Fill Color Based on DC::tdc.order");
	}
	
	@Override
	protected Component createCenterComponent() {
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridLayout(5, 3, 8, 8));
		
		for(int i = 0; i < 13; i++) {
			String prompt = String.format("%d - %d", 10*i, 10*i + 9);
			Color color = _orderColors[i];
			ColorLabel cl = new ColorLabel(this, color, prompt);
			panel.add(cl);
		}
		
		return panel;
	}

	@Override
	public void colorChanged(Component component, Color color) {
		// TODO Auto-generated method stub
		
	}

}
