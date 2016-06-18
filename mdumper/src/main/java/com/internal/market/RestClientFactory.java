package com.internal.market;

import org.apache.http.impl.client.HttpClientBuilder;

import com.internal.market.utils.DumperUtils;

public class RestClientFactory {

	public static Object createRESTClient( String name) {
		
		Object webClient = null;
		if (name != null && name.equals(DumperUtils.APACHE_HTTP_CLIENT)) {
			webClient = HttpClientBuilder.create().build();
		}
		
		return webClient;
		
	}
}
