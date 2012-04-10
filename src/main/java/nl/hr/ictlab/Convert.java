package nl.hr.ictlab;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.io.*;
public class Convert
{
	static float X0=155000.0f; //nodig voor UTM -> lng/lat
	static float Y0=463000.0f; //nodig voor UTM -> lng/lat
	static float lat0=52.15517440f; //nodig voor UTM -> lng/lat
	static float lng0=5.38720621f; //nodig voor UTM -> lng/lat
	
	static Map<String,Float> latpqK = new HashMap<>(); //nodig voor UTM -> lng/lat
	static Map<String,Float> lngpqL = new HashMap<>(); //nodig voor UTM -> lng/lat
	
	static { //vullen van latpqK/lngpqL
		latpqK.put("1p",0f);
		latpqK.put("1q",1f);
		latpqK.put("1K",3235.65389f);
		latpqK.put("2p",2f);
		latpqK.put("2q",0f);
		latpqK.put("2K",-32.58297f);
		latpqK.put("3p",0f);
		latpqK.put("3q",2f);
		latpqK.put("3K",-0.24750f);
		latpqK.put("4p",2f);
		latpqK.put("4q",1f);
		latpqK.put("4K",-0.84978f);
		latpqK.put("5p",0f);
		latpqK.put("5q",3f);
		latpqK.put("5K",-0.06650f);
		latpqK.put("6p",2f);
		latpqK.put("6q",2f);
		latpqK.put("6K",-0.01709f);
		latpqK.put("7p",1f);
		latpqK.put("7q",0f);
		latpqK.put("7K",-0.00738f);
		latpqK.put("8p",4f);
		latpqK.put("8q",0f);
		latpqK.put("8K",0.00530f);
		latpqK.put("9p",2f);
		latpqK.put("9q",3f);
		latpqK.put("9K",-0.00039f);
		latpqK.put("10p",4f);
		latpqK.put("10q",1f);
		latpqK.put("10K",0.00033f);
		latpqK.put("11p",1f);
		latpqK.put("11q",1f);
		latpqK.put("11K",-0.00012f);
		
		lngpqL.put("1p",1f);
		lngpqL.put("1q",0f);
		lngpqL.put("1K",5260.52916f);
		lngpqL.put("2p",1f);
		lngpqL.put("2q",1f);
		lngpqL.put("2K",105.94684f);
		lngpqL.put("3p",1f);
		lngpqL.put("3q",2f);
		lngpqL.put("3K",2.45656f);
		lngpqL.put("4p",3f);
		lngpqL.put("4q",0f);
		lngpqL.put("4K",-0.81885f);
		lngpqL.put("5p",1f);
		lngpqL.put("5q",3f);
		lngpqL.put("5K",0.05594f);
		lngpqL.put("6p",3f);
		lngpqL.put("6q",1f);
		lngpqL.put("6K",-0.05607f);
		lngpqL.put("7p",0f);
		lngpqL.put("7q",1f);
		lngpqL.put("7K",0.01199f);
		lngpqL.put("8p",3f);
		lngpqL.put("8q",2f);
		lngpqL.put("8K",-0.00256f);
		lngpqL.put("9p",1f);
		lngpqL.put("9q",4f);
		lngpqL.put("9K",0.00128f);
		lngpqL.put("10p",0f);
		lngpqL.put("10q",2f);
		lngpqL.put("10K",0.00022f);
		lngpqL.put("11p",2f);
		lngpqL.put("11q",0f);
		lngpqL.put("11K",-0.00022f);
		lngpqL.put("12p",5f);
		lngpqL.put("12q",0f);
		lngpqL.put("12K",0.00026f); 
	}
	
	
	private static class Point{ //Punt voor lng/lat
		public double lng;
		public double lat;
		public Point(double lng,double lat){
			this.lng = lng;
			this.lat = lat;
		}
		public String toString(){
			return "["+lat+", "+lng+"]";
		}
	}
	
