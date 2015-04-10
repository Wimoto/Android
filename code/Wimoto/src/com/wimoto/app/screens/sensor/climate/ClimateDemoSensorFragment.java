package com.wimoto.app.screens.sensor.climate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wimoto.app.model.demosensors.ClimateDemoSensor;

public class ClimateDemoSensorFragment extends ClimateSensorFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		runDemo();
		
		return mView;
	}
	
	@Override
	public void onStop() {
		stopDemo();
		
		super.onStop();
	}
	
	protected void runDemo() {
		((ClimateDemoSensor)mSensor).runDemo();
	}

	protected void stopDemo() {
		((ClimateDemoSensor)mSensor).stopDemo();
	}
}
