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
import com.wimoto.app.model.demosensors.DemoClimateSensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AlarmPickerView;
import com.wimoto.app.widgets.AlarmPickerView.AlarmPickerListener;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class DemoClimateSensorFragment extends SensorFragment implements AlarmPickerListener {

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

	private AlarmPickerView mAlarmPickerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.sensor_climate_fragment, null);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews() {
		super.initViews();

		mView.setBackgroundColor(getResources().getColor(getBackgroundColorRes()));

		mTemperatureSparkView = (LineSparkView) mView.findViewById(R.id.temperatureSparkView);
		mTemperatureSparkView.setValues(mSensor.getLastValues(DemoClimateSensor.SENSOR_DEMO_FIELD_CLIMATE_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.WHITE);

		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperatureTextView);
		mTemperatureTextView.setText(Float.toString(((DemoClimateSensor)mSensor).getTemperature()));

		mTemperatureAlarmLayout = (LinearLayout) mView.findViewById(R.id.temperatureAlarmLayout);
		mTemperatureSwitch = (AnimationSwitch) mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
						DemoClimateSensor climateSensor = (DemoClimateSensor) mSensor;
						climateSensor.setTemperatureAlarmSet(isChecked);

						if (isChecked) {
							showAlarmPickerView(mTemperatureSwitch, ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE, -60, 130);
						} else {
							enableSwitches();
						}
					}
				});

		mTemperatureAlarmLowTextView = (TextView) mView.findViewById(R.id.temperatureLowTextView);
		mTemperatureAlarmLowTextView.setText(Float.toString(((DemoClimateSensor)mSensor).getTemperatureAlarmLow()));
		
		mTemperatureAlarmHighTextView = (TextView) mView.findViewById(R.id.temperatureHighTextView);
		mTemperatureAlarmHighTextView.setText(Float.toString(((DemoClimateSensor)mSensor).getTemperatureAlarmHigh()));

		mHumiditySparkView = (LineSparkView) mView.findViewById(R.id.humiditySparkView);
		mHumiditySparkView.setValues(mSensor.getLastValues(DemoClimateSensor.SENSOR_DEMO_FIELD_CLIMATE_HUMIDITY));
		mHumiditySparkView.setBackgroundColor(Color.TRANSPARENT);
		mHumiditySparkView.setLineColor(Color.WHITE);

		mHumidityTextView = (TextView) mView.findViewById(R.id.humidityTextView);
		mHumidityTextView.setText(Float.toString(((DemoClimateSensor)mSensor).getHumidity()));

		mHumidityAlarmLayout = (LinearLayout) mView.findViewById(R.id.humidityAlarmLayout);
		mHumiditySwitch = (AnimationSwitch) mView.findViewById(R.id.humidity_switch);
		mHumiditySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
						DemoClimateSensor climateSensor = (DemoClimateSensor) mSensor;
						climateSensor.setHumidityAlarmSet(isChecked);

						if (isChecked) {
							showAlarmPickerView(mHumiditySwitch, ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY, 10, 50);
						} else {
							enableSwitches();
						}
					}
				});

		mHumidityAlarmLowTextView = (TextView) mView.findViewById(R.id.humidityLowTextView);
		mHumidityAlarmLowTextView.setText(Float.toString(((DemoClimateSensor)mSensor).getHumidityAlarmLow()));
		
		mHumidityAlarmHighTextView = (TextView) mView.findViewById(R.id.humidityHighTextView);
		mHumidityAlarmHighTextView.setText(Float.toString(((DemoClimateSensor)mSensor).getHumidityAlarmHigh()));

		mLightSparkView = (LineSparkView) mView.findViewById(R.id.lightSparkView);
		mLightSparkView.setValues(mSensor.getLastValues(DemoClimateSensor.SENSOR_DEMO_FIELD_CLIMATE_LIGHT));
		mLightSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLightSparkView.setLineColor(Color.WHITE);

		mLightTextView = (TextView) mView.findViewById(R.id.lightTextView);
		mLightTextView.setText(Float.toString(((DemoClimateSensor)mSensor).getLight()));

		mLightAlarmLayout = (LinearLayout) mView.findViewById(R.id.lightAlarmLayout);
		mLightSwitch = (AnimationSwitch) mView.findViewById(R.id.light_switch);
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				DemoClimateSensor climateSensor = (DemoClimateSensor) mSensor;
				climateSensor.setLightAlarmSet(isChecked);

				if (isChecked) {
					showAlarmPickerView(mLightSwitch, ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT, 10, 50);
				} else {
					enableSwitches();
				}
			}
		});

		mLightAlarmLowTextView = (TextView) mView.findViewById(R.id.lightLowTextView);
		mLightAlarmLowTextView.setText(Float.toString(((DemoClimateSensor)mSensor).getLightAlarmLow()));
		
		mLightAlarmHighTextView = (TextView) mView.findViewById(R.id.lightHighTextView);
		mLightAlarmHighTextView.setText(Float.toString(((DemoClimateSensor)mSensor).getLightAlarmHigh()));

		getSensorFooterView().setLogo(R.drawable.climate_logo);

		mAlarmPickerView = (AlarmPickerView) mView.findViewById(R.id.alarmPickerView);
		mAlarmPickerView.setListener(this);
		
		runDemo();
	}
	
	private void runDemo() {
		((DemoClimateSensor)mSensor).runDemo();
	}

	@Override
	public void onPause() {
		((DemoClimateSensor)mSensor).stopDemo();
		super.onPause();
	}

	@Override
	public void setSensor(Sensor sensor) {
		super.setSensor(sensor);

		if (mSensor != null) {
			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE);
			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_SET);
			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW);
			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH);

			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY);
			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_SET);
			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW);
			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH);

			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT);
			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET);
			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW);
			mSensor.addChangeListener(this,ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH);
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
						mTemperatureTextView.setText(String.format(Locale.US,"%.01f", event.getNewValue()));
						mTemperatureSparkView.invalidate();
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY.equals(propertyName)) {
						mHumidityTextView.setText(String.format(Locale.US,"%.01f", event.getNewValue()));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT.equals(propertyName)) {
						mLightTextView.setText(String.format(Locale.US,"%.01f", event.getNewValue()));
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_SET.equals(propertyName)) {
						mTemperatureSwitch.setChecked(((Boolean) event.getNewValue()).booleanValue());
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW.equals(propertyName)) {
						mTemperatureAlarmLowTextView.setText(event.getNewValue() + "");
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH.equals(propertyName)) {
						mTemperatureAlarmHighTextView.setText(event.getNewValue() + "");
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_SET.equals(propertyName)) {
						mHumiditySwitch.setChecked(((Boolean) event.getNewValue()).booleanValue());
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW.equals(propertyName)) {
						mHumidityAlarmLowTextView.setText(event.getNewValue() + "");
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH.equals(propertyName)) {
						mHumidityAlarmHighTextView.setText(event.getNewValue() + "");
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET.equals(propertyName)) {
						mLightSwitch.setChecked(((Boolean) event.getNewValue()).booleanValue());
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW.equals(propertyName)) {
						mLightAlarmLowTextView.setText(event.getNewValue() + "");
					} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH.equals(propertyName)) {
						mLightAlarmHighTextView.setText(event.getNewValue() + "");
					}
					
					String propertyString = ((DemoClimateSensor)mSensor).getPropertyString(propertyName);
					String alarmMessage = ((DemoClimateSensor)mSensor).getAlarmMessage(propertyString);
					
					showAlarmMessage(mSensor, propertyString, alarmMessage);
				}
			}
		});
	}

	private void showAlarmPickerView(AnimationSwitch animationSwitch, String sensorCharacteristic, int absoluteMinValue, int absoluteMaxValue) {
		mAlarmPickerView.setInitValues(animationSwitch, sensorCharacteristic, absoluteMinValue, absoluteMaxValue);

		if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE.equals(sensorCharacteristic)) {
			mAlarmPickerView.setSelectedMinValue(((DemoClimateSensor)mSensor).getTemperatureAlarmLow());
			mAlarmPickerView.setSelectedMaxValue(((DemoClimateSensor)mSensor).getTemperatureAlarmHigh());
			
			mTemperatureSwitch.setEnabled(true);
			mHumiditySwitch.setEnabled(false);
			mLightSwitch.setEnabled(false);
		} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY.equals(sensorCharacteristic)) {
			mAlarmPickerView.setSelectedMinValue(((DemoClimateSensor)mSensor).getHumidityAlarmLow());
			mAlarmPickerView.setSelectedMaxValue(((DemoClimateSensor)mSensor).getHumidityAlarmHigh());
			
			mTemperatureSwitch.setEnabled(false);
			mHumiditySwitch.setEnabled(true);
			mLightSwitch.setEnabled(false);
		} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT.equals(sensorCharacteristic)) {
			mAlarmPickerView.setSelectedMinValue(((DemoClimateSensor)mSensor).getLightAlarmLow());
			mAlarmPickerView.setSelectedMaxValue(((DemoClimateSensor)mSensor).getLightAlarmHigh());
			
			mTemperatureSwitch.setEnabled(false);
			mHumiditySwitch.setEnabled(false);
			mLightSwitch.setEnabled(true);
		}

		mAlarmPickerView.show();
	}
	
	private void enableSwitches() {
		mAlarmPickerView.hide();
		
		mTemperatureSwitch.setEnabled(true);
		mHumiditySwitch.setEnabled(true);
		mLightSwitch.setEnabled(true);
	}

	@Override
	public void onSave(AnimationSwitch animationSwitch, AlarmPickerView view, boolean needSave) {
		DemoClimateSensor climateSensor = (DemoClimateSensor) mSensor;

		String sensorCharacteristic = view.getSensorCharacteristic();
		if (ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE.equals(sensorCharacteristic)) {
			climateSensor.setTemperatureAlarmLow(view.getSelectedMinValue());
			climateSensor.setTemperatureAlarmHigh(view.getSelectedMaxValue());
		} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY.equals(sensorCharacteristic)) {
			climateSensor.setHumidityAlarmLow(view.getSelectedMinValue());
			climateSensor.setHumidityAlarmHigh(view.getSelectedMaxValue());
		} else if (ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT.equals(sensorCharacteristic)) {
			climateSensor.setLightAlarmLow(view.getSelectedMinValue());
			climateSensor.setLightAlarmHigh(view.getSelectedMaxValue());
		}
		
		if (!needSave) {
			animationSwitch.setChecked(false);
		}
		
		enableSwitches();
	}
	
	protected void switchOffAlarm(String propertyString) {
		if (ClimateSensor.SENSOR_FIELD_TEMPERATURE.equals(propertyString)) {
			mTemperatureSwitch.setChecked(false);
		} else if (ClimateSensor.SENSOR_FIELD_HUMIDITY.equals(propertyString)) {
			mHumiditySwitch.setChecked(false);
		} else if (ClimateSensor.SENSOR_FIELD_LIGHT.equals(propertyString)) {
			mLightSwitch.setChecked(false);
		}
	}
}
