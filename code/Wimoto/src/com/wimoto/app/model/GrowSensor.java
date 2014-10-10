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

public class GrowSensor extends Sensor {

	public static final String SENSOR_FIELD_GROW_LIGHT						= "GrowLight";
	public static final String SENSOR_FIELD_GROW_LIGHT_ALARM_SET			= "GrowLightAlarmSet";
	public static final String SENSOR_FIELD_GROW_LIGHT_ALARM_LOW			= "GrowLightAlarmLow";
	public static final String SENSOR_FIELD_GROW_LIGHT_ALARM_HIGH			= "GrowLightAlarmHigh";
	
	public static final String SENSOR_FIELD_GROW_MOISTURE					= "GrowMoisture";
	public static final String SENSOR_FIELD_GROW_MOISTURE_ALARM_SET			= "GrowMoistureAlarmSet";
	public static final String SENSOR_FIELD_GROW_MOISTURE_ALARM_LOW			= "GrowMoistureAlarmLow";
	public static final String SENSOR_FIELD_GROW_MOISTURE_ALARM_HIGH		= "GrowMoistureAlarmHigh";

	public static final String SENSOR_FIELD_GROW_TEMPERATURE				= "GrowTemperature";
	public static final String SENSOR_FIELD_GROW_TEMPERATURE_ALARM_SET		= "GrowTemperatureAlarmSet";
	public static final String SENSOR_FIELD_GROW_TEMPERATURE_ALARM_LOW		= "GrowTemperatureAlarmLow";
	public static final String SENSOR_FIELD_GROW_TEMPERATURE_ALARM_HIGH		= "GrowTemperatureAlarmHigh";
	
	private static final String BLE_GROW_SERVICE_UUID_LIGHT		 			= "DAF4470C-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_LIGHT_CURRENT 			= "DAF4470D-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_LIGHT_ALARM_LOW			= "DAF4470E-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_LIGHT_ALARM_HIGH			= "DAF4470F-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_LIGHT_ALARM_SET			= "DAF44710-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_LIGHT_ALARM				= "DAF44711-BFB0-4DD8-9293-62AF5F545E31";
	
	private static final String BLE_GROW_SERVICE_UUID_MOISTURE 				= "DAF44712-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_MOISTURE_CURRENT	 		= "DAF44713-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW		= "DAF44714-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH		= "DAF44715-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_MOISTURE_ALARM_SET		= "DAF44716-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_MOISTURE_ALARM			= "DAF44717-BFB0-4DD8-9293-62AF5F545E31";
	
	private static final String BLE_GROW_SERVICE_UUID_TEMPERATURE 			= "DAF44706-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT		= "DAF44707-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_LOW	= "DAF44708-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_HIGH	= "DAF44709-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_SET	= "DAF4470A-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM		= "DAF4470B-BFB0-4DD8-9293-62AF5F545E31";

	private float mLight;
	private float mMoisture;
	private float mTemperature;
	
	private boolean mLightAlarmSet;
	private int mLightAlarmLow;
	private int mLightAlarmHigh;

	private boolean mMoistureAlarmSet;
	private int mMoistureAlarmLow;
	private int mMoistureAlarmHigh;

	private boolean mTemperatureAlarmSet;
	private int mTemperatureAlarmLow;
	private int mTemperatureAlarmHigh;
	
	public GrowSensor() {
		super();
		
		mTitle = AppContext.getContext().getString(R.string.sensor_grow);
		
		mSensorValues.put(SENSOR_FIELD_GROW_LIGHT, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_GROW_MOISTURE, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_GROW_TEMPERATURE, new LinkedList<Float>());
	}

	public GrowSensor(BluetoothConnection connection) {
		this();
		
		setConnection(connection);
	}
	
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
			mConnection.readCharacteristic(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_ALARM_SET);
			mConnection.readCharacteristic(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_ALARM_LOW);
			mConnection.readCharacteristic(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_ALARM_HIGH);			
			mConnection.enableChangesNotification(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_ALARM);
			mConnection.enableChangesNotification(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_CURRENT);
			
