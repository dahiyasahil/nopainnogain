package com.internal.market;

import org.apache.http.client.HttpClient;

import com.internal.market.fetcher.Fetcher;
import com.internal.market.fetcher.GoogleStockFetcher;
import com.internal.market.utils.DumperUtils;

public class MDumper {

	private static String baseUrl = "http://www.google.com/finance/info?q=";
	private static String stockMarketName = "NSE";
	private static String companyName = "AIAENG";
	
	
	public static void main(String[] args) {

		try {
			Object webClient = RestClientFactory.createRESTClient(DumperUtils.APACHE_HTTP_CLIENT);

			if (webClient != null) {
				Fetcher stockFetcher = new GoogleStockFetcher(baseUrl, (HttpClient) webClient);
				String res = stockFetcher.fetchMarketFeeds(stockMarketName, companyName);
				System.out.println(res);
			} else {
				System.out.println("web clients  found null");

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
