package com.wimoto.app.model;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Observable;

import android.bluetooth.BluetoothGattCharacteristic;

import com.couchbase.lite.Document;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.BluetoothConnection;
import com.wimoto.app.utils.AppContext;

public class SentrySensor extends Sensor {
	
	public static final String SENSOR_FIELD_SENTRY_ACCELEROMETER					= "SentryAccelerometer";
	public static final String SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_SET			= "SentryAccelerometerAlarmSet";
	public static final String SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_CLEAR		= "SentryAccelerometerAlarmClear";
	
	public static final String SENSOR_FIELD_SENTRY_PASSIVE_INFRARED					= "SentryInfrared";
	public static final String SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_SET		= "SentryInfraredAlarmSet";
	
	private static final String BLE_SENTRY_SERVICE_UUID_ACCELEROMETER 				= "4209DC68-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT 			= "4209DC69-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_SET		= "4209DC6A-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_CLEAR		= "4209DC6B-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM			= "4209DC6C-E433-4420-83D8-CDAACCD2E312";
	
	private static final String BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED 			= "4209DC6D-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT		= "4209DC6E-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM_SET		= "4209DC6F-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM			= "4209DC70-E433-4420-83D8-CDAACCD2E312";
	
	private float mAccelerometer;
	private float mInfrared;
	
	private boolean mAccelerometerAlarmSet;
	private boolean mAccelerometerAlarmClear;
	
	private boolean mInfraredAlarmSet;
	
	public SentrySensor() {
		super();
		
		mTitle = AppContext.getContext().getString(R.string.sensor_sentry);
		
		mSensorValues.put(SENSOR_FIELD_SENTRY_ACCELEROMETER, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED, new LinkedList<Float>());
	}

	public SentrySensor(BluetoothConnection connection) {
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
			mConnection.readCharacteristic(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_SET);
			mConnection.readCharacteristic(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_CLEAR);	
			mConnection.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM);
			mConnection.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT);
			
			mConnection.readCharacteristic(BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM_SET);
			mConnection.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM);
			mConnection.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT);
		}
	}
	
	public SensorProfile getType() {
		return SensorProfile.SENTRY;
	}
	
	public float getAccelerometer() {
		return mAccelerometer;
	}
	
	public void setAccelerometer(float accelerometer) {
		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER, mAccelerometer, accelerometer);

		mAccelerometer = accelerometer;
		addValue(SENSOR_FIELD_SENTRY_ACCELEROMETER, mAccelerometer);
	}

	public float getInfrared() {
		return mInfrared;
	}
	
	public void setInfrared(float infrared) {		
		notifyObservers(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED, mInfrared, infrared);

		mInfrared = infrared;
		addValue(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED, mInfrared);
	}
	
	public boolean isAccelerometerAlarmSet() {
		return mAccelerometerAlarmSet;
	}

	public void setAccelerometerAlarmSet(boolean accelerometerAlarmSet) {
		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_SET, mAccelerometerAlarmSet, accelerometerAlarmSet);
		
		mAccelerometerAlarmSet = accelerometerAlarmSet;
		enableAlarm(mAccelerometerAlarmSet, BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_SET);
	}
	
	public boolean isAccelerometerAlarmClear() {
		return mAccelerometerAlarmClear;
	}

	public void setAccelerometerAlarmClear(boolean accelerometerAlarmClear) {
		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_CLEAR, mAccelerometerAlarmClear, accelerometerAlarmClear);
		
		mAccelerometerAlarmClear = accelerometerAlarmClear;
		enableAlarm(mAccelerometerAlarmClear, BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_CLEAR);
	}
	
	public boolean isInfraredAlarmSet() {
		return mInfraredAlarmSet;
	}

	public void setInfraredAlarmSet(boolean infraredAlarmSet) {
		notifyObservers(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_SET, mInfraredAlarmSet, infraredAlarmSet);
		
		mInfraredAlarmSet = infraredAlarmSet;
		enableAlarm(mInfraredAlarmSet, BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM_SET);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT)) {
				setAccelerometer(bi.floatValue());
			} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT)) {
				setInfrared(bi.floatValue());
			} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_SET)) {
				setAccelerometerAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_CLEAR)) {
				setAccelerometerAlarmClear((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM_SET)) {
				setInfraredAlarmSet((bi.floatValue() == 0) ? false:true);
			} 
		}
		
		super.update(observable, data);
	}
}
