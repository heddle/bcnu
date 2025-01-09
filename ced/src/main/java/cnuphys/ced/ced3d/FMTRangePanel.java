package cnuphys.ced.ced3d;

import javax.swing.*;

import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.ced.ced3d.view.CedView3D;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;

public class FMTRangePanel extends JPanel {

	private final JTextField minField;
	private final JTextField maxField;
	private int minValue = 1;
	private int maxValue = 1024;

	private CedView3D _view;

	public FMTRangePanel(CedView3D view) {
		_view = view;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


		// label: "Full Range: 1-1024"
		JLabel fullRangeLabel = new JLabel("Full Range: 1-1024", SwingConstants.CENTER);
		fullRangeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(fullRangeLabel);

		// Panel for range selector controls
		JPanel rangePanel = new JPanel();
		rangePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		minField = new JTextField(String.valueOf(minValue), 5);
		maxField = new JTextField(String.valueOf(maxValue), 5);

		JLabel toLabel = new JLabel("to");

		rangePanel.add(minField);
		rangePanel.add(toLabel);
		rangePanel.add(maxField);
		add(rangePanel);

		// Add listeners to text fields
		ActionListener updateCallback = e -> updateRange();
		FocusAdapter focusListener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateRange();
			}
		};

		minField.addActionListener(updateCallback);
		maxField.addActionListener(updateCallback);

		minField.addFocusListener(focusListener);
		maxField.addFocusListener(focusListener);

		setBorder(new CommonBorder("Visible Strips"));
	}

	private void updateRange() {
		try {
			int newMin = Integer.parseInt(minField.getText().trim());
			int newMax = Integer.parseInt(maxField.getText().trim());

			if (newMin <= newMax && newMin >= 1 && newMax <= 1024) {
				minValue = newMin;
				maxValue = newMax;
				_view.refresh();
			} else {
				JOptionPane.showMessageDialog(this, "Invalid range. Ensure 1 <= min <= max <= 1024.", "Error",
						JOptionPane.ERROR_MESSAGE);
				resetFields();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Please enter valid integer values.", "Error",
					JOptionPane.ERROR_MESSAGE);
			resetFields();
		}
	}

	private void resetFields() {
		minField.setText(String.valueOf(minValue));
		maxField.setText(String.valueOf(maxValue));
	}

	public boolean showStrip(int strip) {
		return strip >= minValue && strip <= maxValue;
	}

}
