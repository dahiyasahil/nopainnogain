package com.internal.market;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;

import com.internal.market.fetcher.internal.TechPaisaStockIndicatorFetcher;
import com.internal.market.utils.DumperUtils;

public class TechPaisaDumper {

	private static String stockMarketName = "NSE";

	public static void main(String[] args) {

		Map<String, Float> stockStrengthMap = new HashMap<String, Float>();
		
		
		try {
			HttpClient webClient = (HttpClient) RestClientFactory.createRESTClient(DumperUtils.APACHE_HTTP_CLIENT);
			if (webClient != null) {
				
				TechPaisaStockIndicatorFetcher techPaisaStockIndicatorFetcher = new TechPaisaStockIndicatorFetcher(null,
						"http://techpaisa.com/stock/", (HttpClient) webClient);

				stockStrengthMap = techPaisaStockIndicatorFetcher.getAvgTechnicalStrengthOfStock(stockMarketName, DumperUtils.stockList);
					

				Map<String, Float> map = DumperUtils.sortByValue(stockStrengthMap);
				for (Map.Entry<String, Float> entry : map.entrySet()) {
					System.out.println(entry.getKey().toUpperCase() + " : Strength =  " + entry.getValue());
					
				}
				String date = DumperUtils.sdf.format(new Date());
				DumperUtils.dumpStrengthToFile(map, "Strength-" +  date);

			} else {
				System.out.println("web clients  found null");

			}
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	


}
