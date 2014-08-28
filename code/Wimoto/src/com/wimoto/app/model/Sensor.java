package com.wimoto.app.model;

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

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.View;
import com.wimoto.app.bluetooth.BluetoothConnection;
import com.wimoto.app.bluetooth.BluetoothConnection.WimotoProfile;

public class Sensor extends Observable implements Observer {
			
	public static final String CB_DOCUMENT_SENSOR_ID		= "sensorId";
	
	protected BluetoothConnection mConnection;
	protected Document mDocument;
	
	protected String title;
	protected String id;
	
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
		
		setChanged();
		notifyObservers();
	}

	public String getTitle() {
		if ((mDocument == null) && (mConnection != null)) {
			return mConnection.getName();
		}
		
		return title;
	}

	public String getId() {
		if ((mDocument == null) && (mConnection != null)) {
			return mConnection.getId();
		}
		
		return id;
	}

	public WimotoProfile getType() {
		return WimotoProfile.UNDEFINED;
	}

	public boolean isConnected() {
		return (mConnection != null);
	}

	public int getRssi() {
		if (mConnection != null) {
			return mConnection.getRssi();
		}
		return 0; 
	}
	
	public void setDocument(Document document) {
		this.mDocument = document;
		
		if (mDocument != null) {
			id 		= (String) mDocument.getProperty("id");
			title 	= (String) mDocument.getProperty("title");			
			
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

	public Document getDocument() {
		return mDocument;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
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
