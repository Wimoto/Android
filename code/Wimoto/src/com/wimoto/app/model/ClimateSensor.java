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

public class ClimateSensor extends Sensor {

	public static final String SENSOR_FIELD_CLIMATE_TEMPERATURE					= "ClimateTemperature";
	public static final String SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_SET		= "ClimateTemperatureAlarmSet";
	public static final String SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW		= "ClimateTemperatureAlarmLow";
	public static final String SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH		= "ClimateTemperatureAlarmHigh";
	
	public static final String SENSOR_FIELD_CLIMATE_HUMIDITY					= "ClimateHumidity";
	public static final String SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_SET			= "ClimateHumidityAlarmSet";
	public static final String SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW			= "ClimateHumidityAlarmLow";
	public static final String SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH			= "ClimateHumidityAlarmHigh";

	public static final String SENSOR_FIELD_CLIMATE_LIGHT						= "ClimateLight";
	public static final String SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET				= "ClimateLightAlarmSet";
	public static final String SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW				= "ClimateLightAlarmLow";
	public static final String SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH			= "ClimateLightAlarmHigh";
	
	private static final String BLE_CLIMATE_SERVICE_UUID_TEMPERATURE 			= "E0035608-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT 		= "E0035609-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW		= "E003560A-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH	= "E003560B-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_SET		= "E003560C-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM			= "E003560D-EC48-4ED0-9F3B-5419C00A94FD";
	
	private static final String BLE_CLIMATE_SERVICE_UUID_HUMIDITY 				= "E0035614-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT	 		= "E0035615-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_LOW		= "E0035616-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_HIGH		= "E0035617-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_SET		= "E0035618-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM			= "E0035619-EC48-4ED0-9F3B-5419C00A94FD";
	
	private static final String BLE_CLIMATE_SERVICE_UUID_LIGHT 					= "E003560E-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT		 		= "E003560F-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_LOW			= "E0035610-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_HIGH			= "E0035611-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_SET			= "E0035612-EC48-4ED0-9F3B-5419C00A94FD";
	private static final String BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM				= "E0035613-EC48-4ED0-9F3B-5419C00A94FD";
	
	private float mTemperature;
	private float mHumidity;
	private float mLight;
	
	private boolean mTemperatureAlarmSet;
	private int mTemperatureAlarmLow;
	private int mTemperatureAlarmHigh;

	private boolean mHumidityAlarmSet;
	private int mHumidityAlarmLow;
	private int mHumidityAlarmHigh;

	private boolean mLightAlarmSet;
	private int mLightAlarmLow;
	private int mLightAlarmHigh;
	
