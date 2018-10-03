import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;

public class JPanelForButtons extends RefreshableJPanel {
	private static final long serialVersionUID = 1L;
	private JButton startDT, pauseDT;
	private DTCoinModel model;
	
	public JPanelForButtons(DTCoinModel m) {
		this.model = m;
		startDT =  ButtonFactory.getStartDTButton(m);
		pauseDT = ButtonFactory.getPauseDTButton(m);
		
		this.setLayout(new GridLayout(1,2));
		this.add(startDT);
		this.add(pauseDT);
	}


	@Override
	/**
	 * Start DT button is enabled if counting is paused
	 * Pause DT button is enabled if counting is happening
	 */
	public void refresh() {
		DTCoinStatus status = this.model.getDTCoinStatus();
		this.startDT.setEnabled(status == DTCoinStatus.PAUSED);
		this.pauseDT.setEnabled(status == DTCoinStatus.COUNTING);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(450,50);
		
	}
}
