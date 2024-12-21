package cnuphys.ced.cedview.alert;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.UnicodeSupport;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Enumeration;

public class AlertWireProjectionPanel extends JPanel {

    private final JSlider zSlider;
    private final JLabel zSliderLabel;

    private final JSlider thetaSlider;
    private final JLabel thetaSliderLabel;

    private final AlertXYView alertXYView;

    private final JRadioButton useWireMidpointButton;
    private final JRadioButton fixedZButton;
    private final JRadioButton fixedThetaButton;

    private static final Font FONT = Fonts.tweenFont;

    public AlertWireProjectionPanel(AlertXYView alertXYView) {
        this.alertXYView = alertXYView;
        setLayout(new VerticalFlowLayout());

        // Radio Buttons
        ButtonGroup projectionGroup = new ButtonGroup();
        useWireMidpointButton = createRadioButton("Use wire midpoints", projectionGroup, e -> setSliderState(false, false));
        fixedZButton = createRadioButton("Fixed z (xy plane)", projectionGroup, e -> setSliderState(true, false));
        fixedThetaButton = createRadioButton("Fixed " + UnicodeSupport.SMALL_THETA, projectionGroup, e -> setSliderState(false, true));

        useWireMidpointButton.setSelected(true);

        add(useWireMidpointButton);
        add(fixedZButton);
        add(fixedThetaButton);
        add(Box.createRigidArea(new Dimension(0, 8))); // Spacer

        // Z Slider
        zSliderLabel = createLabel();
        add(zSliderLabel);

        zSlider = createSlider(0, 300, 150, 50, 10, this::updateZLabel);
        add(zSlider);

        add(Box.createRigidArea(new Dimension(0, 8))); // Spacer

        // Theta Slider
        thetaSliderLabel = createLabel();
        add(thetaSliderLabel);

        thetaSlider = createSlider(0, 90, 10, 10, 5, this::updateThetaLabel);
        add(thetaSlider);

        setBorder(new CommonBorder("DC Wire Projection"));
        updateZLabel(null);
        updateThetaLabel(null);
    }

    private JLabel createLabel() {
        JLabel label = new JLabel("  ");
        label.setFont(FONT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JRadioButton createRadioButton(String text, ButtonGroup group, ActionListener actionListener) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(FONT);
        radioButton.addActionListener(actionListener);
        group.add(radioButton);
        return radioButton;
    }

    private JSlider createSlider(int min, int max, int value, int major, int minor, ChangeListener changeListener) {
        JSlider slider = new JSlider(min, max, value) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = 220;
                return d;
            }
        };

        slider.setMajorTickSpacing(major);
        slider.setMinorTickSpacing(minor);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setEnabled(false);
        customizeSliderLabels(slider);
        slider.addChangeListener(changeListener);
        return slider;
    }

    private void customizeSliderLabels(JSlider slider) {
        Dictionary<?, ?> labelTable = slider.createStandardLabels(50);
        slider.setLabelTable(labelTable);

        if (labelTable != null) {
            Enumeration<?> keys = labelTable.keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object val = labelTable.get(key);
                if (val instanceof JLabel) {
                    ((JLabel) val).setFont(FONT);
                }
            }
        }
    }

    private void setSliderState(boolean zEnabled, boolean thetaEnabled) {
        zSlider.setEnabled(zEnabled);
        thetaSlider.setEnabled(thetaEnabled);
        alertXYView.refresh();
    }

    private void updateZLabel(ChangeEvent e) {
        zSliderLabel.setText("z = " + zSlider.getValue() + " mm");
        alertXYView.refresh();
    }

    private void updateThetaLabel(ChangeEvent e) {
        thetaSliderLabel.setText(UnicodeSupport.SMALL_THETA + " = " + thetaSlider.getValue() + " degrees");
        alertXYView.refresh();
    }
    
    /**
     * Get the projection
     * @return the projection
     */
	public E_DCProjection getProjection() {
		if (useWireMidpointButton.isSelected()) {
			return E_DCProjection.MIDPOINT;
		} else if (fixedZButton.isSelected()) {
			return E_DCProjection.FIXED_Z;
		} else {
			return E_DCProjection.FIXED_THETA;
		}
	}

 
    public double getFixedZ() {
        return zSlider.getValue();
    }

    public double getFixedTheta() {
        return thetaSlider.getValue();
    }
}
