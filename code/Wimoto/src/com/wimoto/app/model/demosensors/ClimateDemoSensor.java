package com.wimoto.app.model.demosensors;

import java.util.LinkedList;
import java.util.Random;

import android.os.Handler;

import com.wimoto.app.AppContext;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice.State;
import com.wimoto.app.model.ClimateSensor;

public class ClimateDemoSensor extends ClimateSensor {

	public static final String SENSOR_CLIMATE_DEMO		= "ClimateDemo";
	public static final String SENSOR_CLIMATE_DEMO_ID	= "AA:11:BB:22:CC:33";
	
	private Handler mHandler;
	
	public ClimateDemoSensor(AppContext context) {
		super(context);
		
		mTitle = mContext.getString(R.string.sensor_climate_demo);
		mId = SENSOR_CLIMATE_DEMO;
		
		mTemperature = 22.0f;
        mHumidity = 50.0f;
        mLight = 60.0f;
        
        mTemperatureAlarmLow = 15.0f;
        mTemperatureAlarmHigh = 28.0f;
        
        mHumidityAlarmLow = 37.0f;
        mHumidityAlarmHigh = 69.0f;
        
        mLightAlarmLow = 49.0f;
        mLightAlarmHigh = 72.0f;
		
		mSensorValues.put(SENSOR_FIELD_CLIMATE_TEMPERATURE, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_CLIMATE_HUMIDITY, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_CLIMATE_LIGHT, new LinkedList<Float>());
		
		mHandler = new Handler();
	}

	public void runDemo() {
		notifyObservers(SENSOR_FIELD_STATE, State.DISCONNECTED, State.CONNECTED);
		mRunnable.run();
	}
	
	public void stopDemo() {
		notifyObservers(SENSOR_FIELD_STATE, State.CONNECTED, State.DISCONNECTED);
		mHandler.removeCallbacks(mRunnable);
	}

	private Runnable mRunnable = new Runnable() 
	{
	    public void run() 
	    {
	    	demoUpdate();
	        mHandler.postDelayed(this, 2000);
	    }
	};
	
	public void demoUpdate() {
		Random random = new Random(); 
		
		int temperatureStep = 2 - random.nextInt(5);
		if (mTemperature + temperatureStep < -5) {
			setTemperature(mTemperature + 2.0f);
		} else if (mTemperature + temperatureStep > 50) {
			setTemperature(mTemperature - 2.0f);
		} else {
			setTemperature(mTemperature + temperatureStep);
		}
		
		int humidityStep = 2 - random.nextInt(5);
		if (mHumidity + humidityStep < 0) {
			setHumidity(mHumidity + 2.0f);
		} else if (mHumidity + humidityStep > 100) {
			setHumidity(mHumidity - 2.0f);
		} else {
			setHumidity(mHumidity + humidityStep);
		}
				
		int lightStep = 2 - random.nextInt(5);
		if (mLight + lightStep < 0) {
			setLight(mLight + 2.0f);
		} else if (mLight + lightStep > 200) {
			setLight(mLight - 2.0f);
		} else {
			setLight(mLight + lightStep);
		}
	}
}
