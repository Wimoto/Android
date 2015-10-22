package com.wimoto.app.model;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.wimoto.app.AppContext;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.WimotoDevice;
import com.wimoto.app.bluetooth.WimotoDevice.State;

public class SentrySensor extends Sensor {
	
	public static final String SENSOR_FIELD_SENTRY_ACCELEROMETER_X					= "mAccelerationX";
	public static final String SENSOR_FIELD_SENTRY_ACCELEROMETER_Y					= "mAccelerationY";
	public static final String SENSOR_FIELD_SENTRY_ACCELEROMETER_Z					= "mAccelerationZ";

	public static final String SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_ENABLED		= "mAccelerometerAlarmEnabledDate";
	public static final String SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_DISABLED		= "mAccelerometerAlarmDisabledDate";
	
	public static final String SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_SET			= "SentryAccelerometerAlarmSet";
	public static final String SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_CLEAR		= "SentryAccelerometerAlarmClear";
	
	public static final String SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_ENABLED	= "mInfraredAlarmEnabledDate";
	public static final String SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_DISABLED	= "mInfraredAlarmDisabledDate";
	
	public static final String SENSOR_FIELD_SENTRY_PASSIVE_INFRARED					= "SentryInfrared";
	public static final String SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_SET		= "SentryInfraredAlarmSet";

	public static final String BLE_SENTRY_AD_SERVICE_UUID_ACCELEROMETER 			= "0000DC68-0000-1000-8000-00805F9B34FB";
	
	private static final String BLE_SENTRY_SERVICE_UUID_ACCELEROMETER 				= "4209DC68-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT 			= "4209DC69-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_SET		= "4209DC6A-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_CLEAR		= "4209DC6B-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM			= "4209DC6C-E433-4420-83D8-CDAACCD2E312";

	public static final String BLE_SENTRY_AD_SERVICE_UUID_PASSIVE_INFRARED 			= "0000DC6D-0000-1000-8000-00805F9B34FB";
	
	private static final String BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED 			= "4209DC6D-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT		= "4209DC6E-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM_SET		= "4209DC6F-E433-4420-83D8-CDAACCD2E312";
	private static final String BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM			= "4209DC70-E433-4420-83D8-CDAACCD2E312";
	
	private static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
	
	private float mAccelerationX;
	private float mAccelerationY;
	private float mAccelerationZ;
	private float mInfrared;
	
	private Date mAccelerometerAlarmEnabledDate;
	private Date mAccelerometerAlarmDisabledDate;
	
	private boolean mAccelerometerAlarmSet;
	private boolean mAccelerometerAlarmClear;
	
	private Date mInfraredAlarmEnabledDate;
	private Date mInfraredAlarmDisabledDate;
	
	private boolean mInfraredAlarmSet;
	
	private long mAccelerometerAlarmTimeshot;
	private long mInfraredAlarmTimeshot;
	
	public SentrySensor(AppContext context) {
		super(context);
		
		mTitle = mContext.getString(R.string.sensor_sentry);
		
		mSensorValues.put(SENSOR_FIELD_SENTRY_ACCELEROMETER_X, new LinkedList<Float>());
		mSensorValues.put(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED, new LinkedList<Float>());
	}

	public WimotoDevice.Profile getProfile() {
		return WimotoDevice.Profile.SENTRY;
	}
	
//	public float getAccelerometer() {
//		return mAccelerometer;
//	}
//	
//	public void setAccelerometer(float accelerometer) {
//		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER, mAccelerometer, accelerometer);
//
//		mAccelerometer = accelerometer;
//		addValue(SENSOR_FIELD_SENTRY_ACCELEROMETER, mAccelerometer);
//	}
	
	public void setAccelerationX(float accelerationX) {		
		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER_X, mAccelerationX, accelerationX);

