package nl.hr.ictlab.gui;

import nl.hr.ictlab.Convert;

import org.opengis.feature.simple.SimpleFeature;

import com.mongodb.BasicDBObject;
import com.vividsolutions.jts.geom.Point;

public class PointColumn extends AbstractColumn {

	public PointColumn(String name, int index,ColumnType type) {
		super(name, index, type);
	}

	@Override
	public void parse(SimpleFeature sf, BasicDBObject out) {
		BasicDBObject coordinates = new BasicDBObject();
		Point p = (Point)sf.getAttribute(index);
		double[] xy = Convert.convertm(p.getX(),p.getY());
		coordinates.put("x",xy[0]);
		coordinates.put("y",xy[1]);
		out.put("coordinates",coordinates);
	}
	
	@Override
	public boolean addUniquevalue(String s){
		return false;
	}
}
