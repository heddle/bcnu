package cnuphys.eventManager.graphics;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;

public class SelectColumnsPanel extends JPanel implements ListSelectionListener {

	// list for all known banks
	private AllBanksList _blist;

	// list for corresponding columns
	private ColumnList _clist;

	// for the expression name
	private JTextField _expressionName;

	/**
	 *
	 * @param label
	 */
	public SelectColumnsPanel(String label) {
		this(label, null);
	}

	/**
	 *
	 * @param label
	 * @param extraButton an extra button added between the lists
	 */
	public SelectColumnsPanel(String label, JButton extraButton) {
		setLayout(new BorderLayout(2, 4));
		addNorth(label);
		addWest();
		addEast();
		addCenter(extraButton);
		addBankColumnListener(this);
	}
	
	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 4, def.left + 4, def.bottom + 4, def.right + 4);
	}


	/**
	 * Add a selection listener to the bank and column lists
	 *
	 * @param lsl the selection listener
	 */
	public void addBankColumnListener(ListSelectionListener lsl) {
		_blist.addListSelectionListener(lsl);
		_clist.addListSelectionListener(lsl);
	}

	// add the center component
	private void addCenter(JButton extraButton) {
		if (extraButton != null) {
			VerticalFlowLayout vfl = new VerticalFlowLayout();
			JPanel p = new JPanel();
			p.setLayout(vfl);
			p.add(Box.createVerticalStrut(50));
			p.add(extraButton);
			add(p, BorderLayout.CENTER);
		}
	}
	
	//add the north component
	private void addNorth(String label) {
		if (label != null) {
			JLabel lab = new JLabel(label);
			lab.setBorder(new EmptyBorder(4, 4, 4, 4));
			lab.setFont(Fonts.mediumFont);
			add(lab, BorderLayout.NORTH);
		}
	}
	
	// add the center component
	private void addWest() {
		_blist = new AllBanksList();
		add(_blist.getScrollPane(), BorderLayout.WEST);
	}
	
	// add the center component
	private void addEast() {
		_clist = new ColumnList(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(_clist.getScrollPane(), BorderLayout.EAST);
	}



	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}

		Object o = e.getSource();


		String bname = _blist.getSelectedValue();
		if ((o == _blist) && (bname != null)) {
			_clist.setList(_blist.getSelectedValue());
		}

	}
	
	//clear all selections
	public void clear() {
//		_blist.clearSelection();
		_clist.clearSelection();
	}

	/**
	 * Get the selected bank
	 * @return the selected bank  (or <code>null</code>
	 */
	public String getSelectedBank() {
		return _blist.getSelectedValue();
	}

	/**
	 * Get the selected columns
	 * @return a list of selected columns  (or <code>null</code>
	 */
	public List<String> getSelectedColumns() {
		List<String> slist = _clist.getSelectedValuesList();
		if ((slist == null) || slist.isEmpty()) {
			return null;
		}
		return slist;
	}

	/**
	 * See if there are any selected columns
	 * @return true if any selected columns
	 */
	public boolean haveSelectedColumns() {
		return getSelectedColumns() != null;
	}

	/**
	 * Get the expression name
	 *
	 * @return the expression name
	 */
	public String getExpressionName() {
		return (_expressionName == null) ? null : _expressionName.getText();
	}


}