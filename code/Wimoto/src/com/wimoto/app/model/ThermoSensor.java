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

public class ThermoSensor extends Sensor {

	private static String BLE_THERMO_SERVICE_UUID_TEMPERATURE 			= "497B8E4E-B61E-4F82-8FE9-B12CF2497338";
	private static String BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT 		= "497B8E4F-B61E-4F82-8FE9-B12CF2497338";
	
	private static String BLE_THERMO_SERVICE_UUID_PROBE 				= "497B8E54-B61E-4F82-8FE9-B12CF2497338";
	private static String BLE_THERMO_CHAR_UUID_PROBE_CURRENT		 	= "497B8E55-B61E-4F82-8FE9-B12CF2497338";
	
	public static final String THERMO_TEMPERATURE						= "ThermoTemprature";
	public static final String THERMO_PROBE								= "ThermoProbe";
	
	private float mTemperature;
	private float mProbe;
	
	public ThermoSensor() {
		super();

		title = AppContext.getContext().getString(R.string.sensor_thermo);
		
		mSensorValues.put(THERMO_TEMPERATURE, new LinkedList<Float>());
		mSensorValues.put(THERMO_PROBE, new LinkedList<Float>());
	}

	public ThermoSensor(BluetoothConnection connection) {
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

	@Override
	protected void enableChangesNotification() {
		super.enableChangesNotification();
		
		if ((mConnection != null) && (mDocument != null)) {
			mConnection.enableChangesNotification(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT);
			mConnection.enableChangesNotification(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_CURRENT);
		}
	}	
	
	public WimotoProfile getType() {
		return WimotoProfile.THERMO;
	}
	
	public float getTemperature() {
		return mTemperature;
	}

	public float getProbe() {
		return mProbe;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT)) {
				mTemperature = bi.floatValue();			
				
				addValue(THERMO_TEMPERATURE, mTemperature);
			} else if (uuid.equals(BLE_THERMO_CHAR_UUID_PROBE_CURRENT)) {
				mProbe = bi.floatValue();		
				
				addValue(THERMO_PROBE, mProbe);
			}
		}
		
		super.update(observable, data);
	}
}
