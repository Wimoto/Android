package com.wimoto.app.screens.sensor.water;

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
import com.wimoto.app.model.Sensor;
import com.wimoto.app.model.WaterSensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AlarmPickerView;
import com.wimoto.app.widgets.AlarmPickerView.AlarmPickerListener;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class WaterSensorFragment extends SensorFragment {

	private TextView mContactTextView;
	private TextView mLevelTextView;

	private LineSparkView mContactSparkView;
	private LineSparkView mLevelSparkView;
	
	private LinearLayout mContactAlarmLayout;
	private LinearLayout mLevelAlarmLayout;
	
	private AnimationSwitch mContactSwitch;
	private AnimationSwitch mLevelSwitch;
	
	private TextView mLevelAlarmLowTextView;
	private TextView mLevelAlarmHighTextView;
	
	private AlarmPickerView mAlarmLevelPickerView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = (ViewGroup)inflater.inflate(R.layout.sensor_water_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mContactSparkView = (LineSparkView) mView.findViewById(R.id.contactSparkView);
		mContactSparkView.setValues(mSensor.getLastValues(WaterSensor.SENSOR_FIELD_WATER_CONTACT));
		mContactSparkView.setBackgroundColor(Color.TRANSPARENT);
		mContactSparkView.setLineColor(Color.WHITE);
		
		mContactTextView = (TextView) mView.findViewById(R.id.contactTextView);
		mContactTextView.setText(Float.toString(((WaterSensor)mSensor).getContact()));
		
		mContactAlarmLayout = (LinearLayout) mView.findViewById(R.id.contactAlarmLayout);
		
		mContactSwitch = (AnimationSwitch)mView.findViewById(R.id.contact_switch);
		mContactSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				((WaterSensor)mSensor).setContactAlarmSet(isChecked);
			}
		});
		
		mLevelSparkView = (LineSparkView) mView.findViewById(R.id.levelSparkView);
		mLevelSparkView.setValues(mSensor.getLastValues(WaterSensor.SENSOR_FIELD_WATER_LEVEL));
		mLevelSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLevelSparkView.setLineColor(Color.WHITE);
		
		mLevelTextView = (TextView) mView.findViewById(R.id.levelTextView);
		mLevelTextView.setText(Float.toString(((WaterSensor)mSensor).getLevel()));
		
		mLevelAlarmLayout = (LinearLayout) mView.findViewById(R.id.levelAlarmLayout);
		
		mAlarmLevelPickerView = new AlarmPickerView(getActivity(), WaterSensor.SENSOR_FIELD_WATER_LEVEL, 10, 50, 
				new AlarmPickerListener() {
			@Override
			public void onSave(float lowerValue, float upperValue) {
				mView.removeView(mAlarmLevelPickerView);
				
				WaterSensor waterSensor = (WaterSensor) mSensor;
				waterSensor.setLevelAlarmSet(true);
				
				waterSensor.setLevelAlarmLow(lowerValue);
				waterSensor.setLevelAlarmHigh(upperValue);
			}
			
			@Override
			public void onCancel() {
				mView.removeView(mAlarmLevelPickerView);
				
				WaterSensor waterSensor = (WaterSensor) mSensor;
				waterSensor.setLevelAlarmSet(false);	
			}
		});
		
		mLevelSwitch = (AnimationSwitch)mView.findViewById(R.id.level_switch);
		mLevelSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showLevelPickerView();
				} else {
					mView.removeView(mAlarmLevelPickerView);
					
					((WaterSensor)mSensor).setLevelAlarmSet(false);
				}
			}
		});
		
		mLevelAlarmLowTextView = (TextView) mView.findViewById(R.id.levelLowTextView);
		mLevelAlarmLowTextView.setText(Float.toString(((WaterSensor)mSensor).getLevelAlarmLow()));
		
		mLevelAlarmHighTextView = (TextView) mView.findViewById(R.id.levelHighTextView);
		mLevelAlarmHighTextView.setText(Float.toString(((WaterSensor)mSensor).getLevelAlarmHigh()));
		
		getSensorFooterView().setLogo(R.drawable.leak_logo);
	}
	
	@Override
	public void setSensor(Sensor sensor) {
		super.setSensor(sensor);
		
		if (mSensor != null) {
			mSensor.addChangeListener(this, WaterSensor.SENSOR_FIELD_WATER_CONTACT);
			mSensor.addChangeListener(this, WaterSensor.SENSOR_FIELD_WATER_CONTACT_ALARM_SET);
			
			mSensor.addChangeListener(this, WaterSensor.SENSOR_FIELD_WATER_LEVEL);
			mSensor.addChangeListener(this, WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_SET);
			mSensor.addChangeListener(this, WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_LOW);
			mSensor.addChangeListener(this, WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_HIGH);
		}
	}

	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_water;
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
						mContactTextView.setText(getString(R.string.sensor_two_hyphens));
						mLevelTextView.setText(getString(R.string.sensor_two_hyphens));
	        			
						mContactAlarmLayout.setVisibility(View.INVISIBLE);
	        			mLevelAlarmLayout.setVisibility(View.INVISIBLE);
					} else {
						mContactAlarmLayout.setVisibility(View.VISIBLE);
						mLevelAlarmLayout.setVisibility(View.VISIBLE);
					}
				} else if (WaterSensor.SENSOR_FIELD_WATER_CONTACT.equals(propertyName)) {
					mContactTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					mContactSparkView.invalidate();
					if(((WaterSensor)mSensor).isContactAlarmSet()) {
						showAlert(getString(R.string.sensor_water_alert_contact));
					}
				} else if (WaterSensor.SENSOR_FIELD_WATER_LEVEL.equals(propertyName)) {
					mLevelTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					if(((WaterSensor)mSensor).isLevelAlarmSet() && outOfRange((Float)event.getNewValue(), 
							((WaterSensor)mSensor).getLevelAlarmHigh(), ((WaterSensor)mSensor).getLevelAlarmLow())) {
						showAlert(getString(R.string.sensor_water_alert_level));
					}
				} else if (WaterSensor.SENSOR_FIELD_WATER_CONTACT_ALARM_SET.equals(propertyName)) {
					mContactSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_SET.equals(propertyName)) {
					mLevelSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_LOW.equals(propertyName)) {
					mLevelAlarmLowTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				} else if (WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_HIGH.equals(propertyName)) {
					mLevelAlarmHighTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
				} 
			}
		});
	}
	
	private void showLevelPickerView() {
		mView.addView(mAlarmLevelPickerView);

		mAlarmLevelPickerView.setSelectedMinValue(((WaterSensor)mSensor).getLevelAlarmLow());
		mAlarmLevelPickerView.setSelectedMaxValue(((WaterSensor)mSensor).getLevelAlarmHigh());
		
		mAlarmLevelPickerView.show();
	}
}
