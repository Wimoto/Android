package com.wimoto.app.screens.sensor.water;

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
import com.wimoto.app.model.WaterSensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class WaterSensorFragment extends SensorFragment implements AlarmSliderDialogListener {

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = inflater.inflate(R.layout.sensor_water_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mContactSparkView = (LineSparkView) mView.findViewById(R.id.contactSparkView);
		mContactSparkView.setValues(mSensor.getLastValues(WaterSensor.SENSOR_FIELD_WATER_CONTACT));
		mContactSparkView.setBackgroundColor(Color.TRANSPARENT);
		mContactSparkView.setLineColor(Color.BLACK);
		
		mContactTextView = (TextView) mView.findViewById(R.id.contactTextView);
		
		mContactAlarmLayout = (LinearLayout) mView.findViewById(R.id.contactAlarmLayout);
		mContactSwitch = (AnimationSwitch)mView.findViewById(R.id.contact_switch);
		mContactSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				WaterSensor waterSensor = (WaterSensor) mSensor;
				waterSensor.setContactAlarmSet(isChecked);
			}
		});
		
		mLevelSparkView = (LineSparkView) mView.findViewById(R.id.levelSparkView);
		mLevelSparkView.setValues(mSensor.getLastValues(WaterSensor.SENSOR_FIELD_WATER_LEVEL));
		mLevelSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLevelSparkView.setLineColor(Color.BLACK);
		
		mLevelTextView = (TextView) mView.findViewById(R.id.levelTextView);
		
		mLevelAlarmLayout = (LinearLayout) mView.findViewById(R.id.levelAlarmLayout);
		mLevelSwitch = (AnimationSwitch)mView.findViewById(R.id.level_switch);
		mLevelSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				WaterSensor waterSensor = (WaterSensor) mSensor;
				waterSensor.setLevelAlarmSet(isChecked);

				if (isChecked) {
					AlarmSliderDialog waterSlider = new AlarmSliderDialog(getActivity(), WaterSensor.SENSOR_FIELD_WATER_LEVEL, self(), 10, 50);
					waterSlider.setSelectedMinValue(waterSensor.getLevelAlarmLow());
					waterSlider.setSelectedMaxValue(waterSensor.getLevelAlarmHigh());
					waterSlider.create().show();
				}
			}
		});
		
		mLevelAlarmLowTextView = (TextView) mView.findViewById(R.id.levelLowTextView);
		mLevelAlarmHighTextView = (TextView) mView.findViewById(R.id.levelHighTextView);
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
	
	
	protected AlarmSliderDialogListener self() {
		return this;
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
					mContactTextView.setText(String.format("%.01f", event.getNewValue()));
					mContactSparkView.invalidate();
				} else if (WaterSensor.SENSOR_FIELD_WATER_LEVEL.equals(propertyName)) {
					mLevelTextView.setText(String.format("%.01f", event.getNewValue()));
				} else if (WaterSensor.SENSOR_FIELD_WATER_CONTACT_ALARM_SET.equals(propertyName)) {
					mContactSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_SET.equals(propertyName)) {
					mLevelSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_LOW.equals(propertyName)) {
					mLevelAlarmLowTextView.setText(event.getNewValue()  + "");
				} else if (WaterSensor.SENSOR_FIELD_WATER_LEVEL_ALARM_HIGH.equals(propertyName)) {
					mLevelAlarmHighTextView.setText(event.getNewValue() + "");
				} 
			}
		});
	}
	
	@Override
	public void onSave(AlarmSliderDialog dialog) {
		WaterSensor waterSensor = (WaterSensor) mSensor;
		
		String sensorCharacteristic = dialog.getSensorCharacteristic();
		if (WaterSensor.SENSOR_FIELD_WATER_CONTACT.equals(sensorCharacteristic)) {
//			waterSensor.setContactAlarmLow((int)dialog.getSelectedMinValue());
//			waterSensor.setContactAlarmHigh((int)dialog.getSelectedMaxValue());
		} else if (WaterSensor.SENSOR_FIELD_WATER_LEVEL.equals(sensorCharacteristic)) {
			waterSensor.setLevelAlarmLow((int)dialog.getSelectedMinValue());
			waterSensor.setLevelAlarmHigh((int)dialog.getSelectedMaxValue());
		}
	}
}
