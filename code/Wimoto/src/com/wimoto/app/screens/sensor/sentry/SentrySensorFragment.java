package com.wimoto.app.screens.sensor.sentry;

import java.util.Observable;

import android.graphics.Color;
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
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class SentrySensorFragment extends SensorFragment {
	
	private TextView mAccelerometerTextView;
	private TextView mHumidityTextView;
	
	private LineSparkView mAccelerometerSparkView;
	private LineSparkView mInfaredSparkView;
	
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
		
		mAccelerometerSparkView = (LineSparkView) mView.findViewById(R.id.accelerometerSparkView);
		mAccelerometerSparkView.setValues(mSensor.getLastValues(SentrySensor.SENTRY_ACCELEROMETER));
		mAccelerometerSparkView.setBackgroundColor(Color.TRANSPARENT);
		mAccelerometerSparkView.setLineColor(Color.BLACK);
		
		mAccelerometerTextView = (TextView) mView.findViewById(R.id.temperature_text);
		mTemperatureSwitch = (AnimationSwitch)mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mInfaredSparkView = (LineSparkView) mView.findViewById(R.id.infaredSparkView);
		mInfaredSparkView.setValues(mSensor.getLastValues(SentrySensor.SENTRY_INFARED));
		mInfaredSparkView.setBackgroundColor(Color.TRANSPARENT);
		mInfaredSparkView.setLineColor(Color.BLACK);
		
		mHumidityTextView = (TextView) mView.findViewById(R.id.humidity_text);
		mHumiditySwitch = (AnimationSwitch)mView.findViewById(R.id.humidity_switch);
		mHumiditySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
	}
	
	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_sentry;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
        		if (mSensor == null) {        			
        			mAccelerometerTextView.setText(getString(R.string.sensor_two_hyphens));
        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else if (!mSensor.isConnected()){      
        			mAccelerometerTextView.setText(getString(R.string.sensor_two_hyphens));
        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else {
        			SentrySensor sentrySensor = (SentrySensor) mSensor;
        			
        			mAccelerometerTextView.setText(String.format("%.01f", sentrySensor.getAccelerometer()));
        			mHumidityTextView.setText(String.format("%.01f", sentrySensor.getInfared()));
        		}        		
            }
        });
	}
}
