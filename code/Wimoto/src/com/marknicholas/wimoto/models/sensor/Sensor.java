package com.marknicholas.wimoto.models.sensor;

import com.couchbase.lite.Document;

public class Sensor {
	
	public static final int TYPE_CLIMATE = 0;
	public static final int TYPE_GROW = 1;
	public static final int TYPE_SENTRY = 2;
	public static final int TYPE_THERMO = 3;
	public static final int TYPE_WATER = 4;
	
	protected String title;
	protected String id;
	protected String rssi;
	
	protected boolean registered;
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
		
		String sensorRssi = (String)document.getProperty("rssi");
		sensor.setRssi(sensorRssi);
		
		sensor.setRegistered(true);
		
		return sensor;
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
	
	
}
