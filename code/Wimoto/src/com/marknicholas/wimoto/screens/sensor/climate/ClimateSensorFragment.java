package com.marknicholas.wimoto.screens.sensor.climate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.screens.sensor.SensorFragment;
import com.marknicholas.wimoto.widgets.AnimationSwitch;
import com.marknicholas.wimoto.widgets.AnimationSwitch.OnCheckedChangeListener;

public class ClimateSensorFragment extends SensorFragment {
	
	private AnimationSwitch mTemperatureSwitch;
	private AnimationSwitch mHumiditySwitch;
	private AnimationSwitch mLightSwitch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = inflater.inflate(R.layout.sensor_climate_fragment, null);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		super.initViews();
		
		mTemperatureSwitch = (AnimationSwitch)mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		
		mHumiditySwitch = (AnimationSwitch)mView.findViewById(R.id.humidity_switch);
		mHumiditySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mLightSwitch = (AnimationSwitch)mView.findViewById(R.id.light_switch);
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
	}
}
