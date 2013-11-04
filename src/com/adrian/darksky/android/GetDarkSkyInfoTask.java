package com.adrian.darksky.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;

public class GetDarkSkyInfoTask extends AsyncTask<LocationInfo, Integer, String> {
	public static String DARK_SKY_API_KEY = "e80103b48665176f4f4c1a9a26172539";
	
	public enum API_CALL_TYPE{
		FORECAST("forecast"),
		BRIEF_FORECAST("brief_forecast");
		
		private String request;
		private API_CALL_TYPE(String request) {
			this.request = request;
		}
		public String toString(){
			return request;
		}
	}
	
	private API_CALL_TYPE call = API_CALL_TYPE.FORECAST; 
	
	public GetDarkSkyInfoTask(API_CALL_TYPE callType) {
		call = callType;
	}

	@Override
	protected String doInBackground(LocationInfo... params) {
		float latitude = params[0].lastLat;
		float longitude = params[0].lastLong;
		
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
		
		return baos.toString();
	}
	
	private String getLocationURL(float latitude, float longitude){
		Log.i("Api Call: ", "https://api.darkskyapp.com/v1/" + call.toString() + "/" + DARK_SKY_API_KEY + "/" + latitude + "," + longitude);
    	return "https://api.darkskyapp.com/v1/" + call.toString() + "/" + DARK_SKY_API_KEY + "/" + latitude + "," + longitude;
    }
}
