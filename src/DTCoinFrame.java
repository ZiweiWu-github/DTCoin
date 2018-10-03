import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DTCoinFrame{
	private ArrayList<RefreshableJPanel> panels = new ArrayList<RefreshableJPanel>();

	public DTCoinFrame(DTCoinModel m) {
		JFrame frame = new JFrame("DTCoin");
		
		//create RefreshableJPanels and add them to ArrayList
		JPanelForInfo p1 = new JPanelForInfo(m);
		panels.add(p1);
		JPanelForButtons p2 = new JPanelForButtons(m);
		panels.add(p2);
		
		//Add a ChangeListener
		m.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				refresh();	
			}
		});
		
		
		//Layout for the panels
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		frame.add(p1, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		frame.add(p2,c);
		
		//Do JFrame stuffs
		frame.setVisible(true);
		frame.setResizable(false);
		frame.pack();
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter () {
			
			@Override
			public void windowClosing(WindowEvent e) {
				boolean isCounting = m.getDTCoinStatus() == DTCoinStatus.COUNTING;
				if(isCounting) {
					m.PauseWithoutListeners();
				}
				int result = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to stop praising the Lord?", 
						"Leaving the Lord?", JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE);
				if(result == JOptionPane.YES_OPTION) {
					m.Stop();
					System.exit(0);
				}
				else {
					if(isCounting) {
						m.ResumeWithoutListeners();
					}
				}
			}
		});
	}
	
	public void refresh() {
		for(RefreshableJPanel p : panels) {
			p.refresh();
		}
	}
}
