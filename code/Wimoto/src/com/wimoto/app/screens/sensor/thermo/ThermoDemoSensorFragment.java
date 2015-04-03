package com.wimoto.app.screens.sensor.thermo;

import com.wimoto.app.model.demosensors.ThermoDemoSensor;

public class ThermoDemoSensorFragment extends ThermoSensorFragment {

	@Override
	protected void runDemo() {
		((ThermoDemoSensor)mSensor).runDemo();
	}

	@Override
	protected void stopDemo() {
		((ThermoDemoSensor)mSensor).stopDemo();
	}
}
