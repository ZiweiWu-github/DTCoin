import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ButtonFactory{
	
	/*
	 * Returns the button to start DT
	 */
	public static JButton getStartDTButton(DTCoinModel m) {
		JButton startDT = new JButton("Start DT!");
		startDT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.Resume();
			}
		});
		return startDT;
	}
	
	/*
	 * Returns the button to pause DT
	 */
	public static JButton getPauseDTButton(DTCoinModel m) {
		JButton pauseDT = new JButton("Pause DT");
		pauseDT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m.Pause();
			}
		});
		return pauseDT;
	}
	
}
