package com.wimoto.app.screens.sensor.thermo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wimoto.app.model.demosensors.ThermoDemoSensor;

public class ThermoDemoSensorFragment extends ThermoSensorFragment {

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
		((ThermoDemoSensor)mSensor).runDemo();
	}

	protected void stopDemo() {
		((ThermoDemoSensor)mSensor).stopDemo();
	}
}
