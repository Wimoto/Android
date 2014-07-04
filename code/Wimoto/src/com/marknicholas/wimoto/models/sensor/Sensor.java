package com.marknicholas.wimoto.models.sensor;

public class Sensor {
	
	protected String title;
	protected String id;
	protected String rrsi;
	
	protected boolean registered;

	public Sensor() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRrsi() {
		return rrsi;
	}

	public void setRrsi(String rrsi) {
		this.rrsi = rrsi;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	
	
}
