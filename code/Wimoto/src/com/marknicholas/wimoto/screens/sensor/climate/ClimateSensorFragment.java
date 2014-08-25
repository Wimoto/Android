package com.marknicholas.wimoto.screens.sensor.climate;

import java.util.Observable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wimoto.app.R;
import com.marknicholas.wimoto.model.ClimateSensor;
import com.marknicholas.wimoto.screens.sensor.SensorFragment;
import com.marknicholas.wimoto.widgets.AnimationSwitch;
import com.marknicholas.wimoto.widgets.AnimationSwitch.OnCheckedChangeListener;

public class ClimateSensorFragment extends SensorFragment {
	
	private TextView mTemperatureTextView;
	private TextView mHumidityTextView;
	private TextView mLightTextView;
	
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
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperature_text);
		mTemperatureSwitch = (AnimationSwitch) mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mHumidityTextView = (TextView) mView.findViewById(R.id.humidity_text);
		mHumiditySwitch = (AnimationSwitch)mView.findViewById(R.id.humidity_switch);
		mHumiditySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mLightTextView = (TextView) mView.findViewById(R.id.light_text);
		mLightSwitch = (AnimationSwitch)mView.findViewById(R.id.light_switch);
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
	}
	
	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	int colorId = R.color.color_sensor_climate;
        		if (mSensor == null) {
        			colorId = R.color.color_light_gray;
        			
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else if (!mSensor.isConnected()){
        			colorId = R.color.color_light_gray;
      
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else {
        			ClimateSensor climateSensor = (ClimateSensor) mSensor;
        			
        			mTemperatureTextView.setText(String.format("%.01f", climateSensor.getTemperature()));
        			mHumidityTextView.setText(String.format("%.01f", climateSensor.getHumidity()));
        			mLightTextView.setText(String.format("%.00f", climateSensor.getLight()));
        		}
        		
        		mView.setBackgroundColor(getResources().getColor(colorId));
            }
        });
	}
}
