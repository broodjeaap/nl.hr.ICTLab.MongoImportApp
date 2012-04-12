package nl.hr.ictlab.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.opengis.feature.simple.SimpleFeature;

import com.mongodb.BasicDBObject;

public abstract class AbstractColumn {
	
	protected boolean enabled = true;
	protected boolean available = true;
	protected String name = "";
	protected int index = 0;
	protected ColumnType type;
	
	protected List<String> sampleData;
	protected Set<String> uniqueValues;
	protected String highest;
	protected String lowest;
	
	public AbstractColumn(String name,int index,ColumnType type){
		this.name = name;
		this.index = index;
		this.type = type;
		this.sampleData = new ArrayList<>();
		this.uniqueValues = new TreeSet<>();
	}
	
	public boolean addUniquevalue(String s){
		return uniqueValues.add(s);
	}
	
	public void newHighest(String s){
		if(s.compareTo(this.highest) > 0){
			this.highest = s;
		}
	}
	
	public void newLowest(String s){
		if(s.compareTo(this.lowest) < 0){
			this.lowest = s;
		}
	}
	
	public abstract void parse(SimpleFeature sf,BasicDBObject out);

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ColumnType getType() {
		return type;
	}

	public void setType(ColumnType type) {
		this.type = type;
	}
	
	public void addSample(String s){
		sampleData.add(s);
	}
	
	public List<String> getSampleData() {
		return sampleData;
	}

	public void setSampleData(List<String> sampleData) {
		this.sampleData = sampleData;
	}

	public String toString(){
		return name;
	}

	public Set<String> getUniqueValues() {
		return uniqueValues;
	}

	public void setUniqueValues(Set<String> uniqueValues) {
		this.uniqueValues = uniqueValues;
	}

	public String getHighest() {
		return highest;
	}

	public void setHighest(String highest) {
		this.highest = highest;
	}

	public String getLowest() {
		return lowest;
	}

	public void setLowest(String lowest) {
		this.lowest = lowest;
	}
}
