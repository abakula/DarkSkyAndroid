package com.adrian.darksky.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class GetDarkSkyInfoTask extends AsyncTask<Float, Integer, String> {
	public static String DARK_SKY_API_KEY = MainActivity.DARK_SKY_API_KEY;
	
	private enum API_CALL{
		FORECAST("forecast"),
		BRIEF_FORECAST("brief_forecast");
		
		private String request;
		
		private API_CALL(String request) {
			this.request = request;
		}
		
		public String toString(){
			return request;
		}
	}
	
	private API_CALL call = API_CALL.FORECAST; 
	
	public GetDarkSkyInfoTask(){
	}
	
	public GetDarkSkyInfoTask(boolean brief) {
		if(brief)
			call = API_CALL.BRIEF_FORECAST;
	}

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
	
	private String getLocationURL(float latitude, float longitude){
    	return "https://api.darkskyapp.com/v1/" + call.toString() + "/" + DARK_SKY_API_KEY + "/" + latitude + "," + longitude;
    }
}
