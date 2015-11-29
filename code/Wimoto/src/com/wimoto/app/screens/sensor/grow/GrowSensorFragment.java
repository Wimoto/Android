package com.wimoto.app.screens.sensor.grow;

import java.beans.PropertyChangeEvent;
import java.util.Locale;

import android.app.AlertDialog;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.sensors.GrowSensor;
import com.wimoto.app.model.sensors.GrowSensor.GrowCalibrationState;
import com.wimoto.app.model.sensors.Sensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.pickers.AlarmPickerView;
import com.wimoto.app.widgets.pickers.AlarmPickerView.AlarmPickerListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;
import com.wimoto.app.widgets.temperature.TemperatureValueView;

public class GrowSensorFragment extends SensorFragment {
	
	private TextView mLightTextView;
	private MoistureTextView mMoistureTextView;
	private TextView mMoisturePercentageTextView;
	
	private TemperatureValueView mTemperatureTextView;
	
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
	
	private AlarmPickerView mAlarmLightPickerView;
	private AlarmPickerView mAlarmMoisturePickerView;
	private AlarmPickerView mAlarmTemperaturePickerView;
	
	private TextView mReCalibrateTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = (ViewGroup)inflater.inflate(R.layout.sensor_grow_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mLightSparkView = (LineSparkView) mView.findViewById(R.id.lightSparkView);
		mLightSparkView.setValues(mSensor.getLastValues(GrowSensor.SENSOR_FIELD_GROW_LIGHT));
		mLightSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLightSparkView.setLineColor(Color.WHITE);
		
		mLightTextView = (TextView) mView.findViewById(R.id.lightTextView);
		mLightTextView.setText(String.format(Locale.US, "%.01f", ((GrowSensor) mSensor).getLight()));
		
		mLightAlarmLayout = (LinearLayout) mView.findViewById(R.id.lightAlarmLayout);
		
		mAlarmLightPickerView = new AlarmPickerView(getActivity(), GrowSensor.SENSOR_FIELD_GROW_LIGHT, -60, 130, 
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmLightPickerView);
				
				GrowSensor growSensor = (GrowSensor) mSensor;
				growSensor.setLightAlarmSet(true);
				
				growSensor.setLightAlarmLow(lowerValue, true);
				growSensor.setLightAlarmHigh(upperValue, true);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmLightPickerView);
				
				GrowSensor growSensor = (GrowSensor) mSensor;
				growSensor.setLightAlarmSet(false);	
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
					mView.removeView(mAlarmLightPickerView);
					
