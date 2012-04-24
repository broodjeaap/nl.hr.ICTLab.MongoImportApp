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

import com.mongodb.BasicDBList;
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
			BasicDBObject metaData = new BasicDBObject();
			coll = db.getCollection("MetaData");
			metaData.put("name", collectionName);
			for(AbstractColumn ac : list){
				metaData.put(ac.getName(), getMetaData(ac));
			}
			coll.insert(metaData);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static BasicDBObject getMetaData(AbstractColumn ac){
		BasicDBObject ret = new BasicDBObject();
		switch(ac.getType()){
			case Selection:{
				ret.put("type","selection");
				BasicDBList values = new BasicDBList();
				for(String s : ac.getActiveUniqueValues()){
					values.add(s);
				}
				ret.put("values", values);
				break;
			}
			case Range:{
				ret.put("type", "range");
				ret.put("from", ac.getLowest());
				ret.put("to", ac.getHighest());
				break;
			}
			case Location:{
				ret.put("type", "location");
				break;
			}
			default:{
				ret.put("type", "none");
				break;
			}
		}
		return ret;
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
