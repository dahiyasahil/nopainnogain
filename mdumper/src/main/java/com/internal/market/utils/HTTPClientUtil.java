package com.internal.market.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.internal.market.RestClientFactory;

public class HTTPClientUtil {
	
	
	
	public static HttpClient getHttpClient(){
		HttpClient webClient = (HttpClient) RestClientFactory.createRESTClient(DumperUtils.APACHE_HTTP_CLIENT);
		return webClient;
	}
	
	
	public static HttpEntity getResponseEntity(HttpClient client,String url) {
		HttpResponse response = null;
		HttpEntity entity = null;
		HttpGet request = new HttpGet(url);
		try {
			response = client.execute(request);
			entity = response.getEntity();
		} catch (IOException e) {
			
		}
		return entity;
		
		
	}
	
	public static void close(HttpEntity entity){
		try {
			EntityUtils.consume(entity);
		} catch (IOException e) {
			
		}
	}
	
}