					((GrowSensor)mSensor).setLightAlarmSet(false);
				}
			}
		});
		
		mLightAlarmLowTextView = (TextView) mView.findViewById(R.id.lightLowTextView);
		mLightAlarmLowTextView.setText(Float.toString(((GrowSensor)mSensor).getLightAlarmLow()));
		
		mLightAlarmHighTextView = (TextView) mView.findViewById(R.id.lightHighTextView);
		mLightAlarmHighTextView.setText(Float.toString(((GrowSensor)mSensor).getLightAlarmHigh()));
		
		mMoistureSparkView = (LineSparkView) mView.findViewById(R.id.moistureSparkView);
		mMoistureSparkView.setValues(mSensor.getLastValues(GrowSensor.SENSOR_FIELD_GROW_MOISTURE));
		mMoistureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mMoistureSparkView.setLineColor(Color.WHITE);
		
		mMoistureTextView = (MoistureTextView) mView.findViewById(R.id.moistureTextView);
		
		mMoisturePercentageTextView = (TextView) mView.findViewById(R.id.moisturePercentageTextView);
		
		mReCalibrateTextView = (TextView) mView.findViewById(R.id.reCalibrateTextView);
		mReCalibrateTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reCalibrateAction();
			}
		});
		
		mMoistureAlarmLayout = (LinearLayout) mView.findViewById(R.id.moistureAlarmLayout);
		
		mAlarmMoisturePickerView = new AlarmPickerView(getActivity(), GrowSensor.SENSOR_FIELD_GROW_MOISTURE, 10, 50, 
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmMoisturePickerView);
				
				GrowSensor growSensor = (GrowSensor) mSensor;
				growSensor.setMoistureAlarmSet(true);
				
				growSensor.setMoistureAlarmLow(lowerValue, true);
				growSensor.setMoistureAlarmHigh(upperValue, true);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmMoisturePickerView);
				
				GrowSensor growSensor = (GrowSensor) mSensor;
				growSensor.setMoistureAlarmSet(false);	
			}
		});
		
		mMoistureSwitch = (AnimationSwitch)mView.findViewById(R.id.moisture_switch);
		mMoistureSwitch.setSyncMode();
		mMoistureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showMoisturePickerView();
				} else {
					mView.removeView(mAlarmMoisturePickerView);
					
					((GrowSensor)mSensor).setMoistureAlarmSet(false);
				}
			}
		});
		
		mMoistureAlarmLowTextView = (TextView) mView.findViewById(R.id.moistureLowTextView);
		mMoistureAlarmLowTextView.setText(Float.toString(((GrowSensor)mSensor).getMoistureAlarmLow()));
		
		mMoistureAlarmHighTextView = (TextView) mView.findViewById(R.id.moistureHighTextView);
		mMoistureAlarmHighTextView.setText(Float.toString(((GrowSensor)mSensor).getMoistureAlarmHigh()));
		
		mTemperatureSparkView = (LineSparkView) mView.findViewById(R.id.temperatureSparkView);
		mTemperatureSparkView.setValues(mSensor.getLastValues(GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE));
		mTemperatureSparkView.setBackgroundColor(Color.TRANSPARENT);
		mTemperatureSparkView.setLineColor(Color.WHITE);
		
		mTemperatureTextView = (TemperatureValueView) mView.findViewById(R.id.temperatureTextView);
		mTemperatureTextView.setTemperature(((GrowSensor)mSensor).getTemperature());
		
		mTemperatureAlarmLayout = (LinearLayout) mView.findViewById(R.id.temperatureAlarmLayout);
		
		mAlarmTemperaturePickerView = new AlarmPickerView(getActivity(), GrowSensor.SENSOR_FIELD_GROW_MOISTURE, 10, 50,
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmTemperaturePickerView);
				
				GrowSensor growSensor = (GrowSensor) mSensor;
				growSensor.setLightAlarmSet(true);
				
				growSensor.setTemperatureAlarmLow(lowerValue, true);
				growSensor.setTemperatureAlarmHigh(upperValue, true);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmTemperaturePickerView);
				
				GrowSensor growSensor = (GrowSensor) mSensor;
				growSensor.setTemperatureAlarmSet(false);	
			}
		});
		
		mTemperatureSwitch = (AnimationSwitch)mView.findViewById(R.id.temperature_switch);
		mTemperatureSwitch.setSyncMode();
		mTemperatureSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showTemperaturePickerView();
				} else {
					((GrowSensor)mSensor).setTemperatureAlarmSet(false);
				}
			}
		});
		
		mTemperatureAlarmLowTextView = (TextView) mView.findViewById(R.id.temperatureLowTextView);
		mTemperatureAlarmLowTextView.setText(Float.toString(((GrowSensor)mSensor).getLightAlarmLow()));
		
		mTemperatureAlarmHighTextView = (TextView) mView.findViewById(R.id.temperatureHighTextView);
		mTemperatureAlarmHighTextView.setText(Float.toString(((GrowSensor)mSensor).getLightAlarmHigh()));
		
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
			
			mSensor.addChangeListener(this, GrowSensor.SENSOR_FIELD_GROW_CALIBRATION_STATE);
		}
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
				Object newValue = event.getNewValue();
				GrowSensor sensor = (GrowSensor)mSensor;
				
				if (Sensor.SENSOR_FIELD_STATE.equals(propertyName)) {
					int state = getConnectionState(newValue);
					if (state == BluetoothProfile.STATE_CONNECTED) {
						mLightAlarmLayout.setVisibility(View.VISIBLE);
						mMoistureAlarmLayout.setVisibility(View.VISIBLE);
	        			mTemperatureAlarmLayout.setVisibility(View.VISIBLE);
	        			
	        			mTemperatureTextView.setTemperature(sensor.getTemperature());
	        			
	        			mMoistureTextView.setMoistureCalibration(sensor.getMoisture(), sensor.getHumidityLowCalibration(), sensor.getHumidityHighCalibration());
	        			if ((sensor.getHumidityLowCalibration() != null) && (sensor.getHumidityHighCalibration() != null)) {
	        				//mMoisturePercentageTextView.setVisibility(View.INVISIBLE);
	        				mReCalibrateTextView.setVisibility(View.VISIBLE);
	        			} else {
	        				//mMoisturePercentageTextView.setVisibility(View.VISIBLE);
	        				mReCalibrateTextView.setVisibility(View.INVISIBLE);
	        				
	        				reCalibrateAction();
	        			}
	        			
	        			mLightTextView.setText(String.format(Locale.US, "%.01f", sensor.getLight()));
					} else {
						mLightTextView.setText(getString(R.string.sensor_two_hyphens));
						//mMoistureTextView.setText(getString(R.string.sensor_two_hyphens));
						mTemperatureTextView.setText(getString(R.string.sensor_two_hyphens));
	        			
						mLightAlarmLayout.setVisibility(View.INVISIBLE);
						mMoistureAlarmLayout.setVisibility(View.INVISIBLE);
	        			mTemperatureAlarmLayout.setVisibility(View.INVISIBLE);
					}
				} else if (GrowSensor.SENSOR_FIELD_GROW_LIGHT.equals(propertyName)) {
					mLastUpdateText.refresh();
					mLightTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					mLightSparkView.invalidate();
//					if(sensor.isLightAlarmSet() && outOfRange((Float)event.getNewValue(), 
//							sensor.getLightAlarmHigh(), sensor.getLightAlarmLow())) {
//						showAlert(getString(R.string.sensor_grow_alert_light));
//					}
				} else if (GrowSensor.SENSOR_FIELD_GROW_MOISTURE.equals(propertyName)) {
					mLastUpdateText.refresh();
					mMoistureTextView.setMoistureCalibration((Float)event.getNewValue(), sensor.getHumidityLowCalibration(), sensor.getHumidityHighCalibration());
//					if(sensor.isMoistureAlarmSet() && outOfRange((Float)event.getNewValue(), 
//							sensor.getMoistureAlarmHigh(), sensor.getMoistureAlarmLow())) {
//						showAlert(getString(R.string.sensor_grow_alert_moisture));
//					}
				} else if (GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE.equals(propertyName)) {
					mLastUpdateText.refresh();
					mTemperatureTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
//					if(sensor.isTemperatureAlarmSet() && outOfRange((Float)event.getNewValue(), 
//							sensor.getTemperatureAlarmHigh(), sensor.getTemperatureAlarmLow())) {
//						showAlert(getString(R.string.sensor_grow_alert_temperature));
//					}
				} else if (GrowSensor.SENSOR_FIELD_GROW_LIGHT_ALARM_SET.equals(propertyName)) {
					mLightSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (GrowSensor.SENSOR_FIELD_GROW_LIGHT_ALARM_LOW.equals(propertyName)) {
					mLightAlarmLowTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				} else if (GrowSensor.SENSOR_FIELD_GROW_LIGHT_ALARM_HIGH.equals(propertyName)) {
					mLightAlarmHighTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				} else if (GrowSensor.SENSOR_FIELD_GROW_MOISTURE_ALARM_SET.equals(propertyName)) {
					mMoistureSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (GrowSensor.SENSOR_FIELD_GROW_MOISTURE_ALARM_LOW.equals(propertyName)) {
					mMoistureAlarmLowTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				} else if (GrowSensor.SENSOR_FIELD_GROW_MOISTURE_ALARM_HIGH.equals(propertyName)) {
					mMoistureAlarmHighTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				}  else if (GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE_ALARM_SET.equals(propertyName)) {
					mTemperatureSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE_ALARM_LOW.equals(propertyName)) {
					mTemperatureAlarmLowTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				} else if (GrowSensor.SENSOR_FIELD_GROW_TEMPERATURE_ALARM_HIGH.equals(propertyName)) {
					mTemperatureAlarmHighTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				} else if (GrowSensor.SENSOR_FIELD_GROW_CALIBRATION_STATE.equals(propertyName)) {
					Log.e("GrowCalibrationState", "changed");
					if (sensor.getCalibrationState() == GrowCalibrationState.HIGH_VALUE_FINISHED) {
						showCalibrationEndAlert();
					} else if (sensor.getCalibrationState() == GrowCalibrationState.LOW_VALUE_FINISHED) {
						mMoistureTextView.setMoistureCalibration(sensor.getMoisture(), sensor.getHumidityLowCalibration(), sensor.getHumidityHighCalibration());
						if ((sensor.getHumidityLowCalibration() != null) && (sensor.getHumidityHighCalibration() != null)) {
	        				//mMoisturePercentageTextView.setVisibility(View.INVISIBLE);
	        				mReCalibrateTextView.setVisibility(View.VISIBLE);
	        			} else {
	        				//mMoisturePercentageTextView.setVisibility(View.VISIBLE);
	        				mReCalibrateTextView.setVisibility(View.INVISIBLE);
	        			}
						
						sensor.setCalibrationState(GrowCalibrationState.DEFAULT);
					}
					
					mReCalibrateTextView.setVisibility((sensor.getCalibrationState() == GrowCalibrationState.DEFAULT) ? View.VISIBLE : View.INVISIBLE);
				}
			}
		});
	}
	
	private void showLightPickerView() {
		mView.addView(mAlarmLightPickerView);

		mAlarmLightPickerView.setSelectedMinValue(((GrowSensor)mSensor).getLightAlarmLow());
		mAlarmLightPickerView.setSelectedMaxValue(((GrowSensor)mSensor).getLightAlarmHigh());
		
		mAlarmLightPickerView.show();
	}
	
	private void showMoisturePickerView() {
		mView.addView(mAlarmMoisturePickerView);
		
		mAlarmMoisturePickerView.setSelectedMinValue(((GrowSensor)mSensor).getMoistureAlarmLow());
		mAlarmMoisturePickerView.setSelectedMaxValue(((GrowSensor)mSensor).getMoistureAlarmHigh());
		
		mAlarmMoisturePickerView.show();
	}
	
	private void showTemperaturePickerView() {
		mView.addView(mAlarmTemperaturePickerView);
		
		mAlarmTemperaturePickerView.setSelectedMinValue(((GrowSensor)mSensor).getTemperatureAlarmLow());
		mAlarmTemperaturePickerView.setSelectedMaxValue(((GrowSensor)mSensor).getTemperatureAlarmHigh());
		
		mAlarmTemperaturePickerView.show();
	}
	
	private void reCalibrateAction() {
		((GrowSensor)mSensor).setHumidityLowCalibration(null);
		((GrowSensor)mSensor).setHumidityHighCalibration(null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Place the device in dry soil.");
		builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((GrowSensor)mSensor).setCalibrationState(GrowCalibrationState.HIGH_VALUE_STARTED);
			}
		});
		builder.setCancelable(false);
		builder.create().show();
	}
	
	private void showCalibrationEndAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Pour water over the soil, then wait a few seconds.");
		builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((GrowSensor)mSensor).setCalibrationState(GrowCalibrationState.LOW_VALUE_STARTED);
			}
		});
		builder.setCancelable(false);
		builder.create().show();
	}
}
