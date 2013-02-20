package com.adrian.darksky.android;

public enum DarkSkyField {
	HOUR_PRECIPITATION("hourPrecipitation"),
	DAY_PRECIPITATION("dayPrecipitation"),
	
	HOUR_SUMMARY("hourSummary"),
	DAY_SUMMARY("daySummary"),
	CURRENT_SUMMARY("currentSummary"),
	
	IS_PRECIPITATING("isPrecipitating"),
	MINUTES_UNTIL_CHANGE("minutesUntilChange"),
	CURRENT_TEMP("currentTemp"),
	TIMEZONE("timezone"),
	CHECK_TIMEOUT("checkTimeout"),
	RADAR_STATION("radarStation"),
	PROBABILITY("probability"),
	INTENSITY("intensity"),
	ERROR("error"),
	TEMP("temp"),
	TYPE("type"),
	TIME("time");
	
	private String fieldName;
	
	DarkSkyField(String fieldName){
		this.fieldName = fieldName;
	}
	
	public String toString(){
		return fieldName;
	}
}
