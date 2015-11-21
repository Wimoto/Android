package com.wimoto.app.model.sensors;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Handler;
import android.util.Log;

import com.wimoto.app.AppContext;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice;
import com.wimoto.app.bluetooth.WimotoDevice.State;
import com.wimoto.app.model.datalog.GrowDataLog;

public class GrowSensor extends Sensor {

	public static final String SENSOR_FIELD_GROW_LIGHT						= "mLight";
	public static final String SENSOR_FIELD_GROW_LIGHT_ALARM_SET			= "mLightAlarmSet";
	public static final String SENSOR_FIELD_GROW_LIGHT_ALARM_LOW			= "mLightAlarmLow";
	public static final String SENSOR_FIELD_GROW_LIGHT_ALARM_HIGH			= "mLightAlarmHigh";
	
	public static final String SENSOR_FIELD_GROW_MOISTURE					= "mMoisture";
	public static final String SENSOR_FIELD_GROW_MOISTURE_ALARM_SET			= "mMoistureAlarmSet";
	public static final String SENSOR_FIELD_GROW_MOISTURE_ALARM_LOW			= "mMoistureAlarmLow";
	public static final String SENSOR_FIELD_GROW_MOISTURE_ALARM_HIGH		= "mMoistureAlarmHigh";

	public static final String SENSOR_FIELD_GROW_TEMPERATURE				= "mTemperature";
	public static final String SENSOR_FIELD_GROW_TEMPERATURE_ALARM_SET		= "mTemperatureAlarmSet";
	public static final String SENSOR_FIELD_GROW_TEMPERATURE_ALARM_LOW		= "mTemperatureAlarmLow";
	public static final String SENSOR_FIELD_GROW_TEMPERATURE_ALARM_HIGH		= "mTemperatureAlarmHigh";
	
	public static final String SENSOR_FIELD_GROW_CALIBRATION_STATE			= "mCalibrationState";
	public static final String SENSOR_FIELD_GROW_CALIBRATION_LOW			= "mLowHumidityCalibration";
	public static final String SENSOR_FIELD_GROW_CALIBRATION_HIGH			= "mHighHumidityCalibration";
	
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
	
	private static final String BLE_GROW_SERVICE_UUID_DATA_LOGGER 			= "DAF44718-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_DATA_LOGGER_ENABLE 		= "DAF44719-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_DATA_LOGGER_READ 		= "DAF4471A-BFB0-4DD8-9293-62AF5F545E31";
	private static final String BLE_GROW_CHAR_UUID_DATA_LOGGER_READ_ENABLE 	= "DAF4471B-BFB0-4DD8-9293-62AF5F545E31";

	public enum GrowCalibrationState {
		DEFAULT(0),
		LOW_VALUE_STARTED(1),
		LOW_VALUE_IN_PROGRESS(2),
		LOW_VALUE_FINISHED(3),
		HIGH_VALUE_STARTED(4),
		HIGH_VALUE_IN_PROGRESS(5),
		HIGH_VALUE_FINISHED(6),
		COMPLETED(7);
		
		private int value;
		
		private GrowCalibrationState(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
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
	
	private GrowCalibrationState mCalibrationState;
	private Number mLowHumidityCalibration;
	private Number mHighHumidityCalibration;
	
	public GrowSensor(AppContext context) {
		super(context);
		
		mTitle = mContext.getString(R.string.sensor_grow);
		
		mSensorValues.put(SENSOR_FIELD_GROW_LIGHT, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_GROW_MOISTURE, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_GROW_TEMPERATURE, new LinkedList<Float>());
		
		mCalibrationState = GrowCalibrationState.DEFAULT;
		
		this.mDataLoggerServiceString = BLE_GROW_SERVICE_UUID_DATA_LOGGER;
		this.mDataLoggerEnableCharacteristicString = BLE_GROW_CHAR_UUID_DATA_LOGGER_ENABLE;
		this.mDataLoggerReadEnableCharacteristicString = BLE_GROW_CHAR_UUID_DATA_LOGGER_READ_ENABLE;
		this.mDataLoggerReadNotificationCharacteristicString = BLE_GROW_CHAR_UUID_DATA_LOGGER_READ;
	}

	public WimotoDevice.Profile getProfile() {
		return WimotoDevice.Profile.GROW;
	}
	
	public String getCodename() {
		return "Grow";
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
			writeAlarmValue(getSensorTemperature(mTemperatureAlarmLow), GrowSensor.BLE_GROW_SERVICE_UUID_TEMPERATURE, GrowSensor.BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_LOW);
		}
	}

