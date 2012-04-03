package nl.hr.ictlab;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Point;


public class ShapeFileContainer {
	List<Class<?>> types = new ArrayList<>();
	List<Column> columns = new ArrayList<>();
	
	
	public void addColumn(Class<?> type,String name){
		columns.add(new Column(type,name));
	}
	
	public void add(int index,Object o){
		if(columns.get(index).getType() == o.getClass()){
			columns.get(index).add(o);
		}
	}
	
	public Integer getInteger(int index,int row){
		return columns.get(index).getInt(row);
	}
	public String getString(int index,int row){
		return columns.get(index).getString(row);
	}
	public Double getDouble(int index,int row){
		return columns.get(index).getDouble(row);
	}
	public Long getLong(int index,int row){
		return columns.get(index).getLong(row);
	}
	public Point getPoint(int index,int row){
		return columns.get(index).getPoint(row);
	}
	
	public String toJson(){
		return rangeToJson(0,columns.get(0).size()-1);
	}
	
	public void toJson(BufferedWriter out) throws IOException{
		rangeToJson(0,columns.get(0).size()-1,out);
	}
	
	public void rangeToJson(int start, int end,BufferedWriter out) throws IOException{
		out.write("{");
		out.newLine();
		out.write("\"type\": \"FeatureCollection\",");
		out.newLine();
		out.write("\"features\": [\n");
		out.newLine();
		if(end >= columns.get(0).size()){
			end = columns.get(0).size()-1;
		}
		for(int a = start;a < end;++a){
			out.write(rowToJson(a));
			if(a < end-1){
				out.write(",");
			}
			out.newLine();
		}
		out.newLine();
		out.write("]");
		out.newLine();
	}
	
	public String rangeToJson(int start, int end){
		StringBuilder sb = new StringBuilder("{\n\"type\": \"FeatureCollection\",\n\"features\": [\n");
		if(end >= columns.get(0).size()){
			end = columns.get(0).size()-1;
		}
		for(int a = start;a < end;++a){
			sb.append(rowToJson(a));
			sb.append(",\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("\n]\n}");
		return sb.toString();
	}
	
	public String rowToJson(int i){
		StringBuilder sb = new StringBuilder("{ \"type\": \"Feature\", \"properties\": { ");
		for(Column c : columns){
			sb.append(c.toJSON(i));
			sb.append(", ");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.deleteCharAt(sb.length()-1);
		sb.append("} }");
		return sb.toString();
	}
	
	public String printRow(int i){
		StringBuilder sb = new StringBuilder();
		sb.append("Printing row ");
		sb.append(i);
		sb.append(": \n\t");
		for(Column c : columns){
			sb.append(c.valueToString(i));
			sb.append("\n\t");
		}
		return sb.toString();
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("ShapeFileContainer: [Columns: [");
		for(Column c : columns){
			sb.append(c.toString());
			sb.append(", ");
		}
		sb.delete(sb.length()-2,sb.length()-1);
		sb.append("], ColumnCount: ");
		sb.append(columns.size());
		sb.append(", Rows: ");
		sb.append(columns.get(0).size());
		return sb.toString();
	}
}
