package com.wimoto.app.model;

import java.math.BigInteger;
import java.util.LinkedList;

import android.bluetooth.BluetoothGattCharacteristic;

import com.wimoto.app.AppContext;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice;
import com.wimoto.app.bluetooth.WimotoDevice.State;

public class ThermoSensor extends Sensor {

	public static final String SENSOR_FIELD_THERMO_TEMPERATURE					= "ThermoTemperature";
	public static final String SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_SET		= "ThermoTemperatureAlarmSet";
	public static final String SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_LOW		= "ThermoTemperatureAlarmLow";
	public static final String SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_HIGH		= "ThermoTemperatureAlarmHigh";
	
	public static final String SENSOR_FIELD_THERMO_PROBE						= "ThermoProbe";
	public static final String SENSOR_FIELD_THERMO_PROBE_ALARM_SET				= "ThermoProbeAlarmSet";
	public static final String SENSOR_FIELD_THERMO_PROBE_ALARM_LOW				= "ThermoProbeAlarmLow";
	public static final String SENSOR_FIELD_THERMO_PROBE_ALARM_HIGH				= "ThermoProbeAlarmHigh";

	public static final String BLE_THERMO_AD_SERVICE_UUID_TEMPERATURE 			= "00008E4E-0000-1000-8000-00805F9B34FB";
	
	private static final String BLE_THERMO_SERVICE_UUID_TEMPERATURE 			= "497B8E4E-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT 		= "497B8E4F-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_LOW		= "497B8E50-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_HIGH		= "497B8E51-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_SET		= "497B8E52-B61E-4F82-8FE9-B12CF2497338";
	private static final String BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM			= "497B8E53-B61E-4F82-8FE9-B12CF2497338";

	public static final String BLE_THERMO_AD_SERVICE_UUID_PROBE		 			= "00008E54-0000-1000-8000-00805F9B34FB";
	
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
	
	public ThermoSensor(AppContext context) {
		super(context);

		mTitle = mContext.getString(R.string.sensor_thermo);
		
		mSensorValues.put(SENSOR_FIELD_THERMO_TEMPERATURE, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_THERMO_PROBE, new LinkedList<Float>());
	}

	public WimotoDevice.Profile getProfile() {
		return WimotoDevice.Profile.THERMO;
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

	public void setTemperatureAlarmLow(float temperatureAlarmLow, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_LOW, mTemperatureAlarmLow, temperatureAlarmLow);
		
		mTemperatureAlarmLow = temperatureAlarmLow;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mTemperatureAlarmLow).intValue(), ThermoSensor.BLE_THERMO_SERVICE_UUID_TEMPERATURE, ThermoSensor.BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_LOW);
		}
	}

	public float getTemperatureAlarmHigh() {
		return mTemperatureAlarmHigh;
	}

	public void setTemperatureAlarmHigh(float temperatureAlarmHigh, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_HIGH, mTemperatureAlarmHigh, temperatureAlarmHigh);

		mTemperatureAlarmHigh = temperatureAlarmHigh;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mTemperatureAlarmHigh).intValue(), ThermoSensor.BLE_THERMO_SERVICE_UUID_TEMPERATURE, ThermoSensor.BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_HIGH);
		}
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

	public void setProbeAlarmLow(float probeAlarmLow, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_THERMO_PROBE_ALARM_LOW, mProbeAlarmLow, probeAlarmLow);
		
		mTemperatureAlarmLow = probeAlarmLow;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mProbeAlarmLow).intValue(), ThermoSensor.BLE_THERMO_SERVICE_UUID_PROBE, ThermoSensor.BLE_THERMO_CHAR_UUID_PROBE_ALARM_LOW);
		}
	}

	public float getProbeAlarmHigh() {
		return mProbeAlarmHigh;
	}

	public void setProbeAlarmHigh(float probeAlarmHigh, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_THERMO_PROBE_ALARM_HIGH, mProbeAlarmHigh, probeAlarmHigh);

		mProbeAlarmHigh = probeAlarmHigh;
		if (doWrite) {
			writeAlarmValue(Float.valueOf(mProbeAlarmHigh).intValue(), ThermoSensor.BLE_THERMO_SERVICE_UUID_PROBE, ThermoSensor.BLE_THERMO_CHAR_UUID_PROBE_ALARM_HIGH);
		}
	}
	
	public boolean isProbeAlarmSet() {
		return mProbeAlarmSet;
	}

	public void setProbeAlarmSet(boolean probeAlarmSet) {
		notifyObservers(SENSOR_FIELD_THERMO_PROBE_ALARM_SET, mProbeAlarmSet, probeAlarmSet);
		
		mProbeAlarmSet = probeAlarmSet;
		enableAlarm(mTemperatureAlarmSet, BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_ALARM_SET);
	}
	
	// WimotoDeviceCallback
	@Override
	public void onConnectionStateChange(State state) {
		super.onConnectionStateChange(state);

		if (state == State.CONNECTED) {
			mWimotoDevice.readCharacteristic(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_SET);
			mWimotoDevice.readCharacteristic(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_LOW);
			mWimotoDevice.readCharacteristic(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_HIGH);			
			mWimotoDevice.readCharacteristic(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_ALARM_SET);
			mWimotoDevice.readCharacteristic(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_ALARM_LOW);
			mWimotoDevice.readCharacteristic(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_ALARM_HIGH);
			
			mWimotoDevice.enableChangesNotification(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_THERMO_SERVICE_UUID_TEMPERATURE, BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT);
			
			mWimotoDevice.enableChangesNotification(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_THERMO_SERVICE_UUID_PROBE, BLE_THERMO_CHAR_UUID_PROBE_CURRENT);			
		}
	}

	@Override
	public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
		super.onCharacteristicChanged(characteristic);
		
		String uuid = characteristic.getUuid().toString().toUpperCase();
		
		BigInteger bi = new BigInteger(characteristic.getValue());
		if (uuid.equals(BLE_THERMO_CHAR_UUID_TEMPERATURE_CURRENT)) {
			setTemperature(bi.floatValue());				
		} else if (uuid.equals(BLE_THERMO_CHAR_UUID_PROBE_CURRENT)) {
			setProbe(bi.floatValue());				
		} else if (uuid.equals(BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_SET)) {
			setTemperatureAlarmSet((bi.floatValue() == 0) ? false:true);
		} else if (uuid.equals(BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_LOW)) {
			setTemperatureAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		} else if (uuid.equals(BLE_THERMO_CHAR_UUID_TEMPERATURE_ALARM_HIGH)) {
			setTemperatureAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		} else if (uuid.equals(BLE_THERMO_CHAR_UUID_PROBE_ALARM_SET)) {
			setProbeAlarmSet((bi.floatValue() == 0) ? false:true);
		} else if (uuid.equals(BLE_THERMO_CHAR_UUID_PROBE_ALARM_LOW)) {
			setProbeAlarmLow(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		} else if (uuid.equals(BLE_THERMO_CHAR_UUID_PROBE_ALARM_HIGH)) {
			setProbeAlarmHigh(Float.valueOf(bi.floatValue()/100.0f).intValue(), false);
		} 
	}
}
