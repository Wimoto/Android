package com.wimoto.app.model;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Observable;

import android.bluetooth.BluetoothGattCharacteristic;

import com.couchbase.lite.Document;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.BluetoothConnection;
import com.wimoto.app.bluetooth.BluetoothConnection.WimotoProfile;
import com.wimoto.app.utils.AppContext;

public class WaterSensor extends Sensor {
	
	public static final String SENSOR_FIELD_WATER_CONTACT					= "WaterContact";
	public static final String SENSOR_FIELD_WATER_CONTACT_ALARM_SET			= "WaterContactAlarmSet";
	
	public static final String SENSOR_FIELD_WATER_LEVEL						= "WaterLevel";
	public static final String SENSOR_FIELD_WATER_LEVEL_ALARM_SET			= "WaterLevelAlarmSet";
	public static final String SENSOR_FIELD_WATER_LEVEL_ALARM_LOW			= "WaterLevelAlarmLow";
	public static final String SENSOR_FIELD_WATER_LEVEL_ALARM_HIGH			= "WaterLevelAlarmHigh";
	
	private static final String BLE_WATER_SERVICE_UUID_CONTACT	 			= "35D8C7DB-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_CONTACT_CURRENT 		= "35D8C7DC-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_CONTACT_ALARM_SET		= "35D8C7DD-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_CONTACT_ALARM			= "35D8C7DE-9D78-43C2-AB2E-0E48CAC2DBDA";
	
	private static final String BLE_WATER_SERVICE_UUID_LEVEL 				= "35D8C7DF-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_LEVEL_CURRENT		 	= "35D8C7E0-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_LEVEL_ALARM_LOW			= "35D8C7E1-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_LEVEL_ALARM_HIGH		= "35D8C7E2-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_LEVEL_ALARM_SET			= "35D8C7E3-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_LEVEL_ALARM				= "35D8C7E4-9D78-43C2-AB2E-0E48CAC2DBDA";
	
	private float mContact;
	private float mLevel;
	
	private boolean mContactAlarmSet;

	private boolean mLevelAlarmSet;
	private int mLevelAlarmLow;
	private int mLevelAlarmHigh;
	
	public WaterSensor() {
		super();
		
		mTitle = AppContext.getContext().getString(R.string.sensor_water);
		
		mSensorValues.put(SENSOR_FIELD_WATER_CONTACT, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_WATER_LEVEL, new LinkedList<Float>());
	}

	public WaterSensor(BluetoothConnection connection) {
		this();
		
		setConnection(connection);
	}
	
	@Override
	public void setConnection(BluetoothConnection connection) {
		super.setConnection(connection);
		
		initiateSensorCharacteristics();
	}
	
	@Override
	public void setDocument(Document document) {
		super.setDocument(document);
		
		initiateSensorCharacteristics();		
	}

	@Override
	protected void initiateSensorCharacteristics() {
		super.initiateSensorCharacteristics();
		
		if ((mConnection != null) && (mDocument != null)) {
			mConnection.readCharacteristic(BLE_WATER_SERVICE_UUID_CONTACT, BLE_WATER_CHAR_UUID_CONTACT_ALARM_SET);
			mConnection.enableChangesNotification(BLE_WATER_SERVICE_UUID_CONTACT, BLE_WATER_CHAR_UUID_CONTACT_ALARM);
			mConnection.enableChangesNotification(BLE_WATER_SERVICE_UUID_CONTACT, BLE_WATER_CHAR_UUID_CONTACT_CURRENT);
			
			mConnection.readCharacteristic(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_ALARM_SET);
			mConnection.readCharacteristic(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_ALARM_LOW);
			mConnection.readCharacteristic(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_ALARM_HIGH);
			mConnection.enableChangesNotification(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_ALARM);
			mConnection.enableChangesNotification(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_CURRENT);
		}
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.WATER;
	}
	
	public float getContact() {
		return mContact;
	}
	
	public void setContact(float contact) {
		notifyObservers(SENSOR_FIELD_WATER_CONTACT, mContact, contact);

		mContact = contact;
		addValue(SENSOR_FIELD_WATER_CONTACT, mContact);
	}

	public float getLevel() {
		return mLevel;
	}
	
	public void setLevel(float level) {		
		notifyObservers(SENSOR_FIELD_WATER_LEVEL, mLevel, level);

		mLevel = level;
		addValue(SENSOR_FIELD_WATER_LEVEL, mLevel);
	}
	
	public boolean isContactAlarmSet() {
		return mContactAlarmSet;
	}
	
	public void setContactAlarmSet(boolean contactAlarmSet) {
		notifyObservers(SENSOR_FIELD_WATER_CONTACT_ALARM_SET, mContactAlarmSet, contactAlarmSet);
		
		mContactAlarmSet = contactAlarmSet;
		enableAlarm(mContactAlarmSet, BLE_WATER_SERVICE_UUID_CONTACT, BLE_WATER_CHAR_UUID_CONTACT_ALARM_SET);
	}
	
	public boolean isLevelAlarmSet() {
		return mLevelAlarmSet;
	}

	public void setLevelAlarmSet(boolean levelAlarmSet) {
		notifyObservers(SENSOR_FIELD_WATER_LEVEL_ALARM_SET, mLevelAlarmSet, levelAlarmSet);
		
		mLevelAlarmSet = levelAlarmSet;
		enableAlarm(mLevelAlarmSet, BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_ALARM_SET);
	}
	
	public int getLevelAlarmLow() {
		return mLevelAlarmLow;
	}

	public void setLevelAlarmLow(int levelAlarmLow) {
		notifyObservers(SENSOR_FIELD_WATER_LEVEL_ALARM_LOW, mLevelAlarmLow, levelAlarmLow);
		
		mLevelAlarmLow = levelAlarmLow;
		writeAlarmValue(mLevelAlarmLow, WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_LOW, WaterSensor.BLE_WATER_CHAR_UUID_LEVEL_ALARM_LOW);	
	}

	public int getLevelAlarmHigh() {
		return mLevelAlarmHigh;
	}

	public void setLevelAlarmHigh(int levelAlarmHigh) {
		notifyObservers(SENSOR_FIELD_WATER_LEVEL_ALARM_HIGH, mLevelAlarmHigh, levelAlarmHigh);
		
		mLevelAlarmHigh = levelAlarmHigh;
		writeAlarmValue(mLevelAlarmHigh, WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_HIGH, WaterSensor.BLE_WATER_CHAR_UUID_LEVEL_ALARM_HIGH);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_WATER_CHAR_UUID_CONTACT_CURRENT)) {
				setContact(bi.floatValue());
			} else if (uuid.equals(BLE_WATER_CHAR_UUID_LEVEL_CURRENT)) {
				setLevel(bi.floatValue());
			} else if (uuid.equals(BLE_WATER_CHAR_UUID_CONTACT_ALARM_SET)) {
				setContactAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_WATER_CHAR_UUID_LEVEL_ALARM_SET)) {
				setLevelAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_WATER_CHAR_UUID_LEVEL_ALARM_LOW)) {
				setLevelAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_WATER_CHAR_UUID_LEVEL_ALARM_HIGH)) {
				setLevelAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue());
			}
		}
		
		super.update(observable, data);
	}
}
