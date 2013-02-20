package com.adrian.darksky.android;

public enum DarkSkyField {
	IS_PRECIPITATING("isPrecipitating"),
	MINUTES_UNTIL_CHANGE("minutesUntilChange"),
	CURRENT_SUMMARY("currentSummary"),
	HOUR_SUMMARY("hourSummary"),
	DAY_SUMMARY("daySummary"),
	CURRENT_TEMP("currentTemp"),
	TIMEZONE("timezone"),
	CHECK_TIMEOUT("checkTimeout"),
	RADAR_STATION("radarStation"),
	HOUR_PRECIPITATION("hourPrecipitation"),
	DAY_PRECIPITATION("dayPrecipitation"),
	PROBABILITY("probability"),
	INTENSITY("intensity"),
	ERROR("error"),
	TYPE("type"),
	TEMP("temp"),
	TIME("time");
	
	private String fieldName;
	
	DarkSkyField(String fieldName){
		this.fieldName = fieldName;
	}
	
	public String toString(){
		return fieldName;
	}
}
