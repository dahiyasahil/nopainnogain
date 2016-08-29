package com.internal.market.fetcher;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.internal.market.utils.DumperUtils;

public class QuandlDataFetcher {

	/*
	 * http://www.google.com/finance/historical?q=GOOG&histperiod=daily&
	 * startdate=Apr+1+2014&enddate=Apr+15+2014&output=json
	 * 
	 */
	private HttpClient webClient;

	private String tableLineTag = "<table class=\"gf-table historical_price\">";

	private String baseUrl = "https://www.quandl.com/api/v3/datasets/";
	private String apiKey = "MsiGxzNopwz1QxJ5yyTr";

	final RequestConfig params = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();

	public QuandlDataFetcher(String url, HttpClient webClient) throws Exception {

		this.baseUrl = url;
		if (webClient != null)
			this.webClient = webClient;
		else
			throw new Exception("WebClient can't be null");

	}

	public QuandlDataFetcher(HttpClient webClient) throws Exception {

		if (webClient != null)
			this.webClient = webClient;
		else
			throw new Exception("WebClient can't be null");

	}

	public Object fetchHistoricData(String stockMarketName, String stock) {

		String url = baseUrl + stockMarketName + "/" + stock + ".csv?api_key=" + apiKey;

		HttpGet request = new HttpGet(url);
		request.setConfig(params);

		HttpResponse response = null;
		try {
			response = webClient.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();

				String responseStr = EntityUtils.toString(entity);
				return DumperUtils.getStockInfoObjs(responseStr);
			} else {
				System.out.println("Error: URL = " + url);
				System.out.println("Response code = " + response.getStatusLine().getStatusCode());
				return null;
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}


	public Object fetchMarketFeeds(String stockMarketName, List<String> companies) {
		return null;
		
	}

}
