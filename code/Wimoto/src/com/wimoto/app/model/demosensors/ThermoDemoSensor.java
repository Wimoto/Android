package com.wimoto.app.model.demosensors;

import java.util.LinkedList;

import com.wimoto.app.R;
import com.wimoto.app.model.ThermoSensor;
import com.wimoto.app.utils.AppContext;

public class ThermoDemoSensor extends ThermoSensor {

	public static final String SENSOR_THERMO_DEMO	= "ThermoDemo";
	
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
	}
	
}
