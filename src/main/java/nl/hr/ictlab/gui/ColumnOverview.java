package nl.hr.ictlab.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.data.FeatureReader;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.mongodb.MongoException;

public class ColumnOverview extends JPanel implements Runnable,ActionListener,ListSelectionListener{

	private static final long serialVersionUID = -4940872995827295317L;
	
	private JMenuItem menuItemOpen;
	private JMenuItem menuItemMongoDB;
	
	
	
	private JList<AbstractColumn> enabledColumns;
	private DefaultListModel<AbstractColumn> enabledColumnListModel;
	private JList<AbstractColumn> disabledColumns;
	private DefaultListModel<AbstractColumn> disabledColumnListModel;
	private JButton toEnabled;
	private JButton toDisabled;
	private JButton allToEnabled;
	private JButton allToDisabled;
	
	private JPanel columnPanel;
	private JCheckBox columnEnabled;
	private JCheckBox columnAvailable;
	private JComboBox<ColumnType> columnType;
	private JTextField columnName;
	
	private RangePanel rangePanel;
	private SelectionPanel selectionPanel;
	private NonePanel nonePanel;
	
	private File currentShapeFile;
	
	private AbstractColumn selectedColumn = null;
	
	public ColumnOverview(){
		super(new BorderLayout());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.getAccessibleContext().setAccessibleDescription("Open");
		menuBar.add(menu);
		
		//Columns
		JPanel columnListPanel = new JPanel(new BorderLayout());
		columnListPanel.setBorder(BorderFactory.createTitledBorder("Columns"));
		
		//enabledColumns
		enabledColumnListModel = new DefaultListModel<>();		
		enabledColumns = new JList<>(enabledColumnListModel);
		enabledColumns.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		

		//Scroller for enabledColumns
		JScrollPane enabledColumnScroller = new JScrollPane(enabledColumns);
		enabledColumnScroller.setPreferredSize(new Dimension(150,400));
		columnListPanel.add(enabledColumnScroller,BorderLayout.EAST);
		enabledColumnScroller.setBorder(BorderFactory.createTitledBorder("Enabled"));
		
		//buttons for moving columns between the enabled/disabled lists
		allToEnabled = new JButton(">>>");
		toEnabled = new JButton(">");
		toDisabled = new JButton("<");
		allToDisabled = new JButton("<<<");
		
		//Panel that holds the buttons
		JPanel columnButtons = new JPanel(new GridLayout(10,0));
		columnButtons.add(new JLabel(" "));
		columnButtons.add(new JLabel(" "));
		columnButtons.add(new JLabel(" "));
		columnButtons.add(allToEnabled);
		columnButtons.add(toEnabled);
		columnButtons.add(toDisabled);
		columnButtons.add(allToDisabled);
		columnButtons.add(new JLabel(" "));
		columnButtons.add(new JLabel(" "));
		columnButtons.add(new JLabel(" "));
		columnListPanel.add(columnButtons,BorderLayout.CENTER);
		
		//disabledColumns
		disabledColumnListModel = new DefaultListModel<>();
		disabledColumns = new JList<>(disabledColumnListModel);
		disabledColumns.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//Scroller for disabledColumns
		JScrollPane disabledColumnScroller = new JScrollPane(disabledColumns);
		disabledColumnScroller.setPreferredSize(new Dimension(150,400));
		columnListPanel.add(disabledColumnScroller,BorderLayout.WEST);
		disabledColumnScroller.setBorder(BorderFactory.createTitledBorder("Disabled"));
		
		//ColumnPanel, displays all the information/options for a column
		columnPanel = new JPanel(new BorderLayout());
		columnPanel.setBorder(BorderFactory.createTitledBorder("Column settings"));
		
		//ColumnOptionsPanel, displays the enabled/disabled checkbox and name of the column
		JPanel columnOptionsPanel = new JPanel(new FlowLayout());
		columnOptionsPanel.setBorder(BorderFactory.createTitledBorder("General"));
		
		//Panel wrapping around the available checkbox
		JPanel columnOptionAvailable = new JPanel();
		columnAvailable = new JCheckBox();
		columnOptionAvailable.add(new JLabel("Available: "));
		columnOptionAvailable.add(columnAvailable);
		columnOptionsPanel.add(columnOptionAvailable);
		
		//Panel wrapping around the type dropdown
		JPanel columnOptionTypePanel = new JPanel();
		columnOptionTypePanel.add(new JLabel("Type: "));
		columnType = new JComboBox<>(ColumnType.values());
		columnOptionTypePanel.add(columnType);
		columnOptionsPanel.add(columnOptionTypePanel);
		
		//Panel wrapping around the name textfield
		JPanel columnOptionNamePanel = new JPanel();
		columnName = new JTextField();
		columnName.setColumns(15);
		columnOptionNamePanel.add(new JLabel("Name: "));
		columnOptionNamePanel.add(columnName);
		columnOptionsPanel.add(columnOptionNamePanel);
		columnPanel.add(columnOptionsPanel,BorderLayout.NORTH);
		
		
		//Panel that wraps the type and sample panels
		JPanel columnSamplePanel = new JPanel();
		columnSamplePanel.setBorder(BorderFactory.createTitledBorder("Sample Data"));
		
		rangePanel = new RangePanel(selectedColumn);
		selectionPanel = new SelectionPanel(selectedColumn);
		nonePanel = new NonePanel();
		//columnPanel.add(selectionPanel,BorderLayout.LINE_START);

		columnName.addActionListener(this);
		enabledColumns.addListSelectionListener(this);
		allToEnabled.addActionListener(this);
		toEnabled.addActionListener(this);
		toDisabled.addActionListener(this);
		allToDisabled.addActionListener(this);
		disabledColumns.addListSelectionListener(this);
		columnAvailable.addActionListener(this);
		columnType.addActionListener(this);
		this.add(columnPanel,BorderLayout.CENTER);
		this.add(columnListPanel,BorderLayout.WEST);
	}
	
