package nl.hr.ictlab.gui;

import nl.hr.ictlab.Convert;

import org.opengis.feature.simple.SimpleFeature;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiLineString;

public class MultiLineStringColumn extends AbstractColumn {

	public MultiLineStringColumn(String name, int index, ColumnType type) {
		super(name, index, type);
	}

	@Override
	public void parse(SimpleFeature sf, BasicDBObject out) {
		BasicDBList coordinates = new BasicDBList();
		MultiLineString mls = (MultiLineString)sf.getAttribute(index);
		Coordinate[] coordinateArray =  mls.getCoordinates();
		BasicDBObject c = new BasicDBObject();
		for(int a = 0;a < coordinateArray.length;++a){
			double[] xy = Convert.convertm(coordinateArray[a].x,coordinateArray[a].y);
			c.put("x", xy[0]);
			c.put("y", xy[1]);
			coordinates.add(c);
		}
		out.put("coordinates",coordinates);
	}

}
