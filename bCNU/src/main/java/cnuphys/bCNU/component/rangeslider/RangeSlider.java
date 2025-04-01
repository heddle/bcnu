package cnuphys.bCNU.component.rangeslider;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cnuphys.bCNU.util.Fonts;

public class RangeSlider extends JPanel {
    private JSlider slider;
    private JLabel valueLabel;
    private Consumer<Integer> onChange;
    private Consumer<Integer> onFinalChange;
    
    private static final Font FONT = Fonts.tweenFont;

    
    public RangeSlider(int min, int max, int defaultVal, int majorTick, int minorTick, boolean showValue) {
        setLayout(new BorderLayout());
         
        slider = new JSlider(JSlider.HORIZONTAL, min, max, defaultVal);
        
		if (majorTick > 0 && majorTick < max) {
			slider.setMajorTickSpacing(majorTick);
		}
		
		if (minorTick > 0 && minorTick < majorTick) {
			slider.setMinorTickSpacing(minorTick);
		}
		
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.setFont(FONT);
        slider.setFocusable(false);
        
        //show current value?
		if (showValue) {
	        valueLabel = new JLabel(String.valueOf(defaultVal), SwingConstants.CENTER);
	        valueLabel.setFont(FONT);
		}

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider.getValue();

				if (showValue) {
					valueLabel.setText(String.valueOf(value));
				}
                if (onChange != null) {
                    onChange.accept(value);
                }
                if (!slider.getValueIsAdjusting() && onFinalChange != null) {
                    onFinalChange.accept(value);
                }
            }
        });
        
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(slider, BorderLayout.CENTER);
        
		if (showValue) {
			sliderPanel.add(valueLabel, BorderLayout.SOUTH);
		}
        
        add(sliderPanel, BorderLayout.CENTER);
    }
    
    public void setOnChange(Consumer<Integer> callback) {
        this.onChange = callback;
    }
    
    public void setOnFinalChange(Consumer<Integer> callback) {
        this.onFinalChange = callback;
    }
    
    public int getValue() {
        return slider.getValue();
    }
}