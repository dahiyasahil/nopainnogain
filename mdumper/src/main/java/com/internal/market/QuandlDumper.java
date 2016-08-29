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

import com.internal.market.fetcher.QuandlDataFetcher;
import com.internal.market.object.BasicStockInfo;
import com.internal.market.utils.DumperUtils;

public class QuandlDumper {


	public static void main(String[] args) {


		try {
			HttpClient webClient = (HttpClient) RestClientFactory.createRESTClient(DumperUtils.APACHE_HTTP_CLIENT);

			QuandlDataFetcher fetcher = new QuandlDataFetcher(webClient);

			List<String> stocks = new ArrayList<String>();
			stocks.addAll(DumperUtils.stockGroups);
			stocks.addAll(DumperUtils.stockList);
			for (String stock : stocks) {
				List<BasicStockInfo> data = (List<BasicStockInfo>) fetcher.fetchHistoricData("NSE",
						stock.toUpperCase());

				if (data != null) {
					DumperUtils.dumpToFile(data, "Quandl-hist-data-" + stock);
					System.out.println("Data dumped for stock = " + stock);
				} else {
					System.out.println("Data found null for stock = " + stock);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
