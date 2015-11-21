package com.wimoto.app.model.sensors;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Timer;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.View;
import com.mobitexoft.utils.SHA256Hash;
import com.mobitexoft.utils.propertyobserver.PropertyObservable;
import com.wimoto.app.AppContext;
import com.wimoto.app.bluetooth.WimotoDevice;
import com.wimoto.app.bluetooth.WimotoDevice.State;
import com.wimoto.app.model.SensorValue;
import com.wimoto.app.model.SensorValueListener;
import com.wimoto.app.model.datalog.DataLog;
import com.wimoto.app.model.demosensors.ClimateDemoSensor;
import com.wimoto.app.model.demosensors.ThermoDemoSensor;

public abstract class Sensor extends PropertyObservable implements Observer, WimotoDevice.WimotoDeviceCallback {
			
	public interface DataReadingListener {
		void didReadSensorDataLogger(ArrayList<DataLog> data);
		void didUpdateSensorReadingData(ArrayList<SensorValue> data);
	}
	
	public static final String SENSOR_FIELD_ID							= "id";
	public static final String SENSOR_FIELD_TITLE						= "title";
	public static final String SENSOR_FIELD_STATE						= "mState";
	public static final String SENSOR_FIELD_CONNECTION					= "mConnection";
	public static final String SENSOR_FIELD_DEVICE						= "mWimotoDevice";
	public static final String SENSOR_FIELD_BATTERY_LEVEL				= "batteryLevel";
	public static final String SENSOR_FIELD_RSSI						= "rssi";
	public static final String SENSOR_FIELD_DL_STATE					= "mDataLoggerState";
	
	private static final String BLE_GENERIC_SERVICE_UUID_BATTERY		 	= "0000180F-0000-1000-8000-00805F9B34FB";
	private static final String BLE_GENERIC_CHAR_UUID_BATTERY_LEVEL			= "00002A19-0000-1000-8000-00805F9B34FB";
	
	public enum DataLoggerState {
		NONE(0),
		UNKNOWN(1),
		DISABLED(2),
		ENABLED(3),
		READ(4);
		
		private int value;
		
		private DataLoggerState(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
		
	protected AppContext mContext;
	
	protected Document mDocument;
	protected WimotoDevice mWimotoDevice;
	
	protected String mId;
	protected String mTitle;
	protected State mState;
	
	private DataReadingListener mDataReadingListener;
	private DataLoggerState mDataLoggerState;
	private ArrayList<DataLog> mSensorDataLogs;
	
	protected String mDataLoggerServiceString;
	protected String mDataLoggerEnableCharacteristicString;
	protected String mDataLoggerReadEnableCharacteristicString;
	protected String mDataLoggerReadNotificationCharacteristicString;
	
	private int mBatteryLevel;
	private int mRssi;
	
	private Timer mRssiTimer;
	
	protected Map<String, LinkedList<Float>> mSensorValues;
	
	public static Sensor getSensorFromDocument(AppContext context, Document document) {
		Sensor sensor = null;
		
		Integer property = (Integer)document.getProperty("sensor_type");
		
		WimotoDevice.Profile profile = WimotoDevice.Profile.values()[property.intValue()];
		if (profile == WimotoDevice.Profile.CLIMATE) {
			sensor = new ClimateSensor(context);
		} else if (profile == WimotoDevice.Profile.GROW) {
			sensor = new GrowSensor(context);
		} else if (profile == WimotoDevice.Profile.SENTRY) {
			sensor = new SentrySensor(context);
		} else if (profile == WimotoDevice.Profile.THERMO) {
			sensor = new ThermoSensor(context);
		} else if (profile == WimotoDevice.Profile.WATER) {
			sensor = new WaterSensor(context);
		} else if (profile == WimotoDevice.Profile.CLIMATE_DEMO) {
			sensor = new ClimateDemoSensor(context);
		} else if (profile == WimotoDevice.Profile.THERMO_DEMO) {
			sensor = new ThermoDemoSensor(context);
		}
		sensor.setDocument(document);
		
		return sensor;
	}

	public Sensor(AppContext context) {
		mContext = context;
		
		mSensorValues = new HashMap<String, LinkedList<Float>>();
		
		setDataLoggerState(DataLoggerState.NONE);
	}
	
	public abstract WimotoDevice.Profile getProfile();
	public abstract String getCodename();
	
//	public Sensor(BluetoothConnection connection) {
//		setConnection(connection);
//	}
			
//	public void setConnection(BluetoothConnection connection) {
//		if (mConnection != null) {
//			mConnection.deleteObserver(this);
//		}
//		
//		notifyObservers(SENSOR_FIELD_CONNECTION, mConnection, connection);
//		
//		mConnection = connection;
//		mConnection.connect();
//		if (mConnection == null) {
////			if (mRssiTimer != null) {
////				mRssiTimer.cancel();
////			}
////			mRssiTimer = null;
//		} else {
//			mConnection.addObserver(this);
////						
////			if (mRssiTimer == null) {
////				mRssiTimer = new Timer();
////
////				mRssiTimer.schedule(new TimerTask() {
////					@Override
////					public void run() {
////		        		if (mConnection != null) {
////		        			mConnection.readRssi();
////		        		}
////					}
////				}, 0, 1000);
////			}
//		}		
//	}

	public void connectDevice(WimotoDevice device) {
		mWimotoDevice = device; 
		mWimotoDevice.connect(this);
	}
	
	public void disconnect() {
		mWimotoDevice.disconnect();
	}
	
	public String getId() {
		return mId;
	}
	
	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		notifyObservers(SENSOR_FIELD_TITLE, mTitle, title);
		
		mTitle = title;
	
		if (mDocument != null) {
			Map<String, Object> currentProperties = mDocument.getProperties();

			Map<String, Object> newProperties = new HashMap<String, Object>();
			newProperties.putAll(currentProperties);

			newProperties.put(SENSOR_FIELD_TITLE, title);
			
			try {
				mDocument.putProperties(newProperties);
			} catch (Exception e) {
				// TODO catch exception
			}
		}		
	}
	
