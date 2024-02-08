package cnuphys.bCNU.wordle;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

public class TestWordle {

	
	public static void main(String arg[]) {
	       SwingUtilities.invokeLater(() -> {
	            Wordle wordle = Wordle.getInstance();
	            
				WindowAdapter windowAdapter = new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				};
				wordle.addWindowListener(windowAdapter);
	            
	            wordle.setVisible(true);
	            
	 	       System.out.println("TestWordle");

	 	       Brain brain = Brain.getInstance();
	 	       brain.setCurrentWord("STEAM");
	 	       
	 	       brain.processCharacterEntry('B');
	 	       brain.processCharacterEntry('E');
	 	       brain.processCharacterEntry('E');
	 	       brain.processCharacterEntry('T');
	 	       brain.processCharacterEntry('X');
	 	       brain.processCharacterEntry('X');
	        });
	       
	 	}
}
