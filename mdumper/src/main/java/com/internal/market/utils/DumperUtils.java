package com.internal.market.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.internal.market.object.BasicStockInfo;

public class DumperUtils {
	
	public final static String APACHE_HTTP_CLIENT = "APACHE_HTTP_CLIENT";
	

	public static void dumpToFile(List<BasicStockInfo> infoObjList, String fileName) {
		// TODO Auto-generated method stub
		File file = new File(fileName);
		FileOutputStream fout;
		
		BufferedWriter writer = null;
		try {
			fout = new FileOutputStream(file);
			writer = new BufferedWriter(new OutputStreamWriter(fout));
			
			writer.write("Date,Open,High,Low,Close,Volume\n");
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
			if ( writer != null) {
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
	
}
