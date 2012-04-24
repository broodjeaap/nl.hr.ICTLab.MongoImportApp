package nl.hr.ictlab.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RangePanel extends JPanel implements ListSelectionListener{
	private static final long serialVersionUID = -8337372596947505509L;
	
	
	private AbstractColumn column;
	
	private JList<String> fromList;
	private DefaultListModel<String> fromListModel;
	private JList<String> toList;
	private DefaultListModel<String> toListModel;
	
	public RangePanel(AbstractColumn column){
		super(new BorderLayout());
		this.column = column;
		this.setBorder(BorderFactory.createTitledBorder("Type Settings - Range"));
		//Panel holding the highest/lowest lists
		JPanel highestLowest = new JPanel(new BorderLayout());
		fromListModel = new DefaultListModel<>();
		fromList = new JList<>(fromListModel);
		fromList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane fromScroller = new JScrollPane(fromList);
		fromScroller.setBorder(BorderFactory.createTitledBorder("From"));
		highestLowest.add(fromScroller,BorderLayout.WEST);
		
		toListModel = new DefaultListModel<>();
		toList = new JList<>(toListModel);
		toList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane toScroller = new JScrollPane(toList);
		toScroller.setBorder(BorderFactory.createTitledBorder("To"));
		highestLowest.add(toScroller,BorderLayout.EAST);
		this.add(highestLowest);
	}
	
	public void setUniqueValues(){
		fromListModel.clear();
		toListModel.clear();
		List<String> list = column.getSortedList();
		List<String> listReverse = new ArrayList<>(list);
		Collections.reverse(listReverse);
		for(int a = 0;a < list.size();++a){
			fromListModel.addElement(list.get(a));
			toListModel.addElement(listReverse.get(a));
		}
	}
	
	public void setColumn(AbstractColumn column){
		this.column = column;
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(!event.getValueIsAdjusting()){
			if(event.getSource() == fromList){
				column.setLowest(fromListModel.get(fromList.getSelectedIndex()));
			} else if(event.getSource() == toList){
				column.setHighest(toListModel.get(toList.getSelectedIndex()));
			}
		}
	}
}
