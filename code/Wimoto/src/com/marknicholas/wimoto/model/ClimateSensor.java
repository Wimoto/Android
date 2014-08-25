package com.marknicholas.wimoto.model;

import java.math.BigInteger;
import java.util.Observable;

import android.bluetooth.BluetoothGattCharacteristic;

import com.wimoto.app.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection.WimotoProfile;
import com.marknicholas.wimoto.utils.AppContext;

public class ClimateSensor extends Sensor {

	private static String BLE_CLIMATE_SERVICE_UUID_TEMPERATURE 			= "E0035608-EC48-4ED0-9F3B-5419C00A94FD";
	private static String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT 	= "E0035609-EC48-4ED0-9F3B-5419C00A94FD";
	
	private static String BLE_CLIMATE_SERVICE_UUID_LIGHT 				= "E003560E-EC48-4ED0-9F3B-5419C00A94FD";
	private static String BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT		 	= "E003560F-EC48-4ED0-9F3B-5419C00A94FD";

	private static String BLE_CLIMATE_SERVICE_UUID_HUMIDITY 			= "E0035614-EC48-4ED0-9F3B-5419C00A94FD";
	private static String BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT	 	= "E0035615-EC48-4ED0-9F3B-5419C00A94FD";
	
	private float mTemperature;
	private float mLight;
	private float mHumidity;
	
	public ClimateSensor() {
		title = AppContext.getContext().getString(R.string.sensor_climate);
	}

	public ClimateSensor(BluetoothConnection connection) {
		super(connection);		
	}
	
	public void setConnection(BluetoothConnection connection) {
		super.setConnection(connection);
		
		if (connection != null) {
			connection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT);
			connection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT);
			connection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT);
		}
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.CLIMATE;
	}
	
	public float getTemperature() {
		return mTemperature;
	}

	public float getLight() {
		return mLight;
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
			if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT)) {
				mTemperature = (float)(-46.85 + (175.72*bi.floatValue()/65536));				
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT)) {
				mLight = (float)(0.96 * bi.floatValue());	
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT)) {
				mHumidity = (float)(-6.0 + (125.0*bi.floatValue()/65536)) * (-1);	
			}
		}
		
		super.update(observable, data);
	}
}
