package com.internal.market.fetcher.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.internal.market.fetcher.Fetcher;

public class TechPaisaStockIndicatorFetcher implements Fetcher {

	private String baseUrl;

	private String baseUrlTechAnalysisDetials = "http://techpaisa.com/stock/";

	private HttpClient webClient;

	public TechPaisaStockIndicatorFetcher(String url, HttpClient webClient) throws Exception {

		this.baseUrl = url;
		if (webClient != null)
			this.webClient = webClient;
		else
			throw new Exception("WebClient can't be null");

	}

	public TechPaisaStockIndicatorFetcher(String url, String baseUrlTechAlsys, HttpClient webClient) throws Exception {

		this.baseUrl = url;
		this.baseUrlTechAnalysisDetials = baseUrlTechAlsys;
		if (webClient != null)
			this.webClient = webClient;
		else
			throw new Exception("WebClient can't be null");

	}

	/*
	 * http://techpaisa.com/api/stock-screener/?app_id=f17ff0dd&app_key=
	 * 274ca69cc1c5fbf844d6fed929383fcc&ind=nifty&result=json&ts=on&foi=on
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.internal.market.fetcher.Fetcher#fetchMarketFeeds(java.lang.String,
	 * java.lang.String)
	 */
	public Object fetchMarketFeeds(String stockMarketName, List<String> companies) {
		Map<String, Object> resObj = new HashMap<String, Object>();

		for (String company : companies) {

			String url = baseUrl + stockMarketName + ":" + company;
			System.out.println("url = " + url);

			HttpGet request = new HttpGet(url);

			try {
				HttpResponse response = webClient.execute(request);
				System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
				HttpEntity entity = response.getEntity();
				resObj.put(company,EntityUtils.toString(entity));
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return resObj;
	}

	public Map<String, Float> getAvgTechnicalStrengthOfStock(String stockMarketName, List<String> companies) {

		Map<String, Float> resObj = new HashMap<String, Float>();

		for (String company : companies) {

			String url = baseUrlTechAnalysisDetials + company;
			// System.out.println("url = " + url);

			HttpGet request = new HttpGet(url);

			String responseStr = null;

			try {
				HttpResponse response = webClient.execute(request);
				// System.out.println("Response Code : "
				// + response.getStatusLine().getStatusCode());
				HttpEntity entity = response.getEntity();
				responseStr = EntityUtils.toString(entity);
				resObj.put(company, parseResponseForTechnicalStrenght(responseStr));
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resObj;
	}

	/*
	 * Technical Strength: 7 / 10
	 */
	private float parseResponseForTechnicalStrenght(String resStr) {

		int index = resStr.indexOf("Technical Strength:");
		if (index != -1) {
			int endIndex = index + (new String("Technical Strength:")).length();
			String intStr = resStr.substring(endIndex, endIndex + 8);
			String[] tmp = intStr.split("/");

			return Float.parseFloat(tmp[0].trim());
		}
		return 0;
	}
}
