package com.wimoto.app.model;

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
import com.wimoto.app.model.demosensors.ClimateDemoSensor;
import com.wimoto.app.model.demosensors.ThermoDemoSensor;

public abstract class Sensor extends PropertyObservable implements Observer, WimotoDevice.WimotoDeviceCallback {
			
	public static final String SENSOR_FIELD_ID							= "id";
	public static final String SENSOR_FIELD_TITLE						= "title";
	public static final String SENSOR_FIELD_STATE						= "mState";
	public static final String SENSOR_FIELD_CONNECTION					= "mConnection";
	public static final String SENSOR_FIELD_DEVICE						= "mDevice";
	public static final String SENSOR_FIELD_BATTERY_LEVEL				= "batteryLevel";
	public static final String SENSOR_FIELD_RSSI						= "rssi";
	
	private static final String BLE_GENERIC_SERVICE_UUID_BATTERY		 	= "0000180F-0000-1000-8000-00805F9B34FB";
	private static final String BLE_GENERIC_CHAR_UUID_BATTERY_LEVEL			= "00002A19-0000-1000-8000-00805F9B34FB";
		
	protected AppContext mContext;
	
	protected Document mDocument;
	protected WimotoDevice mWimotoDevice;
	
	protected String mId;
	protected String mTitle;
	protected State mState;
	
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
	}
	
	public abstract WimotoDevice.Profile getProfile();
	
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
		notifyObservers(SENSOR_FIELD_DEVICE, mWimotoDevice, device);
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
		
		if (list.size() > 20) {
			list.removeFirst();
		}
	}
	
	public LinkedList<Float> getLastValues(String valueType) {
		return mSensorValues.get(valueType);
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
}