	public void setState(State state) {
		notifyObservers(SENSOR_FIELD_STATE, (mState == null) ? 0: mState.getValue(), (state == null) ? 0: state.getValue());
		mState = state;
		
		if (mState == State.CONNECTED) {
			setDataLoggerState(DataLoggerState.UNKNOWN);
		}
	}
		
	public float getBatteryLevel() {
		return mBatteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		notifyObservers(SENSOR_FIELD_BATTERY_LEVEL, mBatteryLevel, batteryLevel);
		mBatteryLevel = batteryLevel;
	}

	public int getRssi() {
		return mRssi; 
	}

	public void setRssi(int rssi) {
		notifyObservers(SENSOR_FIELD_RSSI, mRssi, rssi);
		
		mRssi = rssi;
	}
	
	public void setDataReadingListener(DataReadingListener listener) {
		this.mDataReadingListener = listener;
	}
	
	protected void setDataLoggerState(DataLoggerState dataLoggerState) {
		notifyObservers(SENSOR_FIELD_DL_STATE, mDataLoggerState, dataLoggerState);
		this.mDataLoggerState = dataLoggerState;
		
		if (mDataLoggerState == DataLoggerState.ENABLED) {
			this.mSensorDataLogs = new ArrayList<DataLog>();
		} else if (mDataLoggerState == DataLoggerState.DISABLED) {
			if (mSensorDataLogs != null) {
				mDataReadingListener.didReadSensorDataLogger(mSensorDataLogs);
			}
			mSensorDataLogs = null;
		}
	}
	
	protected DataLoggerState getDataLoggerStateForCharacteristic(BluetoothGattCharacteristic characteristic) {
		byte[] bytes = characteristic.getValue();
		return ((bytes[0] & 0xff) == 1) ? DataLoggerState.ENABLED : DataLoggerState.DISABLED;
	}
	
	public void enableDataLogger(boolean doEnable) {
		setDataLoggerState(DataLoggerState.UNKNOWN);
		
		if ((mState == State.CONNECTED) && (mWimotoDevice != null)) {			
			byte[] bytes = {(byte) 0x01};
			mWimotoDevice.writeCharacteristic(mDataLoggerServiceString, mDataLoggerEnableCharacteristicString, bytes);
		}
	}
	
