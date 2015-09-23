package com.wimoto.app.screens.sensor.thermo;

import java.beans.PropertyChangeEvent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wimoto.app.R;
import com.wimoto.app.model.Sensor;
import com.wimoto.app.model.ThermoSensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.pickers.AlarmPickerTemperatureView;
import com.wimoto.app.widgets.pickers.AlarmPickerView.AlarmPickerListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;
import com.wimoto.app.widgets.temperature.TemperatureValueTextView;
import com.wimoto.app.widgets.temperature.TemperatureValueView;

public class ThermoSensorFragment extends SensorFragment {
	
	private TemperatureValueView mTemperatureTextView;
	private TemperatureValueView mProbeTextView;
	
	private LineSparkView mTemperatureSparkView;
	private LineSparkView mProbeSparkView;
	
	private LinearLayout mTemperatureAlarmLayout;
	private LinearLayout mProbeAlarmLayout;
	
	private AnimationSwitch mTemperatureSwitch;
	private AnimationSwitch mProbeSwitch;
	
	private TemperatureValueTextView mTemperatureAlarmLowTextView;
	private TemperatureValueTextView mTemperatureAlarmHighTextView;	

	private TemperatureValueTextView mProbeAlarmLowTextView;
	private TemperatureValueTextView mProbeAlarmHighTextView;
	
