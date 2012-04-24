package nl.hr.ictlab.gui;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import com.vividsolutions.jts.geom.Point;

public class ColumnFactory {
	
	public static AbstractColumn getColumn(int a,ColumnType type,SimpleFeatureType ft,SimpleFeature sf){
		Class<?> c = ft.getDescriptor(a).getType().getBinding();
		AbstractColumn column = null;
		if(c == Point.class){
			column = new PointColumn(ft.getDescriptor(a).getLocalName(),a,ColumnType.Location);
		} else if (c == Integer.class) {
			column = new IntegerColumn(ft.getDescriptor(a).getLocalName(),a,ColumnType.Selection);
		} else if (c == String.class) {
			column = new StringColumn(ft.getDescriptor(a).getLocalName(),a,ColumnType.Selection);
		} else if (c == Long.class) {
			column = new LongColumn(ft.getDescriptor(a).getLocalName(),a,ColumnType.Selection);
		} else if (c == Double.class) {
			column = new DoubleColumn(ft.getDescriptor(a).getLocalName(),a,ColumnType.Selection);
		}
		column.setHighest(sf.getAttribute(a).toString());
		column.setLowest(sf.getAttribute(a).toString());
		/*
		else if (c == MultiPolygon.class) {
			//column = new MultiPolygonColumn(ft.getDescriptor(a).getLocalName(),a,ColumnType.None);
			System.out.println("Unimplemented type: "+c);
			succes = false;
			break;
		} else if (c == Date.class) {
			column = new DateColumn(ft.getDescriptor(a).getLocalName(),a,ColumnType.None);
			System.out.println("Unimplemented type: "+c);
			succes = false;
			break;
		} else if (c == MultiLineString.class) {
			//column = new MultiLineStringColumn(ft.getDescriptor(a).getLocalName(),a,ColumnType.None);
			System.out.println("Unimplemented type: "+c);
			succes = false;
			break;
		} */
		return column;
	}
}
