package nl.hr.ictlab;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Point;

public class JsonWriter {
	private BufferedWriter out;
	public JsonWriter(BufferedWriter out){
		this.out = out;
	}
	
	public void writeFeature(SimpleFeature sf,boolean last) throws IOException{
		out.newLine();
		out.write("{ \"type\": \"Feature\", \"properties\": { ");
        SimpleFeatureType ft = sf.getFeatureType();
        Point p = null;
		for(int a = 0;a < sf.getAttributeCount();++a){
			Class<?> c = ft.getDescriptor(a).getType().getBinding();
			if(c == String.class){// String heeft " " nodig om de data.
				out.write("\"");
				out.write(ft.getDescriptor(a).getLocalName());
				out.write("\": ");
				out.write("\"");
				out.write(sf.getAttribute(a).toString());
				out.write("\"");
				if(a < sf.getAttributeCount()-1){
					out.write(", ");
				}
			} else if (c == Point.class){ // point moet "{ "type": "Point", "coordinates": [x,y] } zijn
				if(p != null){
					System.out.println("meerdere Points...");
				}
				p = (Point)sf.getAttribute(a);
			} else { //en anders is het een int/long/double/float
				out.write("\"");
				out.write(ft.getDescriptor(a).getLocalName());
				out.write("\": ");
				out.write(sf.getAttribute(a).toString());
				if(a < sf.getAttributeCount()-1){
					out.write(", ");
				}
			}
    	}
		if(p != null){
			out.write("}, \"geometry\": { \"type\": \"Point\", \"coordinates\": ");
			out.write(Convert.convertmString(p.getX(),p.getY()));
			out.write(" }");
		} else {
			out.write("} ");
		}
		
		if(last){
			out.write(" }");
		} else {
			out.write(" },");
		}
	}
	
	public void start() throws IOException{
		out.write("{ \"type\": \"FeatureCollection\", \"features\": [");
	}
	
	public void end() throws IOException{
		out.newLine();
		out.write("] }");
		out.close();
	}
}
