package com.wimoto.app.model.sensors;

import java.math.BigInteger;
import java.util.LinkedList;

import android.bluetooth.BluetoothGattCharacteristic;

import com.wimoto.app.AppContext;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice;
import com.wimoto.app.bluetooth.WimotoDevice.State;

public class WaterSensor extends Sensor {
	
	public static final String SENSOR_FIELD_WATER_CONTACT					= "WaterContact";
	public static final String SENSOR_FIELD_WATER_CONTACT_ALARM_SET			= "WaterContactAlarmSet";
	
	public static final String SENSOR_FIELD_WATER_LEVEL						= "WaterLevel";
	public static final String SENSOR_FIELD_WATER_LEVEL_ALARM_SET			= "WaterLevelAlarmSet";
	public static final String SENSOR_FIELD_WATER_LEVEL_ALARM_LOW			= "WaterLevelAlarmLow";
	public static final String SENSOR_FIELD_WATER_LEVEL_ALARM_HIGH			= "WaterLevelAlarmHigh";
	
	public static final String BLE_WATER_AD_SERVICE_UUID_CONTACT		 	= "0000C7DB-0000-1000-8000-00805F9B34FB";
	
	private static final String BLE_WATER_SERVICE_UUID_CONTACT	 			= "35D8C7DB-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_CONTACT_CURRENT 		= "35D8C7DC-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_CONTACT_ALARM_SET		= "35D8C7DD-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static final String BLE_WATER_CHAR_UUID_CONTACT_ALARM			= "35D8C7DE-9D78-43C2-AB2E-0E48CAC2DBDA";
	
	public static final String BLE_WATER_AD_SERVICE_UUID_LEVEL			 	= "0000C7DF-0000-1000-8000-00805F9B34FB";
	
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
	private float mLevelAlarmLow;
	private float mLevelAlarmHigh;
	
	public WaterSensor(AppContext context) {
		super(context);
		
		mTitle = mContext.getString(R.string.sensor_water);
		
		mSensorValues.put(SENSOR_FIELD_WATER_CONTACT, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_WATER_LEVEL, new LinkedList<Float>());
	}

	public WimotoDevice.Profile getProfile() {
		return WimotoDevice.Profile.WATER;
	}
	
	public String getCodename() {
		return "Water";
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
	
	public float getLevelAlarmLow() {
		return mLevelAlarmLow;
	}

	public void setLevelAlarmLow(float levelAlarmLow, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_WATER_LEVEL_ALARM_LOW, mLevelAlarmLow, levelAlarmLow);
		
		mLevelAlarmLow = levelAlarmLow;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mLevelAlarmLow).intValue(), WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_LOW, WaterSensor.BLE_WATER_CHAR_UUID_LEVEL_ALARM_LOW);
		}
	}

	public float getLevelAlarmHigh() {
		return mLevelAlarmHigh;
	}

	public void setLevelAlarmHigh(float levelAlarmHigh, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_WATER_LEVEL_ALARM_HIGH, mLevelAlarmHigh, levelAlarmHigh);
		
		mLevelAlarmHigh = levelAlarmHigh;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mLevelAlarmHigh).intValue(), WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_HIGH, WaterSensor.BLE_WATER_CHAR_UUID_LEVEL_ALARM_HIGH);
		}
	}
	
	// WimotoDeviceCallback
	@Override
	public void onConnectionStateChange(State state) {
		super.onConnectionStateChange(state);

		if (state == State.CONNECTED) {
			mWimotoDevice.readCharacteristic(BLE_WATER_SERVICE_UUID_CONTACT, BLE_WATER_CHAR_UUID_CONTACT_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_WATER_SERVICE_UUID_CONTACT, BLE_WATER_CHAR_UUID_CONTACT_ALARM_SET);
			mWimotoDevice.readCharacteristic(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_ALARM_SET);
			mWimotoDevice.readCharacteristic(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_ALARM_LOW);
			mWimotoDevice.readCharacteristic(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_ALARM_HIGH);

			mWimotoDevice.enableChangesNotification(BLE_WATER_SERVICE_UUID_CONTACT, BLE_WATER_CHAR_UUID_CONTACT_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_WATER_SERVICE_UUID_CONTACT, BLE_WATER_CHAR_UUID_CONTACT_CURRENT);
			
			mWimotoDevice.enableChangesNotification(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_CURRENT);			
		}
	}

	@Override
	public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
		super.onCharacteristicChanged(characteristic);
		
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
			setLevelAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		} else if (uuid.equals(BLE_WATER_CHAR_UUID_LEVEL_ALARM_HIGH)) {
			setLevelAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		}
	}
}