	public static void main(String[] args){
		List<String> input = new ArrayList<>(); //regels opslaan uit het bestand
		String file = "speeltoestellen"; //bestand wat verwerkt gaat worden
		File f = new File(file+".json");
		String tmp = f.getName();
		tmp = tmp.substring(0,tmp.indexOf("."));
		System.out.println(f.getAbsolutePath());
		long start = System.currentTimeMillis();
		System.out.print("Reading file...");
		try{
			FileInputStream fstream = new FileInputStream(file+".json");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				input.add(strLine);
			}
			in.close();
		}catch (Exception e){
				System.err.println("Error: " + e.getMessage());
		}
		System.out.println("done ("+(System.currentTimeMillis() - start)+"ms)");
		start = System.currentTimeMillis();
		System.out.print("Converting UTM to lng/lat...");
		List<String> output = new ArrayList<>(input.size()); //opslag van regels na verwerking
		int current = input.size();
		while(--current >= 0){
			String test = geoJsonLngLatFix(input.get(current)); //UTM string verwerken naar lng/lat
			output.add(test);
			input.remove(current);
		}
		Collections.reverse(output);
		System.out.println("done ("+(System.currentTimeMillis() - start)+"ms)");
		start = System.currentTimeMillis();
		System.out.print("Writing to new file...");
		try{
			FileWriter fstream = new FileWriter(file+"_new.json");
			BufferedWriter out = new BufferedWriter(fstream);
			for(String s : output){
				out.write(s+"\n");
			}
			out.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
		System.out.println("done ("+(System.currentTimeMillis() - start)+"ms)");
		/* test
		String s = "91707.800000, 437145.780000";
		float x = Float.valueOf(s.substring(0,s.indexOf(",")));
		float y = Float.valueOf(s.substring(s.indexOf(",")+1,s.length()));
		System.out.println(convertm(s));
		*/
	}
	
	public static String geoJsonLngLatFix(String s){ //Specifieke functie om GeoJson UTM om te zetten naar lng/lat
		StringBuilder tmp = new StringBuilder(s);
		int start = tmp.indexOf("[",tmp.lastIndexOf("coordinates"))+1;
		int end = tmp.lastIndexOf("]");
		if(start == 0 || end == -1){
			return s;
		}
		return tmp.replace(start-1,end+1,convertm(tmp.substring(start,end)).toString()).toString();
	}
	
	public static double[] convertm(String s){ //UTM -> lng/lat functie van http://www.gpscoordinaten.nl/beta/converteer-rd-coordinaten.php
		s = s.replaceAll(" ","");
		float x = Float.valueOf(s.substring(0,s.indexOf(",")));
		float y = Float.valueOf(s.substring(s.indexOf(",")+1,s.length()));
		return convertm(x,y);
	}
	public static double[] convertm(double x,double y) { //UTM -> lng/lat functie van http://www.gpscoordinaten.nl/beta/converteer-rd-coordinaten.php
		return new double[] {RD2lng(x,y),RD2lat(x,y)};
	}
	
	public static String convertmString(double x, double y){
		return convertm(x,y).toString();
	}
	
	public static double RD2lat(double x,double y){ //UTM -> lng/lat functie van http://www.gpscoordinaten.nl/beta/converteer-rd-coordinaten.php
		double sum=0f;
		double dX=0.00001f*(x-X0);
		double dY=0.00001f*(y-Y0);
		for (int i=1;i<12;i++) {
			sum+=latpqK.get(i+"K")*Math.pow(dX,latpqK.get(i+"p"))*Math.pow(dY,latpqK.get(i+"q"));
		}
		return (lat0+sum/3600.0f);
	}
	
	
	public static double RD2lng(double x,double y){ //UTM -> lng/lat functie van http://www.gpscoordinaten.nl/beta/converteer-rd-coordinaten.php
		double sum=0f;
		double dX=0.00001f*(x-X0);
		double dY=0.00001f*(y-Y0);
		for (int i=1;i<13;i++) {
			sum+=lngpqL.get(i+"K")*Math.pow(dX,lngpqL.get(i+"p"))*Math.pow(dY,lngpqL.get(i+"q"));
		}
		return (lng0+sum/3600.0f);
	} 
	
} 
