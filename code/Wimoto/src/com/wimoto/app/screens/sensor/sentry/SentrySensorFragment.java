package com.wimoto.app.screens.sensor.sentry;

import java.util.Observable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.SentrySensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;

public class SentrySensorFragment extends SensorFragment {
	
	private TextView mTemperatureTextView;
	private TextView mHumidityTextView;
	
	private AnimationSwitch mTemperatureSwitch;
	private AnimationSwitch mHumiditySwitch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = inflater.inflate(R.layout.sensor_sentry_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperature_text);
		mTemperatureSwitch = (AnimationSwitch)mView.findViewById(R.id.temperature_switch);
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
	}
	
	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	int colorId = R.color.color_sensor_sentry;
        		if (mSensor == null) {
        			colorId = R.color.color_light_gray;
        			
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else if (!mSensor.isConnected()){
        			colorId = R.color.color_light_gray;
      
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else {
        			SentrySensor sentrySensor = (SentrySensor) mSensor;
        			
        			mTemperatureTextView.setText(String.format("%.01f", sentrySensor.getTemperature()));
        			mHumidityTextView.setText(String.format("%.01f", sentrySensor.getHumidity()));
        		}
        		
        		mView.setBackgroundColor(getResources().getColor(colorId));
            }
        });
	}
}
