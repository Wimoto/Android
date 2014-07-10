package com.marknicholas.wimoto.models.sensor;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;

public class GrowSensor extends Sensor {
	
	public GrowSensor() {
		title = MainActivity.getAppContext().getResources().getString(R.string.sensor_grow);
		type = Sensor.TYPE_GROW;
	}
}
