package com.wimoto.app.model.demosensors;

import java.util.LinkedList;

import com.wimoto.app.R;
import com.wimoto.app.model.ClimateSensor;
import com.wimoto.app.utils.AppContext;

public class ClimateDemoSensor extends ClimateSensor {

	public static final String SENSOR_CLIMATE_DEMO	= "ClimateDemo";
	
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
	}
}
