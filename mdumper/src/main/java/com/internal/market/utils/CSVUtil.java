package com.internal.market.utils;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
public class CSVUtil {
	
	public CSVUtil(){
		
		
	}
	
	public CSVReader getReader(String fileName) throws FileNotFoundException{
		
		
	      CSVReader reader = new CSVReader(new FileReader(fileName));
	      return reader;
	}
	
	
	public CSVWriter getWriter(String fileName) throws IOException{
		
		 CSVWriter writer = new CSVWriter(new FileWriter(fileName));
		 return writer;      
		
	}
	
	public void appendFile(CSVWriter writer,Map map){
		for(Object key :map.keySet()){
			String record = key +","+map.get((String)key);
			appendFile(writer,record.split(","));
		}
		
		
	}
	
	public void appendFile(CSVWriter writer,String[] record){
		writer.writeNext(record);
		
	}
	
		
	public void close(Closeable formatter) {
		if (formatter != null) {
			try {
				formatter.close();
			} catch (IOException e) {

			}
		}
	}
	

}
