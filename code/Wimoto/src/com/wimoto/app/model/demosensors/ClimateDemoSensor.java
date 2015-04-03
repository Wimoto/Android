package com.wimoto.app.model.demosensors;

import java.util.LinkedList;
import java.util.Random;

import android.os.Handler;

import com.wimoto.app.R;
import com.wimoto.app.model.ClimateSensor;
import com.wimoto.app.utils.AppContext;

public class ClimateDemoSensor extends ClimateSensor {

	public static final String SENSOR_CLIMATE_DEMO	= "ClimateDemo";
	
	private Handler mHandler;
	
	public ClimateDemoSensor() {
		mTitle = AppContext.getContext().getString(R.string.sensor_climate_demo);
		mId = SENSOR_CLIMATE_DEMO;
		
		mIsDemoSensor = true;
		
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
		notifyObservers(SENSOR_FIELD_CONNECTION, 0, 0);
		mRunnable.run();
	}
	
	public void stopDemo() {
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
			mTemperature += 2.0;
		} else if (mTemperature + temperatureStep > 50) {
			mTemperature -= 2.0;
		} else {
			mTemperature += temperatureStep;
		}
		
		notifyObservers(SENSOR_FIELD_CLIMATE_TEMPERATURE, mTemperature, mTemperature);

		int humidityStep = 2 - random.nextInt(5);
		if (mHumidity + humidityStep < 0) {
			mHumidity += 2.0;
		} else if (mHumidity + humidityStep > 100) {
			mHumidity -= 2.0;
		} else {
			mHumidity += humidityStep;
		}
		
		notifyObservers(SENSOR_FIELD_CLIMATE_HUMIDITY, mHumidity, mHumidity);
		
		int lightStep = 2 - random.nextInt(5);
		if (mLight + lightStep < 0) {
			mLight += 2.0;
		} else if (mLight + lightStep > 200) {
			mLight -= 2.0;
		} else {
			mLight += lightStep;
		}
		
		notifyObservers(SENSOR_FIELD_CLIMATE_LIGHT, mLight, mLight);
	}
}
