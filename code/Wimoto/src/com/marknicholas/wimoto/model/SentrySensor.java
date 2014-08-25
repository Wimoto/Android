package com.marknicholas.wimoto.model;

import java.math.BigInteger;
import java.util.Observable;

import android.bluetooth.BluetoothGattCharacteristic;

import com.wimoto.app.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection.WimotoProfile;
import com.marknicholas.wimoto.utils.AppContext;

public class SentrySensor extends Sensor {
	
	private static String BLE_SENTRY_SERVICE_UUID_ACCELEROMETER 			= "4209DC68-E433Ð4420-83D8-CDAACCD2E312";
	private static String BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT 		= "4209DC69-E433Ð4420-83D8-CDAACCD2E312";
	
	private static String BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED 			= "4209DC6D-E433Ð4420-83D8-CDAACCD2E312";
	private static String BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT	 	= "4209DC6E-E433Ð4420-83D8-CDAACCD2E312";
	
	private float mTemperature;
	private float mHumidity;
	
	public SentrySensor() {
		title = AppContext.getContext().getString(R.string.sensor_sentry);
	}

	public SentrySensor(BluetoothConnection connection) {
		super(connection);
	}
	
	public void setConnection(BluetoothConnection connection) {
		super.setConnection(connection);
		
		if (connection != null) {
			connection.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT);
			connection.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT);
		}
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.SENTRY;
	}
	
	public float getTemperature() {
		return mTemperature;
	}

	public float getHumidity() {
		return mHumidity;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT)) {
				mTemperature = bi.floatValue();				
			} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT)) {
				mHumidity = bi.floatValue();	
			}
		}
		
		super.update(observable, data);
	}
}
