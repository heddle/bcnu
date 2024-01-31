package cnuphys.ced.component;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.util.Fonts;
import cnuphys.bCNU.util.TextUtilities;
import cnuphys.ced.cedview.CedView;
import cnuphys.ced.clasio.ClasIoPresentBankPanel;

public class MatchedBankPanel extends JPanel {

	// the view owner
	private CedView _view;

	//the text area for matches
	private JTextArea _matchTextArea;

	// relevant present banks
	private ClasIoPresentBankPanel _presentBankPanel;


	public MatchedBankPanel(CedView view) {
		_view = view;
		setLayout(new BorderLayout(4, 4));

		makeTextArea();
		makeBankPanel();
	}

	//create the text area for entering banks
	private void makeTextArea() {

		_matchTextArea = new JTextArea(6, 20);
		_matchTextArea.setLineWrap(true);
		_matchTextArea.setEditable(true);
		_matchTextArea.setFont(Fonts.mediumFont);
		_matchTextArea.setText(matchesToString());

		_matchTextArea.setBorder(new CommonBorder("Enter matches, comma separated, case sensitive"));

		add(_matchTextArea, BorderLayout.NORTH);

		KeyAdapter keyAdapter = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent kev) {
				if (kev.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						stringToMatches();
					} catch (Exception e) {

					}
				}
			}
		};
		_matchTextArea.addKeyListener(keyAdapter);

		FocusAdapter focusAdapter = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				stringToMatches();
			}

		};
		_matchTextArea.addFocusListener(focusAdapter);
	}


	//create the bank panel
	private void makeBankPanel() {
		_presentBankPanel = new ClasIoPresentBankPanel(_view, null, 16);
		add(_presentBankPanel.getScrollPane(), BorderLayout.CENTER);
	}

	//convert the string in the text area to an array of matches
	private void stringToMatches() {
		String s = _matchTextArea.getText();

		//cant set to null
		if (s == null) {
			_matchTextArea.setText(matchesToString());
			return;
		}
		s = s.replaceAll("\\s", "");

		if (s.length() == 0) {
			_matchTextArea.setText(matchesToString());
			return;
		}

		_view.setBankMatches(TextUtilities.tokens(s, ","));
		_matchTextArea.setText(matchesToString());
		_view.writeCommonProperties();
	}

	//convert the view matches to a comma separated string
	public String matchesToString() {
		return TextUtilities.stringArrayToString(_view.getBanksMatches());
	}


	public void update() {
		_matchTextArea.setText(matchesToString());
	}


	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
	}


}
