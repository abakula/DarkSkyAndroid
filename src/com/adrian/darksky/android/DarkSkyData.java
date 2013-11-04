package com.adrian.darksky.android;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.adrian.darksky.android.GetDarkSkyInfoTask.API_CALL_TYPE;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;

public class DarkSkyData {
	private TreeMap<Long, String> hourTimeToProb = new TreeMap<Long, String>();
	private TreeMap<Long, String> dayTimeToProb = new TreeMap<Long, String>();
	
	private HashMap<DarkSkyField, String> otherData = new HashMap<DarkSkyField, String>();
	
	private LocationInfo currentLocationInfo;
	
	public DarkSkyData(LocationInfo currentLocationInfo) throws JSONException {
		this.currentLocationInfo = currentLocationInfo; 
		
		JSONObject data = new JSONObject(loadData());
		extractData(data);
		
		setHourTimeToProb(data.getJSONArray(DarkSkyField.HOUR_PRECIPITATION.toString()));
		setDayTimeToProb(data.getJSONArray(DarkSkyField.DAY_PRECIPITATION.toString()));
	}
	
	public void extractData(JSONObject data) throws JSONException{
		for(DarkSkyField field : DarkSkyField.values()){
			Log.v("Data", field + ":" + data.getString(field.toString()));
			otherData.put(field, data.getString(field.toString()));
		}
	}
	
	private String loadData(){
    	String darkSkyText = "";
		try {
			darkSkyText = new GetDarkSkyInfoTask(API_CALL_TYPE.FORECAST).execute(currentLocationInfo).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return darkSkyText;
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
	
	public HashMap<DarkSkyField, String> getOtherData(){
		return otherData;
	}
}

