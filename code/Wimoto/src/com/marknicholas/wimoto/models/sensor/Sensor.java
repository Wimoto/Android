package com.marknicholas.wimoto.models.sensor;

public class Sensor {
	
	protected String title;
	protected String id;
	protected String rrsi;

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
	
	
}