	private void createAndShowGUI() {
		JFrame frame = new JFrame("Kolommen");
		
		//Top menu
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		menuItemOpen = new JMenuItem("Open");
		menuItemOpen.addActionListener(this);
		file.add(menuItemOpen);
		menuBar.add(file);
		JMenu export = new JMenu("Export");
		menuItemMongoDB = new JMenuItem("MongoDB");
		menuItemMongoDB.addActionListener(this);
		export.add(menuItemMongoDB);
		menuBar.add(export);
		
		try {
			currentShapeFile = new File("D:\\School\\ICTLab\\gemeentewerken\\GW-Objecten\\speeltoestellen\\speeltoestellen.shp"); 
			processShapeFile(currentShapeFile);
		} catch (IllegalArgumentException | NoSuchElementException
				| IOException e) {
			e.printStackTrace();
		}
		selectedColumn = enabledColumnListModel.get(0);
		enabledColumns.setSelectedIndex(0);
		
		frame.setJMenuBar(menuBar);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(1024,756));
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);
	}
	
	private void processShapeFile(File file) throws IllegalArgumentException, NoSuchElementException, IOException{
		FileDataStore store = FileDataStoreFinder.getDataStore(file);
		FeatureReader<SimpleFeatureType, SimpleFeature> fr = store.getFeatureReader();
		enabledColumnListModel.clear();
		SimpleFeature sf = fr.next();
		SimpleFeatureType ft = sf.getFeatureType();
		boolean succes = true;
		for(int a = 0;a < ft.getAttributeCount();++a){
			AbstractColumn column = ColumnFactory.getColumn(a,ColumnType.None,ft,sf);
			if(column == null){
				System.out.println("Unimplemented type: "+ft.getDescriptor(a).getType().getBinding());
				succes = false;
				break;
			}
			enabledColumnListModel.addElement(column);
		}
		if(!succes){
			System.out.println("Something went wrong, please retry or select another shapefile");
			enabledColumnListModel.clear();
		} else {
			List<AbstractColumn> list = Exporter.minimize(enabledColumnListModel);
			//Pre-process the shapefile
			fr = store.getFeatureReader();
			String tmp = null;
			while(fr.hasNext()){
				sf = fr.next();
				for(AbstractColumn ac : list){
					tmp = sf.getAttribute(ac.getIndex()).toString();
					ac.addUniquevalue(tmp);
					ac.newHighest(tmp);
					ac.newLowest(tmp);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource()==columnEnabled){ //enable/disable checkbox
			selectedColumn.setEnabled(columnEnabled.isSelected());
		} else if(event.getSource()==columnAvailable){ //available checkbox
			selectedColumn.setAvailable(columnAvailable.isSelected());
		} else if(event.getSource()==columnType){ //column type dropdown list
			removeColumnTypePanel();
			selectedColumn.setType(columnType.getItemAt(columnType.getSelectedIndex()));
			setColumnTypePanel();
		} else if(event.getSource()==menuItemOpen){ //File > Open
			currentShapeFile = JFileDataStoreChooser.showOpenFile("shp", null); 
			try {
				processShapeFile(currentShapeFile);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (event.getSource()==menuItemMongoDB){ // Export > Mongo
			try {
				Exporter.toMongo(enabledColumnListModel, currentShapeFile, "ICTLab");
			} catch (MongoException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (event.getSource()==columnName){ //Name change textfield
			enabledColumnListModel.get(enabledColumns.getSelectedIndex()).setName(columnName.getText());
			enabledColumns.repaint();
		} else if (event.getSource()==allToEnabled){ //move all items to the enabled column
			for(int a = disabledColumnListModel.size()-1;a >= 0;--a){
				enabledColumnListModel.addElement(disabledColumnListModel.get(a));
				disabledColumnListModel.remove(a);
			}
		} else if (event.getSource()==toEnabled){ //move the selected item to the enabled column
			enabledColumnListModel.addElement(disabledColumnListModel.get(disabledColumns.getSelectedIndex()));
			disabledColumnListModel.remove(disabledColumns.getSelectedIndex());
			selectedColumn.setEnabled(true);
		} else if (event.getSource()==toDisabled){ //move the selected item to the disabled column
			disabledColumnListModel.addElement(enabledColumnListModel.get(enabledColumns.getSelectedIndex()));
			enabledColumnListModel.remove(enabledColumns.getSelectedIndex());
			selectedColumn.setEnabled(false);
		} else if (event.getSource()==allToDisabled){ //move all the items to the disabled column
			for(int a = enabledColumnListModel.size()-1;a >= 0;--a){
				disabledColumnListModel.addElement(enabledColumnListModel.get(a));
				enabledColumnListModel.remove(a);
			}
		}
	}
	
	private JPanel getColumnTypePanel(ColumnType type){
		switch(type){
			case Range:{
				return rangePanel;
			}
			case Selection:{
				return selectionPanel;
			}
			case None:{
				return nonePanel;
			}
			default:
				return nonePanel;
		}
	}
	
	private void removeColumnTypePanel(){
		ColumnType type = selectedColumn.getType();
		JPanel ac = getColumnTypePanel(type);
		columnPanel.remove(ac);
	}
	
	private void setColumnTypePanel(){
		columnPanel.add(getColumnTypePanel(selectedColumn.getType()),BorderLayout.CENTER);
		rangePanel.setColumn(selectedColumn);
		selectionPanel.setColumn(selectedColumn);
		rangePanel.setUniqueValues();
		columnPanel.validate();
		this.repaint();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(!event.getValueIsAdjusting()){
			if(event.getSource() == enabledColumns && enabledColumns.getSelectedIndex() >= 0){
				removeColumnTypePanel();
				selectedColumn = enabledColumnListModel.get(enabledColumns.getSelectedIndex());
				columnName.setText(selectedColumn.getName());
				columnAvailable.setSelected(selectedColumn.isAvailable());
				columnType.setSelectedItem(selectedColumn.getType());
				toDisabled.setEnabled(true);
				toEnabled.setEnabled(false);
				disabledColumns.clearSelection();
				setColumnTypePanel();
			} else if (event.getSource() == disabledColumns && disabledColumns.getSelectedIndex() >= 0){
				removeColumnTypePanel();
				selectedColumn = disabledColumnListModel.get(disabledColumns.getSelectedIndex());
				columnName.setText(selectedColumn.getName());
				columnAvailable.setSelected(selectedColumn.isAvailable());
				columnType.setSelectedItem(selectedColumn.getType());
				toDisabled.setEnabled(false);
				toEnabled.setEnabled(true);
				enabledColumns.clearSelection();
				setColumnTypePanel();	
			}
		}
	}
	
	@Override
	public void run() {
		createAndShowGUI();
	}


}
