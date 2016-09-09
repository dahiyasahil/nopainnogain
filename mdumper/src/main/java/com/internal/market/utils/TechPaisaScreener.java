package com.internal.market.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.internal.market.fetcher.internal.TechPaisaStockIndicatorFetcher;



public class TechPaisaScreener {
	
	
	final String HTTP_URL = "http://techpaisa.com/api/stock-screener/?";

	final String EQUALS = "=";
	
	final String AMPERSON = "&";
	
	String queryURL = HTTP_URL;
	
	
	 
	
	public TechPaisaScreener addParam(QueryParam param){
		
		this.queryURL = queryURL+param.getKey()+EQUALS+param.getValue()+AMPERSON;
		return this;
		
	}
	
	public String urlForStrongFutures(){
	
		this.addParam(QueryParam.APP_ID).addParam(QueryParam.APP_KEY).addParam(QueryParam.STOCK_TRADED_IN_FUTURES).addParam(QueryParam.TECHNICALLY_STRONG_STOCKS).addParam(QueryParam.INDEX);
		return queryURL;
		
	}
	
	public List<String> parseJsonEntity(HttpEntity entity) throws ParseException, org.json.simple.parser.ParseException, IOException{
		List<String> strongFutureList = new ArrayList<String>();
		String str = EntityUtils.toString(entity);
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(str);
		
		JSONObject jsonObj =  (JSONObject) obj;
		JSONArray msg = (JSONArray) jsonObj.get("s");
		
	
		Iterator<JSONArray> iterator = msg.iterator();

		while (iterator.hasNext()) {
			JSONArray value = (JSONArray)iterator.next();
			Iterator<String> iterator2 = value.iterator();
			while (iterator2.hasNext()){	
				strongFutureList.add((String)iterator2.next());
				iterator2.next();
			}
		}
		
		return strongFutureList;
	}
	
	public static void main(String[] args) throws Exception {
		TechPaisaScreener tp = new TechPaisaScreener();
		HttpClient client = HTTPClientUtil.getHttpClient();
		String url = tp.urlForStrongFutures();
		HttpEntity entity =HTTPClientUtil.getResponseEntity(client, url);
		
		List<String> stockList =  tp.parseJsonEntity(entity);
		
		
		Map<String, Float> stockStrengthMap = new HashMap<String, Float>();
		TechPaisaStockIndicatorFetcher techPaisaStockIndicatorFetcher = new TechPaisaStockIndicatorFetcher(null,"http://techpaisa.com/stock/", (HttpClient) client);
		stockStrengthMap = techPaisaStockIndicatorFetcher.getAvgTechnicalStrengthOfStock(stockList );
	
	
		DumperUtils.sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
		String date = DumperUtils.sdf.format(new Date());
		DumperUtils.dumpStrengthToFile(stockStrengthMap, "Strength-" +  date +".csv");
		
		
	
	}
	
	
}
