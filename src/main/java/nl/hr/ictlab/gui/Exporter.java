package nl.hr.ictlab.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import org.geotools.data.FeatureReader;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Exporter {
	
	public static void toMongo(DefaultListModel<AbstractColumn> columns,File file,String dbName) throws MongoException, IOException{
		toMongo(columns,file,dbName,file.getName().substring(0,file.getName().indexOf(".")));
	}
	
	public static void toMongo(DefaultListModel<AbstractColumn> columns,File file,String dbName,String collectionName){
		try{
		Mongo m = new Mongo( "localhost" , 27017 );
		DB db = m.getDB(dbName);
		
		List<AbstractColumn> list = minimize(columns);
		DBCollection coll = db.getCollection(collectionName);
		FileDataStore store = FileDataStoreFinder.getDataStore(file);
		FeatureReader<SimpleFeatureType, SimpleFeature> sf = store.getFeatureReader();
		BasicDBObject doc = null;
		SimpleFeature simpleFeature;
		while(sf.hasNext()){
			simpleFeature = sf.next();
			doc = new BasicDBObject();
			for(AbstractColumn ac : list){
				ac.parse(simpleFeature,doc);
			}
			coll.insert(doc);
		}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static List<AbstractColumn> minimize(DefaultListModel<AbstractColumn> columns){
		List<AbstractColumn> newColumns = new ArrayList<>();
		AbstractColumn ac;
		for(int a = 0;a < columns.getSize();++a){
			ac = columns.get(a);
			if(ac.isEnabled()){
				newColumns.add(ac);
			}
		}
		return newColumns;
	}
}
