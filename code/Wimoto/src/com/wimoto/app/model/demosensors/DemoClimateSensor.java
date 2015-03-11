package com.wimoto.app.model.demosensors;

import java.util.LinkedList;

import com.wimoto.app.R;
import com.wimoto.app.model.ClimateSensor;
import com.wimoto.app.utils.AppContext;

public class DemoClimateSensor extends ClimateSensor {

	public DemoClimateSensor() {
		mTitle = AppContext.getContext().getString(R.string.sensor_climate_demo);
		
		mTemperature = 22.0f;
        mHumidity = 50.0f;
        mLight = 60.0f;
        
        mTemperatureAlarmLow = 15;
        mTemperatureAlarmHigh = 28;
        
        mHumidityAlarmLow = 37;
        mHumidityAlarmHigh = 69;
        
        mLightAlarmLow = 49;
        mLightAlarmHigh = 72;
		
		mSensorValues.put(SENSOR_FIELD_CLIMATE_TEMPERATURE, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_CLIMATE_HUMIDITY, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_CLIMATE_LIGHT, new LinkedList<Float>());
	}
	
}
