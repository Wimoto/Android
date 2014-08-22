package com.marknicholas.wimoto.screens.sensor.grow;

import java.util.Observable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.model.GrowSensor;
import com.marknicholas.wimoto.screens.sensor.SensorFragment;
import com.marknicholas.wimoto.widgets.AnimationSwitch;
import com.marknicholas.wimoto.widgets.AnimationSwitch.OnCheckedChangeListener;

public class GrowSensorFragment extends SensorFragment {
	
	private TextView mLightTextView;
	private TextView mMoistureTextView;
	private TextView mTemperatureTextView;
	
	private AnimationSwitch mLightSwitch;
	private AnimationSwitch mMoistureSwitch;
	private AnimationSwitch mTemperatureSwitch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = inflater.inflate(R.layout.sensor_grow_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mLightTextView = (TextView) mView.findViewById(R.id.light_text);
		mLightSwitch = (AnimationSwitch)mView.findViewById(R.id.light_switch);
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mMoistureTextView = (TextView) mView.findViewById(R.id.moisture_text);
		mMoistureSwitch = (AnimationSwitch)mView.findViewById(R.id.moisture_switch);
		mMoistureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperature_text);
		mTemperatureSwitch = (AnimationSwitch)mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
            	int colorId = R.color.color_sensor_grow;
        		if (mSensor == null) {
        			colorId = R.color.color_light_gray;
        			
        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
        			mMoistureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else if (!mSensor.isConnected()){
        			colorId = R.color.color_light_gray;
      
        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
        			mMoistureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else {
        			GrowSensor growSensor = (GrowSensor) mSensor;
        			
        			mLightTextView.setText(String.format("%.00f", growSensor.getLight()));
        			mMoistureTextView.setText(String.format("%.01f", growSensor.getMoisture()));
        			mTemperatureTextView.setText(String.format("%.01f", growSensor.getTemperature()));
        		}
        		
        		mView.setBackgroundColor(getResources().getColor(colorId));
            }
        });
	}
}
