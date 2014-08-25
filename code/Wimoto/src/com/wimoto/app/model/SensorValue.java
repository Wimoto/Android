package com.wimoto.app.model;

import java.util.Date;

public class SensorValue {

	private String mSensorId;
	private String mType;
	private float mValue;
	private Date mCreatedAt;
	
	public SensorValue(String sensorId, String type, float value) {
		mSensorId 	= sensorId;
		mType 		= type;
		mValue 		= value;
		mCreatedAt 	= new Date();
	}
}
