package com.marknicholas.wimoto.models.sensor;

import com.couchbase.lite.Document;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection.WimotoProfile;

public class Sensor {
	
	public static String BLE_GENERIC_SERVICE_UUID_DEVICE 			= "0000180A-0000-1000-8000-00805F9B34FB";
	
	public static String BLE_GENERIC_CHAR_UUID_SYSTEM_ID 			= "00002A23-0000-1000-8000-00805F9B34FB";
	public static String BLE_GENERIC_CHAR_UUID_MODEL_NUMBER 		= "00002A24-0000-1000-8000-00805F9B34FB";
	
	public static final int TYPE_CLIMATE = 0;
	public static final int TYPE_GROW = 1;
	public static final int TYPE_SENTRY = 2;
	public static final int TYPE_THERMO = 3;
	public static final int TYPE_WATER = 4;
	
	protected BluetoothConnection mConnection;
	
	protected String title;
	protected String id;
	protected String rssi = "";
	
	protected boolean registered;
	protected boolean connected;
	
	protected int type;

	public Sensor() {
		super();
	}
	
	public static Sensor getSensorFromDocument(Document document) {
		Sensor sensor = null;
		
		Integer sensorType = (Integer)document.getProperty("sensor_type");
		
		switch (sensorType) {
			case Sensor.TYPE_CLIMATE: {
				sensor = new ClimateSensor();
				break;
			}
			case Sensor.TYPE_GROW: {
				sensor = new GrowSensor(); 
				break;
			}
			case Sensor.TYPE_SENTRY: {
				sensor = new SentrySensor(); 
				break;
			}
			case Sensor.TYPE_THERMO: {
				sensor = new ThermoSensor();
				break;
			}
			case Sensor.TYPE_WATER: {
				sensor = new WaterSensor();
				break;
			}
		}
		
		String sensorTitle = (String)document.getProperty("title");
		sensor.setTitle(sensorTitle);
		
		String sensorId = (String)document.getProperty("id");
		sensor.setId(sensorId);
		
		sensor.setRegistered(true);
		
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
	
	public Sensor(BluetoothConnection connection) {
		setConnection(connection);		
	}
	
	public void setConnection(BluetoothConnection connection) {
		mConnection = connection;
		
		title = connection.getName();
		id = connection.getId();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRssi() {
		return rssi;
	}

	public void setRssi(String rrsi) {
		this.rssi = rrsi;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public int getType() {
		return type;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	
}