	public void readDataLogger() {
		setDataLoggerState(DataLoggerState.READ);
		
		if ((mState == State.CONNECTED) && (mWimotoDevice != null)) {
			mWimotoDevice.enableChangesNotification(mDataLoggerServiceString, mDataLoggerReadNotificationCharacteristicString);
			
			byte[] bytes = {(byte) 0x01};
			mWimotoDevice.writeCharacteristic(mDataLoggerServiceString, mDataLoggerReadEnableCharacteristicString, bytes);
		}
	}
	
	protected void writeSensorDataLog(DataLog dataLog) {
		Log.e("DataLog", "WriteDataLog");
		mSensorDataLogs.add(dataLog);
	}
	
	public void setDocument(Document document) {
		this.mDocument = document;
		
		if (mDocument != null) {
			mId 		= (String) mDocument.getProperty(SENSOR_FIELD_ID);
			mTitle 	= (String) mDocument.getProperty(SENSOR_FIELD_TITLE);			
			
			Database database = mDocument.getDatabase(); 
			
			Set<String> keySet = mSensorValues.keySet();
			
			for (String key:keySet) {
				final String finalKey = key; 
				
				View view = database.getView(finalKey);
				if (view.getMap() == null) {
					Mapper mapper = new Mapper() {
						@Override
						public void map(Map<String, Object> document, Emitter emitter) {
							String type = (String) document.get(SensorValue.CB_DOCUMENT_TYPE);
		                    if (finalKey.equals(type)) {
		                        List<Object> keys = new ArrayList<Object>();
		                        keys.add(document.get(SensorValue.CB_DOCUMENT_SENSOR_VALUE_SENSOR_ID));
		                        keys.add(document.get(SensorValue.CB_DOCUMENT_SENSOR_VALUE_CREATED_AT));
		                        emitter.emit(keys, document);
		                    }
						}
					};
					view.setMap(mapper, "1.0");
				}
				
				try {
					LinkedList<Float> list = mSensorValues.get(finalKey);
					
					Query query = view.createQuery();
					query.setDescending(true);
					
			        List<Object> startKeys = new ArrayList<Object>();
			        startKeys.add(mId);
			        startKeys.add(new HashMap<String, Object>());
	
			        List<Object> endKeys = new ArrayList<Object>();
			        endKeys.add(mId);
	
			        query.setStartKey(startKeys);
			        query.setEndKey(endKeys);
					
					QueryEnumerator enumerator = query.run();
					
					while (enumerator.hasNext()) {
						SensorValue sensorValue = new SensorValue(enumerator.next().getDocument());
						list.add(sensorValue.getValue());				
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void requestSensorValues(SensorValueListener listener) {
		ArrayList<SensorValue> sensorValues = new ArrayList<SensorValue>();
		
		Database database = mDocument.getDatabase(); 
		
		Set<String> keySet = mSensorValues.keySet();
		
		for (String key:keySet) {
			final String finalKey = key; 
			
			View view = database.getView(finalKey);
			if (view.getMap() == null) {
				Mapper mapper = new Mapper() {
					@Override
					public void map(Map<String, Object> document, Emitter emitter) {
						String type = (String) document.get(SensorValue.CB_DOCUMENT_TYPE);
	                    if (finalKey.equals(type)) {
	                        List<Object> keys = new ArrayList<Object>();
	                        keys.add(document.get(SensorValue.CB_DOCUMENT_SENSOR_VALUE_SENSOR_ID));
	                        keys.add(document.get(SensorValue.CB_DOCUMENT_SENSOR_VALUE_CREATED_AT));
	                        emitter.emit(keys, document);
	                    }
					}
				};
				view.setMap(mapper, "1.0");
			}
			
			try {
				Query query = view.createQuery();
				query.setDescending(true);
				
		        List<Object> startKeys = new ArrayList<Object>();
		        startKeys.add(mId);
		        startKeys.add(new HashMap<String, Object>());

		        List<Object> endKeys = new ArrayList<Object>();
		        endKeys.add(mId);

		        query.setStartKey(startKeys);
		        query.setEndKey(endKeys);
				
				QueryEnumerator enumerator = query.run();
				
				while (enumerator.hasNext()) {
					SensorValue sensorValue = new SensorValue(enumerator.next().getDocument());
					sensorValues.add(sensorValue);				
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		listener.onSensorValuesReturned(sensorValues);
	}

	protected void initiateSensorCharacteristics() {
//		if ((mConnection != null) && (mDocument != null)) {
//			mConnection.readCharacteristic(BLE_GENERIC_SERVICE_UUID_BATTERY, BLE_GENERIC_CHAR_UUID_BATTERY_LEVEL);
//		}
	}
	
	protected void enableAlarm(boolean doEnable, String serviceUuidString, String characteristicUuidString) {
		if (mWimotoDevice != null) {
			if (doEnable) {
				byte[] bytes = {(byte) 0x01};
				mWimotoDevice.writeCharacteristic(serviceUuidString, characteristicUuidString, bytes);
			} else {
				byte[] bytes = {(byte) 0x00};
				mWimotoDevice.writeCharacteristic(serviceUuidString, characteristicUuidString, bytes);
			}
		}
	}
	
	protected void writeAlarmValue(int alarmValue, String serviceUuidString, String characteristicUuidString) {
		if (mWimotoDevice != null) {
			BigInteger bigInt = BigInteger.valueOf(alarmValue); 
			Log.e("", "writeAlarmValue _" + SHA256Hash.toHexString(bigInt.toByteArray()) + " for " + alarmValue);
			mWimotoDevice.writeCharacteristic(serviceUuidString, characteristicUuidString, bigInt.toByteArray());
		}
	}
	
	public Document getDocument() {
		return mDocument;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		Log.e("", "Sensor update");
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_GENERIC_CHAR_UUID_BATTERY_LEVEL)) {
				setBatteryLevel(Float.valueOf(bi.floatValue()).intValue());
			}
		} else {
			setRssi((Integer)data);
		}
	}
	
	protected void addValue(String type, float value) {
		SensorValue sensorValue = new SensorValue(mDocument.getDatabase().createDocument());
		sensorValue.setSensorId(mId);
		sensorValue.setType(type);
		sensorValue.setValue(value);
		
		sensorValue.save();
		
		LinkedList<Float> list = (LinkedList<Float>) mSensorValues.get(type);
		list.addLast(Float.valueOf(value));
		
//		if (list.size() > 20) {
//			list.removeFirst();
//		}
	}
	
	protected float roundToOne(float value) {
		return Math.round(value*10)/10;
	}
	
	public LinkedList<Float> getLastValues(String valueType) {
		LinkedList<Float> list = (LinkedList<Float>) mSensorValues.get(valueType);
		
		int listSize = list.size();
		
		if (list.size() <= 20) {
			return list;
		}
		
		LinkedList<Float> resultList = new LinkedList<Float>();
		for (int index = (listSize - 20); index < listSize; index++) {
			resultList.addLast(list.get(index));
		}
		
		return resultList;
	}
	
	public static float fahrToCels(float fahrValue) {
		return (fahrValue - 32.0f) * 5.0f / 9.0f;
	}
	
	public static float celsToFahr(float celsValue) {
		return celsValue * 9.0f / 5.0f + 32.0f; 
	}

	
	// WimotoDeviceCallback
	@Override
	public void onConnectionStateChange(State state) {
		Log.e("", "Sensor onConnectionStateChange " + state);
		
		setState(state);
	}
	
	@Override
	public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
		// TODO Auto-generated method stub
	}	
	
	@Override
	public void onCharacteristicWritten(BluetoothGattCharacteristic characteristic, int status) {
		String uuid = characteristic.getUuid().toString().toUpperCase();
		
		if (uuid.equals(mDataLoggerEnableCharacteristicString)) {
			if (status != 0) {
				setDataLoggerState(DataLoggerState.DISABLED);
			} else {
				mWimotoDevice.readCharacteristic(mDataLoggerServiceString, mDataLoggerEnableCharacteristicString);
			}
		} else if (uuid.equals(mDataLoggerReadEnableCharacteristicString)) {
			setDataLoggerState(DataLoggerState.DISABLED);
		}
	}
}
