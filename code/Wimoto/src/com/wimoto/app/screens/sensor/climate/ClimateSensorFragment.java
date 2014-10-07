package com.wimoto.app.screens.sensor.climate;

import java.util.Observable;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
		mTemperatureSparkView.setValues(mSensor.getLastValues(ClimateSensor.CLIMATE_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.BLACK);
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperatureTextView);
		mTemperatureSwitch = (AnimationSwitch) mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				((ClimateSensor)mSensor).enableTemperatureAlarm(isChecked);
				
				if (isChecked) {
					AlarmSliderDialog temperatureSlider = new AlarmSliderDialog(getActivity(), ClimateSensor.CLIMATE_TEMPERATURE, self(), -60, 130);
					temperatureSlider.setSelectedMinValue(-25.0f);
					temperatureSlider.setSelectedMaxValue(0.0f);
					temperatureSlider.create().show();
				}
			}
		});
		
		mTemperatureAlarmLowTextView = (TextView) mView.findViewById(R.id.temperatureLowTextView);
		mTemperatureAlarmHighTextView = (TextView) mView.findViewById(R.id.temperatureHighTextView);
		
		mHumiditySparkView = (LineSparkView) mView.findViewById(R.id.humiditySparkView);
		mHumiditySparkView.setValues(mSensor.getLastValues(ClimateSensor.CLIMATE_HUMIDITY));
		mHumiditySparkView.setBackgroundColor(Color.TRANSPARENT);
		mHumiditySparkView.setLineColor(Color.BLACK);
		
		mHumidityTextView = (TextView) mView.findViewById(R.id.humidity_text);
		mHumiditySwitch = (AnimationSwitch)mView.findViewById(R.id.humidity_switch);
		mHumiditySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				((ClimateSensor)mSensor).enableHumidityAlarm(isChecked);

				if (isChecked) {
					AlarmSliderDialog humiditySlider = new AlarmSliderDialog(getActivity(), ClimateSensor.CLIMATE_HUMIDITY, self(), 10, 50);
					humiditySlider.setSelectedMinValue(10.0f);
					humiditySlider.setSelectedMaxValue(20.0f);
					humiditySlider.create().show();
				}
			}
		});
		
		mHumidityAlarmLowTextView = (TextView) mView.findViewById(R.id.humidityLowTextView);
		mHumidityAlarmHighTextView = (TextView) mView.findViewById(R.id.humidityHighTextView);
		
		mLightSparkView = (LineSparkView) mView.findViewById(R.id.lightSparkView);
		mLightSparkView.setValues(mSensor.getLastValues(ClimateSensor.CLIMATE_LIGHT));
		mLightSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLightSparkView.setLineColor(Color.BLACK);
		
		mLightTextView = (TextView) mView.findViewById(R.id.light_text);
		mLightSwitch = (AnimationSwitch)mView.findViewById(R.id.light_switch);
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				((ClimateSensor)mSensor).enableLightAlarm(isChecked);
				
				if (isChecked) {
					AlarmSliderDialog lightSlider = new AlarmSliderDialog(getActivity(), ClimateSensor.CLIMATE_LIGHT, self(), 10, 50);
					lightSlider.setSelectedMinValue(10.0f);
					lightSlider.setSelectedMaxValue(20.0f);
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
			mSensor.addChangeListener(this, Sensor.OBSERVER_FIELD_SENSOR_CONNECTION);
			mSensor.addChangeListener(this, Sensor.OBSERVER_FIELD_SENSOR_BATTERY_LEVEL);
			mSensor.addChangeListener(this, Sensor.OBSERVER_FIELD_SENSOR_RSSI);
		}
	}

	protected AlarmSliderDialogListener self() {
		return this;
	}
	
	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_climate;
	}
	
//	@Override
//	public void update(Observable observable, Object data) {
//		super.update(observable, data);
//		
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//        		if (mSensor == null) {
//        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
//        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
//        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
//        		} else if (!mSensor.isConnected()){
//        			mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
//        			mHumidityTextView.setText(getString(R.string.sensor_two_hyphens));
//        			mLightTextView.setText(getString(R.string.sensor_two_hyphens));
//        		} else {
//        			ClimateSensor climateSensor = (ClimateSensor) mSensor;
//        			
//        			mTemperatureTextView.setText(String.format("%.01f", climateSensor.getTemperature()));
//        			mHumidityTextView.setText(String.format("%.01f", climateSensor.getHumidity()));
//        			mLightTextView.setText(String.format("%.00f", climateSensor.getLight()));
//        			
//        			mTemperatureAlarmLowTextView.setText(climateSensor.getTemperatureAlarmLow() + "");
//        			mTemperatureAlarmHighTextView.setText(climateSensor.getTemperatureAlarmHigh() + "");
//        			
//        			mTemperatureSparkView.invalidate();
//        		}        		
//            }
//        });
//	}

	@Override
	public void onSave(AlarmSliderDialog dialog) {
		String sensorCharacteristic = dialog.getSensorCharacteristic();
		if (ClimateSensor.CLIMATE_TEMPERATURE.equals(sensorCharacteristic)) {
			((ClimateSensor)mSensor).writeAlarmValue((int)dialog.getSelectedMinValue(), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, ClimateSensor.BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW);
			((ClimateSensor)mSensor).writeAlarmValue((int)dialog.getSelectedMaxValue(), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, ClimateSensor.BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH);
		} else if (ClimateSensor.CLIMATE_HUMIDITY.equals(sensorCharacteristic)) {
			((ClimateSensor)mSensor).writeAlarmValue((int)dialog.getSelectedMinValue(), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_HUMIDITY, ClimateSensor.BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_LOW);
			((ClimateSensor)mSensor).writeAlarmValue((int)dialog.getSelectedMaxValue(), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_HUMIDITY, ClimateSensor.BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_HIGH);
		} else if (ClimateSensor.CLIMATE_LIGHT.equals(sensorCharacteristic)) {
			((ClimateSensor)mSensor).writeAlarmValue((int)dialog.getSelectedMinValue(), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_LIGHT, ClimateSensor.BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_LOW);
			((ClimateSensor)mSensor).writeAlarmValue((int)dialog.getSelectedMaxValue(), ClimateSensor.BLE_CLIMATE_SERVICE_UUID_LIGHT, ClimateSensor.BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_HIGH);
		}
	}
}