			mConnection.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_SET);
			mConnection.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW);
			mConnection.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH);			
			mConnection.enableChangesNotification(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM);
			mConnection.enableChangesNotification(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_CURRENT);
			
			mConnection.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_SET);
			mConnection.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_LOW);
			mConnection.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_HIGH);			
			mConnection.enableChangesNotification(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM);
			mConnection.enableChangesNotification(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT);
		}
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.GROW;
	}
	
	public float getLight() {
		return mLight;
	}
	
	public void setLight(float light) {		
		notifyObservers(SENSOR_FIELD_GROW_LIGHT, mLight, light);

		mLight = light;
		addValue(SENSOR_FIELD_GROW_LIGHT, mLight);
	}
	
	public float getMoisture() {
		return mMoisture;
	}
	
	public void setMoisture(float moisture) {		
		notifyObservers(SENSOR_FIELD_GROW_MOISTURE, mMoisture, moisture);

		mMoisture = moisture;
		addValue(SENSOR_FIELD_GROW_MOISTURE, mMoisture);
	}
	
	public float getTemperature() {
		return mTemperature;
	}
	
	public void setTemperature(float temperature) {		
		notifyObservers(SENSOR_FIELD_GROW_TEMPERATURE, mTemperature, temperature);

		mTemperature = temperature;
		addValue(SENSOR_FIELD_GROW_TEMPERATURE, mTemperature);
	}
	
	public boolean isLightAlarmSet() {
		return mLightAlarmSet;
	}

	public void setLightAlarmSet(boolean lightAlarmSet) {
		notifyObservers(SENSOR_FIELD_GROW_LIGHT_ALARM_SET, mLightAlarmSet, lightAlarmSet);
		
		mLightAlarmSet = lightAlarmSet;
		enableAlarm(mLightAlarmSet, BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_ALARM_SET);
	}

	public int getLightAlarmLow() {
		return mLightAlarmLow;
	}

	public void setLightAlarmLow(int lightAlarmLow) {
		notifyObservers(SENSOR_FIELD_GROW_LIGHT_ALARM_LOW, mLightAlarmLow, lightAlarmLow);
		
		mLightAlarmLow = lightAlarmLow;
		writeAlarmValue(mLightAlarmLow, GrowSensor.BLE_GROW_SERVICE_UUID_LIGHT, GrowSensor.BLE_GROW_CHAR_UUID_LIGHT_ALARM_LOW);
	}

	public int getLightAlarmHigh() {
		return mLightAlarmHigh;
	}

	public void setLightAlarmHigh(int lightAlarmHigh) {
		notifyObservers(SENSOR_FIELD_GROW_LIGHT_ALARM_HIGH, mLightAlarmHigh, lightAlarmHigh);
		
		mLightAlarmHigh = lightAlarmHigh;
		writeAlarmValue(mLightAlarmHigh, GrowSensor.BLE_GROW_SERVICE_UUID_LIGHT, GrowSensor.BLE_GROW_CHAR_UUID_LIGHT_ALARM_HIGH);
	}
	
	public boolean isMoistureAlarmSet() {
		return mMoistureAlarmSet;
	}

	public void setMoistureAlarmSet(boolean moistureAlarmSet) {
		notifyObservers(SENSOR_FIELD_GROW_MOISTURE_ALARM_SET, mMoistureAlarmSet, moistureAlarmSet);
		
		mMoistureAlarmSet = moistureAlarmSet;
		enableAlarm(mMoistureAlarmSet, BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_SET);
	}

	public int getMoistureAlarmLow() {
		return mMoistureAlarmLow;
	}

	public void setMoistureAlarmLow(int moistureAlarmLow) {
		notifyObservers(SENSOR_FIELD_GROW_MOISTURE_ALARM_LOW, mMoistureAlarmLow, moistureAlarmLow);
		
		mMoistureAlarmLow = moistureAlarmLow;
		writeAlarmValue(mMoistureAlarmLow, GrowSensor.BLE_GROW_SERVICE_UUID_MOISTURE, GrowSensor.BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW);
	}

	public int getMoistureAlarmHigh() {
		return mMoistureAlarmHigh;
	}

	public void setMoistureAlarmHigh(int moistureAlarmHigh) {
		notifyObservers(SENSOR_FIELD_GROW_MOISTURE_ALARM_HIGH, mMoistureAlarmHigh, moistureAlarmHigh);
		
		mMoistureAlarmHigh = moistureAlarmHigh;
		writeAlarmValue(mMoistureAlarmHigh, GrowSensor.BLE_GROW_SERVICE_UUID_MOISTURE, GrowSensor.BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH);
	}
	
	public boolean isTemperatureAlarmSet() {
		return mTemperatureAlarmSet;
	}

	public void setTemperatureAlarmSet(boolean temperatureAlarmSet) {
		notifyObservers(SENSOR_FIELD_GROW_TEMPERATURE_ALARM_SET, mTemperatureAlarmSet, temperatureAlarmSet);
		
		mTemperatureAlarmSet = temperatureAlarmSet;
		enableAlarm(temperatureAlarmSet, BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_SET);
	}

	public int getTemperatureAlarmLow() {
		return mTemperatureAlarmLow;
	}

	public void setTemperatureAlarmLow(int temperatureAlarmLow) {
		notifyObservers(SENSOR_FIELD_GROW_TEMPERATURE_ALARM_LOW, mTemperatureAlarmLow, temperatureAlarmLow);
		
		mTemperatureAlarmLow = temperatureAlarmLow;
		writeAlarmValue(mTemperatureAlarmLow, GrowSensor.BLE_GROW_SERVICE_UUID_TEMPERATURE, GrowSensor.BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_LOW);
	}

	public int getTemperatureAlarmHigh() {
		return mTemperatureAlarmHigh;
	}

	public void setTemperatureAlarmHigh(int temperatureAlarmHigh) {
		notifyObservers(SENSOR_FIELD_GROW_TEMPERATURE_ALARM_HIGH, mTemperatureAlarmLow, temperatureAlarmHigh);
		
		mTemperatureAlarmLow = temperatureAlarmHigh;
		writeAlarmValue(temperatureAlarmHigh, GrowSensor.BLE_GROW_SERVICE_UUID_TEMPERATURE, GrowSensor.BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_HIGH);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {			
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_GROW_CHAR_UUID_LIGHT_CURRENT)) {
				setLight(bi.floatValue());				
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_CURRENT)) {
				setMoisture(bi.floatValue());				
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT)) {
				setTemperature(bi.floatValue());			
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_LIGHT_ALARM_SET)) {
				setLightAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_LIGHT_ALARM_LOW)) {
				setLightAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_LIGHT_ALARM_HIGH)) {
				setLightAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_ALARM_SET)) {
				setMoistureAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW)) {
				setMoistureAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH)) {
				setMoistureAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_SET)) {
				setTemperatureAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_LOW)) {
				setTemperatureAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_HIGH)) {
				setTemperatureAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue());
			}
		}
		
		super.update(observable, data);
	}
}
