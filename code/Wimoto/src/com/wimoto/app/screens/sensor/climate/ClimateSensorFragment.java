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
		mTemperatureTextView.setText(Float.toString(((ClimateSensor)mSensor).getTemperature()));
		
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
		mTemperatureAlarmLowTextView.setText(Float.toString(((ClimateSensor)mSensor).getTemperatureAlarmLow()));
		
		mTemperatureAlarmHighTextView = (TextView) mView.findViewById(R.id.temperatureHighTextView);
		mTemperatureAlarmHighTextView.setText(Float.toString(((ClimateSensor)mSensor).getTemperatureAlarmHigh()));
		
		mHumiditySparkView = (LineSparkView) mView.findViewById(R.id.humiditySparkView);
		mHumiditySparkView.setValues(mSensor.getLastValues(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY));
		mHumiditySparkView.setBackgroundColor(Color.TRANSPARENT);
		mHumiditySparkView.setLineColor(Color.WHITE);
		
		mHumidityTextView = (TextView) mView.findViewById(R.id.humidityTextView);
		mHumidityTextView.setText(Float.toString(((ClimateSensor)mSensor).getHumidity()));
		
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
		mHumidityAlarmLowTextView.setText(Float.toString(((ClimateSensor)mSensor).getHumidityAlarmLow()));
		
		mHumidityAlarmHighTextView = (TextView) mView.findViewById(R.id.humidityHighTextView);
		mHumidityAlarmHighTextView.setText(Float.toString(((ClimateSensor)mSensor).getHumidityAlarmHigh()));
		
		mLightSparkView = (LineSparkView) mView.findViewById(R.id.lightSparkView);
		mLightSparkView.setValues(mSensor.getLastValues(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT));
		mLightSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLightSparkView.setLineColor(Color.WHITE);
		
		mLightTextView = (TextView) mView.findViewById(R.id.lightTextView);
		mLightTextView.setText(Float.toString(((ClimateSensor)mSensor).getLight()));
		
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
		mLightAlarmLowTextView.setText(Float.toString(((ClimateSensor)mSensor).getLightAlarmLow()));
		
		mLightAlarmHighTextView = (TextView) mView.findViewById(R.id.lightHighTextView);
		mLightAlarmHighTextView.setText(Float.toString(((ClimateSensor)mSensor).getLightAlarmHigh()));
		
		getSensorFooterView().setLogo(R.drawable.climate_logo);
	
//		if (((ClimateSensor)mSensor).isDemoSensor()) {
//			mView.setBackgroundColor(getResources().getColor(getBackgroundColorRes()));
//
//			runDemo();
//		}
		
	}
	
	private void runDemo() {
		//((ClimateSensor)mSensor).runDemo();
	}

	@Override
	public void onStop() {
		//((ClimateSensor)mSensor).stopDemo();
		super.onPause();
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
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY.equals(propertyName)) {
						mHumidityTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT.equals(propertyName)) {
						mLightTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
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
					
					//String propertyString = ((ClimateSensor)mSensor).getPropertyString(propertyName);
                    //String alarmMessage = ((ClimateSensor)mSensor).getAlarmMessage(propertyString);
                     
                    //showAlert(propertyString, alarmMessage);
				}
			}
		});
	}
	
	private void showAlert(String propertyString, String alarmMessage) {
//		if (propertyString.equals(ClimateSensor.SENSOR_FIELD_TEMPERATURE)) {
//			if (mIsShowTemperatureAlert) {
//				return;
//			}
//			
//			if (!alarmMessage.isEmpty()) {
//				mIsShowTemperatureAlert = true;
//				showAlarmMessage(mSensor, propertyString, alarmMessage);
//			}
//		} else if (propertyString.equals(ClimateSensor.SENSOR_FIELD_HUMIDITY)) {
//			if (mIsShowHumidityAlert) {
//				return;
//			}
//			
//			if (!alarmMessage.isEmpty()) {
//				mIsShowHumidityAlert = true;
//				showAlarmMessage(mSensor, propertyString, alarmMessage);
//			}
//		} else if (propertyString.equals(ClimateSensor.SENSOR_FIELD_LIGHT)) {
//			if (mIsShowLightAlert) {
//				return;
//			}
//			
//			if (!alarmMessage.isEmpty()) {
//				mIsShowLightAlert = true;
//				showAlarmMessage(mSensor, propertyString, alarmMessage);
//			}
//		}
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
	
//	@Override
//	public void onButtonTapped(AlarmPickerView view, boolean needSave) {
//		mView.removeView(view);
////		ClimateSensor climateSensor = (ClimateSensor) mSensor;
////
////		String sensorCharacteristic = view.getSensorCharacteristic();
////		if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE.equals(sensorCharacteristic)) {
////			mIsShowTemperatureAlert = false;
////			climateSensor.setTemperatureAlarmSet(needSave);	
////			
////			//climateSensor.setTemperatureAlarmLow(view.getSelectedMinValue());
////			//climateSensor.setTemperatureAlarmHigh(view.getSelectedMaxValue());
////		} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY.equals(sensorCharacteristic)) {
////			mIsShowHumidityAlert = false;
////			climateSensor.setHumidityAlarmSet(needSave);
////			
////			//climateSensor.setHumidityAlarmLow(view.getSelectedMinValue());
////			//climateSensor.setHumidityAlarmHigh(view.getSelectedMaxValue());	
////		} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT.equals(sensorCharacteristic)) {
////			mIsShowLightAlert = false;
////			climateSensor.setLightAlarmSet(needSave);
////			
////			//climateSensor.setLightAlarmLow(view.getSelectedMinValue());
////			//climateSensor.setLightAlarmHigh(view.getSelectedMaxValue());	
////		}
////		
//////		if (!needSave) {
//////			animationSwitch.setChecked(false);
//////		}
////		
////		enableSwitches();
//	}
	
	protected void switchOffAlarm(String propertyString) {
//		if (ClimateSensor.SENSOR_FIELD_TEMPERATURE.equals(propertyString)) {
//			mTemperatureSwitch.setChecked(false);
//			mIsShowTemperatureAlert = false;
//		} else if (ClimateSensor.SENSOR_FIELD_HUMIDITY.equals(propertyString)) {
//			mHumiditySwitch.setChecked(false);
//			mIsShowHumidityAlert = false;
//		} else if (ClimateSensor.SENSOR_FIELD_LIGHT.equals(propertyString)) {
//			mLightSwitch.setChecked(false);
//			mIsShowLightAlert = false;
//		}
	}
}
