package nl.hr.ictlab.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class MongoExportFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 2649960335292135650L;
	
	JTextField server;
	JTextField port;
	JTextField database;
	JTextField collection;
	JButton export;
	
	JProgressBar progressBar;
	JLabel progressLabel;
	File file;
	
	DefaultListModel<AbstractColumn> columns;
	
	public MongoExportFrame()  {
		super();
		JPanel topWrapper = new JPanel(new FlowLayout());
		JPanel serverSettingsPanel = new JPanel();
		serverSettingsPanel.setBorder(BorderFactory.createTitledBorder("Server"));
		serverSettingsPanel.add(new JLabel("Location:"));
		server = new JTextField("127.0.0.1");
		server.setColumns(20);
		serverSettingsPanel.add(server);
		serverSettingsPanel.add(new JLabel("Port:"));
		port = new JTextField("27017");
		port.setColumns(5);
		serverSettingsPanel.add(port);
		topWrapper.add(serverSettingsPanel);
		
		JPanel collectionSettingsPanel = new JPanel();
		collectionSettingsPanel.setBorder(BorderFactory.createTitledBorder("Import Settings"));
		database = new JTextField("ICTLab");
		database.setColumns(15);
		collectionSettingsPanel.add(new JLabel("DataBase:"));
		collectionSettingsPanel.add(database);
		collectionSettingsPanel.add(new JLabel("Collection:"));
		collection = new JTextField("FileName");
		collection.setColumns(20);
		collectionSettingsPanel.add(collection);
		topWrapper.add(collectionSettingsPanel);
		
		
		JPanel wrapper=  new JPanel(new BorderLayout());
		wrapper.add(topWrapper,BorderLayout.NORTH);
		
		JPanel bottomWrapper = new JPanel(new FlowLayout());
		export = new JButton("Export");
		export.addActionListener(this);
		bottomWrapper.add(export);
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(650, 10));
		progressLabel = new JLabel("0/0");
		bottomWrapper.add(progressLabel); 
		bottomWrapper.add(progressBar);
		wrapper.add(bottomWrapper,BorderLayout.SOUTH);
		
		this.add(wrapper);
		this.pack();
	}

	public void setCollectionName(File file){
		collection.setText(file.getName().substring(0,file.getName().indexOf(".")));
		this.file = file;
	}
	
	public void updateValue(int value){
		progressBar.setValue(value);
		if(value < progressBar.getMaximum()-1){
			progressLabel.setText(value+"/"+progressBar.getMaximum());
		} else {
			progressLabel.setText("done");
		}
		progressLabel.validate();
	}
	
	public void setMaxProgress(int max){
		progressBar.setMaximum(max);
	}
	
	public void setMinProgress(int min){
		progressBar.setMinimum(min);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Exporter.toMongo(server.getText(), Integer.parseInt(port.getText()), database.getText(), collection.getText(), columns, file, this);
	}

	public void setColumns(DefaultListModel<AbstractColumn> enabledColumnListModel, int rowCount) {
		this.columns = enabledColumnListModel;
		progressBar.setMaximum(rowCount);
		progressBar.setValue(0);
		progressLabel.setText("0/"+rowCount);
	}
}
