package nl.hr.ictlab.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SelectionPanel extends JPanel  implements ActionListener,ListSelectionListener{
	private static final long serialVersionUID = -3057277243526869912L;
	
	private AbstractColumn column;
	
	private DefaultListModel<String> activeListModel;
	private JList<String> activeList;
	private DefaultListModel<String> inactiveListModel;
	private JList<String> inactiveList;
	
	private JButton toActive;
	private JButton toInactive;
	private JButton allToActive;
	private JButton allToInactive;
	
	public SelectionPanel(AbstractColumn column){
		super(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Type Settings - Selection"));
		JPanel eastWrapper = new JPanel(new BorderLayout());
		eastWrapper.setBorder(BorderFactory.createTitledBorder("Values"));
		activeListModel = new DefaultListModel<>();
		activeList = new JList<>(activeListModel);
		activeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane activeScroller = new JScrollPane(activeList);
		activeScroller.setBorder(BorderFactory.createTitledBorder("Active"));
		activeScroller.setPreferredSize(new Dimension(280,400));
		eastWrapper.add(activeScroller,BorderLayout.EAST);
		
		JPanel controls = new JPanel(new GridLayout(10,0));
		toActive = new JButton(">");
		toInactive = new JButton("<");
		allToActive = new JButton(">>>");
		allToInactive = new JButton("<<<");
		controls.add(new JLabel(" "));
		controls.add(new JLabel(" "));
		controls.add(new JLabel(" "));
		controls.add(allToActive);
		controls.add(toActive);
		controls.add(toInactive);
		controls.add(allToInactive);
		controls.add(new JLabel(" "));
		controls.add(new JLabel(" "));
		controls.add(new JLabel(" "));
		eastWrapper.add(controls,BorderLayout.CENTER);
		
		inactiveListModel = new DefaultListModel<>();
		inactiveList = new JList<>(inactiveListModel);
		inactiveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane inactiveScroller = new JScrollPane(inactiveList);
		inactiveScroller.setBorder(BorderFactory.createTitledBorder("Inactive"));
		inactiveScroller.setPreferredSize(new Dimension(280,400));
		eastWrapper.add(inactiveScroller,BorderLayout.WEST);
		
		inactiveList.addListSelectionListener(this);
		activeList.addListSelectionListener(this);
		allToActive.addActionListener(this);
		toActive.addActionListener(this);
		toInactive.addActionListener(this);
		allToInactive.addActionListener(this);
		toActive.setEnabled(false);
		this.add(eastWrapper);
	}

	public void setColumn(AbstractColumn column) {
		List<String> tmp;
		if(this.column != null){
			tmp = new ArrayList<>();
			for(int a = 0;a < activeListModel.size();++a){
				tmp.add(activeListModel.get(a));
			}
			this.column.setActiveUniqueValues(tmp);
			tmp = new ArrayList<>();
			for(int a = 0;a < inactiveListModel.size();++a){
				tmp.add(inactiveListModel.get(a));
			}
			this.column.setInactiveUniqueValues(tmp);
		}
		inactiveListModel.clear();
		activeListModel.clear();
		this.column = column;
		tmp = this.column.getActiveUniqueValues();
		for(int a = 0;a < tmp.size();++a){
			activeListModel.addElement(tmp.get(a));
		}
		tmp = this.column.getInactiveUniqueValues();
		for(int a = 0;a < tmp.size();++a){
			inactiveListModel.addElement(tmp.get(a));
		}
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if(action.getSource() == allToActive){
			for(int a = inactiveListModel.size()-1;a >= 0;--a){
				activeListModel.addElement(inactiveListModel.get(a));
				inactiveListModel.remove(a);
			}
		} else if(action.getSource() == toActive){
			activeListModel.addElement(inactiveListModel.get(inactiveList.getSelectedIndex()));
			inactiveListModel.remove(inactiveList.getSelectedIndex());
		} else if(action.getSource() == toInactive){
			inactiveListModel.addElement(activeListModel.get(activeList.getSelectedIndex()));
			activeListModel.remove(activeList.getSelectedIndex());
		} else if(action.getSource() == allToInactive){
			for(int a = activeListModel.size()-1;a >= 0;--a){
				inactiveListModel.addElement(activeListModel.get(a));
				activeListModel.remove(a);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(!event.getValueIsAdjusting()){
			if(event.getSource() == activeList && activeList.getSelectedIndex() >= 0){
				toInactive.setEnabled(true);
				toActive.setEnabled(false);
				inactiveList.clearSelection();
			} else if (event.getSource() == inactiveList && inactiveList.getSelectedIndex() >= 0){
				toActive.setEnabled(true);
				toInactive.setEnabled(false);
				activeList.clearSelection();
			}
		}
	}
}
