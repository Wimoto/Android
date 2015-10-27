package com.wimoto.app.model.demosensors;

import java.util.LinkedList;
import java.util.Random;

import android.os.Handler;

import com.wimoto.app.AppContext;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice.State;
import com.wimoto.app.model.ThermoSensor;

public class ThermoDemoSensor extends ThermoSensor {

	public static final String SENSOR_THERMO_DEMO		= "ThermoDemo";
	public static final String SENSOR_THERMO_DEMO_ID	= "XX:00:YY:99:ZZ:88";
	
	private Handler mHandler;
	
	public ThermoDemoSensor(AppContext context) {
		super(context);
		
		mTitle = mContext.getString(R.string.sensor_thermo_demo);
		mId = SENSOR_THERMO_DEMO;
		
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
		
		int probeStep = 2 - random.nextInt(5);
		if (mProbe + probeStep < -5) {
			setProbe(mProbe + 2.0f);
		} else if (mProbe + probeStep > 70) {
			setProbe(mProbe - 2.0f);
		} else {
			setProbe(mProbe + probeStep);
		}		
	}
	
}
