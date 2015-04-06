package com.wimoto.app.model;

public enum SensorProfile {
	UNDEFINED(0),
	CLIMATE(1),
	GROW(2),
	SENTRY(3),
	THERMO(4),
	WATER(5),
	CLIMATE_DEMO(6),
	THERMO_DEMO(7);
	
	private int value;
	
	private SensorProfile(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