	public ClimateSensor() {
		super();
		
		mTitle = AppContext.getContext().getString(R.string.sensor_climate);
		
		mSensorValues.put(SENSOR_FIELD_CLIMATE_TEMPERATURE, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_CLIMATE_HUMIDITY, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_CLIMATE_LIGHT, new LinkedList<Float>());
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
			
			mConnection.readCharacteristic(BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_SET);
			mConnection.readCharacteristic(BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_LOW);
			mConnection.readCharacteristic(BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_HIGH);			
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM);
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT);
			
			mConnection.readCharacteristic(BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_SET);
			mConnection.readCharacteristic(BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_LOW);
			mConnection.readCharacteristic(BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_HIGH);			
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM);
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT);
		}
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.CLIMATE;
	}
	
	public float getTemperature() {
		return mTemperature;
	}
	
	public void setTemperature(float temperature) {
		notifyObservers(SENSOR_FIELD_CLIMATE_TEMPERATURE, mTemperature, temperature);

		mTemperature = temperature;
		addValue(SENSOR_FIELD_CLIMATE_TEMPERATURE, mTemperature);
	}

	public float getLight() {
		return mLight;
	}

	public void setLight(float light) {		
		notifyObservers(SENSOR_FIELD_CLIMATE_LIGHT, mLight, light);

		mLight = light;
		addValue(SENSOR_FIELD_CLIMATE_LIGHT, mLight);
	}

	public float getHumidity() {
		return mHumidity;
	}

	public void setHumidity(float humidity) {
		notifyObservers(SENSOR_FIELD_CLIMATE_HUMIDITY, mHumidity, humidity);
		
		mHumidity = humidity;
		addValue(SENSOR_FIELD_CLIMATE_HUMIDITY, mHumidity);
	}

	public int getTemperatureAlarmLow() {
		return mTemperatureAlarmLow;
	}

	public void setTemperatureAlarmLow(int temperatureAlarmLow) {
		notifyObservers(SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW, mTemperatureAlarmLow, temperatureAlarmLow);
		
		mTemperatureAlarmLow = temperatureAlarmLow;
		writeAlarmValue(mTemperatureAlarmLow, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, ClimateSensor.BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW);
	}

	public int getTemperatureAlarmHigh() {
		return mTemperatureAlarmHigh;
	}

	public void setTemperatureAlarmHigh(int temperatureAlarmHigh) {
		notifyObservers(SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH, mTemperatureAlarmHigh, temperatureAlarmHigh);

		mTemperatureAlarmHigh = temperatureAlarmHigh;
		writeAlarmValue(mTemperatureAlarmHigh, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, ClimateSensor.BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH);
	}
	
	public boolean isTemperatureAlarmSet() {
		return mTemperatureAlarmSet;
	}

	public void setTemperatureAlarmSet(boolean temperatureAlarmSet) {
		notifyObservers(SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_SET, mTemperatureAlarmSet, temperatureAlarmSet);
		
		mTemperatureAlarmSet = temperatureAlarmSet;
		enableAlarm(mTemperatureAlarmSet, BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_SET);
	}

	public boolean isHumidityAlarmSet() {
		return mHumidityAlarmSet;
	}

	public void setHumidityAlarmSet(boolean humidityAlarmSet) {
		notifyObservers(SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_SET, mHumidityAlarmSet, humidityAlarmSet);
		
		mHumidityAlarmSet = humidityAlarmSet;
		enableAlarm(mHumidityAlarmSet, BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_SET);
	}

	public int getHumidityAlarmLow() {
		return mHumidityAlarmLow;
	}

	public void setHumidityAlarmLow(int humidityAlarmLow) {
		notifyObservers(SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW, mHumidityAlarmLow, humidityAlarmLow);
		
		mHumidityAlarmLow = humidityAlarmLow;
		writeAlarmValue(mHumidityAlarmLow, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_HUMIDITY, ClimateSensor.BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_LOW);	
	}

	public int getHumidityAlarmHigh() {
		return mHumidityAlarmHigh;
	}

	public void setHumidityAlarmHigh(int humidityAlarmHigh) {
		notifyObservers(SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH, mHumidityAlarmHigh, humidityAlarmHigh);
		
		mHumidityAlarmHigh = humidityAlarmHigh;
		writeAlarmValue(mHumidityAlarmHigh, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_HUMIDITY, ClimateSensor.BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_HIGH);
	}

	public boolean isLightAlarmSet() {
		return mLightAlarmSet;
	}

	public void setLightAlarmSet(boolean lightAlarmSet) {
		notifyObservers(SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET, mLightAlarmSet, lightAlarmSet);
		
		mLightAlarmSet = lightAlarmSet;
		enableAlarm(mLightAlarmSet, BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_SET);
	}

	public int getLightAlarmLow() {
		return mLightAlarmLow;
	}

	public void setLightAlarmLow(int lightAlarmLow) {
		notifyObservers(SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW, mLightAlarmLow, lightAlarmLow);
		
		mLightAlarmLow = lightAlarmLow;
		writeAlarmValue(mLightAlarmLow, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_LIGHT, ClimateSensor.BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_LOW);
	}

	public int getLightAlarmHigh() {
		return mLightAlarmHigh;
	}

	public void setLightAlarmHigh(int lightAlarmHigh) {
		notifyObservers(SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH, mLightAlarmHigh, lightAlarmHigh);
		
		mLightAlarmHigh = lightAlarmHigh;
		writeAlarmValue(mLightAlarmHigh, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_LIGHT, ClimateSensor.BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_HIGH);
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {			
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT)) {
				setTemperature((float)(-46.85 + (175.72*bi.floatValue()/65536)));				
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT)) {
				setLight((float)(0.96 * bi.floatValue()));				
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT)) {
				setHumidity((float)(-6.0 + (125.0*bi.floatValue()/65536)) * (-1));				
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_SET)) {
				setTemperatureAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW)) {
				setTemperatureAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH)) {
				setTemperatureAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_SET)) {
				setHumidityAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_LOW)) {
				setHumidityAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_HIGH)) {
				setHumidityAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_SET)) {
				setLightAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_LOW)) {
				setLightAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_HIGH)) {
				setLightAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue());
			}
		}
		
		super.update(observable, data);
	}
}
