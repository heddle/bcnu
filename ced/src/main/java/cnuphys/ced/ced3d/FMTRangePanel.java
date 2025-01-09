package cnuphys.ced.ced3d;
import javax.swing.*;

import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.view.BaseView;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FMTRangePanel extends JPanel {

    private final JTextField rangeField;
    private String _rangeText = "1-1024";
    private BaseView _view;
    Font font = Fonts.defaultFont;

    public FMTRangePanel(BaseView view) {
    	_view = view;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Top label: "Full Range: 1-1024"
        JLabel fullRangeLabel = new JLabel("Full Range: 1-1024", SwingConstants.CENTER);
        fullRangeLabel.setFont(font);
        fullRangeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(fullRangeLabel);

        // Next Label: "Example: 1-10, 17, 230-244"
        JLabel visibleStripsLabel = new JLabel("e.g. 1-10, 17, 230-244", SwingConstants.CENTER);
        visibleStripsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(visibleStripsLabel);


        // Single text field for range input
        rangeField = new JTextField(30);
        rangeField.setText(_rangeText);
        rangeField.setFont(font);
        rangeField.setMaximumSize(new Dimension(200, 25));
        rangeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(rangeField);

        // Add listeners to the text field
        ActionListener updateCallback = e -> updateRange();
        FocusAdapter focusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateRange();
            }
        };

        rangeField.addActionListener(updateCallback);
        rangeField.addFocusListener(focusListener);
    }

    private void updateRange() {
        String input = rangeField.getText().trim();
        if (isValidRange(input)) {
            _rangeText = input;
			if (_view != null) {
				_view.refresh();
			}
        } else {
            JOptionPane.showMessageDialog(this, "Invalid range format. Use formats like '1-10, 17, 230-244'.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            rangeField.setText(_rangeText);
        }
    }

    private boolean isValidRange(String input) {
        String regex = "^\\s*(\\d+(-\\d+)?)(\\s*,\\s*\\d+(-\\d+)?)*\\s*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public boolean showStrip(int strip) {
        String[] parts = _rangeText.split(",");
        for (String part : parts) {
            part = part.trim();
            if (part.contains("-")) {
                String[] bounds = part.split("-");
                int min = Integer.parseInt(bounds[0].trim());
                int max = Integer.parseInt(bounds[1].trim());
                if (strip >= min && strip <= max) {
                    return true;
                }
            } else {
                int single = Integer.parseInt(part);
                if (strip == single) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Range Selector Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);

            FMTRangePanel panel = new FMTRangePanel(null);
            frame.add(panel);

            frame.setVisible(true);
            
            // Example usage
            
            boolean isVisible = panel.showStrip(5); // Change this to test different strips
            
        });
    }
}
