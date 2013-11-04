package com.adrian.darksky.android;

import org.json.JSONException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

public class MainActivity extends Activity {
	private DarkSkyChartFactory darkSkyDayChart = new DarkSkyChartFactory(this, new LayoutParams(LayoutParams.WRAP_CONTENT, 200), "Day");
	private DarkSkyChartFactory darkSkyHourChart = new DarkSkyChartFactory(this, new LayoutParams(LayoutParams.WRAP_CONTENT, 200), "Hour");
	
	private UpdateUITask uiTask = new UpdateUITask();
	private LocationBroadcastReceiver locationReceiver = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        locationReceiver = new LocationBroadcastReceiver(getBaseContext(), uiTask);
        LocationLibrary.initialiseLibrary(getBaseContext(), this.getPackageName());  
    }
    
    @Override
    protected void onResume() {
    	super.onResume();

		registerReceiver(locationReceiver, new IntentFilter(LocationLibraryConstants.getLocationChangedPeriodicBroadcastAction()));
    	uiTask.doTask();
    } 
    
    protected void onPause() {
    	super.onPause();
    	
    	unregisterReceiver(locationReceiver);
    }
    
    public void setText(int textViewId, String text){
    	TextView daySummaryTextView = (TextView) findViewById(textViewId);
    	daySummaryTextView.setText(text);
    }
    
    private class UpdateUITask implements LocationTask{
    	@Override
    	public void doTask() {
    		View reg = getLayoutInflater().inflate(R.layout.activity_main, null);
    		
    		DarkSkyData currentDarkSkyData;
			try {
				currentDarkSkyData = new DarkSkyData(locationReceiver.getLocationInfo());
			} catch (JSONException e) {
				//We got bad JSON back, do not change the data.
				Log.e("JSON error", "We got poor json back: " + e.getStackTrace());
				return;
			}
    		
    		LinearLayout layout = (LinearLayout) reg.findViewById(R.id.regular);
    		layout.removeAllViews();
    		
    		layout.addView(darkSkyDayChart.getChart(currentDarkSkyData.getDayTimeToProb()));
    		layout.addView(darkSkyHourChart.getChart(currentDarkSkyData.getHourTimeToProb()));
    		
    		setContentView(reg);
    		
    		setText(R.id.daySummary, "Today: " + currentDarkSkyData.getOtherData().get(DarkSkyField.DAY_SUMMARY));
    		setText(R.id.hourSummary, "In the next hour: " +  currentDarkSkyData.getOtherData().get(DarkSkyField.HOUR_SUMMARY));
    		setText(R.id.currentSummary, "Currently: " +  currentDarkSkyData.getOtherData().get(DarkSkyField.CURRENT_SUMMARY));
    	}
    }
}
