package com.wimoto.app.screens.sensor.climate;

import com.wimoto.app.model.demosensors.ClimateDemoSensor;

public class ClimateDemoSensorFragment extends ClimateSensorFragment {

	@Override
	protected void runDemo() {
		((ClimateDemoSensor)mSensor).runDemo();
	}

	@Override
	protected void stopDemo() {
		((ClimateDemoSensor)mSensor).stopDemo();
	}
}
