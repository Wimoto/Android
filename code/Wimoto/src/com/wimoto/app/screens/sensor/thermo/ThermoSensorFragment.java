package com.wimoto.app.screens.sensor.thermo;

import java.util.Observable;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.ThermoSensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class ThermoSensorFragment extends SensorFragment {
	
	private TextView mTemperatureTextView;
	private TextView mProbeTextView;
	
	private LineSparkView mTemperatureSparkView;
	private LineSparkView mProbeSparkView;
	
	private AnimationSwitch mTemperatureSwitch;
	private AnimationSwitch mProbeSwitch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = inflater.inflate(R.layout.sensor_thermo_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mTemperatureSparkView = (LineSparkView) mView.findViewById(R.id.temperatureSparkView);
		mTemperatureSparkView.setValues(mSensor.getLastValues(ThermoSensor.THERMO_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.BLACK);
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperature_text);
		mTemperatureSwitch = (AnimationSwitch)mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mProbeSparkView = (LineSparkView) mView.findViewById(R.id.probeSparkView);
		mProbeSparkView.setValues(mSensor.getLastValues(ThermoSensor.THERMO_PROBE));
		mProbeSparkView.setBackgroundColor(Color.TRANSPARENT);
		mProbeSparkView.setLineColor(Color.BLACK);
		
		mProbeTextView = (TextView) mView.findViewById(R.id.probe_text);
		mProbeSwitch = (AnimationSwitch)mView.findViewById(R.id.probe_switch);
		mProbeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
	}
	
	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_thermo;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
        		if (mSensor == null) {        			
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mProbeTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else if (!mSensor.isConnected()){      
        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
        			mProbeTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else {
        			ThermoSensor thermoSensor = (ThermoSensor) mSensor;
        			
        			mTemperatureTextView.setText(String.format("%.01f", thermoSensor.getTemperature()));
        			mProbeTextView.setText(String.format("%.01f", thermoSensor.getProbe()));
        		}        		
            }
        });
	}
}
