package nl.hr.ictlab.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.opengis.feature.simple.SimpleFeature;

import com.mongodb.BasicDBObject;

public abstract class AbstractColumn {
	
	protected boolean enabled = true;
	protected boolean available = true;
	protected String name = "";
	protected int index = 0;
	protected ColumnType type;
	
	protected static AbstractColumn locationColumn;
	
	protected Set<String> allUniqueValues;
	protected List<String> activeUniqueValues = null;
	protected List<String> inactiveUniqueValues = null;
	protected List<String> sortedUniqueValues = null;
	protected String highest;
	protected String lowest;
	
	protected static final int UNIQUE_LIMIT = 500;
	
	protected int uniqueValueCount = 0;
	
	public AbstractColumn(String name,int index,ColumnType type){
		this.name = name;
		this.index = index;
		this.type = type;
		if(this.type == ColumnType.Location){
			if(locationColumn == null){
				locationColumn = this;
				this.name = "Location";
			} else {
				System.out.println("Two (or more) Location values");
			}
		}
		this.allUniqueValues = new HashSet<>();
	}
	
	public boolean addUniquevalue(String s){
		if(uniqueValueCount < UNIQUE_LIMIT){
			if(allUniqueValues.add(s)){
				uniqueValueCount++;
			}
			if(uniqueValueCount >= UNIQUE_LIMIT){
				type = ColumnType.None;
			}
			return true;
		} else {
			return false;
		}
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

	public String toString(){
		return name;
	}

	public List<String> getUniqueValues() {
		return activeUniqueValues;
	}

	public void setUniqueValues(List<String> uniqueValues) {
		this.activeUniqueValues = uniqueValues;
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

	public Set<String> getAllUniqueValues() {
		return allUniqueValues;
	}

	public void setAllUniqueValues(Set<String> allUniqueValues) {
		this.allUniqueValues = allUniqueValues;
	}

	public List<String> getActiveUniqueValues() {
		if(activeUniqueValues == null && inactiveUniqueValues == null){
			activeUniqueValues = new ArrayList<>(allUniqueValues);
			inactiveUniqueValues = new ArrayList<>();
		}
		return activeUniqueValues;
	}

	public void setActiveUniqueValues(List<String> activeUniqueValues) {
		this.activeUniqueValues = activeUniqueValues;
	}

	public List<String> getInactiveUniqueValues() {
		if(activeUniqueValues == null && inactiveUniqueValues == null){
			activeUniqueValues = new ArrayList<>(allUniqueValues);
			inactiveUniqueValues = new ArrayList<>();
		}
		return inactiveUniqueValues;
	}

	public void setInactiveUniqueValues(List<String> inactiveUniqueValues) {
		this.inactiveUniqueValues = inactiveUniqueValues;
	}

	public List<String> getSortedList() {
		if(sortedUniqueValues == null){
			sortedUniqueValues = new ArrayList<>(allUniqueValues);
			Collections.sort(sortedUniqueValues);
		}
		return sortedUniqueValues; 
	}
}
