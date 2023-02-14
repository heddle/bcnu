package cnuphys.ced.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class ColorMenu extends JMenu implements ActionListener {
	
	private JRadioButtonMenuItem _dcStandardColorsRB;
	private JRadioButtonMenuItem _dcOrderColorsRB;
	
	private JMenuItem _orderColorsItem;
	
	//show all the colors
	private OrderColors _orderColors = new OrderColors();
	

	public ColorMenu() {
		super("Colors");
		
		ButtonGroup bg = new ButtonGroup();
		
		_dcStandardColorsRB = createRadioMenuItem("DC Standard Coloring", bg, true, this);
		_dcOrderColorsRB = createRadioMenuItem("DC Order-based Coloring", bg, true, this);
		
		_orderColorsItem = new JMenuItem("Colors of Order Based...");
		_orderColorsItem.addActionListener(this);
		add(_orderColorsItem);
		_orderColorsItem.setEnabled(false);
		
	}
	
	
	
	// convenience method for adding a radio button
	private JRadioButtonMenuItem createRadioMenuItem(String label, ButtonGroup bg, boolean on,
													 ActionListener al) {

		JRadioButtonMenuItem mi = new JRadioButtonMenuItem(label, on);
		mi.addActionListener(al);
		bg.add(mi);
		add(mi);
		return mi;
	}

	private void handleOrderColors() {
		if (_orderColors == null) {
			_orderColors = new OrderColors();
		}
		
		_orderColors.setVisible(true);
	}
	
	/**
	 * Check whether we should use the DC TDC coloring based or the order column
	 * @return true if we should use the DC TDC coloring 
	 */
	public boolean useOrderColoring() {
		return _dcOrderColorsRB.isSelected();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == _dcStandardColorsRB) {
			_orderColorsItem.setEnabled(false);
			if (_orderColors != null) {
				_orderColors.setVisible(false);
			}
			Ced.refresh();
		}
		else if (source == _dcOrderColorsRB) {
			_orderColorsItem.setEnabled(true);
			Ced.refresh();
		}
		else if (source == _orderColorsItem) {
			handleOrderColors();
		}
		
	}

}
