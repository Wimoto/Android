package com.wimoto.app.model;

import java.math.BigInteger;
import java.util.LinkedList;

import android.bluetooth.BluetoothGattCharacteristic;

import com.wimoto.app.AppContext;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice;
import com.wimoto.app.bluetooth.WimotoDevice.State;

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
	
	public static final String BLE_GROW_AD_SERVICE_UUID_LIGHT 				= "0000470C-0000-1000-8000-00805F9B34FB";
	
	public static final String BLE_GROW_SERVICE_UUID_LIGHT		 			= "DAF4470C-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_LIGHT_CURRENT 			= "DAF4470D-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_LIGHT_ALARM_LOW			= "DAF4470E-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_LIGHT_ALARM_HIGH			= "DAF4470F-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_LIGHT_ALARM_SET			= "DAF44710-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_LIGHT_ALARM				= "DAF44711-BFB0-4DD8-9293-62AF5F545E31";
	
	public static final String BLE_GROW_AD_SERVICE_UUID_MOISTURE 			= "00004712-0000-1000-8000-00805F9B34FB";
	
	private static final String BLE_GROW_SERVICE_UUID_MOISTURE 				= "DAF44712-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_MOISTURE_CURRENT	 		= "DAF44713-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW		= "DAF44714-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH		= "DAF44715-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_MOISTURE_ALARM_SET		= "DAF44716-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_MOISTURE_ALARM			= "DAF44717-BFB0-4DD8-9293-62AF5F545E31";
	
	public static final String BLE_GROW_AD_SERVICE_UUID_TEMPERATURE 		= "00004706-0000-1000-8000-00805F9B34FB";
	
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
	private float mLightAlarmLow;
	private float mLightAlarmHigh;

	private boolean mMoistureAlarmSet;
	private float mMoistureAlarmLow;
	private float mMoistureAlarmHigh;

	private boolean mTemperatureAlarmSet;
	private float mTemperatureAlarmLow;
	private float mTemperatureAlarmHigh;
	
	public GrowSensor(AppContext context) {
		super(context);
		
		mTitle = mContext.getString(R.string.sensor_grow);
		
		mSensorValues.put(SENSOR_FIELD_GROW_LIGHT, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_GROW_MOISTURE, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_GROW_TEMPERATURE, new LinkedList<Float>());
	}

	public WimotoDevice.Profile getProfile() {
		return WimotoDevice.Profile.GROW;
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

	public float getLightAlarmLow() {
		return mLightAlarmLow;
	}

	public void setLightAlarmLow(float lightAlarmLow, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_GROW_LIGHT_ALARM_LOW, mLightAlarmLow, lightAlarmLow);
		
		mLightAlarmLow = lightAlarmLow;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mLightAlarmLow).intValue(), GrowSensor.BLE_GROW_SERVICE_UUID_LIGHT, GrowSensor.BLE_GROW_CHAR_UUID_LIGHT_ALARM_LOW);
		}
	}

	public float getLightAlarmHigh() {
		return mLightAlarmHigh;
	}

	public void setLightAlarmHigh(float lightAlarmHigh, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_GROW_LIGHT_ALARM_HIGH, mLightAlarmHigh, lightAlarmHigh);
		
		mLightAlarmHigh = lightAlarmHigh;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mLightAlarmHigh).intValue(), GrowSensor.BLE_GROW_SERVICE_UUID_LIGHT, GrowSensor.BLE_GROW_CHAR_UUID_LIGHT_ALARM_HIGH);
		}
	}
	
	public boolean isMoistureAlarmSet() {
		return mMoistureAlarmSet;
	}

	public void setMoistureAlarmSet(boolean moistureAlarmSet) {
		notifyObservers(SENSOR_FIELD_GROW_MOISTURE_ALARM_SET, mMoistureAlarmSet, moistureAlarmSet);
		
		mMoistureAlarmSet = moistureAlarmSet;
		enableAlarm(mMoistureAlarmSet, BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_SET);
	}

	public float getMoistureAlarmLow() {
		return mMoistureAlarmLow;
	}

	public void setMoistureAlarmLow(float moistureAlarmLow, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_GROW_MOISTURE_ALARM_LOW, mMoistureAlarmLow, moistureAlarmLow);
		
		mMoistureAlarmLow = moistureAlarmLow;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mMoistureAlarmLow).intValue(), GrowSensor.BLE_GROW_SERVICE_UUID_MOISTURE, GrowSensor.BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW);
		}
	}

	public float getMoistureAlarmHigh() {
		return mMoistureAlarmHigh;
	}

	public void setMoistureAlarmHigh(float moistureAlarmHigh, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_GROW_MOISTURE_ALARM_HIGH, mMoistureAlarmHigh, moistureAlarmHigh);
		
		mMoistureAlarmHigh = moistureAlarmHigh;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mMoistureAlarmHigh).intValue(), GrowSensor.BLE_GROW_SERVICE_UUID_MOISTURE, GrowSensor.BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH);
		}
	}
	
	public boolean isTemperatureAlarmSet() {
		return mTemperatureAlarmSet;
	}

	public void setTemperatureAlarmSet(boolean temperatureAlarmSet) {
		notifyObservers(SENSOR_FIELD_GROW_TEMPERATURE_ALARM_SET, mTemperatureAlarmSet, temperatureAlarmSet);
		
		mTemperatureAlarmSet = temperatureAlarmSet;
		enableAlarm(temperatureAlarmSet, BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_SET);
	}

	public float getTemperatureAlarmLow() {
		return mTemperatureAlarmLow;
	}

	public void setTemperatureAlarmLow(float temperatureAlarmLow, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_GROW_TEMPERATURE_ALARM_LOW, mTemperatureAlarmLow, temperatureAlarmLow);
		
		mTemperatureAlarmLow = temperatureAlarmLow;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mTemperatureAlarmLow).intValue(), GrowSensor.BLE_GROW_SERVICE_UUID_TEMPERATURE, GrowSensor.BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_LOW);
		}
	}

	public float getTemperatureAlarmHigh() {
		return mTemperatureAlarmHigh;
	}

	public void setTemperatureAlarmHigh(float temperatureAlarmHigh, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_GROW_TEMPERATURE_ALARM_HIGH, mTemperatureAlarmLow, temperatureAlarmHigh);
		
		mTemperatureAlarmLow = temperatureAlarmHigh;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(temperatureAlarmHigh).intValue(), GrowSensor.BLE_GROW_SERVICE_UUID_TEMPERATURE, GrowSensor.BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_HIGH);
		}
	}
		
	// WimotoDeviceCallback
	@Override
	public void onConnectionStateChange(State state) {
		super.onConnectionStateChange(state);

		if (state == State.CONNECTED) {
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_ALARM_SET);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_ALARM_LOW);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_ALARM_HIGH);
			
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_SET);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH);
			
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_SET);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_LOW);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_HIGH);			
			
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_CURRENT);
			
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_CURRENT);
			
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT);
		}
	}

	@Override
	public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
		super.onCharacteristicChanged(characteristic);
				
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
			setLightAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_LIGHT_ALARM_HIGH)) {
			setLightAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_ALARM_SET)) {
			setMoistureAlarmSet((bi.floatValue() == 0) ? false:true);
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW)) {
			setMoistureAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH)) {
			setMoistureAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_SET)) {
			setTemperatureAlarmSet((bi.floatValue() == 0) ? false:true);
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_LOW)) {
			setTemperatureAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_HIGH)) {
			setTemperatureAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		}
	}
}
