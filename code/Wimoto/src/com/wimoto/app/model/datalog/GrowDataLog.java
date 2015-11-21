package com.wimoto.app.model.datalog;

import org.json.JSONException;
import org.json.JSONObject;

public class GrowDataLog extends DataLog {

	private static final String DATALOG_TEMPERATURE		= "Temperature";
	private static final String DATALOG_LIGHT		 	= "Light";
	private static final String DATALOG_MOISTURE		= "Moiture";
	
	private int mRawSoilTemperature;
	private int mRawLight;
	private int mRawSoilMoisture;
	
	private float mSoilTemperature;
	private float mLight;
	private float mSoilMoisture;
	
	public GrowDataLog(byte[] data) {
		super(data);
		
		this.mRawSoilTemperature 	= ((data[8] & 0xff) << 8) | (data[9] & 0xff);
		
		this.mRawLight 				= ((data[10] & 0xff) << 8) | (data[11] & 0xff);
		//this.mLight 				= 0.96f * mRawLight;
		this.mLight 				= mRawLight;
		
		this.mRawSoilMoisture 		= ((data[12] & 0xff) << 8) | (data[13] & 0xff);
		this.mSoilMoisture			= mRawSoilMoisture;
	}
	
	public void setSoilTemperature(float soilTemperature) {
		this.mSoilTemperature = soilTemperature;
	}

	public int getRawSoilTemperature() {
		return mRawSoilTemperature;
	}
	
	public JSONObject getJSONObject() {
		JSONObject object = super.getJSONObject();
		
		try {
			object.put(DATALOG_TEMPERATURE, mSoilTemperature);
			object.put(DATALOG_LIGHT, mLight);
			object.put(DATALOG_MOISTURE, mSoilMoisture);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return object;
	}
}
