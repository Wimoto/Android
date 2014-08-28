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

public class SentrySensor extends Sensor {
	
	private static String BLE_SENTRY_SERVICE_UUID_ACCELEROMETER 			= "4209DC68-E433Ð4420-83D8-CDAACCD2E312";
	private static String BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT 		= "4209DC69-E433Ð4420-83D8-CDAACCD2E312";
	
	private static String BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED 			= "4209DC6D-E433Ð4420-83D8-CDAACCD2E312";
	private static String BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT	 	= "4209DC6E-E433Ð4420-83D8-CDAACCD2E312";
	
	public static final String SENTRY_ACCELEROMETER							= "SentryAccelerometer";
	public static final String SENTRY_INFARED								= "SentryInfared";
	
	private float mAccelerometer;
	private float mInfared;
	
	public SentrySensor() {
		super();
		
		title = AppContext.getContext().getString(R.string.sensor_sentry);
		
		mSensorValues.put(SENTRY_ACCELEROMETER, new LinkedList<Float>());
		mSensorValues.put(SENTRY_INFARED, new LinkedList<Float>());
	}

	public SentrySensor(BluetoothConnection connection) {
		this();
		
		setConnection(connection);
	}
	
	@Override
	public void setConnection(BluetoothConnection connection) {
		super.setConnection(connection);
		
		enableChangesNotification();
	}
	
	@Override
	public void setDocument(Document document) {
		super.setDocument(document);
		
		enableChangesNotification();		
	}

	private void enableChangesNotification() {
		if ((mConnection != null) && (mDocument != null)) {
			mConnection.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT);
			mConnection.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT);
		}
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.SENTRY;
	}
	
	public float getAccelerometer() {
		return mAccelerometer;
	}

	public float getInfared() {
		return mInfared;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT)) {
				mAccelerometer = bi.floatValue();
				
				addValue(SENTRY_ACCELEROMETER, mAccelerometer);
			} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT)) {
				mInfared = bi.floatValue();	
				
				addValue(SENTRY_INFARED, mInfared);
			}
		}
		
		super.update(observable, data);
	}
}
