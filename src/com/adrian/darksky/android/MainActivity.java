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
    	DarkSkyData currentDarkSkyData = new DarkSkyData(loadData(currentLocationInfo));
    	
		LinearLayout layout = (LinearLayout) reg.findViewById(R.id.regular);
		layout.removeAllViews();
		
		layout.addView(darkSkyDayChart.getChart(currentDarkSkyData.getDayTimeToProb()));
		layout.addView(darkSkyHourChart.getChart(currentDarkSkyData.getHourTimeToProb()));
		
		setContentView(reg);
		
		setText(R.id.daySummary, "Today: " + currentDarkSkyData.getDaySummary());
		setText(R.id.hourSummary, "In the next hour: " + currentDarkSkyData.getHourSummary());
		setText(R.id.currentSummary, "Currently: " + currentDarkSkyData.getCurrentSummary());
    }
    
    public void setText(int textViewId, String text){
    	TextView daySummaryTextView = (TextView) findViewById(textViewId);
    	daySummaryTextView.setText(text);
    }
    
    private LocationInfo initLocation() {
    	//location library initializer
        LocationLibrary.initialiseLibrary(getBaseContext(), "com.adrian.darksky.android");
        LocationLibrary.forceLocationUpdate(getBaseContext());
        return new LocationInfo(getBaseContext());
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private String loadData(LocationInfo locationInfo){
    	String darkSkyText = "";
		try {
			darkSkyText = new GetDarkSkyInfoTask().execute(locationInfo.lastLat, locationInfo.lastLong).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return darkSkyText;
    }
}
