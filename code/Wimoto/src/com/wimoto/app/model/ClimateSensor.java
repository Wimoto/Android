package com.wimoto.app.model;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Observable;

import android.bluetooth.BluetoothGattCharacteristic;

import com.couchbase.lite.Document;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.BluetoothConnection;
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
	
	protected float mTemperature;
	protected float mHumidity;
	protected float mLight;
	
	protected boolean mTemperatureAlarmSet;
	protected float mTemperatureAlarmLow;
	protected float mTemperatureAlarmHigh;

	protected boolean mHumidityAlarmSet;
	protected float mHumidityAlarmLow;
	protected float mHumidityAlarmHigh;

	protected boolean mLightAlarmSet;
	protected float mLightAlarmLow;
	protected float mLightAlarmHigh;
	
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
	
	public SensorProfile getType() {
		return SensorProfile.CLIMATE;
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

	public float getTemperatureAlarmLow() {
		return mTemperatureAlarmLow;
	}

	public void setTemperatureAlarmLow(float temperatureAlarmLow) {
		notifyObservers(SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW, mTemperatureAlarmLow, temperatureAlarmLow);
		
		mTemperatureAlarmLow = temperatureAlarmLow;
		writeAlarmValue(getSensorTemperature(mTemperatureAlarmLow), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, ClimateSensor.BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW);
	}

	public float getTemperatureAlarmHigh() {
		return mTemperatureAlarmHigh;
	}

	public void setTemperatureAlarmHigh(float temperatureAlarmHigh) {
		notifyObservers(SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH, mTemperatureAlarmHigh, temperatureAlarmHigh);

		mTemperatureAlarmHigh = temperatureAlarmHigh;
		writeAlarmValue(getSensorTemperature(mTemperatureAlarmHigh), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, ClimateSensor.BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH);
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

	public float getHumidityAlarmLow() {
		return mHumidityAlarmLow;
	}

	public void setHumidityAlarmLow(float humidityAlarmLow) {
		notifyObservers(SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW, mHumidityAlarmLow, humidityAlarmLow);
		
		mHumidityAlarmLow = humidityAlarmLow;
		writeAlarmValue(getSensorHumidity(humidityAlarmLow), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_HUMIDITY, ClimateSensor.BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_LOW);	}

	public float getHumidityAlarmHigh() {
		return mHumidityAlarmHigh;
	}

	public void setHumidityAlarmHigh(float humidityAlarmHigh) {
		notifyObservers(SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH, mHumidityAlarmHigh, humidityAlarmHigh);
		
		mHumidityAlarmHigh = humidityAlarmHigh;
		writeAlarmValue(getSensorHumidity(humidityAlarmHigh), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_HUMIDITY, ClimateSensor.BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_HIGH);
	}

	public boolean isLightAlarmSet() {
		return mLightAlarmSet;
	}

	public void setLightAlarmSet(boolean lightAlarmSet) {
		notifyObservers(SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET, mLightAlarmSet, lightAlarmSet);
		
		mLightAlarmSet = lightAlarmSet;
		enableAlarm(mLightAlarmSet, BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_SET);
	}

	public float getLightAlarmLow() {
		return mLightAlarmLow;
	}

	public void setLightAlarmLow(float lightAlarmLow) {
		notifyObservers(SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW, mLightAlarmLow, lightAlarmLow);
		
		mLightAlarmLow = lightAlarmLow;
		writeAlarmValue(Float.valueOf(mLightAlarmLow).intValue(), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_LIGHT, ClimateSensor.BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_LOW);
	}

	public float getLightAlarmHigh() {
		return mLightAlarmHigh;
	}

	public void setLightAlarmHigh(float lightAlarmHigh) {
		notifyObservers(SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH, mLightAlarmHigh, lightAlarmHigh);
		
		mLightAlarmHigh = lightAlarmHigh;
		writeAlarmValue(Float.valueOf(mLightAlarmHigh).intValue(), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_LIGHT, ClimateSensor.BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_HIGH);
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {			
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT)) {
				setTemperature(getPhysicalTemperature(bi));				
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT)) {
				setLight((float)(0.96 * bi.floatValue()));				
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT)) {
				setHumidity(getPhysicalHumidity(bi));				
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_SET)) {
				setTemperatureAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW)) {
				setTemperatureAlarmLow(getPhysicalTemperature(bi));
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH)) {
				setTemperatureAlarmHigh(getPhysicalTemperature(bi));
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_SET)) {
				setHumidityAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_LOW)) {
				setHumidityAlarmLow(getPhysicalHumidity(bi));
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_HIGH)) {
				setHumidityAlarmHigh(getPhysicalHumidity(bi));
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
	
	private float getPhysicalTemperature(BigInteger temperature) {
		return (float)(-46.85 + (175.72*temperature.floatValue()/65536));
	}
	
	private int getSensorTemperature(float temperature) {
		return Double.valueOf(((46.85+temperature)*65536)/175.72).intValue();
	}
	
	private float getPhysicalHumidity(BigInteger humidity) {
		return (float)(-6.0 + (125.0*humidity.floatValue()/65536));
	}
	
	private int getSensorHumidity(float humidity) {
		return Double.valueOf(((6+humidity)*65536)/125).intValue();
	}
}
