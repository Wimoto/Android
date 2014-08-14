package com.wimoto.model;

import java.math.BigInteger;
import java.util.Observable;

import android.bluetooth.BluetoothGattCharacteristic;

import com.marknicholas.wimoto.R;
import com.wimoto.bluetooth.BluetoothConnection;
import com.wimoto.bluetooth.BluetoothConnection.WimotoProfile;
import com.wimoto.utils.AppContext;

public class WaterSensor extends Sensor {
	
	private static String BLE_WATER_SERVICE_UUID_CONTACT	 			= "35D8C7DB-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static String BLE_WATER_CHAR_UUID_CONTACT_CURRENT 			= "35D8C7DC-9D78-43C2-AB2E-0E48CAC2DBDA";
	
	private static String BLE_WATER_SERVICE_UUID_LEVEL 					= "35D8C7DF-9D78-43C2-AB2E-0E48CAC2DBDA";
	private static String BLE_WATER_CHAR_UUID_LEVEL_CURRENT		 		= "35D8C7E0-9D78-43C2-AB2E-0E48CAC2DBDA";
	
	private float mContact;
	private float mLevel;
	
	public WaterSensor() {
		title = AppContext.getContext().getString(R.string.sensor_water);
	}

	public WaterSensor(BluetoothConnection connection) {
		super(connection);
	}
	
	public void setConnection(BluetoothConnection connection) {
		super.setConnection(connection);
		
		if (connection != null) {
			connection.enableChangesNotification(BLE_WATER_SERVICE_UUID_CONTACT, BLE_WATER_CHAR_UUID_CONTACT_CURRENT);
			connection.enableChangesNotification(BLE_WATER_SERVICE_UUID_LEVEL, BLE_WATER_CHAR_UUID_LEVEL_CURRENT);
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
			} else if (uuid.equals(BLE_WATER_CHAR_UUID_LEVEL_CURRENT)) {
				mLevel = bi.floatValue();	
			}
		}
		
		super.update(observable, data);
	}
}
