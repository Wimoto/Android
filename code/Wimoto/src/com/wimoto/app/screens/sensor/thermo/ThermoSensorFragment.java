package com.wimoto.app.screens.sensor.thermo;

import java.beans.PropertyChangeEvent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.dialogs.AlarmSliderDialog;
import com.wimoto.app.dialogs.AlarmSliderDialog.AlarmSliderDialogListener;
import com.wimoto.app.model.Sensor;
import com.wimoto.app.model.ThermoSensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class ThermoSensorFragment extends SensorFragment implements AlarmSliderDialogListener {
	
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = inflater.inflate(R.layout.sensor_thermo_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mTemperatureSparkView = (LineSparkView) mView.findViewById(R.id.temperatureSparkView);
		mTemperatureSparkView.setValues(mSensor.getLastValues(ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.BLACK);
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperatureTextView);
		
		mTemperatureAlarmLayout = (LinearLayout) mView.findViewById(R.id.temperatureAlarmLayout);
		mTemperatureSwitch = (AnimationSwitch)mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				ThermoSensor thermoSensor = (ThermoSensor) mSensor;
				thermoSensor.setTemperatureAlarmSet(isChecked);
				
				if (isChecked) {
					AlarmSliderDialog temperatureSlider = new AlarmSliderDialog(getActivity(), ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE, self(), -60, 130);
					temperatureSlider.setSelectedMinValue(thermoSensor.getTemperatureAlarmLow());
					temperatureSlider.setSelectedMaxValue(thermoSensor.getTemperatureAlarmHigh());
					temperatureSlider.create().show();
				}
			}
		});
		
		mTemperatureAlarmLowTextView = (TextView) mView.findViewById(R.id.temperatureLowTextView);
		mTemperatureAlarmHighTextView = (TextView) mView.findViewById(R.id.temperatureHighTextView);
		
		mProbeSparkView = (LineSparkView) mView.findViewById(R.id.probeSparkView);
		mProbeSparkView.setValues(mSensor.getLastValues(ThermoSensor.SENSOR_FIELD_THERMO_PROBE));
		mProbeSparkView.setBackgroundColor(Color.TRANSPARENT);
		mProbeSparkView.setLineColor(Color.BLACK);
		
		mProbeTextView = (TextView) mView.findViewById(R.id.probeTextView);
		
		mProbeAlarmLayout = (LinearLayout) mView.findViewById(R.id.probeAlarmLayout);
		mProbeSwitch = (AnimationSwitch)mView.findViewById(R.id.probe_switch);
		mProbeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				ThermoSensor thermoSensor = (ThermoSensor) mSensor;
				thermoSensor.setTemperatureAlarmSet(isChecked);

				if (isChecked) {
					AlarmSliderDialog probeSlider = new AlarmSliderDialog(getActivity(), ThermoSensor.SENSOR_FIELD_THERMO_PROBE, self(), 10, 50);
					probeSlider.setSelectedMinValue(thermoSensor.getProbeAlarmLow());
					probeSlider.setSelectedMaxValue(thermoSensor.getProbeAlarmHigh());
					probeSlider.create().show();
				}
			}
		});
		
		mProbeAlarmLowTextView = (TextView) mView.findViewById(R.id.probeLowTextView);
		mProbeAlarmHighTextView = (TextView) mView.findViewById(R.id.probeHighTextView);
	}
	
	@Override
	public void setSensor(Sensor sensor) {
		super.setSensor(sensor);
		
		if (mSensor != null) {
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_SET);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_LOW);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_HIGH);
			
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_PROBE);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_SET);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_LOW);
			mSensor.addChangeListener(this, ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_HIGH);
		}
	}
	
	protected AlarmSliderDialogListener self() {
		return this;
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
					mTemperatureTextView.setText(String.format("%.01f", event.getNewValue()));
					mTemperatureSparkView.invalidate();
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE.equals(propertyName)) {
					mProbeTextView.setText(String.format("%.01f", event.getNewValue()));
				}  else if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_SET.equals(propertyName)) {
					mTemperatureSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_LOW.equals(propertyName)) {
					mTemperatureAlarmLowTextView.setText(event.getNewValue()  + "");
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE_ALARM_HIGH.equals(propertyName)) {
					mTemperatureAlarmHighTextView.setText(event.getNewValue() + "");
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_SET.equals(propertyName)) {
					mProbeSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_LOW.equals(propertyName)) {
					mProbeAlarmLowTextView.setText(event.getNewValue()  + "");
				} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE_ALARM_HIGH.equals(propertyName)) {
					mProbeAlarmHighTextView.setText(event.getNewValue() + "");
				} 
			}
		});
	}
	
	@Override
	public void onSave(AlarmSliderDialog dialog) {
		ThermoSensor thermoSensor = (ThermoSensor) mSensor;
		
		String sensorCharacteristic = dialog.getSensorCharacteristic();
		if (ThermoSensor.SENSOR_FIELD_THERMO_TEMPERATURE.equals(sensorCharacteristic)) {
			thermoSensor.setTemperatureAlarmLow((int)dialog.getSelectedMinValue());
			thermoSensor.setTemperatureAlarmHigh((int)dialog.getSelectedMaxValue());
		} else if (ThermoSensor.SENSOR_FIELD_THERMO_PROBE.equals(sensorCharacteristic)) {
			thermoSensor.setProbeAlarmLow((int)dialog.getSelectedMinValue());
			thermoSensor.setProbeAlarmHigh((int)dialog.getSelectedMaxValue());
		}
	}
}
