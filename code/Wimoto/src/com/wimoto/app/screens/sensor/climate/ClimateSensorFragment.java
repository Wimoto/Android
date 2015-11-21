package com.wimoto.app.screens.sensor.climate;

import java.beans.PropertyChangeEvent;
import java.util.Locale;

import android.bluetooth.BluetoothProfile;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.sensors.ClimateSensor;
import com.wimoto.app.model.sensors.Sensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.pickers.AlarmPickerTemperatureView;
import com.wimoto.app.widgets.pickers.AlarmPickerView;
import com.wimoto.app.widgets.pickers.AlarmPickerView.AlarmPickerListener;
import com.wimoto.app.widgets.pickers.TimePickerView;
import com.wimoto.app.widgets.sparkline.LineSparkView;
import com.wimoto.app.widgets.temperature.TemperatureValueTextView;
import com.wimoto.app.widgets.temperature.TemperatureValueView;

public class ClimateSensorFragment extends SensorFragment {
	
	protected TemperatureValueView mTemperatureTextView;
	protected TextView mHumidityTextView;
	protected TextView mLightTextView;
	
	protected LineSparkView mTemperatureSparkView;
	protected LineSparkView mHumiditySparkView;
	protected LineSparkView mLightSparkView;
	
	protected LinearLayout mTemperatureAlarmLayout;
	protected LinearLayout mHumidityAlarmLayout;
	protected LinearLayout mLightAlarmLayout;
	
	protected AnimationSwitch mTemperatureSwitch;
	protected AnimationSwitch mHumiditySwitch;
	protected AnimationSwitch mLightSwitch;
	
	protected TemperatureValueTextView mTemperatureAlarmLowTextView;
	protected TemperatureValueTextView mTemperatureAlarmHighTextView;	

	protected TextView mHumidityAlarmLowTextView;
	protected TextView mHumidityAlarmHighTextView;	
	
	protected TextView mLightAlarmLowTextView;
	protected TextView mLightAlarmHighTextView;
	
	protected AlarmPickerTemperatureView mAlarmTemperaturePickerView;
	protected AlarmPickerView mAlarmHumidityPickerView;
	protected AlarmPickerView mAlarmLightPickerView;
	
	private TimePickerView mTemperaturePickerView;
	
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
		
		mTemperatureTextView = (TemperatureValueView) mView.findViewById(R.id.temperatureTextView);
		
		mTemperatureAlarmLayout = (LinearLayout) mView.findViewById(R.id.temperatureAlarmLayout);
		
		mAlarmTemperaturePickerView = new AlarmPickerTemperatureView(getActivity(), ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE, -60, 130, 
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmTemperaturePickerView);
				
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setTemperatureAlarmSet(true, true);
				
				climateSensor.setTemperatureAlarmLow(lowerValue, true);
				climateSensor.setTemperatureAlarmHigh(upperValue, true);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmTemperaturePickerView);
				
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setTemperatureAlarmSet(false, true);	
			}
		});
		
