package com.adrian.darksky.android;

import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import org.achartengine.GraphicalView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

public class MainActivity extends Activity {
	
	public static String DARK_SKY_API_KEY = "e80103b48665176f4f4c1a9a26172539";

	private final boolean SHOW_VALS = false;
	
	private TreeMap<Long, String> hourTimeToProb = new TreeMap<Long, String>();

	private DarkSkyChartFactory darkSkyDayChart = new DarkSkyChartFactory(this, new LayoutParams(LayoutParams.WRAP_CONTENT, 200), "Day");
	private DarkSkyChartFactory darkSkyHourChart = new DarkSkyChartFactory(this, new LayoutParams(LayoutParams.WRAP_CONTENT, 200), "Hour");

	private JSONObject currentDarkSkyData;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	newDarkSkyData();
    }
    
    private void newDarkSkyData(){
    	View reg = getLayoutInflater().inflate(R.layout.activity_main, null);
    	
    	LocationInfo currentLocationInfo = initLocation();
    	currentDarkSkyData = loadData(currentLocationInfo);
    	
		LinearLayout layout = (LinearLayout) reg.findViewById(R.id.regular);
		layout.removeAllViews();
		
		try {
			TreeMap<Long, String> dayTimeToProb = generateDayPrecipData(currentDarkSkyData);
			layout.addView(darkSkyDayChart.getChart(dayTimeToProb));
		} catch (JSONException e) {
			Log.e("com.adrian.darksky.android.MainActivity", "JSON RESPONSE HAS CHANGED, CAN'T GENERATE GRAPHS");
		}
		try {
			TreeMap<Long, String> hourTimeToProb = generateHourPrecipData(currentDarkSkyData);
			layout.addView(darkSkyDayChart.getChart(hourTimeToProb));
		} catch (JSONException e) {
			Log.e("com.adrian.darksky.android.MainActivity", "JSON RESPONSE HAS CHANGED, CAN'T GENERATE GRAPHS");
		}
		
		setContentView(reg);
		
		TextView daySummaryTextView = (TextView) this.findViewById(R.id.daySummary);
		try {
			daySummaryTextView.setText(currentDarkSkyData.getString(DarkSkyField.DAY_SUMMARY.toString()));
		} catch (JSONException e) {
			Log.e("com.adrian.darksky.android.MainActivity", "JSON RESPONSE HAS CHANGED, COULDN'T MAKE CALL");
		}
		
    }
    
    private LocationInfo initLocation() {
    	//location library initializer
        LocationLibrary.initialiseLibrary(getBaseContext(), "com.adrian.darksky.android");
        LocationLibrary.forceLocationUpdate(getBaseContext());
        return new LocationInfo(getBaseContext());
	}
    
    private static TreeMap<Long, String> generateDayPrecipData(JSONObject darkSkyData) throws JSONException{
    	TreeMap<Long, String> ret = new TreeMap<Long, String>();
    	JSONArray dayPrecipData = darkSkyData.getJSONArray(DarkSkyField.DAY_PRECIPITATION.toString());
		for(int i = 0; i < dayPrecipData.length(); i++){
			Long curTime = Long.parseLong(dayPrecipData.getJSONObject(i).getString("time")) * 1000;
			String curProbability = dayPrecipData.getJSONObject(i).getString("probability");
			ret.put(curTime, curProbability);
		}
		return  ret;
    }
    
    private static TreeMap<Long, String> generateHourPrecipData(JSONObject darkSkyData) throws JSONException{
    	TreeMap<Long, String> ret = new TreeMap<Long, String>();
    	JSONArray dayPrecipData = darkSkyData.getJSONArray(DarkSkyField.HOUR_PRECIPITATION.toString());
		for(int i = 0; i < dayPrecipData.length(); i++){
			Long curTime = Long.parseLong(dayPrecipData.getJSONObject(i).getString("time")) * 1000;
			String curProbability = dayPrecipData.getJSONObject(i).getString("probability");
			ret.put(curTime, curProbability);
		}
		return  ret;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private JSONObject loadData(LocationInfo locationInfo){
    	String darkSkyText = "";
		try {
			darkSkyText = new GetDarkSkyInfoTask().execute(locationInfo.lastLat, locationInfo.lastLong).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	
		try {
			JSONObject darkSkyData = new JSONObject(darkSkyText);
			JSONArray hourPrecipData = darkSkyData.getJSONArray(DarkSkyField.HOUR_PRECIPITATION.toString());
			
			for (int i = 0; i < hourPrecipData.length(); i++) {
				Long curTime = Long.parseLong(hourPrecipData.getJSONObject(i).getString("time")) * 1000;
				String curProbability = hourPrecipData.getJSONObject(i).getString("probability");
				hourTimeToProb.put(curTime, curProbability);
			}
			
			if(SHOW_VALS){
				Log.d("com.adrian.darksky.android.MainActivity", darkSkyData.getString(DarkSkyField.CURRENT_SUMMARY.toString()));
				Log.d("com.adrian.darksky.android.MainActivity", darkSkyData.getString(DarkSkyField.CURRENT_TEMP.toString()));
				Log.d("com.adrian.darksky.android.MainActivity", darkSkyData.getString(DarkSkyField.DAY_SUMMARY.toString()));
				Log.d("com.adrian.darksky.android.MainActivity", darkSkyData.getString(DarkSkyField.MINUTES_UNTIL_CHANGE.toString()));
				Log.d("com.adrian.darksky.android.MainActivity", darkSkyData.getString(DarkSkyField.RADAR_STATION.toString()));
				Log.d("com.adrian.darksky.android.MainActivity", darkSkyData.getString(DarkSkyField.HOUR_SUMMARY.toString()));
				Log.d("com.adrian.darksky.android.MainActivity", darkSkyData.getString(DarkSkyField.CHECK_TIMEOUT.toString()));
				Log.d("com.adrian.darksky.android.MainActivity", String.valueOf(darkSkyData.getBoolean(DarkSkyField.IS_PRECIPITATING.toString())));
			}
			
			return darkSkyData;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
    }
}
