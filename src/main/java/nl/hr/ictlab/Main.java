package nl.hr.ictlab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import nl.hr.ictlab.ShapeFileContainer;

import org.geotools.data.FeatureReader;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class Main {


    public static void main(String[] args) throws Exception {
    	System.out.println();
        // display a data store file chooser dialog for shapefiles
        File file = new File("D:\\School\\ICTLab\\gemeentewerken\\GW-Objecten\\speeltoestellen\\speeltoestellen.shp");
        //File file = JFileDataStoreChooser.showOpenFile("shp", null);
        //JFileDataStoreChooser.showOpenFile("shp", null);
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = store.getFeatureReader();
        
        SimpleFeature sf;
        sf = fr.next();
        SimpleFeatureType ft = sf.getFeatureType();
        ShapeFileContainer sfc = new ShapeFileContainer();
        System.out.print("Loading columns...");
        for(int a = 0;a < ft.getAttributeCount();++a){
        	sfc.addColumn(ft.getDescriptor(a).getType().getBinding(),ft.getDescriptor(a).getLocalName());
        }
        System.out.println("done");
        System.out.print("Loading shapefile...");
        fr = store.getFeatureReader();
        for(;fr.hasNext();){
        	sf = fr.next();
        	for(int a = 0;a < sf.getAttributeCount();++a){
        		sfc.add(a,sf.getAttribute(a));
        	}
        }
        System.out.println("done");
        System.out.print("writing to file...");
        try{
        	  FileWriter fstream = new FileWriter(file.getName().substring(0,file.getName().indexOf("."))+".json");
        	  BufferedWriter out = new BufferedWriter(fstream);
        	  //out.write(sfc.toJson());
        	  sfc.toJson(out);
        	  out.close();
        	  }	catch (Exception e){
        		  System.err.println("Error: " + e.getMessage());
        }
        System.out.println("done");
        //System.out.println(sfc.rangeToJson(0,10));
    }
}