	private AlarmPickerTemperatureView mAlarmTemperaturePickerView;
	private AlarmPickerTemperatureView mAlarmProbePickerView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = (ViewGroup)inflater.inflate(R.layout.sensor_thermo_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mTemperatureSparkView = (LineSparkView) mView.findViewById(R.id.temperatureSparkView);
		mTemperatureSparkView.setValues(mSensor.getLastValues(ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.WHITE);
		
		mTemperatureTextView = (TemperatureValueView) mView.findViewById(R.id.temperatureTextView);
		mTemperatureTextView.setText(Float.toString(((ThermoSensor)mSensor).getTemperature()));
		
		mTemperatureAlarmLayout = (LinearLayout) mView.findViewById(R.id.temperatureAlarmLayout);
		
		mAlarmTemperaturePickerView = new AlarmPickerTemperatureView(getActivity(), ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE, -60, 130, 
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmTemperaturePickerView);
				
				ThermoSensor thermoSensor = (ThermoSensor) mSensor;
				thermoSensor.setTemperatureAlarmSet(true);
				
				thermoSensor.setTemperatureAlarmLow(lowerValue, true);
				thermoSensor.setTemperatureAlarmHigh(upperValue, true);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmTemperaturePickerView);
				
				ThermoSensor thermoSensor = (ThermoSensor) mSensor;
				thermoSensor.setTemperatureAlarmSet(false);	
			}
		});
		
		mTemperatureSwitch = (AnimationSwitch)mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showTemperaturePickerView();
				} else {
					mView.removeView(mAlarmTemperaturePickerView);
					
					((ThermoSensor)mSensor).setTemperatureAlarmSet(false);
				}
			}
		});
		
		mTemperatureAlarmLowTextView = (TemperatureValueTextView) mView.findViewById(R.id.temperatureLowTextView);
		mTemperatureAlarmLowTextView.setTemperature(((ThermoSensor)mSensor).getTemperatureAlarmLow());
		
		mTemperatureAlarmHighTextView = (TemperatureValueTextView) mView.findViewById(R.id.temperatureHighTextView);
		mTemperatureAlarmHighTextView.setTemperature(((ThermoSensor)mSensor).getTemperatureAlarmHigh());
		
		mProbeSparkView = (LineSparkView) mView.findViewById(R.id.probeSparkView);
		mProbeSparkView.setValues(mSensor.getLastValues(ThermoSensor.SENSOR_FIELD_THERMO_PROBE));
		mProbeSparkView.setBackgroundColor(Color.TRANSPARENT);
		mProbeSparkView.setLineColor(Color.WHITE);
		
		mProbeTextView = (TemperatureValueView) mView.findViewById(R.id.probeTextView);
		mProbeTextView.setText(Float.toString(((ThermoSensor)mSensor).getProbe()));
		
		mProbeAlarmLayout = (LinearLayout) mView.findViewById(R.id.probeAlarmLayout);
		
		mAlarmProbePickerView = new AlarmPickerTemperatureView(getActivity(), ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE, 10, 50, 
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmProbePickerView);
				
				ThermoSensor thermoSensor = (ThermoSensor) mSensor;
				thermoSensor.setProbeAlarmSet(true);
				
				thermoSensor.setProbeAlarmLow(lowerValue, true);
				thermoSensor.setProbeAlarmHigh(upperValue, true);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmProbePickerView);
				
				ThermoSensor thermoSensor = (ThermoSensor) mSensor;
				thermoSensor.setProbeAlarmSet(false);	
			}
		});
		
		mProbeSwitch = (AnimationSwitch)mView.findViewById(R.id.probe_switch);
		mProbeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showProbePickerView();
				} else {
					mView.removeView(mAlarmProbePickerView);
					
					((ThermoSensor)mSensor).setProbeAlarmSet(false);
				}
			}
		});
		
		mProbeAlarmLowTextView = (TemperatureValueTextView) mView.findViewById(R.id.probeLowTextView);
		mProbeAlarmLowTextView.setTemperature(((ThermoSensor)mSensor).getProbeAlarmLow());
		
		mProbeAlarmHighTextView = (TemperatureValueTextView) mView.findViewById(R.id.probeHighTextView);
		mProbeAlarmHighTextView.setTemperature(((ThermoSensor)mSensor).getProbeAlarmHigh());
		
		getSensorFooterView().setLogo(R.drawable.thermo_logo);
	}
	
	@Override
	public void setSensor(Sensor sensor) {
		super.setSensor(sensor);
		
		if (mSensor != null) {
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_SET);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_LOW, true);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_HIGH, true);
			
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_PROBE);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_SET);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_LOW, true);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_HIGH, true);
		}
	}
	
	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_thermo;
	}
	
	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		super.propertyChange(event);
		
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String propertyName = event.getPropertyName();
				if (Sensor.SENSOR_FIELD_DEVICE.equals(propertyName)) {
					if (event.getNewValue() == null) {
						mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
	        			mProbeTextView.setText(getString(R.string.sensor_two_hyphens));
	        			
	        			mTemperatureAlarmLayout.setVisibility(View.INVISIBLE);
	        			mProbeAlarmLayout.setVisibility(View.INVISIBLE);
					} else {
	        			mTemperatureAlarmLayout.setVisibility(View.VISIBLE);
	        			mProbeAlarmLayout.setVisibility(View.VISIBLE);
					}
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE.equals(propertyName)) {
					mTemperatureTextView.setTemperature((Float)event.getNewValue());
					mTemperatureSparkView.invalidate();
					if(((ThermoSensor)mSensor).isTemperatureAlarmSet() && outOfRange((Float)event.getNewValue(),
							((ThermoSensor)mSensor).getTemperatureAlarmHigh(), ((ThermoSensor)mSensor).getTemperatureAlarmLow())) {
						showAlert(getString(R.string.sensor_thermo_alert_temperature));
					}
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE.equals(propertyName)) {
					mProbeTextView.setTemperature((Float)event.getNewValue());
					mProbeSparkView.invalidate();
					if(((ThermoSensor)mSensor).isProbeAlarmSet() && outOfRange((Float)event.getNewValue(), 
							((ThermoSensor)mSensor).getProbeAlarmHigh(), ((ThermoSensor)mSensor).getProbeAlarmLow())) {
						showAlert(getString(R.string.sensor_thermo_alert_probe));
					}
				}  else if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_SET.equals(propertyName)) {
					mTemperatureSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_LOW.equals(propertyName)) {
					mTemperatureAlarmLowTextView.setTemperature((Float)event.getNewValue());
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_HIGH.equals(propertyName)) {
					mTemperatureAlarmHighTextView.setTemperature((Float)event.getNewValue());
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_SET.equals(propertyName)) {
					mProbeSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_LOW.equals(propertyName)) {
					mProbeAlarmLowTextView.setTemperature((Float)event.getNewValue());
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_HIGH.equals(propertyName)) {
					mProbeAlarmHighTextView.setTemperature((Float)event.getNewValue());
				} 
			}
		});
	}
	
	private void showTemperaturePickerView() {
		mView.addView(mAlarmTemperaturePickerView);

		mAlarmTemperaturePickerView.setSelectedMinValue(((ThermoSensor)mSensor).getTemperatureAlarmLow());
		mAlarmTemperaturePickerView.setSelectedMaxValue(((ThermoSensor)mSensor).getTemperatureAlarmHigh());
		
		mAlarmTemperaturePickerView.show();
	}
	
	private void showProbePickerView() {
		mView.addView(mAlarmProbePickerView);

		mAlarmProbePickerView.setSelectedMinValue(((ThermoSensor)mSensor).getProbeAlarmLow());
		mAlarmProbePickerView.setSelectedMaxValue(((ThermoSensor)mSensor).getProbeAlarmHigh());
		
		mAlarmTemperaturePickerView.show();
	}
}
