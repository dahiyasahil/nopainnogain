package com.internal.market;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;

import com.internal.market.fetcher.Fetcher;
import com.internal.market.fetcher.GoogleStockFetcher;
import com.internal.market.fetcher.internal.TechPaisaStockIndicatorFetcher;
import com.internal.market.object.BasicStockInfo;
import com.internal.market.object.GoogleStockInfoResponseObject;
import com.internal.market.utils.DumperUtils;

public class MDumper {

	private static String baseUrl = "http://www.google.com/finance/info?q=";
	private static String stockMarketName = "NSE";
	private static String companyName = "sbin";
	private static String baseResourceDir = "src/test/resources/";

	public static void main(String[] args) {

		Map<String, Float> stockStrengthMap = new HashMap<String, Float>();
		Map<String, GoogleStockInfoResponseObject> stockInfoMap = new HashMap<String, GoogleStockInfoResponseObject>();
		
		
		List<String> stockList = new ArrayList<String>(Arrays.asList("unionbank", "Arvind", "sbin",
				"crompgreav", "dishman", "voltas", "arvind", "pricol", "adanipower", "kpit","escorts","sintex","ncc", "hindalco", "powergrid"));
		
		try {
			Object webClient = RestClientFactory.createRESTClient(DumperUtils.APACHE_HTTP_CLIENT);

			if (webClient != null) {
				GoogleStockFetcher stockFetcher = new GoogleStockFetcher(baseUrl, (HttpClient) webClient);
				
				List<BasicStockInfo> infoObjList = (List<BasicStockInfo>)stockFetcher.fetchHistoricData(stockMarketName, "sbin", "2016", "Apr", "1", "2016", "Apr", "30");
				
				DumperUtils.dumpToFile(infoObjList, baseResourceDir + "/histData-sbin.txt");
				
				for(String stock: stockList) {
					GoogleStockInfoResponseObject googleResObject = (GoogleStockInfoResponseObject) stockFetcher.fetchMarketFeeds(stockMarketName, stock);
					stockInfoMap.put(stock, googleResObject);
					
				}
				
				
				TechPaisaStockIndicatorFetcher techPaisaStockIndicatorFetcher = new TechPaisaStockIndicatorFetcher(null,
						"http://techpaisa.com/stock/", (HttpClient) webClient);

				for(String stock: stockList) {
					Float strength = techPaisaStockIndicatorFetcher.getAvgTechnicalStrengthOfStock(stockMarketName, stock);
					stockStrengthMap.put(stock, strength);
					
				}

				Map<String, Float> map = sortByValue(stockStrengthMap);
				for (Map.Entry<String, Float> entry : map.entrySet()) {
					System.out.println(entry.getKey() + " : " + entry.getValue());
					System.out.println("    price: " + stockInfoMap.get(entry.getKey()).getL());
				}

			} else {
				System.out.println("web clients  found null");

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Map<String, Float> sortByValue(Map<String, Float> unsortMap) {

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
		Map<String, Float> sortedMap = new LinkedHashMap<String, Float>();
		for (Map.Entry<String, Float> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
}