		mAccelerationX = accelerationX;
		//addValue(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED, mInfrared);
	}

	public void setAccelerationY(float accelerationY) {		
		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER_Y, mAccelerationY, accelerationY);

		mAccelerationY = accelerationY;
		//addValue(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED, mInfrared);
	}
	
	public void setAccelerationZ(float accelerationZ) {		
		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER_Z, mAccelerationZ, accelerationZ);

		mAccelerationZ = accelerationZ;
		//addValue(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED, mInfrared);
	}
	
	public float getInfrared() {
		return mInfrared;
	}
	
	public void setInfrared(float infrared) {		
		notifyObservers(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED, mInfrared, infrared);

		mInfrared = infrared;
		addValue(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED, mInfrared);
	}
	
	public boolean isAccelerometerAlarmSet() {
		return mAccelerometerAlarmSet;
	}

	public void setAccelerometerAlarmSet(boolean accelerometerAlarmSet) {
		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_SET, mAccelerometerAlarmSet, accelerometerAlarmSet);
		
		mAccelerometerAlarmSet = accelerometerAlarmSet;
		enableAlarm(mAccelerometerAlarmSet, BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_SET);
	}
	
	public boolean isAccelerometerAlarmClear() {
		return mAccelerometerAlarmClear;
	}

	public void setAccelerometerAlarmClear(boolean accelerometerAlarmClear) {
		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_CLEAR, mAccelerometerAlarmClear, accelerometerAlarmClear);
		
		mAccelerometerAlarmClear = accelerometerAlarmClear;
		enableAlarm(mAccelerometerAlarmClear, BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_CLEAR);
	}
	
	public boolean isInfraredAlarmSet() {
		return mInfraredAlarmSet;
	}

	public void setInfraredAlarmSet(boolean infraredAlarmSet) {
		notifyObservers(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_SET, mInfraredAlarmSet, infraredAlarmSet);
		
		mInfraredAlarmSet = infraredAlarmSet;
		enableAlarm(mInfraredAlarmSet, BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM_SET);
	}
	
	public Date getAccelerometerAlarmEnabledTime() {
		try {
			return new Date((Long) mDocument.getProperties().get(SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_ENABLED));
		} catch (Exception e) { 
			return null;
		}		
	}
	
	public void setAccelerometerAlarmEnabledTime(Date date) {
		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_ENABLED, mAccelerometerAlarmEnabledDate, date);
		
		mAccelerometerAlarmEnabledDate = date;
	
		if (mDocument != null) {
			Map<String, Object> currentProperties = mDocument.getProperties();

			Map<String, Object> newProperties = new HashMap<String, Object>();
			newProperties.putAll(currentProperties);

			newProperties.put(SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_ENABLED, Long.valueOf(mAccelerometerAlarmEnabledDate.getTime()));
			
			try {
				mDocument.putProperties(newProperties);
			} catch (Exception e) {
				// TODO catch exception
			}
		}		
	}
	
	public Date getAccelerometerAlarmDisabledTime() {
		try {
			return new Date((Long) mDocument.getProperties().get(SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_DISABLED));
		} catch (Exception e) { 
			return null;
		}		
	}
	
	public void setAccelerometerAlarmDisabledTime(Date date) {
		notifyObservers(SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_DISABLED, mAccelerometerAlarmDisabledDate, date);
		
		mAccelerometerAlarmDisabledDate = date;
	
		if (mDocument != null) {
			Map<String, Object> currentProperties = mDocument.getProperties();

			Map<String, Object> newProperties = new HashMap<String, Object>();
			newProperties.putAll(currentProperties);

			newProperties.put(SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_DISABLED, Long.valueOf(mAccelerometerAlarmDisabledDate.getTime()));
			
			try {
				mDocument.putProperties(newProperties);
			} catch (Exception e) {
				// TODO catch exception
			}
		}		
	}
	
	public Date getInfraredAlarmEnabledTime() {
		try {
			return new Date((Long) mDocument.getProperties().get(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_ENABLED));
		} catch (Exception e) { 
			return null;
		}		
	}
	
	public void setInfraredAlarmEnabledTime(Date date) {
		notifyObservers(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_ENABLED, mInfraredAlarmEnabledDate, date);
		
		mInfraredAlarmEnabledDate = date;
	
		if (mDocument != null) {
			Map<String, Object> currentProperties = mDocument.getProperties();

			Map<String, Object> newProperties = new HashMap<String, Object>();
			newProperties.putAll(currentProperties);

			newProperties.put(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_ENABLED, Long.valueOf(mInfraredAlarmEnabledDate.getTime()));
			
			try {
				mDocument.putProperties(newProperties);
			} catch (Exception e) {
				// TODO catch exception
			}
		}		
	}
	
	public Date getInfraredAlarmDisabledTime() {
		try {
			return new Date((Long) mDocument.getProperties().get(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_DISABLED));
		} catch (Exception e) { 
			return null;
		}		
	}
	
	public void setInfraredAlarmDisabledTime(Date date) {
		notifyObservers(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_DISABLED, mInfraredAlarmDisabledDate, date);
		
		mInfraredAlarmDisabledDate = date;
	
		if (mDocument != null) {
			Map<String, Object> currentProperties = mDocument.getProperties();

			Map<String, Object> newProperties = new HashMap<String, Object>();
			newProperties.putAll(currentProperties);

			newProperties.put(SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_DISABLED, Long.valueOf(mInfraredAlarmDisabledDate.getTime()));
			
			try {
				mDocument.putProperties(newProperties);
			} catch (Exception e) {
				// TODO catch exception
			}
		}		
	}
	
	// WimotoDeviceCallback
	@Override
	public void onConnectionStateChange(State state) {
		super.onConnectionStateChange(state);

		if (state == State.CONNECTED) {
			mWimotoDevice.readCharacteristic(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_SET);
			mWimotoDevice.readCharacteristic(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_CLEAR);
			mWimotoDevice.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_ACCELEROMETER, BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT);
			
			mWimotoDevice.readCharacteristic(BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT);
			mWimotoDevice.readCharacteristic(BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM_SET);
			mWimotoDevice.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM);
			mWimotoDevice.enableChangesNotification(BLE_SENTRY_SERVICE_UUID_PASSIVE_INFRARED, BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT);			
		}
	}

	@Override
	public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
		super.onCharacteristicChanged(characteristic);
		
		String uuid = characteristic.getUuid().toString().toUpperCase();
		//Log.e("uuid", uuid);
		
		BigInteger bi = new BigInteger(characteristic.getValue());
		if (uuid.equals(BLE_SENTRY_CHAR_UUID_ACCELEROMETER_CURRENT)) {
			//setAccelerometer(bi.floatValue());
			try {
				byte[] bytes = characteristic.getValue();
				
				int x = bytes[0] & 0xff;
				setAccelerationX(ValueFactor.newInstance(x).mAngleXY);
				
				int y = bytes[1] & 0xff;
				setAccelerationY(ValueFactor.newInstance(x).mAngleXY);
				
				int z = bytes[2] & 0xff;
				setAccelerationZ(ValueFactor.newInstance(x).mAngleZ);
				
				Log.e("", "x " + x + " y " + y + " z " + z);
			} catch (Exception e) { }
		} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_CURRENT)) {
			setInfrared(bi.floatValue());
		} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_SET)) {
			setAccelerometerAlarmSet((bi.floatValue() == 0) ? false:true);
		} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM_CLEAR)) {
			setAccelerometerAlarmClear((bi.floatValue() == 0) ? false:true);
		} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM_SET)) {
			setInfraredAlarmSet((bi.floatValue() == 0) ? false:true);
		} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_ACCELEROMETER_ALARM)) {
			Log.e("accelerometer", "alarm");
		} else if (uuid.equals(BLE_SENTRY_CHAR_UUID_PASSIVE_INFRARED_ALARM)) {
			Log.e("infrared", "alarm");
		}
	}
	
	
	
	public boolean checkAccelerometerAlarm() {
		if (getAccelerometerAlarmEnabledTime() == null || getAccelerometerAlarmDisabledTime() == null) {
			return false;
		}
		
		long currentTimeshot = Calendar.getInstance().getTime().getTime() % MILLIS_PER_DAY;
		if (mAccelerometerAlarmSet && (currentTimeshot > mAccelerometerAlarmTimeshot + 30)) {
			long enabledTimeshot = getAccelerometerAlarmEnabledTime().getTime() % MILLIS_PER_DAY;
			long disabledTimeshot = getAccelerometerAlarmDisabledTime().getTime() % MILLIS_PER_DAY;
			
			if ((currentTimeshot > enabledTimeshot) && (currentTimeshot < disabledTimeshot)) {
					mAccelerometerAlarmTimeshot = currentTimeshot;
					return true;
			}
		} 

		return false;
	}
	
	public boolean checkInfraredAlarm() {
		if (getInfraredAlarmEnabledTime() == null || getInfraredAlarmDisabledTime() == null) {
			return false;
		}
		
		long currentTimeshot = Calendar.getInstance().getTime().getTime() % MILLIS_PER_DAY;
		if (mInfraredAlarmSet && (currentTimeshot > mInfraredAlarmTimeshot + 30)) {
			long enabledTimeshot = getInfraredAlarmEnabledTime().getTime() % MILLIS_PER_DAY;
			long disabledTimeshot = getInfraredAlarmDisabledTime().getTime() % MILLIS_PER_DAY;
			
			if ((currentTimeshot > enabledTimeshot) && (currentTimeshot < disabledTimeshot)) {
					mInfraredAlarmTimeshot = currentTimeshot;
					return true;
			}
		} 

		return false;
	}
	
	private static class ValueFactor {
		
		private static Map<String, ValueFactor> mValueFactors;

		public static ValueFactor newInstance(int factorId) {
			if (mValueFactors == null) {
				mValueFactors = new HashMap<String, SentrySensor.ValueFactor>();
				
				mValueFactors.put("0", new ValueFactor("0", "90"));
				mValueFactors.put("1", new ValueFactor("2.69", "87.31"));
				mValueFactors.put("2", new ValueFactor("5.38", "84.62"));
				mValueFactors.put("3", new ValueFactor("8.08", "81.92"));
				mValueFactors.put("4", new ValueFactor("10.81", "79.19"));
				mValueFactors.put("5", new ValueFactor("13.55", "76.45"));
				mValueFactors.put("6", new ValueFactor("16.33", "73.67"));
				mValueFactors.put("7", new ValueFactor("19.16", "70.84"));
				mValueFactors.put("8", new ValueFactor("22.02", "67.98"));
				mValueFactors.put("9", new ValueFactor("24.95", "65.05"));
				mValueFactors.put("10", new ValueFactor("27.95", "62.05"));
				mValueFactors.put("11", new ValueFactor("31.04", "58.96"));
				mValueFactors.put("12", new ValueFactor("34.23", "55.77"));
				mValueFactors.put("13", new ValueFactor("37.54", "52.46"));
				mValueFactors.put("14", new ValueFactor("41.01", "48.99"));
				mValueFactors.put("15", new ValueFactor("44.68", "45.32"));
				mValueFactors.put("16", new ValueFactor("48.59", "41.41"));
				mValueFactors.put("17", new ValueFactor("52.83", "37.17"));
				mValueFactors.put("18", new ValueFactor("57.54", "32.46"));
				mValueFactors.put("19", new ValueFactor("62.95", "27.05"));
				mValueFactors.put("20", new ValueFactor("69.64", "20.36"));
				mValueFactors.put("21", new ValueFactor("79.86", "10.14"));
				mValueFactors.put("22", new ValueFactor("", ""));
				mValueFactors.put("23", new ValueFactor("", ""));
				mValueFactors.put("24", new ValueFactor("", ""));
				mValueFactors.put("25", new ValueFactor("", ""));
				mValueFactors.put("26", new ValueFactor("", ""));
				mValueFactors.put("27", new ValueFactor("", ""));
				mValueFactors.put("28", new ValueFactor("Shaken ", ""));
				mValueFactors.put("29", new ValueFactor("Shaken ", ""));
				mValueFactors.put("30", new ValueFactor("Shaken ", ""));
				mValueFactors.put("31", new ValueFactor("Shaken ", ""));
				mValueFactors.put("63", new ValueFactor("-2.69", "-87.31"));
				mValueFactors.put("62", new ValueFactor("-5.38", "-84.62"));
				mValueFactors.put("61", new ValueFactor("-8.08", "-81.92"));
				mValueFactors.put("60", new ValueFactor("-10.81", "-79.19"));
				mValueFactors.put("59", new ValueFactor("-13.55", "-76.45"));
				mValueFactors.put("58", new ValueFactor("-16.33", "-73.67"));
				mValueFactors.put("57", new ValueFactor("-19.16", "-70.84"));
				mValueFactors.put("56", new ValueFactor("-22.02", "-67.98"));
				mValueFactors.put("55", new ValueFactor("-24.95", "-65.05"));
				mValueFactors.put("54", new ValueFactor("-27.95", "-62.05"));
				mValueFactors.put("53", new ValueFactor("-31.04", "-58.96"));
				mValueFactors.put("52", new ValueFactor("-34.23", "-55.77"));
				mValueFactors.put("51", new ValueFactor("-37.54", "-52.46"));
				mValueFactors.put("50", new ValueFactor("-41.01", "-48.99"));
				mValueFactors.put("49", new ValueFactor("-44.68", "-45.32"));
				mValueFactors.put("48", new ValueFactor("-48.59", "-41.41"));
				mValueFactors.put("47", new ValueFactor("-52.83", "-37.17"));
				mValueFactors.put("46", new ValueFactor("-57.54", "-32.46"));
				mValueFactors.put("45", new ValueFactor("-62.95", "-27.05"));
				mValueFactors.put("44", new ValueFactor("-69.64", "-20.36"));
				mValueFactors.put("43", new ValueFactor("-79.86", "-10.14"));
				mValueFactors.put("42", new ValueFactor("", ""));
				mValueFactors.put("41", new ValueFactor("", ""));
				mValueFactors.put("40", new ValueFactor("", ""));
				mValueFactors.put("39", new ValueFactor("", ""));
				mValueFactors.put("38", new ValueFactor("", ""));
				mValueFactors.put("37", new ValueFactor("", ""));
				mValueFactors.put("36", new ValueFactor("Shaken ", ""));
				mValueFactors.put("35", new ValueFactor("Shaken ", ""));
				mValueFactors.put("34", new ValueFactor("Shaken ", ""));
				mValueFactors.put("33", new ValueFactor("Shaken ", ""));
				mValueFactors.put("32", new ValueFactor("Shaken ", ""));			
			}
			return mValueFactors.get("" + factorId);
		}
		
		private float mAngleXY;
		private float mAngleZ;
		
		private ValueFactor(String angleXY, String angleZ) {
			mAngleXY = Float.valueOf(angleXY).floatValue();
			mAngleZ = Float.valueOf(angleZ).floatValue();
		}
	}
}
