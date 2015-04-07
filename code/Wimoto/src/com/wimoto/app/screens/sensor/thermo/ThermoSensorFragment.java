package com.wimoto.app.screens.sensor.thermo;

import java.beans.PropertyChangeEvent;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.ClimateSensor;
import com.wimoto.app.model.Sensor;
import com.wimoto.app.model.ThermoSensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AlarmPickerView;
import com.wimoto.app.widgets.AlarmPickerView.AlarmPickerListener;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class ThermoSensorFragment extends SensorFragment {
	
	private TextView mTemperatureTextView;
	private TextView mProbeTextView;
	
	private LineSparkView mTemperatureSparkView;
	private LineSparkView mProbeSparkView;
	
	private LinearLayout mTemperatureAlarmLayout;
	private LinearLayout mProbeAlarmLayout;
	
	private AnimationSwitch mTemperatureSwitch;
	private AnimationSwitch mProbeSwitch;
	
	private TextView mTemperatureAlarmLowTextView;
	private TextView mTemperatureAlarmHighTextView;	

	private TextView mProbeAlarmLowTextView;
	private TextView mProbeAlarmHighTextView;
	
	private AlarmPickerView mAlarmTemperaturePickerView;
	private AlarmPickerView mAlarmProbePickerView;
	
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
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperatureTextView);
		mTemperatureTextView.setText(Float.toString(((ThermoSensor)mSensor).getTemperature()));
		
		mTemperatureAlarmLayout = (LinearLayout) mView.findViewById(R.id.temperatureAlarmLayout);
		
		mAlarmTemperaturePickerView = new AlarmPickerView(getActivity(), ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE, -60, 130, 
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmTemperaturePickerView);
				
				ThermoSensor thermoSensor = (ThermoSensor) mSensor;
				thermoSensor.setTemperatureAlarmSet(true);
				
				thermoSensor.setTemperatureAlarmLow(lowerValue);
				thermoSensor.setTemperatureAlarmHigh(upperValue);
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
		
		mTemperatureAlarmLowTextView = (TextView) mView.findViewById(R.id.temperatureLowTextView);
		mTemperatureAlarmLowTextView.setText(String.format(Locale.US, "%.01f", ((ThermoSensor)mSensor).getTemperatureAlarmLow()));
		
		mTemperatureAlarmHighTextView = (TextView) mView.findViewById(R.id.temperatureHighTextView);
		mTemperatureAlarmHighTextView.setText(String.format(Locale.US, "%.01f", ((ThermoSensor)mSensor).getTemperatureAlarmHigh()));
		
		mProbeSparkView = (LineSparkView) mView.findViewById(R.id.probeSparkView);
		mProbeSparkView.setValues(mSensor.getLastValues(ThermoSensor.SENSOR_FIELD_THERMO_PROBE));
		mProbeSparkView.setBackgroundColor(Color.TRANSPARENT);
		mProbeSparkView.setLineColor(Color.WHITE);
		
		mProbeTextView = (TextView) mView.findViewById(R.id.probeTextView);
		mProbeTextView.setText(Float.toString(((ThermoSensor)mSensor).getProbe()));
		
		mProbeAlarmLayout = (LinearLayout) mView.findViewById(R.id.probeAlarmLayout);
		
		mAlarmProbePickerView = new AlarmPickerView(getActivity(), ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE, 10, 50, 
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmProbePickerView);
				
				ThermoSensor thermoSensor = (ThermoSensor) mSensor;
				thermoSensor.setProbeAlarmSet(true);
				
				thermoSensor.setProbeAlarmLow(lowerValue);
				thermoSensor.setProbeAlarmHigh(upperValue);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmTemperaturePickerView);
				
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
		
		mProbeAlarmLowTextView = (TextView) mView.findViewById(R.id.probeLowTextView);
		mProbeAlarmLowTextView.setText(String.format(Locale.US, "%.01f", ((ThermoSensor)mSensor).getProbeAlarmLow()));
		
		mProbeAlarmHighTextView = (TextView) mView.findViewById(R.id.probeHighTextView);
		mProbeAlarmHighTextView.setText(String.format(Locale.US, "%.01f", ((ThermoSensor)mSensor).getProbeAlarmHigh()));
		
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
				if (Sensor.SENSOR_FIELD_CONNECTION.equals(propertyName)) {
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
					mTemperatureTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					mTemperatureSparkView.invalidate();
					if(((ThermoSensor)mSensor).isTemperatureAlarmSet() && outOfRange((Float)event.getNewValue(),
							((ThermoSensor)mSensor).getTemperatureAlarmHigh(), ((ThermoSensor)mSensor).getTemperatureAlarmLow())) {
						showAlert(getString(R.string.sensor_thermo_alert_temperature));
					}
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE.equals(propertyName)) {
					mProbeTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));	
					mProbeSparkView.invalidate();
					if(((ThermoSensor)mSensor).isProbeAlarmSet() && outOfRange((Float)event.getNewValue(), 
							((ThermoSensor)mSensor).getProbeAlarmHigh(), ((ThermoSensor)mSensor).getProbeAlarmLow())) {
						showAlert(getString(R.string.sensor_thermo_alert_probe));
					}
				}  else if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_SET.equals(propertyName)) {
					mTemperatureSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_LOW.equals(propertyName)) {
					mTemperatureAlarmLowTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_HIGH.equals(propertyName)) {
					mTemperatureAlarmHighTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_SET.equals(propertyName)) {
					mProbeSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_LOW.equals(propertyName)) {
					mProbeAlarmLowTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_HIGH.equals(propertyName)) {
					mProbeAlarmHighTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
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
