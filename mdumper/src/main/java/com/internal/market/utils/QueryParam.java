package com.internal.market.utils;


enum QueryParam{
	
	APP_ID("app_id","3cf09865"),
	APP_KEY("app_key","128078132e8bfad5f1597c1cc8c4ac0a"),
	STOCK_TRADED_IN_FUTURES("foi","1"),
	TECHNICALLY_STRONG_STOCKS("ts","1"),
	INDEX("ind","nifty"),
	
	NEAR_200_DAY_MOVING_AVG("n200s","0"),
	ABOVE_200_DAY_MOVING_AVG("a200s","0"),
	BELOW_200_dAY_MOVING_AVG("b200s","0"),
	
	NEAR_50_DAY_MOVING_AVG("n50s","0"),
	ABOVE_50_DAY_MOVING_AVG("a50s","0"),
	BELOW_50_DAY_MOVING_AVG("b50s","0"),
	
	NEAR_20_DAY_MOVING_AVG("n20s","0"),
	ABOVE_20_DAY_MOVING_AVG("a20s","0"),
	BELOW_20_DAY_MOVING_AVG("b20s","0"),
	
	OVERSOLD_RSI("os","0"),
	BULLISH_DIVERGENCE_RSI("bld","0"),
	BEARISH_DIVERGENCE_RSI("brd","0"),
	
	MACD_BULLISH_CROSSOVER("blcr","0"),
	MACD_BEARISH_CROSSOVER("brcr","0");
	
	String key;
	String value;
	
	QueryParam(String key,String value){
		this.key = key;
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public String getKey(){
		return this.key;
	}
}