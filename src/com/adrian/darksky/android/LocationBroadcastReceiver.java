package com.adrian.darksky.android;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationBroadcastReceiver extends BroadcastReceiver {
	private LocationInfo _locationInfo;
	private LocationTask task;
	
	public LocationBroadcastReceiver(Context context) {
		this(context, null);
	}
	
	public LocationBroadcastReceiver(Context context, LocationTask task){
        LocationLibrary.initialiseLibrary(context, "com.adrian.darksky.android");  
        _locationInfo = new LocationInfo(context);
        this.task = task;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("DarkSky Broadcasts", intent.getAction());
		if(intent.getAction().equals(LocationLibraryConstants.getLocationChangedPeriodicBroadcastAction())){
			Log.i("DarkSky", "Location Changed broadcast found!");
			_locationInfo.refresh(context);
			if(task != null)
				task.doTask();
		}
	}

	public LocationInfo getLocationInfo(){
		return _locationInfo;
	}
	
}
