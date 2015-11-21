package com.wimoto.app.model;

import java.util.ArrayList;

public interface SensorValueListener {
	void onSensorValuesReturned(ArrayList<SensorValue> sensorValues);
}
