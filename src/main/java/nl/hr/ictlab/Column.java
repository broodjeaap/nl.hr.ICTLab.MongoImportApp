package nl.hr.ictlab;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

public class Column {
	String columnName;
	Class<?> type;
	List<Object> values = new ArrayList<Object>();
	
	public Column(Class<?> type,String columnName){
		this.type = type;
		this.columnName = columnName;
	}
	
	public Class<?> getType(){
		return type;
	}
	
	public int size(){
		return values.size();
	}
	
	public void add(Object value){
		this.values.add(value);
	}
	
	public Integer getInt(int i){
		if(type == Integer.class){
			return (Integer)values.get(i);
		}
		return null;
	}
	
	public String getString(int i){
		if(type == String.class){
			return (String)values.get(i);
		}
		return null;
	}
	
	public Long getLong(int i){
		if(type == Long.class){
			return (Long)values.get(i);
		}
		return null;
	}
	
	public Double getDouble(int i){
		if(type == Double.class){
			return (Double)values.get(i);
		}
		return null;
	}
	
	public Point getPoint(int i){
		if(type == Point.class){
			return (Point)values.get(i);
		}
		return null;
	}
	
	public String valueToString(int i){
		return values.get(i).toString();
	}
	
	public String toJSON(int i){
		StringBuilder sb = new StringBuilder();
		sb.append("\"");
		sb.append(columnName);
		sb.append("\"");
		sb.append(": ");
		if(this.type == Point.class){
			sb.append(pointToJson(i));
		} else {
			if(this.type == String.class){
				sb.append("\"");
			}
			sb.append(values.get(i));
			if(this.type == String.class){
				sb.append("\"");
			}
		}
		return sb.toString();
	}
	
	public String pointToJson(int i){
		//{ "type": "Point", "coordinates": [ 95480.500000, 436560.720000 ] }
		StringBuilder sb = new StringBuilder("{ \"type\": \"Point\", \"coordinates\": ");
		Point p = (Point)values.get(i);
		sb.append(Convert.convertmString(p.getX(),p.getY()));
		sb.append(" }");
		return sb.toString();
	}
	
	public String toString(){
		return type.getName();
	}
}
