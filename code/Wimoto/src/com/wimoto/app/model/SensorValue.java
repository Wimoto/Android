package com.wimoto.app.model;

import java.util.HashMap;
import java.util.Map;

import com.couchbase.lite.Document;

public class SensorValue extends CBEntity {

	public static final String CB_DOCUMENT_SENSOR_VALUE_SENSOR_ID		= "sensor_id";
	public static final String CB_DOCUMENT_SENSOR_VALUE_VALUE			= "value";
	public static final String CB_DOCUMENT_SENSOR_VALUE_CREATED_AT		= "created_at";

	private String mSensorId;
	private String mType;
	private Float mValue;
	
	public SensorValue(Document doc) {
		super(doc);
		
		mSensorId  	= getString(CB_DOCUMENT_SENSOR_VALUE_SENSOR_ID);
		mType 		= getString(CB_DOCUMENT_TYPE);	
		mValue 		= getFloat(CB_DOCUMENT_SENSOR_VALUE_VALUE);				
	}

	public void save() {
		Map<String, Object> properties = new HashMap<String, Object>();
		
		properties.put(CB_DOCUMENT_SENSOR_VALUE_SENSOR_ID, mSensorId);
		properties.put(CB_DOCUMENT_TYPE, mType);
		properties.put(CB_DOCUMENT_SENSOR_VALUE_VALUE, mValue.toString());
		properties.put(CB_DOCUMENT_SENSOR_VALUE_CREATED_AT, getCreatedAt());
		
		try {
			mDocument.putProperties(properties);
		} catch (Exception e) {
		}
	}
	
	public void setSensorId(String sensorId) {
		this.mSensorId = sensorId;
	}

	public void setType(String type) {
		this.mType = type;
	}

	public Float getValue() {
		return mValue;
	}
	
	public void setValue(float value) {
		this.mValue = Float.valueOf(value);
	}	
}
