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

	public static final String SENSOR_FIELD_THERMO_TEMPERATURE					= "ThermoTemperature";
	public static final String SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_SET		= "ThermoTemperatureAlarmSet";
	public static final String SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_LOW		= "ThermoTemperatureAlarmLow";
	public static final String SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_HIGH		= "ThermoTemperatureAlarmHigh";
	
	public static final String SENSOR_FIELD_THERMO_PROBE						= "ThermoProbe";
	public static final String SENSOR_FIELD_THERMO_PROBE_ALARM_SET				= "ThermoProbeAlarmSet";
	public static final String SENSOR_FIELD_THERMO_PROBE_ALARM_LOW				= "ThermoProbeAlarmLow";
	public static final String SENSOR_FIELD_THERMO_PROBE_ALARM_HIGH				= "ThermoProbeAlarmHigh";
	
	private static final String BLE_THERMO_SERVICE_UUID_TEMPERATURE 			= "497B8E4E-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT 		= "497B8E4F-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_LOW		= "497B8E50-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_HIGH		= "497B8E51-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_SET		= "497B8E52-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM			= "497B8E53-B61E-4F82-8FE9-B12CF2497338";
	
	private static final String BLE_THERMO_SERVICE_UUID_PROBE 					= "497B8E54-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_PROBE_CURRENT		 		= "497B8E55-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_PROBE_ALARM_LOW			= "497B8E56-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_PROBE_ALARM_HIGH			= "497B8E57-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_PROBE_ALARM_SET			= "497B8E58-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_PROBE_ALARM				= "497B8E59-B61E-4F82-8FE9-B12CF2497338";
	
	protected float mTemperature;
	protected float mProbe;
	
	protected boolean mTemperatureAlarmSet;
	protected float mTemperatureAlarmLow;
	protected float mTemperatureAlarmHigh;

	protected boolean mProbeAlarmSet;
	protected float mProbeAlarmLow;
	protected float mProbeAlarmHigh;
	
	public ThermoSensor() {
		super();

		mTitle = AppContext.getContext().getString(R.string.sensor_thermo);
		
		mSensorValues.put(SENSOR_FIELD_THERMO_TEMPERATURE, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_THERMO_PROBE, new LinkedList<Float>());
	}

	public ThermoSensor(BluetoothConnection connection) {
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
			mConnection.readCharacteristic(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_SET);
			mConnection.readCharacteristic(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_LOW);
			mConnection.readCharacteristic(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_HIGH);			
			mConnection.enableChangesNotification(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM);
			mConnection.enableChangesNotification(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT);
			
			mConnection.readCharacteristic(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_ALARM_SET);
			mConnection.readCharacteristic(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_ALARM_LOW);
			mConnection.readCharacteristic(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_ALARM_HIGH);			
			mConnection.enableChangesNotification(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_ALARM);
			mConnection.enableChangesNotification(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_CURRENT);
		}
	}	
	
	public WimotoProfile getType() {
		return WimotoProfile.THERMO;
	}
	
	public float getTemperature() {
		return mTemperature;
	}
	
	public void setTemperature(float temperature) {
		notifyObservers(SENSOR_FIELD_THERMO_TEMPERATURE, mTemperature, temperature);

		mTemperature = temperature;
		addValue(SENSOR_FIELD_THERMO_TEMPERATURE, mTemperature);
	}

	public float getProbe() {
		return mProbe;
	}
	
	public void setProbe(float probe) {		
		notifyObservers(SENSOR_FIELD_THERMO_PROBE, mProbe, probe);

		mProbe = probe;
		addValue(SENSOR_FIELD_THERMO_PROBE, mProbe);
	}
	
	public float getTemperatureAlarmLow() {
		return mTemperatureAlarmLow;
	}

	public void setTemperatureAlarmLow(float temperatureAlarmLow) {
		notifyObservers(SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_LOW, mTemperatureAlarmLow, temperatureAlarmLow);
		
		mTemperatureAlarmLow = temperatureAlarmLow;
		writeAlarmValue(Float.valueOf(mTemperatureAlarmLow).intValue(), ThermoSensor.BLE_THERMO_SERVICE_UUID_TEMPERATURE, ThermoSensor.BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_LOW);
	}

	public float getTemperatureAlarmHigh() {
		return mTemperatureAlarmHigh;
	}

	public void setTemperatureAlarmHigh(float temperatureAlarmHigh) {
		notifyObservers(SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_HIGH, mTemperatureAlarmHigh, temperatureAlarmHigh);

		mTemperatureAlarmHigh = temperatureAlarmHigh;
		writeAlarmValue(Float.valueOf(mTemperatureAlarmHigh).intValue(), ThermoSensor.BLE_THERMO_SERVICE_UUID_TEMPERATURE, ThermoSensor.BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_HIGH);
	}
	
	public boolean isTemperatureAlarmSet() {
		return mTemperatureAlarmSet;
	}

	public void setTemperatureAlarmSet(boolean temperatureAlarmSet) {
		notifyObservers(SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_SET, mTemperatureAlarmSet, temperatureAlarmSet);
		
		mTemperatureAlarmSet = temperatureAlarmSet;
		enableAlarm(mTemperatureAlarmSet, BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_SET);
	}
	
	public float getProbeAlarmLow() {
		return mProbeAlarmLow;
	}

	public void setProbeAlarmLow(float probeAlarmLow) {
		notifyObservers(SENSOR_FIELD_THERMO_PROBE_ALARM_LOW, mProbeAlarmLow, probeAlarmLow);
		
		mTemperatureAlarmLow = probeAlarmLow;
		writeAlarmValue(Float.valueOf(mProbeAlarmLow).intValue(), ThermoSensor.BLE_THERMO_SERVICE_UUID_PROBE, ThermoSensor.BLE_THERMO_CHAR_UUID_PROBE_ALARM_LOW);
	}

	public float getProbeAlarmHigh() {
		return mProbeAlarmHigh;
	}

	public void setProbeAlarmHigh(float probeAlarmHigh) {
		notifyObservers(SENSOR_FIELD_THERMO_PROBE_ALARM_HIGH, mProbeAlarmHigh, probeAlarmHigh);

		mProbeAlarmHigh = probeAlarmHigh;
		writeAlarmValue(Float.valueOf(mProbeAlarmHigh).intValue(), ThermoSensor.BLE_THERMO_SERVICE_UUID_PROBE, ThermoSensor.BLE_THERMO_CHAR_UUID_PROBE_ALARM_HIGH);
	}
	
	public boolean isProbeAlarmSet() {
		return mProbeAlarmSet;
	}

	public void setProbeAlarmSet(boolean probeAlarmSet) {
		notifyObservers(SENSOR_FIELD_THERMO_PROBE_ALARM_SET, mProbeAlarmSet, probeAlarmSet);
		
		mProbeAlarmSet = probeAlarmSet;
		enableAlarm(mTemperatureAlarmSet, BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_ALARM_SET);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT)) {
				setTemperature(bi.floatValue());				
			} else if (uuid.equals(BLE_THERMO_CHAR_UUID_PROBE_CURRENT)) {
				setProbe(bi.floatValue());				
			} else if (uuid.equals(BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_SET)) {
				setTemperatureAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_LOW)) {
				setTemperatureAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_HIGH)) {
				setTemperatureAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_THERMO_CHAR_UUID_PROBE_ALARM_SET)) {
				setProbeAlarmSet((bi.floatValue() == 0) ? false:true);
			} else if (uuid.equals(BLE_THERMO_CHAR_UUID_PROBE_ALARM_LOW)) {
				setProbeAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} else if (uuid.equals(BLE_THERMO_CHAR_UUID_PROBE_ALARM_HIGH)) {
				setProbeAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue());
			} 
		}
		
		super.update(observable, data);
	}
}
