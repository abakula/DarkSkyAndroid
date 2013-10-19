package com.adrian.darksky.android;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.dreams.DreamService;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class DarkSkyDaydream extends DreamService {
	
	@Override
	public void onDreamingStarted() {
		super.onDreamingStarted();
		
		setContentView(R.layout.daydream);
	}
	
	
}
