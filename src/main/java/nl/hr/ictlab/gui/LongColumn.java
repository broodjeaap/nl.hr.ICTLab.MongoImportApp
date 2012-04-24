package nl.hr.ictlab.gui;

import org.opengis.feature.simple.SimpleFeature;

import com.mongodb.BasicDBObject;

public class LongColumn extends AbstractColumn {

	public LongColumn(String name, int index, ColumnType type) {
		super(name, index, type);
	}

	@Override
	public void parse(SimpleFeature sf, BasicDBObject out) {
		out.put(name, sf.getAttribute(index));
	}
}
