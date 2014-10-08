package com.wimoto.app.screens.sensor.climate;

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
import com.wimoto.app.model.ClimateSensor;
import com.wimoto.app.model.Sensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class ClimateSensorFragment extends SensorFragment implements AlarmSliderDialogListener {
	
	private TextView mTemperatureTextView;
	private TextView mHumidityTextView;
	private TextView mLightTextView;
	
	private LineSparkView mTemperatureSparkView;
	private LineSparkView mHumiditySparkView;
	private LineSparkView mLightSparkView;
	
	private LinearLayout mTemperatureAlarmLayout;
	private LinearLayout mHumidityAlarmLayout;
	private LinearLayout mLightAlarmLayout;
	
	private AnimationSwitch mTemperatureSwitch;
	private AnimationSwitch mHumiditySwitch;
	private AnimationSwitch mLightSwitch;
	
	private TextView mTemperatureAlarmLowTextView;
	private TextView mTemperatureAlarmHighTextView;	

	private TextView mHumidityAlarmLowTextView;
	private TextView mHumidityAlarmHighTextView;	
	
	private TextView mLightAlarmLowTextView;
	private TextView mLightAlarmHighTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = inflater.inflate(R.layout.sensor_climate_fragment, null);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		super.initViews();
		
		mTemperatureSparkView = (LineSparkView) mView.findViewById(R.id.temperatureSparkView);
		mTemperatureSparkView.setValues(mSensor.getLastValues(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.BLACK);
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperatureTextView);
		
		mTemperatureAlarmLayout = (LinearLayout) mView.findViewById(R.id.temperatureAlarmLayout);
		mTemperatureSwitch = (AnimationSwitch) mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setTemperatureAlarmSet(isChecked);
				
				if (isChecked) {
					AlarmSliderDialog temperatureSlider = new AlarmSliderDialog(getActivity(), ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE, self(), -60, 130);
					temperatureSlider.setSelectedMinValue(climateSensor.getTemperatureAlarmLow());
					temperatureSlider.setSelectedMaxValue(climateSensor.getTemperatureAlarmHigh());
					temperatureSlider.create().show();
				}
			}
		});
		
		mTemperatureAlarmLowTextView = (TextView) mView.findViewById(R.id.temperatureLowTextView);
		mTemperatureAlarmHighTextView = (TextView) mView.findViewById(R.id.temperatureHighTextView);
		
		mHumiditySparkView = (LineSparkView) mView.findViewById(R.id.humiditySparkView);
		mHumiditySparkView.setValues(mSensor.getLastValues(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY));
		mHumiditySparkView.setBackgroundColor(Color.TRANSPARENT);
		mHumiditySparkView.setLineColor(Color.BLACK);
		
		mHumidityTextView = (TextView) mView.findViewById(R.id.humidityTextView);
		
		mHumidityAlarmLayout = (LinearLayout) mView.findViewById(R.id.humidityAlarmLayout);
		mHumiditySwitch = (AnimationSwitch)mView.findViewById(R.id.humidity_switch);
		mHumiditySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setHumidityAlarmSet(isChecked);

				if (isChecked) {
					AlarmSliderDialog humiditySlider = new AlarmSliderDialog(getActivity(), ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY, self(), 10, 50);
					humiditySlider.setSelectedMinValue(climateSensor.getHumidityAlarmLow());
					humiditySlider.setSelectedMaxValue(climateSensor.getHumidityAlarmHigh());
					humiditySlider.create().show();
				}
			}
		});
		
		mHumidityAlarmLowTextView = (TextView) mView.findViewById(R.id.humidityLowTextView);
		mHumidityAlarmHighTextView = (TextView) mView.findViewById(R.id.humidityHighTextView);
		
		mLightSparkView = (LineSparkView) mView.findViewById(R.id.lightSparkView);
		mLightSparkView.setValues(mSensor.getLastValues(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT));
		mLightSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLightSparkView.setLineColor(Color.BLACK);
		
		mLightTextView = (TextView) mView.findViewById(R.id.lightTextView);
		
		mLightAlarmLayout = (LinearLayout) mView.findViewById(R.id.lightAlarmLayout);
		mLightSwitch = (AnimationSwitch)mView.findViewById(R.id.light_switch);
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setLightAlarmSet(isChecked);
				
				if (isChecked) {
					AlarmSliderDialog lightSlider = new AlarmSliderDialog(getActivity(), ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT, self(), 10, 50);
					lightSlider.setSelectedMinValue(climateSensor.getLightAlarmLow());
					lightSlider.setSelectedMaxValue(climateSensor.getLightAlarmHigh());
					lightSlider.create().show();
				}
			}
		});
		
		mLightAlarmLowTextView = (TextView) mView.findViewById(R.id.lightLowTextView);
		mLightAlarmHighTextView = (TextView) mView.findViewById(R.id.lightHighTextView);
	}
	
	@Override
	public void setSensor(Sensor sensor) {
		super.setSensor(sensor);
		
		if (mSensor != null) {
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_SET);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH);
			
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_SET);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH);
			
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH);
		}
	}

	protected AlarmSliderDialogListener self() {
		return this;
	}
	
	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_climate;
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
	        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
	        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
	        			
	        			mTemperatureAlarmLayout.setVisibility(View.INVISIBLE);
	        			mHumidityAlarmLayout.setVisibility(View.INVISIBLE);
	        			mLightAlarmLayout.setVisibility(View.INVISIBLE);
					} else {
	        			mTemperatureAlarmLayout.setVisibility(View.VISIBLE);
	        			mHumidityAlarmLayout.setVisibility(View.VISIBLE);
	        			mLightAlarmLayout.setVisibility(View.VISIBLE);
					}
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE.equals(propertyName)) {
					mTemperatureTextView.setText(String.format("%.01f", event.getNewValue()));
					mTemperatureSparkView.invalidate();
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY.equals(propertyName)) {
					mHumidityTextView.setText(String.format("%.01f", event.getNewValue()));
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT.equals(propertyName)) {
					mLightTextView.setText(String.format("%.00f", event.getNewValue()));
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_SET.equals(propertyName)) {
					mTemperatureSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW.equals(propertyName)) {
					mTemperatureAlarmLowTextView.setText(event.getNewValue()  + "");
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH.equals(propertyName)) {
					mTemperatureAlarmHighTextView.setText(event.getNewValue() + "");
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_SET.equals(propertyName)) {
					mHumiditySwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW.equals(propertyName)) {
					mHumidityAlarmLowTextView.setText(event.getNewValue()  + "");
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH.equals(propertyName)) {
					mHumidityAlarmHighTextView.setText(event.getNewValue() + "");
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET.equals(propertyName)) {
					mLightSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW.equals(propertyName)) {
					mLightAlarmLowTextView.setText(event.getNewValue()  + "");
				} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH.equals(propertyName)) {
					mLightAlarmHighTextView.setText(event.getNewValue() + "");
				}
			}
		});
	}
	
	@Override
	public void onSave(AlarmSliderDialog dialog) {
		ClimateSensor climateSensor = (ClimateSensor) mSensor;
		
		String sensorCharacteristic = dialog.getSensorCharacteristic();
		if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE.equals(sensorCharacteristic)) {
			climateSensor.setTemperatureAlarmLow((int)dialog.getSelectedMinValue());
			climateSensor.setTemperatureAlarmHigh((int)dialog.getSelectedMaxValue());
		} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY.equals(sensorCharacteristic)) {
			climateSensor.setHumidityAlarmLow((int)dialog.getSelectedMinValue());
			climateSensor.setHumidityAlarmHigh((int)dialog.getSelectedMaxValue());
		} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT.equals(sensorCharacteristic)) {
			climateSensor.setLightAlarmLow((int)dialog.getSelectedMinValue());
			climateSensor.setLightAlarmHigh((int)dialog.getSelectedMaxValue());
		}
	}
}
