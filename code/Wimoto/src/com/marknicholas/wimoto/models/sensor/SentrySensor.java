package com.marknicholas.wimoto.models.sensor;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;

public class SentrySensor extends Sensor {
	
	public SentrySensor() {
		title = MainActivity.getAppContext().getResources().getString(R.string.sensor_sentry);
	}
}
