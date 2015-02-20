package com.wimoto.app.screens.sensor.grow;

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
import com.wimoto.app.model.GrowSensor;
import com.wimoto.app.model.Sensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.screens.sensor.views.SensorFooterView;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class GrowSensorFragment extends SensorFragment implements AlarmSliderDialogListener {
	
	private TextView mLightTextView;
	private TextView mMoistureTextView;
	private TextView mTemperatureTextView;
	
	private LineSparkView mLightSparkView;
	private LineSparkView mMoistureSparkView;
	private LineSparkView mTemperatureSparkView;
	
	private LinearLayout mLightAlarmLayout;
	private LinearLayout mMoistureAlarmLayout;
	private LinearLayout mTemperatureAlarmLayout;
	
	private AnimationSwitch mLightSwitch;
	private AnimationSwitch mMoistureSwitch;
	private AnimationSwitch mTemperatureSwitch;
	
	private TextView mLightAlarmLowTextView;
	private TextView mLightAlarmHighTextView;	

	private TextView mMoistureAlarmLowTextView;
	private TextView mMoistureAlarmHighTextView;	
	
	private TextView mTemperatureAlarmLowTextView;
	private TextView mTemperatureAlarmHighTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = inflater.inflate(R.layout.sensor_grow_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mLightSparkView = (LineSparkView) mView.findViewById(R.id.lightSparkView);
		mLightSparkView.setValues(mSensor.getLastValues(GrowSensor.SENSOR_FIELD_GROW_LIGHT));
		mLightSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLightSparkView.setLineColor(Color.BLACK);
		
		mLightTextView = (TextView) mView.findViewById(R.id.lightTextView);
		
		mLightAlarmLayout = (LinearLayout) mView.findViewById(R.id.lightAlarmLayout);
		mLightSwitch = (AnimationSwitch)mView.findViewById(R.id.light_switch);
		mLightSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				GrowSensor growSensor = (GrowSensor) mSensor;
				growSensor.setLightAlarmSet(isChecked);
				
				if (isChecked) {
					AlarmSliderDialog lightSlider = new AlarmSliderDialog(getActivity(), GrowSensor.SENSOR_FIELD_GROW_LIGHT, self(), -60, 130);
					lightSlider.setSelectedMinValue(growSensor.getLightAlarmLow());
					lightSlider.setSelectedMaxValue(growSensor.getLightAlarmHigh());
					lightSlider.create().show();
				}
			}
		});
		
		mLightAlarmLowTextView = (TextView) mView.findViewById(R.id.lightLowTextView);
		mLightAlarmHighTextView = (TextView) mView.findViewById(R.id.lightHighTextView);
		
		mMoistureSparkView = (LineSparkView) mView.findViewById(R.id.moistureSparkView);
		mMoistureSparkView.setValues(mSensor.getLastValues(GrowSensor.SENSOR_FIELD_GROW_MOISTURE));
		mMoistureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mMoistureSparkView.setLineColor(Color.BLACK);
		
		mMoistureTextView = (TextView) mView.findViewById(R.id.moistureTextView);
		
		mMoistureAlarmLayout = (LinearLayout) mView.findViewById(R.id.moistureAlarmLayout);
		mMoistureSwitch = (AnimationSwitch)mView.findViewById(R.id.moisture_switch);
		mMoistureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				GrowSensor growSensor = (GrowSensor) mSensor;
				growSensor.setMoistureAlarmSet(isChecked);

				if (isChecked) {
					AlarmSliderDialog moistureSlider = new AlarmSliderDialog(getActivity(), GrowSensor.SENSOR_FIELD_GROW_MOISTURE, self(), 10, 50);
					moistureSlider.setSelectedMinValue(growSensor.getMoistureAlarmLow());
					moistureSlider.setSelectedMaxValue(growSensor.getMoistureAlarmHigh());
					moistureSlider.create().show();
				}
			}
		});
		
		mMoistureAlarmLowTextView = (TextView) mView.findViewById(R.id.moistureLowTextView);
		mMoistureAlarmHighTextView = (TextView) mView.findViewById(R.id.moistureHighTextView);
		
		mTemperatureSparkView = (LineSparkView) mView.findViewById(R.id.temperatureSparkView);
		mTemperatureSparkView.setValues(mSensor.getLastValues(GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.BLACK);
		
		mTemperatureTextView = (TextView) mView.findViewById(R.id.temperatureTextView);
		
		mTemperatureAlarmLayout = (LinearLayout) mView.findViewById(R.id.temperatureAlarmLayout);
		mTemperatureSwitch = (AnimationSwitch)mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				GrowSensor growSensor = (GrowSensor) mSensor;
				growSensor.setTemperatureAlarmSet(isChecked);
				
				if (isChecked) {
					AlarmSliderDialog temperatureSlider = new AlarmSliderDialog(getActivity(), GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE, self(), 10, 50);
					temperatureSlider.setSelectedMinValue(growSensor.getTemperatureAlarmLow());
					temperatureSlider.setSelectedMaxValue(growSensor.getTemperatureAlarmHigh());
					temperatureSlider.create().show();
				}
			}
		});
		
		getSensorFooterView().setLogo(R.drawable.grow_logo);
	}
	
	@Override
	public void setSensor(Sensor sensor) {
		super.setSensor(sensor);
		
		if (mSensor != null) {
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_LIGHT);
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_LIGHT_ALARM_SET);
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_LIGHT_ALARM_LOW);
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_LIGHT_ALARM_HIGH);
			
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_MOISTURE);
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_MOISTURE_ALARM_SET);
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_MOISTURE_ALARM_LOW);
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_MOISTURE_ALARM_HIGH);
			
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE);
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE_ALARM_SET);
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE_ALARM_LOW);
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE_ALARM_HIGH);
		}
	}
	
	protected AlarmSliderDialogListener self() {
		return this;
	}
	
	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_grow;
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
						mLightTextView.setText(getString(R.string.sensor_two_hyphens));
						mMoistureTextView.setText(getString(R.string.sensor_two_hyphens));
						mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
	        			
						mLightAlarmLayout.setVisibility(View.INVISIBLE);
						mMoistureAlarmLayout.setVisibility(View.INVISIBLE);
	        			mTemperatureAlarmLayout.setVisibility(View.INVISIBLE);
					} else {
						mLightAlarmLayout.setVisibility(View.VISIBLE);
						mMoistureAlarmLayout.setVisibility(View.VISIBLE);
	        			mTemperatureAlarmLayout.setVisibility(View.VISIBLE);
					}
				} else if (GrowSensor.SENSOR_FIELD_GROW_LIGHT.equals(propertyName)) {
					mLightTextView.setText(String.format("%.01f", event.getNewValue()));
					mLightSparkView.invalidate();
				} else if (GrowSensor.SENSOR_FIELD_GROW_MOISTURE.equals(propertyName)) {
					mMoistureTextView.setText(String.format("%.01f", event.getNewValue()));
				} else if (GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE.equals(propertyName)) {
					mTemperatureTextView.setText(String.format("%.00f", event.getNewValue()));
				} else if (GrowSensor.SENSOR_FIELD_GROW_LIGHT_ALARM_SET.equals(propertyName)) {
					mLightSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (GrowSensor.SENSOR_FIELD_GROW_LIGHT_ALARM_LOW.equals(propertyName)) {
					mLightAlarmLowTextView.setText(event.getNewValue()  + "");
				} else if (GrowSensor.SENSOR_FIELD_GROW_LIGHT_ALARM_HIGH.equals(propertyName)) {
					mLightAlarmHighTextView.setText(event.getNewValue() + "");
				} else if (GrowSensor.SENSOR_FIELD_GROW_MOISTURE_ALARM_SET.equals(propertyName)) {
					mMoistureSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (GrowSensor.SENSOR_FIELD_GROW_MOISTURE_ALARM_LOW.equals(propertyName)) {
					mMoistureAlarmLowTextView.setText(event.getNewValue()  + "");
				} else if (GrowSensor.SENSOR_FIELD_GROW_MOISTURE_ALARM_HIGH.equals(propertyName)) {
					mMoistureAlarmHighTextView.setText(event.getNewValue() + "");
				}  else if (GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE_ALARM_SET.equals(propertyName)) {
					mTemperatureSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE_ALARM_LOW.equals(propertyName)) {
					mTemperatureAlarmLowTextView.setText(event.getNewValue()  + "");
				} else if (GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE_ALARM_HIGH.equals(propertyName)) {
					mTemperatureAlarmHighTextView.setText(event.getNewValue() + "");
				}
			}
		});
	}
	
	@Override
	public void onSave(AlarmSliderDialog dialog) {
		GrowSensor growSensor = (GrowSensor) mSensor;
		
		String sensorCharacteristic = dialog.getSensorCharacteristic();
		if (GrowSensor.SENSOR_FIELD_GROW_LIGHT.equals(sensorCharacteristic)) {
			growSensor.setLightAlarmLow((int)dialog.getSelectedMinValue());
			growSensor.setLightAlarmHigh((int)dialog.getSelectedMaxValue());
		} else if (GrowSensor.SENSOR_FIELD_GROW_MOISTURE.equals(sensorCharacteristic)) {
			growSensor.setMoistureAlarmLow((int)dialog.getSelectedMinValue());
			growSensor.setMoistureAlarmHigh((int)dialog.getSelectedMaxValue());
		} else if (GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE.equals(sensorCharacteristic)) {
			growSensor.setTemperatureAlarmLow((int)dialog.getSelectedMinValue());
			growSensor.setTemperatureAlarmHigh((int)dialog.getSelectedMaxValue());
		}
	}
}
