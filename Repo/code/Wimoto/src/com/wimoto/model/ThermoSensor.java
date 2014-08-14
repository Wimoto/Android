package com.wimoto.model;

import java.math.BigInteger;
import java.util.Observable;

import android.bluetooth.BluetoothGattCharacteristic;

import com.marknicholas.wimoto.R;
import com.wimoto.bluetooth.BluetoothConnection;
import com.wimoto.bluetooth.BluetoothConnection.WimotoProfile;
import com.wimoto.utils.AppContext;

public class ThermoSensor extends Sensor {

	private static String BLE_THERMO_SERVICE_UUID_TEMPERATURE 			= "497B8E4E-B61E-4F82-8FE9-B12CF2497338";
	private static String BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT 		= "497B8E4F-B61E-4F82-8FE9-B12CF2497338";
	
	private static String BLE_THERMO_SERVICE_UUID_PROBE 				= "497B8E54-B61E-4F82-8FE9-B12CF2497338";
	private static String BLE_THERMO_CHAR_UUID_PROBE_CURRENT		 	= "497B8E55-B61E-4F82-8FE9-B12CF2497338";
	
	private float mTemperature;
	private float mProbe;
	
	public ThermoSensor() {
		title = AppContext.getContext().getString(R.string.sensor_thermo);
	}

	public ThermoSensor(BluetoothConnection connection) {
		super(connection);
	}
	
	public void setConnection(BluetoothConnection connection) {
		super.setConnection(connection);
		
		if (connection != null) {
			connection.enableChangesNotification(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT);
			connection.enableChangesNotification(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_CURRENT);
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
			} else if (uuid.equals(BLE_THERMO_CHAR_UUID_PROBE_CURRENT)) {
				mProbe = bi.floatValue();		
			}
		}
		
		super.update(observable, data);
	}
}
