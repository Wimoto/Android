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

public class ClimateSensor extends Sensor {

	public static final String OBSERVER_FIELD_CLIMATE_SENSOR_TEMPERATURE		= "mConnection";
	
	public static final String BLE_CLIMATE_SERVICE_UUID_TEMPERATURE 			= "E0035608-EC48-4ED0-9F3B-5419C00A94FD";
	public static final String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT 		= "E0035609-EC48-4ED0-9F3B-5419C00A94FD";
	public static final String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW		= "E003560A-EC48-4ED0-9F3B-5419C00A94FD";
	public static final String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH		= "E003560B-EC48-4ED0-9F3B-5419C00A94FD";
	public static final String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_SET		= "E003560C-EC48-4ED0-9F3B-5419C00A94FD";
	public static final String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM			= "E003560D-EC48-4ED0-9F3B-5419C00A94FD";
	
	public static String BLE_CLIMATE_SERVICE_UUID_LIGHT 				= "E003560E-EC48-4ED0-9F3B-5419C00A94FD";
	public static String BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT		 	= "E003560F-EC48-4ED0-9F3B-5419C00A94FD";
	public static String BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_LOW			= "E0035610-EC48-4ED0-9F3B-5419C00A94FD";
	public static String BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_HIGH		= "E0035611-EC48-4ED0-9F3B-5419C00A94FD";
	public static String BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_SET			= "E0035612-EC48-4ED0-9F3B-5419C00A94FD";
	public static String BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM				= "E0035613-EC48-4ED0-9F3B-5419C00A94FD";
	
	public static String BLE_CLIMATE_SERVICE_UUID_HUMIDITY 			= "E0035614-EC48-4ED0-9F3B-5419C00A94FD";
	public static String BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT	 	= "E0035615-EC48-4ED0-9F3B-5419C00A94FD";
	public static String BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_LOW		= "E0035616-EC48-4ED0-9F3B-5419C00A94FD";
	public static String BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_HIGH		= "E0035617-EC48-4ED0-9F3B-5419C00A94FD";
	public static String BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_SET		= "E0035618-EC48-4ED0-9F3B-5419C00A94FD";
	public static String BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM			= "E0035619-EC48-4ED0-9F3B-5419C00A94FD";

	public static final String CLIMATE_TEMPERATURE						= "ClimateTemperature";
	public static final String CLIMATE_LIGHT							= "ClimateLight";
	public static final String CLIMATE_HUMIDITY							= "ClimateHumidity";
		
	private float mTemperature;
	private float mLight;
	private float mHumidity;
	
	private boolean mTemperatureAlarmSet;
	private int mTemperatureAlarmLow;
	private int mTemperatureAlarmHigh;
	
	public ClimateSensor() {
		super();
		
		mTitle = AppContext.getContext().getString(R.string.sensor_climate);
		
		mSensorValues.put(CLIMATE_TEMPERATURE, new LinkedList<Float>());
		mSensorValues.put(CLIMATE_LIGHT, new LinkedList<Float>());
		mSensorValues.put(CLIMATE_HUMIDITY, new LinkedList<Float>());
	}

	public ClimateSensor(BluetoothConnection connection) {
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
			mConnection.readCharacteristic(BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_SET);
			mConnection.readCharacteristic(BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW);
			mConnection.readCharacteristic(BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH);			
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM);
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT);
			
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT);
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT);
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

	public int getTemperatureAlarmLow() {
		return mTemperatureAlarmLow;
	}

	public void setTemperatureAlarmLow(int temperatureAlarmLow) {
		mTemperatureAlarmLow = temperatureAlarmLow;
	}

	public int getTemperatureAlarmHigh() {
		return mTemperatureAlarmHigh;
	}

	public void setTemperatureAlarmHigh(int temperatureAlarmHigh) {
		mTemperatureAlarmHigh = temperatureAlarmHigh;
	}
	
	public boolean isTemperatureAlarmSet() {
		return mTemperatureAlarmSet;
	}

	public void setTemperatureAlarmSet(boolean temperatureAlarmSet) {
		mTemperatureAlarmSet = temperatureAlarmSet;
	}

	public void enableTemperatureAlarm(boolean doEnable) {
		enableAlarm(doEnable, BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_SET);
	}

	public void enableHumidityAlarm(boolean doEnable) {
		enableAlarm(doEnable, BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_SET);
	}

	public void enableLightAlarm(boolean doEnable) {
		enableAlarm(doEnable, BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_SET);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {			
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT)) {
				mTemperature = (float)(-46.85 + (175.72*bi.floatValue()/65536));
				
				addValue(CLIMATE_TEMPERATURE, mTemperature);
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT)) {
				mLight = (float)(0.96 * bi.floatValue());
				
				addValue(CLIMATE_LIGHT, mLight);
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT)) {
				mHumidity = (float)(-6.0 + (125.0*bi.floatValue()/65536)) * (-1);	
				
				addValue(CLIMATE_HUMIDITY, mHumidity);
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_SET)) {
				mTemperatureAlarmSet = (bi.floatValue() == 0) ? false:true;
				Log.e("", "uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_SET) " + bi.floatValue());
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW)) {
				mTemperatureAlarmLow = Float.valueOf(bi.floatValue()/100.0f).intValue();
				Log.e("", "uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW) " + bi.floatValue());
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH)) {
				Log.e("", "uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH)");
				mTemperatureAlarmHigh = Float.valueOf(bi.floatValue()/100.0f).intValue();
			}
		}
		
		super.update(observable, data);
	}
}
