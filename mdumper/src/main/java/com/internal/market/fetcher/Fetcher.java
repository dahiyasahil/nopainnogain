package com.internal.market.fetcher;

import java.util.List;

/*
 * Marker interface to specify the Class will be used for collecting stock market related data from remote source.
 * 
 */
public interface Fetcher {
	
	Object fetchMarketFeeds(String stockMarketName, List<String> company);
}
