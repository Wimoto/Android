package com.wimoto.app.model.demosensors;

import java.util.LinkedList;
import java.util.Random;

import android.os.Handler;

import com.wimoto.app.R;
import com.wimoto.app.model.ThermoSensor;
import com.wimoto.app.utils.AppContext;

public class ThermoDemoSensor extends ThermoSensor {

	public static final String SENSOR_THERMO_DEMO	= "ThermoDemo";
	
	private Handler mHandler;
	
	public ThermoDemoSensor() {
		mTitle = AppContext.getContext().getString(R.string.sensor_thermo_demo);
		mId = SENSOR_THERMO_DEMO;
		
		mIsDemoSensor = true;
		
		mTemperature = 22.0f;
        mProbe = 30.0f;
        
        mTemperatureAlarmLow = 11.2f;
        mTemperatureAlarmHigh = 34.7f;
        
        mProbeAlarmLow = 23.9f;
        mProbeAlarmHigh = 35.5f;
		
        mSensorValues.put(SENSOR_FIELD_THERMO_TEMPERATURE, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_THERMO_PROBE, new LinkedList<Float>());
		
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
		
		notifyObservers(SENSOR_FIELD_THERMO_TEMPERATURE, mTemperature, mTemperature);

		int probeStep = 2 - random.nextInt(5);
		if (mProbe + probeStep < -5) {
			mProbe += 2.0;
		} else if (mProbe + probeStep > 70) {
			mProbe -= 2.0;
		} else {
			mProbe += probeStep;
		}
		
		notifyObservers(SENSOR_FIELD_THERMO_PROBE, mProbe, mProbe);
	}
	
}
