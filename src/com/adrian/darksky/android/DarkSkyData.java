package com.adrian.darksky.android;

import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DarkSkyData {
	private TreeMap<Long, String> hourTimeToProb = new TreeMap<Long, String>();
	private TreeMap<Long, String> dayTimeToProb = new TreeMap<Long, String>();
	
	private String daySummary;
	private String currentSummary;
	private String hourSummary;
	
	public DarkSkyData(String jsonData) {
		try {
			JSONObject data = new JSONObject(jsonData);
			
			setHourTimeToProb(data.getJSONArray(DarkSkyField.HOUR_PRECIPITATION.toString()));
			setDayTimeToProb(data.getJSONArray(DarkSkyField.DAY_PRECIPITATION.toString()));
			
			daySummary = data.getString(DarkSkyField.DAY_SUMMARY.toString());
			hourSummary = data.getString(DarkSkyField.HOUR_SUMMARY.toString());
			currentSummary = data.getString(DarkSkyField.CURRENT_SUMMARY.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private void setHourTimeToProb(JSONArray arr) throws JSONException{
		for(int i = 0; i < arr.length(); i++){
			Long curTime = Long.parseLong(arr.getJSONObject(i).getString("time")) * 1000;
			String curProbability = arr.getJSONObject(i).getString("probability");
			hourTimeToProb.put(curTime, curProbability);
		}
	}
	public TreeMap<Long, String> getHourTimeToProb() {
		return hourTimeToProb;
	}
	
	private void setDayTimeToProb(JSONArray arr) throws JSONException{
		for(int i = 0; i < arr.length(); i++){
			Long curTime = Long.parseLong(arr.getJSONObject(i).getString("time")) * 1000;
			String curProbability = arr.getJSONObject(i).getString("probability");
			dayTimeToProb.put(curTime, curProbability);
		}
	}
	public TreeMap<Long, String> getDayTimeToProb() {
		return dayTimeToProb;
	}

	public String getDaySummary() {
		return daySummary;
	}

	public String getCurrentSummary() {
		return currentSummary;
	}

	public String getHourSummary() {
		return hourSummary;
	}

}

