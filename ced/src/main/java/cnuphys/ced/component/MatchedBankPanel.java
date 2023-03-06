package cnuphys.ced.component;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import cnuphys.bCNU.util.FileUtilities;
import cnuphys.bCNU.util.Fonts;
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
	
	private void makeTextArea() {
		
		_matchTextArea = new JTextArea(5, 30);
		_matchTextArea.setLineWrap(true);
		_matchTextArea.setEditable(true);
		_matchTextArea.setFont(Fonts.mediumFont);
		_matchTextArea.setText(matchesToString());
		
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
	

	
	private void makeBankPanel() {
		_presentBankPanel = new ClasIoPresentBankPanel(null, 6, 4);
		_presentBankPanel.setMatchList(_view.getBanksMatches());
		add(_presentBankPanel, BorderLayout.CENTER);
	}
	
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
		
		_view.setBankMatches(FileUtilities.tokens(s, ","));
		_presentBankPanel.setMatchList(_view.getBanksMatches());
		_matchTextArea.setText(matchesToString());
	}
	
	//convert the view matches to a comma separated string
	public String matchesToString() {
		
		String matches[] = _view.getBanksMatches();
		if (matches == null) {
			return "";
		}
		
		int len = matches.length;
		if (len == 0) {
			return "";
		}
		
		StringBuffer sb = new StringBuffer(256);
		
		for (int i = 0; i < (len-1); i++) {
			sb.append(matches[i] + ", ");
		}
		sb.append(matches[len-1]);
		return sb.toString();
	}
	
	public void update() {
		_presentBankPanel.setMatchList(_view.getBanksMatches());
		_matchTextArea.setText(matchesToString());
	}
	
	
	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
	}


}
