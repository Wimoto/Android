package com.wimoto.app.screens.sensor.climate;

import java.util.Observable;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.ClimateSensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class ClimateSensorFragment extends SensorFragment {
	
	private TextView mTemperatureTextView;
	private TextView mHumidityTextView;
	private TextView mLightTextView;
	
	private LineSparkView mTemperatureSparkView;
	private LineSparkView mHumiditySparkView;
	private LineSparkView mLightSparkView;
	
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
		
		mTemperatureSparkView = (LineSparkView) mView.findViewById(R.id.temperatureSparkView);
		mTemperatureSparkView.setValues(mSensor.getLastValues(ClimateSensor.CLIMATE_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.BLACK);
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperatureTextView);
		mTemperatureSwitch = (AnimationSwitch) mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				mSensor.enableAlarms(true);
			}
		});
		
		mHumiditySparkView = (LineSparkView) mView.findViewById(R.id.humiditySparkView);
		mHumiditySparkView.setValues(mSensor.getLastValues(ClimateSensor.CLIMATE_HUMIDITY));
		mHumiditySparkView.setBackgroundColor(Color.TRANSPARENT);
		mHumiditySparkView.setLineColor(Color.BLACK);
		
		mHumidityTextView = (TextView) mView.findViewById(R.id.humidity_text);
		mHumiditySwitch = (AnimationSwitch)mView.findViewById(R.id.humidity_switch);
		mHumiditySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mLightSparkView = (LineSparkView) mView.findViewById(R.id.lightSparkView);
		mLightSparkView.setValues(mSensor.getLastValues(ClimateSensor.CLIMATE_LIGHT));
		mLightSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLightSparkView.setLineColor(Color.BLACK);
		
		mLightTextView = (TextView) mView.findViewById(R.id.light_text);
		mLightSwitch = (AnimationSwitch)mView.findViewById(R.id.light_switch);
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
	}
	
	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_climate;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
        		if (mSensor == null) {
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else if (!mSensor.isConnected()){
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else {
        			ClimateSensor climateSensor = (ClimateSensor) mSensor;
        			
        			mTemperatureTextView.setText(String.format("%.01f", climateSensor.getTemperature()));
        			mHumidityTextView.setText(String.format("%.01f", climateSensor.getHumidity()));
        			mLightTextView.setText(String.format("%.00f", climateSensor.getLight()));
        			
        			mTemperatureSparkView.invalidate();
        		}        		
            }
        });
	}
}
