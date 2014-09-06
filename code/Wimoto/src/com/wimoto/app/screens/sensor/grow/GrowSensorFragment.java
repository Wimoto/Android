package com.wimoto.app.screens.sensor.grow;

import java.util.Observable;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.GrowSensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class GrowSensorFragment extends SensorFragment {
	
	private TextView mLightTextView;
	private TextView mMoistureTextView;
	private TextView mTemperatureTextView;
	
	private LineSparkView mLightSparkView;
	private LineSparkView mMoistureSparkView;
	private LineSparkView mTemperatureSparkView;
	
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
		
		mLightSparkView = (LineSparkView) mView.findViewById(R.id.lightSparkView);
		mLightSparkView.setValues(mSensor.getLastValues(GrowSensor.GROW_LIGHT));
		mLightSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLightSparkView.setLineColor(Color.BLACK);
		
		mLightTextView = (TextView) mView.findViewById(R.id.light_text);
		mLightSwitch = (AnimationSwitch)mView.findViewById(R.id.light_switch);
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mMoistureSparkView = (LineSparkView) mView.findViewById(R.id.moistureSparkView);
		mMoistureSparkView.setValues(mSensor.getLastValues(GrowSensor.GROW_MOISTURE));
		mMoistureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mMoistureSparkView.setLineColor(Color.BLACK);
		
		mMoistureTextView = (TextView) mView.findViewById(R.id.moisture_text);
		mMoistureSwitch = (AnimationSwitch)mView.findViewById(R.id.moisture_switch);
		mMoistureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mTemperatureSparkView = (LineSparkView) mView.findViewById(R.id.temperatureSparkView);
		mTemperatureSparkView.setValues(mSensor.getLastValues(GrowSensor.GROW_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.BLACK);
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperature_text);
		mTemperatureSwitch = (AnimationSwitch)mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
	}
	
	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_grow;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		
		getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
        		if (mSensor == null) {
        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
        			mMoistureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else if (!mSensor.isConnected()){
        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
        			mMoistureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else {
        			GrowSensor growSensor = (GrowSensor) mSensor;
        			
        			mLightTextView.setText(String.format("%.00f", growSensor.getLight()));
        			mMoistureTextView.setText(String.format("%.01f", growSensor.getMoisture()));
        			mTemperatureTextView.setText(String.format("%.01f", growSensor.getTemperature()));
        		}        		
            }
        });
	}
}
