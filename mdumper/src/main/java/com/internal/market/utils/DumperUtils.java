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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.internal.market.object.BasicStockInfo;

public class DumperUtils {

	public final static String APACHE_HTTP_CLIENT = "APACHE_HTTP_CLIENT";
	
	static SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
	
	public static final String baseOutputDir = "src/test/output/";

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

}
