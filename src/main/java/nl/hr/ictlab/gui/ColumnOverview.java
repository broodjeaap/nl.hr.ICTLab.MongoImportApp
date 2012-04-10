package nl.hr.ictlab.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColumnOverview extends JPanel implements Runnable{

	private static final long serialVersionUID = -4940872995827295317L;
	public ColumnOverview(){
		super(new BorderLayout());
	}
	private void createAndShowGUI() {
		JFrame frame = new JFrame("Kolommen");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setOpaque(true);
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public void run() {
		createAndShowGUI();
	}
}
