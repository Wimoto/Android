package com.marknicholas.wimoto.models.sensor;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;

public class ClimateSensor extends Sensor {

	public ClimateSensor() {
		title = MainActivity.getAppContext().getResources().getString(R.string.sensor_climate);
		type = Sensor.TYPE_CLIMATE;
	}
}
