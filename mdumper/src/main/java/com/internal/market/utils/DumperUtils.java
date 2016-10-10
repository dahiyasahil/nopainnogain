package com.internal.market.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.internal.market.object.BasicStockInfo;

public class DumperUtils {

	public static List<String> stockList = new ArrayList<String>(Arrays.asList("bhel", "unionbank", "Arvind", "sbin",
			"crompgreav", "dishman", "voltas", "pricol", "adanipower", "kpit", "escorts", "sintex", "ncc",
			"hindalco", "powergrid", "recltd", "apollotyre", "albk", "tatachem", "enginersin", "petronet", "havellsindia", "tatapower", "havells", "exideind", "tatamtrdvr", "godrejcp", "m&mfin", "pfc"));
	
	
	public static List<String> stockGroups = new ArrayList<String>(Arrays.asList("CNX_NIFTY", "CNX_COMMODITIES","CNX_ENERGY", "CNX_PHARMA","CNX_INFRA","CNX_AUTO","CNX_MIDCAP","CNX_PSU_BANK","CNX_BANK"));
	
	public final static String APACHE_HTTP_CLIENT = "APACHE_HTTP_CLIENT";
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	private static final String PATH_TO_FUTURE_LIST = "src/main/resources/Futures.csv";
	
	public static final String baseOutputDir = "src/main/resources/data/";
	
	
	public static List<String> getAllFuturesStockList() {

		CSVUtil csv = new CSVUtil();
		List<String> allFuturesStockList = new ArrayList<String>();

		String[] nextLine;
		CSVReader reader = null;
		try {
			reader = csv.getReader(PATH_TO_FUTURE_LIST);
			int i = 0;
			while ((nextLine = reader.readNext()) != null) {
				if (i > 0) {
					allFuturesStockList.add(nextLine[1]);
				}
				i++;
			}
		} catch (IOException e) {

		}

		csv.close(reader);

		return allFuturesStockList;
	}
	

	
	public static void dumpToFile(List<BasicStockInfo> infoObjList, String fileName) {
		// TODO Auto-generated method stub
		File file = new File(baseOutputDir + fileName);
		FileOutputStream fout;

		BufferedWriter writer = null;
		try {
			fout = new FileOutputStream(file);
			writer = new BufferedWriter(new OutputStreamWriter(fout));

			//writer.write("Date,Open,High,Low,Close,Volume\n");
			for (BasicStockInfo obj : infoObjList) {
				writer.write(obj.toString());
				writer.write("\n");
			}
			writer.flush();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static void dumpToFile(Map<String, List<Object>> results) {
		// TODO Auto-generated method stub
		Iterator<Entry<String, List<Object>>> it = results.entrySet().iterator();
		String baseOutputDir = "src/test/output/";
		
		String date = sdf.format(new Date());
		while (it.hasNext()) {
			Entry<String, List<Object>> entry = it.next();

			BufferedWriter bw = null;
			PrintWriter out = null;
			FileWriter fw = null;
			try {
				fw = new FileWriter(baseOutputDir + date + "_" + entry.getKey() + ".txt", true);
				bw = new BufferedWriter(fw,1000000);
				out = new PrintWriter(bw);
				for(Object obj : entry.getValue()) {
					out.println(obj.toString());
					
				}
				out.flush();
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null )
				try {
					bw.close();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

	}

	public static  List<BasicStockInfo> getStockInfoObjs(String responseStr) {

		List<BasicStockInfo> data = new ArrayList<BasicStockInfo>();

		String[] lines = responseStr.split("\n");
		for (String line : lines) {
			String[] cols = line.split(",");
			BasicStockInfo infoObj  = null;
			if (cols.length >= 6) {
				infoObj = new BasicStockInfo();
				infoObj.setDate(cols[0]);
				infoObj.setOpen(cols[1]);
				infoObj.setHigh(cols[2]);
				infoObj.setLow(cols[3]);
				infoObj.setClose(cols[4]);
				infoObj.setVolume(cols[5]);
				data.add(infoObj);
			}
		}
		return data;

	}

	public static void dumpStrengthToFile(Map<String, Float> map,String filename) {

		String file = baseOutputDir + "/hist-strength/" + filename;

		CSVUtil csv = new CSVUtil();
		CSVWriter writer = null;
		try {
			writer = csv.getWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("wrtier is "+writer);
		csv.appendFile(writer, map);
		csv.close(writer);

	}
	
	public static Map<String, Float> sortByValue(Map<String, Float> unsortMap) {
		
		return sortByValue(unsortMap, unsortMap.size());
	}

	public static Map<String, Float> sortByValue(Map<String, Float> unsortMap, int size) {

		// 1. Convert Map to List of Map
		List<Map.Entry<String, Float>> list = new LinkedList<Map.Entry<String, Float>>(unsortMap.entrySet());

		// 2. Sort list with Collections.sort(), provide a custom Comparator
		// Try switch the o1 o2 position for a different order
		Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
			public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// 3. Loop the sorted list and put it into a new insertion order Map
		// LinkedHashMap
		int i =0;
		Map<String, Float> sortedMap = new LinkedHashMap<String, Float>();
	
		for (Map.Entry<String, Float> entry : list) {
				if(i < size){
				sortedMap.put(entry.getKey(), entry.getValue());
				i++;
				}
			

		}

		return sortedMap;
	}
}
