package nl.hr.ictlab.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

public class SelectionPanel extends JPanel  {
	private static final long serialVersionUID = -3057277243526869912L;
	
	private ListModel<String> activeListModel;
	private JList<String> activeJList;
	private ListModel<String> inactiveListModel;
	private JList<String> inactiveJList;
	
	
	public SelectionPanel(){
		super(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Type Settings - Selection"));
		JPanel currentSelectionPanel = new JPanel(new BorderLayout());
		JPanel activeSelectionPanel = new JPanel();
		activeSelectionPanel.setBorder(BorderFactory.createTitledBorder("Active"));
		activeListModel = new DefaultListModel<>();
		activeJList = new JList<>(activeListModel);
		JScrollPane activeScroller = new JScrollPane(activeJList);
		activeScroller.setPreferredSize(new Dimension(150,450));
		activeSelectionPanel.add(activeScroller);
		JPanel inactiveSelectionPanel = new JPanel();
		inactiveSelectionPanel.setBorder(BorderFactory.createTitledBorder("Inactive"));
		inactiveListModel = new DefaultListModel<>();
		inactiveJList = new JList<>(inactiveListModel);
		JScrollPane inactiveScroller = new JScrollPane(inactiveJList);
		inactiveScroller.setPreferredSize(new Dimension(150,450));
		inactiveSelectionPanel.add(inactiveScroller);
		currentSelectionPanel.add(activeSelectionPanel,BorderLayout.WEST);
		currentSelectionPanel.add(inactiveSelectionPanel,BorderLayout.EAST);
		this.add(currentSelectionPanel);
		
		//sampleScroller.setPreferredSize(new Dimension(200,480));
	}
}
