package com.wimoto.app.model;

import java.math.BigInteger;
import java.util.Observable;

import android.bluetooth.BluetoothGattCharacteristic;

import com.wimoto.app.R;
import com.wimoto.app.bluetooth.BluetoothConnection;
import com.wimoto.app.bluetooth.BluetoothConnection.WimotoProfile;
import com.wimoto.app.utils.AppContext;

public class GrowSensor extends Sensor {
	
	private static String BLE_GROW_SERVICE_UUID_LIGHT		 		= "DAF4470C-BFB0-4DD8-9293-62AF5F545E31";
	private static String BLE_GROW_CHAR_UUID_LIGHT_CURRENT 			= "DAF4470D-BFB0-4DD8-9293-62AF5F545E31";
	
	private static String BLE_GROW_SERVICE_UUID_MOISTURE 			= "DAF44712-BFB0-4DD8-9293-62AF5F545E31";
	private static String BLE_GROW_CHAR_UUID_MOISTURE_CURRENT	 	= "DAF44713-BFB0-4DD8-9293-62AF5F545E31";

	private static String BLE_GROW_SERVICE_UUID_TEMPERATURE 		= "DAF44706-BFB0-4DD8-9293-62AF5F545E31";
	private static String BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT	= "DAF44707-BFB0-4DD8-9293-62AF5F545E31";
	
	private float mLight;
	private float mMoisture;
	private float mTemperature;
	
	public GrowSensor() {
		title = AppContext.getContext().getString(R.string.sensor_grow);
	}

	public GrowSensor(BluetoothConnection connection) {
		super(connection);
	}
	
	public void setConnection(BluetoothConnection connection) {
		super.setConnection(connection);
		
		if (connection != null) {
			connection.enableChangesNotification(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_CURRENT);
			connection.enableChangesNotification(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_CURRENT);
			connection.enableChangesNotification(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT);
		}
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.GROW;
	}
	
	public float getLight() {
		return mLight;
	}
	
	public float getMoisture() {
		return mMoisture;
	}
	
	public float getTemperature() {
		return mTemperature;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_GROW_CHAR_UUID_LIGHT_CURRENT)) {
				mLight = bi.floatValue();		
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_CURRENT)) {
				mMoisture = bi.floatValue();	
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT)) {
				mTemperature = bi.floatValue();	
			}
		}
		
		super.update(observable, data);
	}
}
