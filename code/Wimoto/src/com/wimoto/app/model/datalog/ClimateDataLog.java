package com.wimoto.app.model.datalog;

import org.json.JSONException;
import org.json.JSONObject;

public class ClimateDataLog extends DataLog {

	private static final String DATALOG_TEMPERATURE		= "Temperature";
	private static final String DATALOG_LIGHT		 	= "Light";
	private static final String DATALOG_HUMIDITY		= "Humidity";
	
	private int mRawTemperature;
	private int mRawLight;
	private int mRawHumidity;
	
	private float mTemperature;
	private float mLight;
	private float mHumidity;
	
	public ClimateDataLog(byte[] data) {
		super(data);
		
		this.mRawTemperature 	= ((data[8] & 0xff) << 8) | (data[9] & 0xff);
		
		this.mRawLight 			= ((data[10] & 0xff) << 8) | (data[11] & 0xff);
		this.mLight 			= 0.96f * mRawLight;
		
		this.mRawHumidity 		= ((data[12] & 0xff) << 8) | (data[13] & 0xff);
	}
	
	public void setTemperature(float temperature) {
		this.mTemperature = temperature;
	}

	public int getRawTemperature() {
		return mRawTemperature;
	}
	
	public void setHumidity(float humidity) {
		this.mHumidity = humidity;
	}
	
	public int getRawHumidity() {
		return mRawHumidity;
	}

	public JSONObject getJSONObject() {
		JSONObject object = super.getJSONObject();
		
		try {
			object.put(DATALOG_TEMPERATURE, mTemperature);
			object.put(DATALOG_LIGHT, mLight);
			object.put(DATALOG_HUMIDITY, mHumidity);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return object;
	}
}
