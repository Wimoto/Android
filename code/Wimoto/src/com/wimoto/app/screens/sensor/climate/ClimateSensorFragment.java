package com.wimoto.app.screens.sensor.climate;

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
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AlarmPickerView;
import com.wimoto.app.widgets.AlarmPickerView.AlarmPickerListener;
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
	
	private AlarmPickerView mAlarmTemperaturePickerView;
	private AlarmPickerView mAlarmHumidityPickerView;
	private AlarmPickerView mAlarmLightPickerView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = (ViewGroup) inflater.inflate(R.layout.sensor_climate_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		super.initViews();
		
		mTemperatureSparkView = (LineSparkView) mView.findViewById(R.id.temperatureSparkView);
		mTemperatureSparkView.setValues(mSensor.getLastValues(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.WHITE);
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperatureTextView);
		
		mTemperatureAlarmLayout = (LinearLayout) mView.findViewById(R.id.temperatureAlarmLayout);
		
		mAlarmTemperaturePickerView = new AlarmPickerView(getActivity(), ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE, -60, 130, 
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmTemperaturePickerView);
				
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setTemperatureAlarmSet(true);
				
				climateSensor.setTemperatureAlarmLow(lowerValue);
				climateSensor.setTemperatureAlarmHigh(upperValue);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmTemperaturePickerView);
				
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setTemperatureAlarmSet(false);	
			}
		});
		
		mTemperatureSwitch = (AnimationSwitch) mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showTemperaturePickerView();
				} else {
					mView.removeView(mAlarmTemperaturePickerView);
					
					((ClimateSensor)mSensor).setTemperatureAlarmSet(false);
				}
			}
		});
		
		mTemperatureAlarmLowTextView = (TextView) mView.findViewById(R.id.temperatureLowTextView);
		mTemperatureAlarmLowTextView.setText(String.format(Locale.US, "%.01f", ((ClimateSensor)mSensor).getTemperatureAlarmLow()));

		mTemperatureAlarmHighTextView = (TextView) mView.findViewById(R.id.temperatureHighTextView);
		mTemperatureAlarmHighTextView.setText(String.format(Locale.US, "%.01f", ((ClimateSensor)mSensor).getTemperatureAlarmHigh()));
		
		mHumiditySparkView = (LineSparkView) mView.findViewById(R.id.humiditySparkView);
		mHumiditySparkView.setValues(mSensor.getLastValues(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY));
		mHumiditySparkView.setBackgroundColor(Color.TRANSPARENT);
		mHumiditySparkView.setLineColor(Color.WHITE);
		
		mHumidityTextView = (TextView) mView.findViewById(R.id.humidityTextView);
		
		mHumidityAlarmLayout = (LinearLayout) mView.findViewById(R.id.humidityAlarmLayout);
		
		mAlarmHumidityPickerView = new AlarmPickerView(getActivity(), ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY, 10, 50, 
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmHumidityPickerView);
				
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setHumidityAlarmSet(true);
				
				climateSensor.setHumidityAlarmLow(lowerValue);
				climateSensor.setHumidityAlarmHigh(upperValue);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmHumidityPickerView);
				
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setHumidityAlarmSet(false);	
			}
		});
		
		mHumiditySwitch = (AnimationSwitch)mView.findViewById(R.id.humidity_switch);
		mHumiditySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showHumidityPickerView();
				} else {
					mView.removeView(mAlarmHumidityPickerView);
					
					((ClimateSensor)mSensor).setHumidityAlarmSet(false);
				}
			}
		});
		
		mHumidityAlarmLowTextView = (TextView) mView.findViewById(R.id.humidityLowTextView);	
		mHumidityAlarmLowTextView.setText(String.format(Locale.US, "%.01f", ((ClimateSensor)mSensor).getHumidityAlarmLow()));
		
		mHumidityAlarmHighTextView = (TextView) mView.findViewById(R.id.humidityHighTextView);
		mHumidityAlarmHighTextView.setText(String.format(Locale.US, "%.01f", ((ClimateSensor)mSensor).getHumidityAlarmHigh()));
		
		mLightSparkView = (LineSparkView) mView.findViewById(R.id.lightSparkView);
		mLightSparkView.setValues(mSensor.getLastValues(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT));
		mLightSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLightSparkView.setLineColor(Color.WHITE);
		
		mLightTextView = (TextView) mView.findViewById(R.id.lightTextView);
		
		mLightAlarmLayout = (LinearLayout) mView.findViewById(R.id.lightAlarmLayout);
		
		mAlarmLightPickerView = new AlarmPickerView(getActivity(), ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT, 10, 50,
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmLightPickerView);
				
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setLightAlarmSet(true);
				
				climateSensor.setLightAlarmLow(lowerValue);
				climateSensor.setLightAlarmHigh(upperValue);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmLightPickerView);
				
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setLightAlarmSet(false);	
			}
		});
		
		mLightSwitch = (AnimationSwitch)mView.findViewById(R.id.light_switch);
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showLightPickerView();
				} else {
					((ClimateSensor)mSensor).setLightAlarmSet(false);
				}
			}
		});
		
		mLightAlarmLowTextView = (TextView) mView.findViewById(R.id.lightLowTextView);
		mLightAlarmLowTextView.setText(String.format(Locale.US, "%.01f", ((ClimateSensor)mSensor).getLightAlarmLow()));

		mLightAlarmHighTextView = (TextView) mView.findViewById(R.id.lightHighTextView);
		mLightAlarmHighTextView.setText(String.format(Locale.US, "%.01f", ((ClimateSensor)mSensor).getLightAlarmHigh()));
		
		getSensorFooterView().setLogo(R.drawable.climate_logo);
	}
	
	@Override
	public void setSensor(Sensor sensor) {
		super.setSensor(sensor);
		
		if (mSensor != null) {
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE, true);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_SET);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW, true);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH, true);
			
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY, true);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_SET);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW, true);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH, true);
			
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT, true);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW, true);
			mSensor.addChangeListener(this, ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH, true);
		}
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
				} else {
					if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE.equals(propertyName)) {
						mTemperatureTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
						mTemperatureSparkView.invalidate();
						if(((ClimateSensor)mSensor).isTemperatureAlarmSet() && outOfRange((Float)event.getNewValue(), 
								((ClimateSensor)mSensor).getTemperatureAlarmHigh(), ((ClimateSensor)mSensor).getTemperatureAlarmLow())) {
							showAlert(getString(R.string.sensor_climate_alert_temperature));
						}
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY.equals(propertyName)) {
						mHumidityTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
						mHumiditySparkView.invalidate();
						if(((ClimateSensor)mSensor).isHumidityAlarmSet() && outOfRange((Float)event.getNewValue(), 
								((ClimateSensor)mSensor).getHumidityAlarmHigh(), ((ClimateSensor)mSensor).getHumidityAlarmLow())) {
							showAlert(getString(R.string.sensor_climate_alert_humidity));
						}
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT.equals(propertyName)) {
						mLightTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
						mLightSparkView.invalidate();
						if(((ClimateSensor)mSensor).isLightAlarmSet() && outOfRange((Float)event.getNewValue(), 
								((ClimateSensor)mSensor).getLightAlarmHigh(), ((ClimateSensor)mSensor).getLightAlarmLow())) {
							showAlert(getString(R.string.sensor_climate_alert_light));
						}
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_SET.equals(propertyName)) {
						mTemperatureSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW.equals(propertyName)) {
						mTemperatureAlarmLowTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH.equals(propertyName)) {
						mTemperatureAlarmHighTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_SET.equals(propertyName)) {
						mHumiditySwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW.equals(propertyName)) {
						mHumidityAlarmLowTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH.equals(propertyName)) {
						mHumidityAlarmHighTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET.equals(propertyName)) {
						mLightSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW.equals(propertyName)) {
						mLightAlarmLowTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH.equals(propertyName)) {
						mLightAlarmHighTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					}					
				}
			}
		});
	}
		
	private void showTemperaturePickerView() {
		mView.addView(mAlarmTemperaturePickerView);

		mAlarmTemperaturePickerView.setSelectedMinValue(((ClimateSensor)mSensor).getTemperatureAlarmLow());
		mAlarmTemperaturePickerView.setSelectedMaxValue(((ClimateSensor)mSensor).getTemperatureAlarmHigh());
		
		mAlarmTemperaturePickerView.show();
	}
	
	private void showHumidityPickerView() {
		mView.addView(mAlarmHumidityPickerView);
		
		mAlarmHumidityPickerView.setSelectedMinValue(((ClimateSensor)mSensor).getHumidityAlarmLow());
		mAlarmHumidityPickerView.setSelectedMaxValue(((ClimateSensor)mSensor).getHumidityAlarmHigh());
		
		mAlarmHumidityPickerView.show();
	}
	
	private void showLightPickerView() {
		mView.addView(mAlarmLightPickerView);
		
		mAlarmLightPickerView.setSelectedMinValue(((ClimateSensor)mSensor).getLightAlarmLow());
		mAlarmLightPickerView.setSelectedMaxValue(((ClimateSensor)mSensor).getLightAlarmHigh());
		
		mAlarmLightPickerView.show();
	}
	
}
