package nl.hr.ictlab.gui;

import org.opengis.feature.simple.SimpleFeature;

import com.mongodb.BasicDBObject;

public class MultiPolygonColumn extends AbstractColumn {

	public MultiPolygonColumn(String name, int index, ColumnType type) {
		super(name, index, type);
	}

	@Override
	public void parse(SimpleFeature sf, BasicDBObject out) {
		//MultiPolygon mp = (MultiPolygon)sf.getAttribute(index);
		//Coordinate[] coordinatesArray = mp.getCoordinates();
		
	}

}
