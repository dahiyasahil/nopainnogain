package com.internal.market.fetcher;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class GoogleStockFetcher implements Fetcher {

	private String baseUrl;
	
	private HttpClient webClient;
	
	public GoogleStockFetcher(String url, HttpClient webClient) throws Exception {
		
		this.baseUrl = url;
		if (webClient != null)
			this.webClient = webClient;
		else 
			throw new Exception("WebClient can't be null");
		
	}
	
	public String fetchMarketFeeds(String stockMarketName, String company) {
		String url = baseUrl + stockMarketName + ":" + company;
		System.out.println("url = " + url);
		
		HttpGet request = new HttpGet(url);
		
		String responseStr = null;
		
		try {
			HttpResponse response = webClient.execute(request);
			System.out.println("Response Code : " 
	                + response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			responseStr = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseStr;
	}


}
