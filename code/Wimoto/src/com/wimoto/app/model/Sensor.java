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
import java.util.TimerTask;

import android.bluetooth.BluetoothGattCharacteristic;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.View;
import com.wimoto.app.bluetooth.BluetoothConnection;
import com.wimoto.app.bluetooth.BluetoothConnection.WimotoProfile;
import com.wimoto.app.observer.PropertyObservable;

public class Sensor extends PropertyObservable implements Observer {
			
	public static final String CB_DOCUMENT_SENSOR_ID		= "sensorId";
	
	public static final String OBSERVER_FIELD_SENSOR_CONNECTION			= "mConnection";
	public static final String OBSERVER_FIELD_SENSOR_TITLE				= "mTitle";
	public static final String OBSERVER_FIELD_SENSOR_BATTERY_LEVEL		= "mBatteryLevel";
	public static final String OBSERVER_FIELD_SENSOR_RSSI				= "mRssi";
	
	private static final String CB_DOCUMENT_FIELD_SENSOR_ID		= "id";
	private static final String CB_DOCUMENT_FIELD_SENSOR_TITLE	= "title";	
	
	private static final String BLE_GENERIC_SERVICE_UUID_BATTERY		 		= "0000180F-0000-1000-8000-00805F9B34FB";
	private static final String BLE_GENERIC_CHAR_UUID_BATTERY_LEVEL			= "00002A19-0000-1000-8000-00805F9B34FB";
		
	protected BluetoothConnection mConnection;
	protected Document mDocument;
	
	protected String id;
	protected String mTitle;
	private int mBatteryLevel;
	private int mRssi;
	
	private Timer mRssiTimer;
	
	protected Map<String, LinkedList<Float>> mSensorValues;
	
	public static Sensor getSensorFromDocument(Document document) {
		Sensor sensor = null;
		
		Integer property = (Integer)document.getProperty("sensor_type");
		
		WimotoProfile wimotoProfile = WimotoProfile.values()[property.intValue()];
		if (wimotoProfile == WimotoProfile.CLIMATE) {
			sensor = new ClimateSensor();
		} else if (wimotoProfile == WimotoProfile.GROW) {
			sensor = new GrowSensor();
		} else if (wimotoProfile == WimotoProfile.SENTRY) {
			sensor = new SentrySensor();
		} else if (wimotoProfile == WimotoProfile.THERMO) {
			sensor = new ThermoSensor();
		} else if (wimotoProfile == WimotoProfile.WATER) {
			sensor = new WaterSensor();
		} else {
			return null;
		}
		sensor.setDocument(document);
		
		return sensor;
	}

	public static Sensor getSensorFromConnection(BluetoothConnection connection) {
		if (connection.getWimotoProfile() == WimotoProfile.CLIMATE) {
			return new ClimateSensor(connection);
		} else if (connection.getWimotoProfile() == WimotoProfile.GROW) {
			return new GrowSensor(connection);
		} else if (connection.getWimotoProfile() == WimotoProfile.SENTRY) {
			return new SentrySensor(connection);
		} else if (connection.getWimotoProfile() == WimotoProfile.THERMO) {
			return new ThermoSensor(connection);
		} else if (connection.getWimotoProfile() == WimotoProfile.WATER) {
			return new WaterSensor(connection);
		}
		
		return null;
	}
	
	public Sensor() {
		mSensorValues = new HashMap<String, LinkedList<Float>>();
	}
	
	public Sensor(BluetoothConnection connection) {
		setConnection(connection);
	}
		
	public void setConnection(BluetoothConnection connection) {
		if (mConnection != null) {
			mConnection.deleteObserver(this);
		}
		
		notifyObservers(OBSERVER_FIELD_SENSOR_CONNECTION, mConnection, connection);
		
		mConnection = connection;
		if (mConnection == null) {
			if (mRssiTimer != null) {
				mRssiTimer.cancel();
			}
			mRssiTimer = null;
		} else {
			mConnection.addObserver(this);
						
			if (mRssiTimer == null) {
				mRssiTimer = new Timer();

				mRssiTimer.schedule(new TimerTask() {
					@Override
					public void run() {
		        		if (mConnection != null) {
		        			mConnection.readRssi();
		        		}
					}
				}, 0, 1000);
			}
		}		
	}

