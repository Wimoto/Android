package com.wimoto.app.model;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import com.couchbase.lite.Document;
import com.wimoto.app.bluetooth.BluetoothConnection;
import com.wimoto.app.bluetooth.BluetoothConnection.WimotoProfile;

public class Sensor extends Observable implements Observer {
		
	protected BluetoothConnection mConnection;
	protected Document mDocument;
	
	protected String title;
	protected String id;
	
	private Timer mRssiTimer;
	
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
	
	protected void addValue() {
		
	}
	
	protected void getLastValues() {
		
	}
}
