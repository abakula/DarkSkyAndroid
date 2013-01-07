package com.adrian.darksky.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Service;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static String DARK_SKY_API_KEY = "e80103b48665176f4f4c1a9a26172539";
	private static final ThreadLocal<SimpleDateFormat> displayFormat = new ThreadLocal<SimpleDateFormat>() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm", Locale.US);
		@Override
		public SimpleDateFormat get() {
			return dateFormat;
		}
	};
	
	LocationManager locationManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        locationManager = (LocationManager) this.getSystemService(Service.LOCATION_SERVICE);
        LocationLibrary.initialiseLibrary(getBaseContext(), "com.adrian.darksky.android");
        LocationLibrary.forceLocationUpdate(getBaseContext());
        
        LocationInfo latestInfo = new LocationInfo(getBaseContext());

        loadData(latestInfo.lastLat, latestInfo.lastLong);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void loadData(float latitude, float longitude){
    	
    	TextView textView = (TextView) findViewById(R.id.textview);
    	
    	String darkSkyText = "";
		try {
			darkSkyText = new GetDarkSkyInfoTask().execute(latitude, longitude).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	
		StringBuffer outputText = new StringBuffer( "Lat: " + latitude + " | Long: " + longitude + "\n");
		JSONObject darkSkyData = null;
		try {
			darkSkyData = new JSONObject(darkSkyText);
			JSONArray precipData = darkSkyData.getJSONArray("dayPrecipitation");
			
			for(int i = 0; i < precipData.length(); i++){
				Long curTime = Long.parseLong(precipData.getJSONObject(i).getString("time")) * 1000;
				String curProbability = precipData.getJSONObject(i).getString("probability");
				outputText.append(displayFormat.get().format( new Date(curTime)) + " : " + curProbability + "\n");
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
    	textView.setText( outputText );
    	
    }
    
    public class GetDarkSkyInfoTask extends AsyncTask<Float, Integer, String>{
		@Override
		protected String doInBackground(Float... params) {
			float latitude = params[0];
			float longitude = params[1];
			
		   	HttpClient client = new DefaultHttpClient();
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	try {
				HttpResponse response = client.execute(new HttpGet(getLocationURL(latitude, longitude)));
				response.getEntity().writeTo(baos);
				baos.close();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
			
			// TODO Auto-generated method stub
			return baos.toString();
		}
    	
    }
    
    public String getLocationURL(float latitude, float longitude){
    	return "https://api.darkskyapp.com/v1/forecast/" + DARK_SKY_API_KEY + "/" + latitude + "," + longitude;
    }
    
}
