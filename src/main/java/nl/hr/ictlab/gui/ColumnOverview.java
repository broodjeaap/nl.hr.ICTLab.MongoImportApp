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
	
	
	
	private JList<AbstractColumn> columns;
	private DefaultListModel<AbstractColumn> columnListModel;
	private JCheckBox columnEnabled;
	private JCheckBox columnAvailable;
	private JComboBox<ColumnType> columnType;
	private JTextField columnName;

	private JList<String> columnSampleJList;
	private DefaultListModel<String> columnSampleListModel;
	private JList<String> columnUniqueJlist;
	private DefaultListModel<String> columnUniqueListModel;
	private RangePanel rangePanel;
	private SelectionPanel selectionPanel;
	
	private File currentShapeFile;
	
	public ColumnOverview(){
		super(new BorderLayout());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.getAccessibleContext().setAccessibleDescription("Open");
		menuBar.add(menu);
		
		//ColumnList
		JPanel columnListPanel = new JPanel(new GridLayout());
		columnListPanel.setBorder(BorderFactory.createTitledBorder("Columns"));
		columnListModel = new DefaultListModel<>();		
		columns = new JList<>(columnListModel);
		columns.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		columns.addListSelectionListener(this);
		
		//Scroller for ColumnList
		JScrollPane columnScroller = new JScrollPane(columns);
		columnScroller.setPreferredSize(new Dimension(150,400));
		columnListPanel.add(columnScroller);
		
		//ColumnPanel, displays all the information/options for a column
		JPanel columnPanel = new JPanel(new BorderLayout());
		columnPanel.setBorder(BorderFactory.createTitledBorder("Column settings"));
		
		//ColumnOptionsPanel, displays the enabled/disabled checkbox and name of the column
		JPanel columnOptionsPanel = new JPanel(new FlowLayout());
		columnOptionsPanel.setBorder(BorderFactory.createTitledBorder("General"));
		
		//Panel wrapping around the enabled/disabled checkbox
		JPanel columnOptionEnabledPanel = new JPanel();
		columnEnabled = new JCheckBox();
		columnEnabled.addActionListener(this);
		columnOptionEnabledPanel.add(new JLabel("Enabled: "));
		columnOptionEnabledPanel.add(columnEnabled);
		columnOptionsPanel.add(columnOptionEnabledPanel);
		
		//Panel wrapping around the available checkbox
		JPanel columnOptionAvailable = new JPanel();
		columnAvailable = new JCheckBox();
		columnAvailable.addActionListener(this);
		columnOptionAvailable.add(new JLabel("Available: "));
		columnOptionAvailable.add(columnAvailable);
		columnOptionsPanel.add(columnOptionAvailable);
		
		//Panel wrapping around the type dropdown
		JPanel columnOptionTypePanel = new JPanel();
		columnType = new JComboBox<>(ColumnType.values());
		columnType.addActionListener(this);
		columnOptionTypePanel.add(new JLabel("Type: "));
		columnOptionTypePanel.add(columnType);
		columnOptionsPanel.add(columnOptionTypePanel);
		
		//Panel wrapping around the name textfield
		JPanel columnOptionNamePanel = new JPanel();
		columnName = new JTextField();
		columnName.setColumns(15);
		columnName.addActionListener(this);
		columnOptionNamePanel.add(new JLabel("Name: "));
		columnOptionNamePanel.add(columnName);
		columnOptionsPanel.add(columnOptionNamePanel);
		columnPanel.add(columnOptionsPanel,BorderLayout.NORTH);
		//Panel that wraps the type and sample panels
		JPanel columnSamplePanel = new JPanel();
		columnSamplePanel.setBorder(BorderFactory.createTitledBorder("Sample Data"));
		
		columnSampleListModel = new DefaultListModel<>();
		columnSampleJList = new JList<>(columnSampleListModel);
		columnSampleJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sampleScroller = new JScrollPane(columnSampleJList);
		sampleScroller.setPreferredSize(new Dimension(200,480));
		columnSamplePanel.add(sampleScroller);
		columnPanel.add(columnSamplePanel,BorderLayout.WEST);
		
		
		//Panel that displays the specifiek options available to a column (based on the type)
		JPanel columnTypePanel = new JPanel(new BorderLayout());
		columnTypePanel.setBorder(BorderFactory.createTitledBorder("Type Settings"));
		columnPanel.add(columnTypePanel,BorderLayout.CENTER);
		
		//Panel that wraps the unique values list
		JPanel columnUniqueValuePanel = new JPanel();
		columnUniqueValuePanel.setBorder(BorderFactory.createTitledBorder("Unique Values"));
		columnUniqueListModel = new DefaultListModel<>();
		columnUniqueJlist = new JList<>(columnUniqueListModel);
		columnUniqueJlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane uniqueScroller = new JScrollPane(columnUniqueJlist);
		uniqueScroller.setPreferredSize(new Dimension(200,450));
		columnUniqueValuePanel.add(uniqueScroller,BorderLayout.WEST);
		columnTypePanel.add(columnUniqueValuePanel,BorderLayout.WEST);
		rangePanel = new RangePanel();
		selectionPanel = new SelectionPanel();
		columnPanel.add(selectionPanel);
		
		this.add(columnPanel);
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
		columns.setSelectedIndex(0);
		frame.setJMenuBar(menuBar);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(800,600));
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);
	}
	
	private void processShapeFile(File file) throws IllegalArgumentException, NoSuchElementException, IOException{
		FileDataStore store = FileDataStoreFinder.getDataStore(file);
		FeatureReader<SimpleFeatureType, SimpleFeature> fr = store.getFeatureReader();
		columnListModel.clear();
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
			columnListModel.addElement(column);
		}
		if(!succes){
			System.out.println("Something went wrong, please retry or select another shapefile");
			columnListModel.clear();
		} else {
			//Fill column sample data
			fr = store.getFeatureReader(); 
			int count = 0;
			List<AbstractColumn> list = Exporter.minimize(columnListModel);
			while(fr.hasNext() && count++ < 100){
				sf = fr.next();
				for(AbstractColumn ac : list){
					ac.addSample(sf.getAttribute(ac.getIndex()).toString());
					if(fr.hasNext()){
						fr.next(); //provides more 'random' data
					}
				}
			}
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
			columnListModel.get(columns.getSelectedIndex()).setEnabled(columnEnabled.isSelected());
		} else if(event.getSource()==columnAvailable){ //available checkbox
			columnListModel.get(columns.getSelectedIndex()).setAvailable(columnAvailable.isSelected());
		} else if(event.getSource()==columnType){ //column type dropdown list
			columnListModel.get(columns.getSelectedIndex()).setType(columnType.getItemAt(columnType.getSelectedIndex()));
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
				Exporter.toMongo(columnListModel, currentShapeFile, "ICTLab");
			} catch (MongoException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (event.getSource()==columnName){ //Name change textfield
			columnListModel.get(columns.getSelectedIndex()).setName(columnName.getText());
			columns.repaint();
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if(!event.getValueIsAdjusting() && (columns.getSelectedIndex() >= 0)){
			AbstractColumn column = columnListModel.get(columns.getSelectedIndex());
			columnEnabled.setSelected(column.isEnabled());
			columnName.setText(column.getName());
			columnAvailable.setSelected(column.isAvailable());
			columnType.setSelectedItem(column.getType());
			columnSampleListModel.clear();
			for(String s : columnListModel.get(columns.getSelectedIndex()).getSampleData()){
				columnSampleListModel.addElement(s);
			}
			columnUniqueListModel.clear();
			for(String s : columnListModel.get(columns.getSelectedIndex()).getUniqueValues()){
				columnUniqueListModel.addElement(s);
			}
			rangePanel.setHighest(columnListModel.get(columns.getSelectedIndex()).getHighest());
			rangePanel.setLowest(columnListModel.get(columns.getSelectedIndex()).getLowest());
		}
	}
	
	@Override
	public void run() {
		createAndShowGUI();
	}


}
