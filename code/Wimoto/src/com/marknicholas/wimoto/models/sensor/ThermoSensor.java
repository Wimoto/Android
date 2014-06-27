package com.marknicholas.wimoto.models.sensor;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;

public class ThermoSensor extends Sensor {

	public ThermoSensor() {
		title = MainActivity.getAppContext().getResources().getString(R.string.sensor_thermo);
	}
}
