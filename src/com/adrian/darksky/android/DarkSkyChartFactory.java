package com.adrian.darksky.android;

import java.util.Date;
import java.util.TreeMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;

public class DarkSkyChartFactory {
	private Context context;
	private LayoutParams layoutParams;
	private String title;
	
	private XYMultipleSeriesDataset darkSkyDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer darkSkyRenderer = new XYMultipleSeriesRenderer();

	public DarkSkyChartFactory(Context context, LayoutParams layoutParams, String title) {
		this.context = context;
		this.layoutParams = layoutParams;
		this.title = title;
		initChart();
	}
	
	private void initChart(){
    	darkSkyRenderer.setYAxisMax(1);
    	darkSkyRenderer.setShowLegend(false);
    	darkSkyRenderer.setShowGrid(true);
    	darkSkyRenderer.setClickEnabled(false);
    	darkSkyRenderer.setExternalZoomEnabled(false);
    	darkSkyRenderer.setPanEnabled(false);
    	darkSkyRenderer.setZoomEnabled(false);
    	darkSkyRenderer.setZoomEnabled(false, false);
    	darkSkyRenderer.setChartTitle(title);
    	
    	darkSkyRenderer.setYLabelsPadding(15f);
    	darkSkyRenderer.setLabelsTextSize(16f);
    	
    	darkSkyRenderer.setLabelsColor(Color.BLACK);
    	darkSkyRenderer.setYLabelsColor(0, Color.BLACK);
    	darkSkyRenderer.setXLabelsColor(Color.BLACK);
    	darkSkyRenderer.setAxesColor(Color.BLACK);
    	
    	XYSeriesRenderer currentRenderer = new XYSeriesRenderer();
    	darkSkyRenderer.addSeriesRenderer(currentRenderer);
    }
	
	private TimeSeries chartTimeSeries(TreeMap<Long, String> timeToProb){
		TimeSeries ret = new TimeSeries(title);
    	for(Long time : timeToProb.keySet()){
    		ret.add(new Date(time), Double.valueOf(timeToProb.get(time)));
    	}
    	return ret;
    }
	
	public GraphicalView getChart(TreeMap<Long, String> timeToProb){
		GraphicalView ret;
		
		for(int i = 0; i < darkSkyDataset.getSeries().length; i++){
			darkSkyDataset.removeSeries(0);
		}
		darkSkyDataset.addSeries(chartTimeSeries(timeToProb));
		
		ret = ChartFactory.getTimeChartView(context, darkSkyDataset, darkSkyRenderer, "hh:mma");
		ret.setLayoutParams(layoutParams);
		return ret;
	}
}