//		mTemperaturePickerView = new TimePickerView(getActivity(), ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE, 
//				new TimePickerListener() {
//
//					@Override
//					public void onSave(Date lowerDate, Date upperDate) {
//						mView.removeView(mTemperaturePickerView);
//						
//						Log.e("lower", lowerDate.toString());
//						Log.e("upper", upperDate.toString());
//					}
//
//					@Override
//					public void onCancel() {
//						mView.removeView(mTemperaturePickerView);
//						
//						((ClimateSensor)mSensor).setTemperatureAlarmSet(false);
//					}
//		});
		
		mTemperatureSwitch = (AnimationSwitch) mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setSyncMode();
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showTemperaturePickerView();
				} else {
					mView.removeView(mAlarmTemperaturePickerView);
					
					((ClimateSensor)mSensor).setTemperatureAlarmSet(false, true);
				}
			}
		});
		
		mTemperatureAlarmLowTextView = (TemperatureValueTextView) mView.findViewById(R.id.temperatureLowTextView);
		mTemperatureAlarmLowTextView.setTemperature(((ClimateSensor)mSensor).getTemperatureAlarmLow());

		mTemperatureAlarmHighTextView = (TemperatureValueTextView) mView.findViewById(R.id.temperatureHighTextView);
		mTemperatureAlarmHighTextView.setTemperature(((ClimateSensor)mSensor).getTemperatureAlarmHigh());
		
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
				climateSensor.setHumidityAlarmSet(true, true);
				
				climateSensor.setHumidityAlarmLow(lowerValue, true);
				climateSensor.setHumidityAlarmHigh(upperValue, true);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmHumidityPickerView);
				
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setHumidityAlarmSet(false, true);	
			}
		});
		
		mHumiditySwitch = (AnimationSwitch)mView.findViewById(R.id.humidity_switch);
		mHumiditySwitch.setSyncMode();
		mHumiditySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showHumidityPickerView();
				} else {
					mView.removeView(mAlarmHumidityPickerView);
					
					((ClimateSensor)mSensor).setHumidityAlarmSet(false, true);
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
				climateSensor.setLightAlarmSet(true, true);
				
				climateSensor.setLightAlarmLow(lowerValue, true);
				climateSensor.setLightAlarmHigh(upperValue, true);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmLightPickerView);
				
				ClimateSensor climateSensor = (ClimateSensor) mSensor;
				climateSensor.setLightAlarmSet(false, true);	
			}
		});
		
		mLightSwitch = (AnimationSwitch)mView.findViewById(R.id.light_switch);
		mLightSwitch.setSyncMode();
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showLightPickerView();
				} else {
					((ClimateSensor)mSensor).setLightAlarmSet(false, true);
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
				Object newValue = event.getNewValue();
				ClimateSensor sensor = (ClimateSensor)mSensor;
				
				if (Sensor.SENSOR_FIELD_STATE.equals(propertyName)) {
					int state = getConnectionState(newValue);
					if (state == BluetoothProfile.STATE_CONNECTED) {
						mTemperatureAlarmLayout.setVisibility(View.VISIBLE);
	        			mHumidityAlarmLayout.setVisibility(View.VISIBLE);
	        			mLightAlarmLayout.setVisibility(View.VISIBLE);
					} else {
						mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
	        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
	        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
	        			
	        			mTemperatureAlarmLayout.setVisibility(View.INVISIBLE);
	        			mHumidityAlarmLayout.setVisibility(View.INVISIBLE);
	        			mLightAlarmLayout.setVisibility(View.INVISIBLE);
					}
				} else {
					if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE.equals(propertyName)) {
						mTemperatureTextView.setTemperature((Float)newValue);
						mTemperatureSparkView.invalidate();
						if(sensor.isTemperatureAlarmSet() && outOfRange((Float)newValue, 
								sensor.getTemperatureAlarmHigh(), sensor.getTemperatureAlarmLow())) {
							showAlert(getString(R.string.sensor_climate_alert_temperature));
						}
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY.equals(propertyName)) {
						mHumidityTextView.setText(String.format(Locale.US, "%.01f", newValue));
						mHumiditySparkView.invalidate();
						if(sensor.isHumidityAlarmSet() && outOfRange((Float)newValue, 
								sensor.getHumidityAlarmHigh(), sensor.getHumidityAlarmLow())) {
							showAlert(getString(R.string.sensor_climate_alert_humidity));
						}
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT.equals(propertyName)) {
						mLightTextView.setText(String.format(Locale.US, "%.01f", newValue));
						mLightSparkView.invalidate();
						if(sensor.isLightAlarmSet() && outOfRange((Float)newValue, 
								sensor.getLightAlarmHigh(), sensor.getLightAlarmLow())) {
							showAlert(getString(R.string.sensor_climate_alert_light));
						}
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_SET.equals(propertyName)) {
						mTemperatureSwitch.setChecked(((Boolean)newValue).booleanValue());
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW.equals(propertyName)) {
						mTemperatureAlarmLowTextView.setTemperature((Float)newValue);
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH.equals(propertyName)) {
						mTemperatureAlarmHighTextView.setTemperature((Float)newValue);
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_SET.equals(propertyName)) {
						mHumiditySwitch.setChecked(((Boolean)newValue).booleanValue());
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW.equals(propertyName)) {
						mHumidityAlarmLowTextView.setText(String.format(Locale.US, "%.01f", newValue));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH.equals(propertyName)) {
						mHumidityAlarmHighTextView.setText(String.format(Locale.US, "%.01f", newValue));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET.equals(propertyName)) {
						mLightSwitch.setChecked(((Boolean)newValue).booleanValue());
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW.equals(propertyName)) {
						mLightAlarmLowTextView.setText(String.format(Locale.US, "%.01f", newValue));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH.equals(propertyName)) {
						mLightAlarmHighTextView.setText(String.format(Locale.US, "%.01f", newValue));
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
		
		
//		Date date = new Date();
//		mTemperaturePickerView.setMinMaxDate(date, date);
//		
//		mView.addView(mTemperaturePickerView);
//
//		mTemperaturePickerView.show();
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
