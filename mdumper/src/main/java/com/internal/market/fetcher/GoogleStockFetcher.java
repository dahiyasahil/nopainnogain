package com.internal.market.fetcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.ChunkedInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.DecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.internal.market.object.BasicStockInfo;
import com.internal.market.object.GoogleStockInfoResponseObject;

public class GoogleStockFetcher implements Fetcher {

	/*
	 * http://www.google.com/finance/historical?q=GOOG&histperiod=daily&
	 * startdate=Apr+1+2014&enddate=Apr+15+2014&output=json
	 * 
	 */
	private String baseUrl;

	private HttpClient webClient;

	private String tableLineTag = "<table class=\"gf-table historical_price\">";

	public GoogleStockFetcher(String url, HttpClient webClient) throws Exception {

		this.baseUrl = url;
		if (webClient != null)
			this.webClient = webClient;
		else
			throw new Exception("WebClient can't be null");

	}

	public Object fetchHistoricData(String stockMarketName, String stock, String startYear, String startMonth,
			String startDay, String endYear, String endMonth, String endDay) {

		String url = "http://www.google.com/finance/historical?q=" + stockMarketName + ":" + stock
				+ "&histperiod=daily&startdate=" + startMonth + "+" + startDay + "+" + startYear + "&enddate="
				+ endMonth + "+" + endDay + "+" + endYear;

		HttpGet request = new HttpGet(url);
		request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip");

		HttpResponse response = null;
		try {
			response = webClient.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();

				String responseStr = EntityUtils.toString(entity);
				return parseHistoricalResult(responseStr);
			} else {
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

	private List<BasicStockInfo> parseHistoricalResult(String responseStr) {

		int startIndex = responseStr.indexOf(tableLineTag);
		int endIndex = responseStr.indexOf("</table>", startIndex);

		String table = responseStr.substring(startIndex, endIndex);
		
		List<BasicStockInfo> infoObjList = new ArrayList<BasicStockInfo>();
		Document doc = Jsoup.parse(table, "UTF-8");
		Elements rows = doc.select("tr");
		BasicStockInfo infoObj = null;
		for (Element row : rows) {
			System.out.println("---------");
			Elements data = row.getElementsByTag("td");
			
			if (data.size() > 0) {
				infoObj = new BasicStockInfo();
				infoObj.setDate(data.get(0).text());
				//System.out.println("Date: " + data.get(0).text());
				
				infoObj.setClose(data.get(1).text());
				//System.out.println("Open: " + data.get(1).text());
				
				infoObj.setHigh(data.get(2).text());
				//System.out.println("High: " + data.get(2).text());
				
				infoObj.setLow(data.get(3).text());
				//System.out.println("Low: " + data.get(3).text());
				
				infoObj.setClose(data.get(4).text());
				//System.out.println("Close: " + data.get(4).text());
				
				infoObj.setVolume(data.get(5).text());
				//System.out.println("Volume: " + data.get(5).text());
				
				infoObjList.add(infoObj);
				
			}
		}
		return infoObjList;
	}

	public Object fetchMarketFeeds(String stockMarketName, String company) {
		String url = baseUrl + stockMarketName + ":" + company;
		// System.out.println("url = " + url);

		HttpGet request = new HttpGet(url);

		String responseStr = null;

		try {
			HttpResponse response = webClient.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {

				HttpEntity entity = response.getEntity();
				responseStr = EntityUtils.toString(entity);
				// System.out.println(responseStr);

				String token[] = responseStr.split("//");

				String jsonStr = token[1].substring(2, token[1].length() - 2);
				ObjectMapper mapper = new ObjectMapper();

				GoogleStockInfoResponseObject googleRes = mapper.readValue(jsonStr,
						GoogleStockInfoResponseObject.class);

				// System.out.println(googleRes.toString());

				return googleRes;
			} else {
				System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
				return null;
			}
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
