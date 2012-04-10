package nl.hr.ictlab;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Set;

import org.geotools.data.FeatureReader;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;
import com.vividsolutions.jts.geom.Point;

public class MongoDBWriter {
	Mongo m;
	DB db;
	public MongoDBWriter(String dbName) throws UnknownHostException, MongoException{
		m = new Mongo( "localhost" , 27017 );
		
		db = m.getDB(dbName);
	}
	
	public void fromShapeFile(File shapeFile) throws IOException{
		DBCollection coll = db.getCollection(shapeFile.getName().substring(0,shapeFile.getName().indexOf(".")));
		FileDataStore store = FileDataStoreFinder.getDataStore(shapeFile);
        FeatureReader<SimpleFeatureType, SimpleFeature> sf = store.getFeatureReader();
        
        while(sf.hasNext()){
        	writeToDB(sf.next(),coll);
        }
	}
	
	public void writeToDB(SimpleFeature sf,DBCollection coll){
		SimpleFeatureType ft = sf.getFeatureType();
		BasicDBObject doc = new BasicDBObject();
		for(int a = 0;a < sf.getAttributeCount();++a){
			Class<?> c = ft.getDescriptor(a).getType().getBinding();
			if(c == Point.class){
				BasicDBObject coordinates = new BasicDBObject();
				Point p = (Point)sf.getAttribute(a);
				double[] xy = Convert.convertm(p.getX(),p.getY());
				coordinates.put("x",xy[0]);
				coordinates.put("y",xy[1]);
				doc.put("coordinates",coordinates);
			} else {
				doc.put(ft.getDescriptor(a).getLocalName(),sf.getAttribute(a));
			}
		}
		try{
			coll.insert(doc);
		}catch (Exception e){
			System.out.println(e);
		}
	}
}
