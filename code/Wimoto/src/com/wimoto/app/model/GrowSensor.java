package com.wimoto.app.model;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Observable;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.couchbase.lite.Document;
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
	
	public static final String GROW_LIGHT							= "GrowLight";
	public static final String GROW_MOISTURE						= "GrowMoisture";
	public static final String GROW_TEMPERATURE						= "GrowTemperature";
	
	private float mLight;
	private float mMoisture;
	private float mTemperature;
	
	public GrowSensor() {
		super();
		
		title = AppContext.getContext().getString(R.string.sensor_grow);
		
		mSensorValues.put(GROW_LIGHT, new LinkedList<Float>());
		mSensorValues.put(GROW_MOISTURE, new LinkedList<Float>());
		mSensorValues.put(GROW_TEMPERATURE, new LinkedList<Float>());
	}

	public GrowSensor(BluetoothConnection connection) {
		this();
		
		setConnection(connection);
	}
	
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
			mConnection.enableChangesNotification(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_CURRENT);
			mConnection.enableChangesNotification(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_CURRENT);
			mConnection.enableChangesNotification(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT);
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
				
				addValue(GROW_LIGHT, mLight);
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_CURRENT)) {
				mMoisture = bi.floatValue();
				
				addValue(GROW_MOISTURE, mMoisture);
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT)) {
				mTemperature = bi.floatValue();	
				
				Log.e("GROW", "Soil Temperature: " + mTemperature);
				
				addValue(GROW_TEMPERATURE, mTemperature);				
			}
		}
		
		super.update(observable, data);
	}
}
