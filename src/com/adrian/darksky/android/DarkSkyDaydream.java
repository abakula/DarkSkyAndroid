package com.adrian.darksky.android;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.dreams.DreamService;
import android.util.Log;
import android.widget.TextView;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class DarkSkyDaydream extends DreamService {
	private LocationBroadcastReceiver locationReceiver = null;
	
	final ScheduledExecutorService stp = Executors.newScheduledThreadPool(1);
	
	private Handler h = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			TextView daydreamText = (TextView)findViewById(R.id.daydream_textview);
			daydreamText.setText(msg.getData().getString("message"));
			
			stp.schedule(new DaydreamHandler(), msg.getData().getInt("timeout"), TimeUnit.SECONDS);
			
		}
	};
	
	@Override
	public void onDreamingStarted() {
		super.onDreamingStarted();
		
		setContentView(R.layout.daydream);

		locationReceiver = new LocationBroadcastReceiver(getBaseContext());
		
		Message m = new Message();
		Bundle b = new Bundle();
		b.putInt("timeout", 0);
		m.setData(b);
        h.sendMessage(m);
	}
	
	private class DaydreamHandler implements Runnable{
		@Override
		public void run() {
			try {
				LocationInfo locationInfo = locationReceiver.getLocationInfo();
				HashMap<DarkSkyField, String> otherData = new DarkSkyData(locationInfo).getOtherData();
				
				Integer currentTimeout = Integer.parseInt(otherData.get(DarkSkyField.CHECK_TIMEOUT));
				Log.d("Updater", "Will be done in " + currentTimeout);

				String currentSummary = otherData.get(DarkSkyField.CURRENT_SUMMARY);
				Log.i("CURRENT SUMMARY", currentSummary);
				
				Message m = new Message();
				Bundle b = new Bundle();
				b.putInt("timeout", currentTimeout);
				b.putString("message", currentSummary);
				m.setData(b);
				
				h.sendMessage(m);
			} catch (JSONException e) {
			}
		}
    }
}