	public String getTitle() {
		if ((mDocument == null) && (mConnection != null)) {
			return mConnection.getName();
		}
		
		return mTitle;
	}

	public String getId() {
		if ((mDocument == null) && (mConnection != null)) {
			return mConnection.getId();
		}
		
		return id;
	}
	
	public void setTitle(String title) {
		notifyObservers(OBSERVER_FIELD_SENSOR_TITLE, mTitle, title);
		
		mTitle = title;
	
		if (mDocument != null) {
			Map<String, Object> currentProperties = mDocument.getProperties();

			Map<String, Object> newProperties = new HashMap<String, Object>();
			newProperties.putAll(currentProperties);

			newProperties.put(CB_DOCUMENT_FIELD_SENSOR_TITLE, title);
			
			try {
				mDocument.putProperties(newProperties);
			} catch (Exception e) {
				// TODO catch exception
			}
		}		
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.UNDEFINED;
	}

	public float getBatteryLevel() {
		return mBatteryLevel;
	}

	public boolean isConnected() {
		return (mConnection != null);
	}

	public int getRssi() {
		return mRssi; 
	}

	public void setRssi(int rssi) {
		notifyObservers(OBSERVER_FIELD_SENSOR_RSSI, mRssi, rssi);
		
		mRssi = rssi;
	}
	
	public void setDocument(Document document) {
		this.mDocument = document;
		
		if (mDocument != null) {
			id 		= (String) mDocument.getProperty(CB_DOCUMENT_FIELD_SENSOR_ID);
			mTitle 	= (String) mDocument.getProperty(CB_DOCUMENT_FIELD_SENSOR_TITLE);			
			
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
			        startKeys.add(id);
			        startKeys.add(new HashMap<String, Object>());
	
			        List<Object> endKeys = new ArrayList<Object>();
			        endKeys.add(id);
	
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
		if ((mConnection != null) && (mDocument != null)) {
			mConnection.readCharacteristic(BLE_GENERIC_SERVICE_UUID_BATTERY, BLE_GENERIC_CHAR_UUID_BATTERY_LEVEL);
		}
	}
	
	protected void enableAlarm(boolean doEnable, String serviceUuidString, String characteristicUuidString) {
		if (mConnection != null) {
			if (doEnable) {
				byte[] bytes = {(byte) 0x01};
				mConnection.writeCharacteristic(serviceUuidString, characteristicUuidString, bytes);
			} else {
				byte[] bytes = {(byte) 0x00};
				mConnection.writeCharacteristic(serviceUuidString, characteristicUuidString, bytes);
			}
		}
	}
	
	public void writeAlarmValue(int alarmValue, String serviceUuidString, String characteristicUuidString) {
		if (mConnection != null) {
			BigInteger bigInt = BigInteger.valueOf(alarmValue); 
			mConnection.writeCharacteristic(serviceUuidString, characteristicUuidString, bigInt.toByteArray());
		}
	}
	
	public Document getDocument() {
		return mDocument;
	}
	
	@Override
	public void update(Observable observable, Object data) {		
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_GENERIC_CHAR_UUID_BATTERY_LEVEL)) {
				int batteryLevel = Float.valueOf(bi.floatValue()).intValue();
				notifyObservers(OBSERVER_FIELD_SENSOR_BATTERY_LEVEL, mBatteryLevel, batteryLevel);
				mBatteryLevel = batteryLevel;
			}
		} else {
			setRssi((Integer)data);
		}
	}
	
	protected void addValue(String type, float value) {
		SensorValue sensorValue = new SensorValue(mDocument.getDatabase().createDocument());
		sensorValue.setSensorId(id);
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

}
