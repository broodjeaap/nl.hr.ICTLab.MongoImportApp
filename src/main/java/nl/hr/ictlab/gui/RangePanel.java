package nl.hr.ictlab.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RangePanel extends JPanel {
	private static final long serialVersionUID = -8337372596947505509L;
	
	private JLabel highestColumnValue;
	private JLabel lowestColumnValue;
	
	public RangePanel(){
		super(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Type Settings - Range"));
		JPanel top = new JPanel(new BorderLayout());
		JPanel highestLowest = new JPanel(new GridLayout(2,6));
		highestColumnValue = new JLabel("High");
		lowestColumnValue = new JLabel("Low");
		highestLowest.add(new JLabel("Highest: "));
		highestLowest.add(highestColumnValue);
		highestLowest.add(new JLabel("Lowest: "));
		highestLowest.add(lowestColumnValue);
		top.add(highestLowest,BorderLayout.WEST);
		this.add(top,BorderLayout.NORTH);
		
	}
	
	public void setHighest(String s){
		highestColumnValue.setText(s);
	}
	
	public String getHighest(){
		return highestColumnValue.getText();
	}
	
	public void setLowest(String s){
		lowestColumnValue.setText(s);
	}
	
	public String getLowest(){
		return lowestColumnValue.getText();
	}
}
