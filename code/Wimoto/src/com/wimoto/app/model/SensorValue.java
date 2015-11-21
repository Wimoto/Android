package com.wimoto.app.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.couchbase.lite.Document;
import com.wimoto.app.model.sensors.ClimateSensor;
import com.wimoto.app.model.sensors.GrowSensor;

public class SensorValue extends CBEntity {

	public static final String CB_DOCUMENT_SENSOR_VALUE_SENSOR_ID		= "sensor_id";
	public static final String CB_DOCUMENT_SENSOR_VALUE_VALUE			= "value";
	public static final String CB_DOCUMENT_SENSOR_VALUE_CREATED_AT		= "created_at";
	
	private static final String JSON_KEY_PARAMETER 	= "Parameter";
	private static final String JSON_KEY_VALUE		= "Value";
	private static final String JSON_KEY_DATE		= "Date";

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
	
	public JSONObject getJSONObject() {
		String parameter = "";
		
		if (mType.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE)) {
			parameter = "Temperature";
		} else if (mType.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY)) {
			parameter = "Humidity";
		} else if (mType.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT)) {
			parameter = "Light";
		} else if (mType.equals(GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE)) {
			parameter = "Temperature";
		} else if (mType.equals(GrowSensor.SENSOR_FIELD_GROW_MOISTURE)) {
			parameter = "Moisture";
		} else if (mType.equals(GrowSensor.SENSOR_FIELD_GROW_LIGHT)) {
			parameter = "Light";
		} 
		
		JSONObject object = new JSONObject();
		
		try {
			object.put(JSON_KEY_PARAMETER, parameter);
			object.put(JSON_KEY_VALUE, String.format(Locale.US, "%.2f", mValue));
			object.put(JSON_KEY_DATE, getCreatedAt());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return object;
	}
}
