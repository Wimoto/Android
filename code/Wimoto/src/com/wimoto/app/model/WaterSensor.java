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
	
	private static String BLE_WATER_SERVICE_UUID_CONTACT	 			= "35D8C7DB-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static String BLE_WATER_CHAR_UUID_CONTACT_CURRENT 			= "35D8C7DC-9D78-43C2-AB2E-0E48CAC2DBDA";
	
	private static String BLE_WATER_SERVICE_UUID_LEVEL 					= "35D8C7DF-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static String BLE_WATER_CHAR_UUID_LEVEL_CURRENT		 		= "35D8C7E0-9D78-43C2-AB2E-0E48CAC2DBDA";
	
	public static final String WATER_CONTACT							= "WaterContact";
	public static final String WATER_LEVEL								= "WaterLevel";
	
	private float mContact;
	private float mLevel;
	
	public WaterSensor() {
		super();
		
		mTitle = AppContext.getContext().getString(R.string.sensor_water);
		
		mSensorValues.put(WATER_CONTACT, new LinkedList<Float>());
		mSensorValues.put(WATER_CONTACT, new LinkedList<Float>());
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
			mConnection.enableChangesNotification(BLE_WATER_SERVICE_UUID_CONTACT, BLE_WATER_CHAR_UUID_CONTACT_CURRENT);
			mConnection.enableChangesNotification(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_CURRENT);
		}
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.WATER;
	}
	
	public float getContact() {
		return mContact;
	}

	public float getLevel() {
		return mLevel;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_WATER_CHAR_UUID_CONTACT_CURRENT)) {
				mContact = bi.floatValue();			
				
				addValue(WATER_CONTACT, mContact);
			} else if (uuid.equals(BLE_WATER_CHAR_UUID_LEVEL_CURRENT)) {
				mLevel = bi.floatValue();	
				
				addValue(WATER_LEVEL, mLevel);
			}
		}
		
		super.update(observable, data);
	}
}
