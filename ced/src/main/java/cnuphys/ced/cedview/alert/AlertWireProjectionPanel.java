package cnuphys.ced.cedview.alert;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cnuphys.bCNU.dialog.VerticalFlowLayout;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;

import java.awt.*;
import java.util.Dictionary;
import java.util.Enumeration;

public class AlertWireProjectionPanel extends JPanel {

    private final JSlider zSlider;
    private final JLabel zSliderLabel;

     private final AlertXYView alertXYView;

    private static final Font FONT = Fonts.tweenFont;

    public AlertWireProjectionPanel(AlertXYView alertXYView) {
        this.alertXYView = alertXYView;
        setLayout(new VerticalFlowLayout());

        // Z Slider
        zSliderLabel = createLabel();
        add(zSliderLabel);

        zSlider = createSlider(0, 300, 150, 50, 10, this::updateZLabel, true);
        add(zSlider);

        setBorder(new CommonBorder("DC Wire Projection Plane"));
        updateZLabel(null);
    }

    //helper method to create a label
    private JLabel createLabel() {
        JLabel label = new JLabel("  ");
        label.setFont(FONT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    //helper method to create a slider
    private JSlider createSlider(int min, int max, int value, int major, int minor, ChangeListener changeListener, boolean enabled) {
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
        slider.setEnabled(enabled);
        customizeSliderLabels(slider);
        slider.addChangeListener(changeListener);
        return slider;
    }

    //helper method to customize the slider labels
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

 
    //update the z label
    private void updateZLabel(ChangeEvent e) {
        zSliderLabel.setText("z = " + zSlider.getValue() + " mm");
        alertXYView.refresh();
    }

 
	/**
	 * Get the fixed Z value
	 * 
	 * @return the fixed Z value
	 */
    public double getFixedZ() {
        return zSlider.getValue();
    }
}