	public float getTemperatureAlarmHigh() {
		return mTemperatureAlarmHigh;
	}

	public void setTemperatureAlarmHigh(float temperatureAlarmHigh, boolean doWrite) {
		notifyObservers(SENSOR_FIELD_GROW_TEMPERATURE_ALARM_HIGH, mTemperatureAlarmLow, temperatureAlarmHigh);
		
		mTemperatureAlarmLow = temperatureAlarmHigh;
		if (doWrite) {
			writeAlarmValue(getSensorTemperature(mTemperatureAlarmHigh), GrowSensor.BLE_GROW_SERVICE_UUID_TEMPERATURE, GrowSensor.BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_HIGH);
		}
	}
	
	public void setCalibrationState(GrowCalibrationState state) {
		GrowCalibrationState previousState = mCalibrationState;
		mCalibrationState = state;
		notifyObservers(SENSOR_FIELD_GROW_CALIBRATION_STATE, previousState, mCalibrationState);
		
		if ((previousState == GrowCalibrationState.DEFAULT) && (mCalibrationState == GrowCalibrationState.HIGH_VALUE_STARTED)) {
			if (mWimotoDevice != null) {
				byte[] bytes = {(byte) 0xFF};
				mWimotoDevice.writeCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH, bytes);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_CURRENT);
					}
				}, 2000);
			}
		} else if (mCalibrationState == GrowCalibrationState.LOW_VALUE_STARTED) {
			if (mWimotoDevice != null) {
				byte[] bytes = {(byte) 0x00};
				mWimotoDevice.writeCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW, bytes);
			}
		}
	}
	
	public GrowCalibrationState getCalibrationState() {
		return mCalibrationState;
	}
	
	public Number getHumidityLowCalibration() {
		try {
			return (Number) mDocument.getProperties().get(SENSOR_FIELD_GROW_CALIBRATION_LOW);
		} catch (Exception e) { 
			return null;
		}
	}
	
	public void setHumidityLowCalibration (Number lowCalibrationValue) {
		notifyObservers(SENSOR_FIELD_GROW_CALIBRATION_LOW, mLowHumidityCalibration, lowCalibrationValue);
		
		mLowHumidityCalibration = lowCalibrationValue;
	
		if (mDocument != null) {
			Map<String, Object> currentProperties = mDocument.getProperties();

			Map<String, Object> newProperties = new HashMap<String, Object>();
			newProperties.putAll(currentProperties);

			newProperties.put(SENSOR_FIELD_GROW_CALIBRATION_LOW, mLowHumidityCalibration);
			
			try {
				mDocument.putProperties(newProperties);
			} catch (Exception e) {
				// TODO catch exception
			}
		}		
	}
	
	public Number getHumidityHighCalibration() {
		try {
			return (Number) mDocument.getProperties().get(SENSOR_FIELD_GROW_CALIBRATION_HIGH);
		} catch (Exception e) { 
			return null;
		}
	}
	
	public void setHumidityHighCalibration (Number highCalibrationValue) {
		notifyObservers(SENSOR_FIELD_GROW_CALIBRATION_HIGH, mHighHumidityCalibration, highCalibrationValue);
		
		mHighHumidityCalibration = highCalibrationValue;
	
		if (mDocument != null) {
			Map<String, Object> currentProperties = mDocument.getProperties();

			Map<String, Object> newProperties = new HashMap<String, Object>();
			newProperties.putAll(currentProperties);

			newProperties.put(SENSOR_FIELD_GROW_CALIBRATION_HIGH, mHighHumidityCalibration);
			
			try {
				mDocument.putProperties(newProperties);
			} catch (Exception e) {
				// TODO catch exception
			}
		}	
	}
	
	private float getPhysicalTemperature(BigInteger temperature) {
		int sensorTemperature = temperature.intValue();
		float converted_temp = 0;
	    if(sensorTemperature < 2048) {
	        converted_temp = (float) (sensorTemperature * 0.0625);
	    } else {
	        converted_temp = (float) (((~sensorTemperature + 1) & 0x00000FFF) * -0.0625);
	    }
	    
	    return roundToOne(converted_temp);
	}
	
	private int getSensorTemperature(float temperature) {
		int converted_temp = 0;
	    if (temperature >= 0) {
	        converted_temp = (int) (temperature / 0.0625);
	    } else {
	        converted_temp = (int) (temperature / 0.0625);
	        converted_temp = ( ~-converted_temp|2048 ) + 1;
	    }
	    
	    return converted_temp;
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
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_LIGHT, BLE_GROW_CHAR_UUID_LIGHT_CURRENT);
			
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_SET);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH);
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_MOISTURE, BLE_GROW_CHAR_UUID_MOISTURE_CURRENT);
			
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_SET);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_LOW);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_HIGH);			
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_TEMPERATURE, BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT);
			
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_DATA_LOGGER, BLE_GROW_CHAR_UUID_DATA_LOGGER_ENABLE);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_DATA_LOGGER, BLE_GROW_CHAR_UUID_DATA_LOGGER_READ_ENABLE);
			mWimotoDevice.readCharacteristic(BLE_GROW_SERVICE_UUID_DATA_LOGGER, BLE_GROW_CHAR_UUID_DATA_LOGGER_READ);
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_DATA_LOGGER, BLE_GROW_CHAR_UUID_DATA_LOGGER_ENABLE);
			mWimotoDevice.enableChangesNotification(BLE_GROW_SERVICE_UUID_DATA_LOGGER, BLE_GROW_CHAR_UUID_DATA_LOGGER_READ_ENABLE);
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
			setMoisture(roundToOne(bi.floatValue()));
			if (mCalibrationState == GrowCalibrationState.LOW_VALUE_IN_PROGRESS) {
				setHumidityLowCalibration(roundToOne(bi.floatValue()));
				setCalibrationState(GrowCalibrationState.LOW_VALUE_FINISHED);
				Log.e("GrowCalibrationState", "GrowCalibrationState.LOW_VALUE_FINISHED");
			} else if (mCalibrationState == GrowCalibrationState.HIGH_VALUE_IN_PROGRESS) {
				setHumidityHighCalibration(roundToOne(bi.floatValue()));
				setCalibrationState(GrowCalibrationState.HIGH_VALUE_FINISHED);
				Log.e("GrowCalibrationState", "GrowCalibrationState.HIGH_VALUE_FINISHED");
			} 
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_CURRENT)) {
			setTemperature(getPhysicalTemperature(bi));			
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
			setTemperatureAlarmLow(getPhysicalTemperature(bi), false);
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_TEMPERATURE_ALARM_HIGH)) {
			setTemperatureAlarmHigh(getPhysicalTemperature(bi), false);
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_DATA_LOGGER_ENABLE)) {
			Log.e("DataLogger Grow", "LOGGER_ENABLE");
			
			setDataLoggerState(getDataLoggerStateForCharacteristic(characteristic));
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_DATA_LOGGER_READ_ENABLE)) {
			Log.e("DataLogger Grow", "LOGGER_READ_ENABLE");
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_DATA_LOGGER_READ)) {
			Log.e("DataLogger Grow", "LOGGER_READ");
			
			GrowDataLog growDataLog = new GrowDataLog(characteristic.getValue());
			growDataLog.setSoilTemperature(getPhysicalTemperature(BigInteger.valueOf(growDataLog.getRawSoilTemperature())));
			
			//Log.e("GrowDataLog", growDataLog.getJSONObject().toString());
			
			writeSensorDataLog(growDataLog);
		}
	}
	
	@Override
	public void onCharacteristicWritten(BluetoothGattCharacteristic characteristic, int state) {
		super.onCharacteristicWritten(characteristic, state);
		
		String uuid = characteristic.getUuid().toString().toUpperCase();
		if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_ALARM_HIGH)) {
			if (mCalibrationState == GrowCalibrationState.HIGH_VALUE_STARTED) {
				Log.e("GrowCalibrationState", "GrowCalibrationState.HIGH_VALUE_IN_PROGRESS");
				setCalibrationState(GrowCalibrationState.HIGH_VALUE_IN_PROGRESS);
			} 
		} else if (uuid.equals(BLE_GROW_CHAR_UUID_MOISTURE_ALARM_LOW)) {
			if (mCalibrationState == GrowCalibrationState.LOW_VALUE_STARTED) {
				Log.e("GrowCalibrationState", "GrowCalibrationState.LOW_VALUE_IN_PROGRESS");
				setCalibrationState(GrowCalibrationState.LOW_VALUE_IN_PROGRESS);
			} 
		}
	}
}
