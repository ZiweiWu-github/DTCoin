import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class JPanelForInfo extends RefreshableJPanel{
	private static final long serialVersionUID = 1L;
	private DTCoinModel model;
	private String topText, middleText, bottomText, dtCoinText ;
	private String fontSize = "<html><font size = 5 color = black><center>";
	private JLabel label;
	
	public JPanelForInfo(DTCoinModel m) {
		this.model = m;
		this.label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.TOP);
		
		//add all labels in order
		this.setLayout(new GridLayout(1, 1));
		this.add(label);
		
		//fill the text of non-dynamic labels
		topText = this.fontSize + "You have:<br><br>";
		bottomText = this.fontSize + "<font color = black>Until a DTCoin drops from the heavens<br><br>";
	}
	
	@Override
	public void refresh() {
		this.middleText = this.fontSize + "<font color = red>" +this.model.getRemainingTime() + "<br><br>";
		int coins = this.model.getCoinInfo().getAmountOfCoins();
		if(coins == 1) {
			this.dtCoinText = this.fontSize + "You currently have " + 1 + " DTCoin";
		}
		else {
			this.dtCoinText = this.fontSize + "You currently have " + coins + " DTCoins";
		}
		label.setText(topText + middleText + bottomText + dtCoinText);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(450,200);
	}

}
