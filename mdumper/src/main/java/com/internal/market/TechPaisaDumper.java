package com.internal.market;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
				//DumperUtils.stockList
				//removed the stockMarketName from this method call, its actually never used in URL
				stockStrengthMap = techPaisaStockIndicatorFetcher.getAvgTechnicalStrengthOfStock(DumperUtils.getAllFuturesStockList() );
					
				//Pass the second int parameter if the result should be TOP 10 or TOP 5, otherwise the parameter should'nt be passed to get the strength of all futures.
				Map<String, Float> map = DumperUtils.sortByValue(stockStrengthMap,10);
				for (Map.Entry<String, Float> entry : map.entrySet()) {
					System.out.println(entry.getKey().toUpperCase() + " : Strength =  " + entry.getValue());
					
				}
				DumperUtils.sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
				String date = DumperUtils.sdf.format(new Date());
				DumperUtils.dumpStrengthToFile(map, "Strength-" +  date +".csv");

			} else {
				System.out.println("web clients  found null");

			}
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	


}
