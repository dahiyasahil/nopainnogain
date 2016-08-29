package com.internal.market.fetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.internal.market.utils.DumperUtils;

public class PeriodicStockDataCollector implements Runnable {

	private Fetcher fetcher;
	private int timeIntervals = 15;
	private String stockMarketName = null;
	private List<String> companies = null;
	private int maxBuffer = 10;
	private boolean isInterrupted = false;

	/*
	 * @Param Fetcher - fetcher used for collecting data from remote source
	 * 
	 * @Param timeInterval - time inverval in secs.
	 */
	public PeriodicStockDataCollector(Fetcher fetcher, int timeInterval, String mktName, List<String> companies) {
		this.fetcher = fetcher;
		this.timeIntervals = timeInterval;
		this.companies = companies;
		this.stockMarketName = mktName;
	}

	@SuppressWarnings("static-access")
	public void run() {
		Map<String, List<Object>> results = null;
		// TODO Auto-generated method stub
		int count = 0;
		results = new HashMap<String, List<Object>>();
		while (!isInterrupted) {
			try {
				Thread.currentThread().sleep(timeIntervals * 1000);
				@SuppressWarnings("unchecked")
				Map<String, Object> stocksInfoMap = (Map<String, Object>) fetcher.fetchMarketFeeds(stockMarketName,
						companies);
				Iterator<Entry<String, Object>> it = stocksInfoMap.entrySet().iterator();
				List<Object> tmpList = null;
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					if (entry.getValue() != null) {
						if (!results.containsKey(entry.getKey())) {
							tmpList = new ArrayList<Object>();
							tmpList.add(entry.getValue());
							results.put(entry.getKey(), tmpList);
						} else {
							tmpList = results.get(entry.getKey());
							tmpList.add(entry.getValue());
						}
					}
				}
				count++;
				if (count > maxBuffer) {
					DumperUtils.dumpToFile(results);
					results = new HashMap<String, List<Object>>();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		DumperUtils.dumpToFile(results);
	}

	public void startCollection() {
		// TODO Auto-generated method stub
		new Thread(this).start();
	}

	public void interrupt() {
		this.isInterrupted = true;
	}

}
