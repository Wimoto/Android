package com.marknicholas.wimoto.models.sensor;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;

public class WaterSensor extends Sensor {
	
	public WaterSensor() {
		title = MainActivity.getAppContext().getResources().getString(R.string.sensor_water);
	}
}
