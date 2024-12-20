package cnuphys.ced.cedview.alert;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Enumeration;

public class DCWireProjectionPanel extends JPanel {

	private JSlider zSlider;
	private JLabel zSliderLabel;

	private AlertXYView alertXYView;
	
	private JRadioButton useWireMidpointButton;
	private JRadioButton fixedZButton;
	
	private static Font font = Fonts.tweenFont;


	public DCWireProjectionPanel(AlertXYView alertXYView) {
		this.alertXYView = alertXYView;
		setLayout(new VerticalFlowLayout());
		
	// Radio Buttons
		useWireMidpointButton = new JRadioButton("Use wire midpoints");
		fixedZButton = new JRadioButton("Fixed z (xy plane)");
		useWireMidpointButton.setFont(font);
		fixedZButton.setFont(font);

		// Group radio buttons
		ButtonGroup projectionGroup = new ButtonGroup();
		projectionGroup.add(useWireMidpointButton);
		projectionGroup.add(fixedZButton);

		// Set default selection
		useWireMidpointButton.setSelected(true);

		// Add action listeners
		useWireMidpointButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleProjectionChange();
				zSlider.setEnabled(false);
			}
		});

		fixedZButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleProjectionChange();
				zSlider.setEnabled(true);
			}
		});

		// Add radio buttons to panel
		add(useWireMidpointButton);
		add(fixedZButton);

		add(Box.createRigidArea(new Dimension(0, 8))); // Spacer

		// Slider
		zSliderLabel = new JLabel("  ");
		zSliderLabel.setFont(font);
		
		zSliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(zSliderLabel);

		zSlider = new JSlider(0, 300, 150) {
			@Override
            public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.width = 220;
                return d;
            }
		};
		zSlider.setMajorTickSpacing(50);
		zSlider.setMinorTickSpacing(10);
		zSlider.setPaintTicks(true);
		zSlider.setPaintLabels(true);
		zSlider.setEnabled(false);
		
        // Get the label table and set custom font
        zSlider.setLabelTable(zSlider.createStandardLabels(50)); // Create labels
        Dictionary<?, ?> labelTable = zSlider.getLabelTable();
        if (labelTable != null) {
            Enumeration<?> keys = labelTable.keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = labelTable.get(key);
                if (value instanceof JLabel) {
                    JLabel label = (JLabel) value;
                    label.setFont(font); // Replace with your custom font
                }
            }
        }


		// Add change listener to slider
		zSlider.addChangeListener(new ChangeListener() {
			@Override
            public void stateChanged(ChangeEvent e) {
                if (zSlider.getValueIsAdjusting()) {
                    zIsChanging(zSlider.getValue());
                } else {
                    handleZChange(zSlider.getValue());
                }
            }		});

		add(zSlider);
		
		setBorder(new CommonBorder("DC Wire Projection"));
		setZLabel();
	}
	
	private void setZLabel() {
		zSliderLabel.setText("z = " + zSlider.getValue() + " mm");
	}

	// Callback for projection change
	private void handleProjectionChange() {
		alertXYView.refresh();
	}

	// Callback for slider change
	private void handleZChange(int value) {
		setZLabel();
		alertXYView.refresh();
	}
	
	// Callback for slider change as it is changing
	private void zIsChanging(int value) {
		setZLabel();
		alertXYView.refresh();
	}

	
	public boolean useWireMidpoint() {
		return useWireMidpointButton.isSelected();
	}
	
	public double getFixedZ() {
		return zSlider.getValue();
	}


}
