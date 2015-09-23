package com.wimoto.app.screens.sensor.sentry;

import java.beans.PropertyChangeEvent;
import java.util.Date;
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
import com.wimoto.app.model.SentrySensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.pickers.TimePickerView;
import com.wimoto.app.widgets.pickers.TimePickerView.TimePickerListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class SentrySensorFragment extends SensorFragment {
	
	//private TextView mAccelerometerTextView;
	private TextView mInfraredTextView;
	
	private LineSparkView mAccelerometerSparkView;
	private LineSparkView mInfraredSparkView;
	
	private LinearLayout mAccelerometerAlarmLayout;
	private LinearLayout mInfraredAlarmLayout;
	
	private AnimationSwitch mAccelerometerSwitch;
	private AnimationSwitch mInfraredSwitch;
	
	private TimePickerView mAccelerometerDatePickerView;
	private TimePickerView mInfraredDatePickerView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = (ViewGroup)inflater.inflate(R.layout.sensor_sentry_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mAccelerometerSparkView = (LineSparkView) mView.findViewById(R.id.accelerometerSparkView);
		mAccelerometerSparkView.setValues(mSensor.getLastValues(SentrySensor.SENSOR_FIELD_SENTRY_ACCELEROMETER));
		mAccelerometerSparkView.setBackgroundColor(Color.TRANSPARENT);
		mAccelerometerSparkView.setLineColor(Color.WHITE);
		
		//mAccelerometerTextView = (TextView) mView.findViewById(R.id.accelerometerTextView);
		
		mAccelerometerAlarmLayout = (LinearLayout) mView.findViewById(R.id.accelerometerAlarmLayout);
		
		mAccelerometerDatePickerView = new TimePickerView(getActivity(), SentrySensor.SENSOR_FIELD_SENTRY_ACCELEROMETER, 
				new TimePickerListener() {

					@Override
					public void onSave(Date lowerDate, Date upperDate) {
						mView.removeView(mAccelerometerDatePickerView);
					}

					@Override
					public void onCancel() {
						mView.removeView(mAccelerometerDatePickerView);
						
						((SentrySensor)mSensor).setAccelerometerAlarmSet(false);
					}
		});
		
		mAccelerometerSwitch = (AnimationSwitch)mView.findViewById(R.id.accelerometer_switch);
		mAccelerometerSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showAccelerometerDatePickerView();
				} else {
					mView.removeView(mAccelerometerDatePickerView);
					
					((SentrySensor)mSensor).setAccelerometerAlarmSet(false);
				}	
			}
		});
		
		mInfraredSparkView = (LineSparkView) mView.findViewById(R.id.infraredSparkView);
		mInfraredSparkView.setValues(mSensor.getLastValues(SentrySensor.SENSOR_FIELD_SENTRY_PASSIVE_INFRARED));
		mInfraredSparkView.setBackgroundColor(Color.TRANSPARENT);
		mInfraredSparkView.setLineColor(Color.WHITE);
		
		mInfraredTextView = (TextView) mView.findViewById(R.id.infraredTextView);
		mInfraredTextView.setText(Float.toString(((SentrySensor)mSensor).getInfrared()));
		
		mInfraredAlarmLayout = (LinearLayout) mView.findViewById(R.id.infraredAlarmLayout);
		
		mInfraredDatePickerView = new TimePickerView(getActivity(), SentrySensor.SENSOR_FIELD_SENTRY_ACCELEROMETER, 
				new TimePickerListener() {

					@Override
					public void onSave(Date lowerDate, Date upperDate) {
						mView.removeView(mInfraredDatePickerView);
					}

					@Override
					public void onCancel() {
						mView.removeView(mInfraredDatePickerView);
						
						((SentrySensor)mSensor).setInfraredAlarmSet(false);
					}
		});
		
		mInfraredSwitch = (AnimationSwitch)mView.findViewById(R.id.infrared_switch);
		mInfraredSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				if (isChecked) {
					showInfraredDatePickerView();
				} else {
					mView.removeView(mInfraredDatePickerView);
					
					((SentrySensor)mSensor).setInfraredAlarmSet(false);
				}	
			}
		});

		getSensorFooterView().setLogo(R.drawable.sentry_logo);
	}
	
	@Override
	public void setSensor(Sensor sensor) {
		super.setSensor(sensor);
		
		if (mSensor != null) {
			mSensor.addChangeListener(this, SentrySensor.SENSOR_FIELD_SENTRY_ACCELEROMETER);
			mSensor.addChangeListener(this, SentrySensor.SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_SET);
			mSensor.addChangeListener(this, SentrySensor.SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_CLEAR);
			
			mSensor.addChangeListener(this, SentrySensor.SENSOR_FIELD_SENTRY_PASSIVE_INFRARED);
			mSensor.addChangeListener(this, SentrySensor.SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_SET);
		}
	}

	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_sentry;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		super.propertyChange(event);
		
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String propertyName = event.getPropertyName();
				if (Sensor.SENSOR_FIELD_DEVICE.equals(propertyName)) {
					if (event.getNewValue() == null) {
						//mAccelerometerTextView.setText(getString(R.string.sensor_two_hyphens));
						mInfraredTextView.setText(getString(R.string.sensor_two_hyphens));
	        			
						mAccelerometerAlarmLayout.setVisibility(View.INVISIBLE);
						mInfraredAlarmLayout.setVisibility(View.INVISIBLE);
					} else {
						mAccelerometerAlarmLayout.setVisibility(View.VISIBLE);
						mInfraredAlarmLayout.setVisibility(View.VISIBLE);
					}
				} else if (SentrySensor.SENSOR_FIELD_SENTRY_ACCELEROMETER.equals(propertyName)) {
					//mAccelerometerTextView.setText(String.format("%.01f", event.getNewValue()));
					mAccelerometerSparkView.invalidate();
					if(((SentrySensor)mSensor).isAccelerometerAlarmSet()) {
						showAlert(getString(R.string.sensor_sentry_alert_accelerometer));
					}
				} else if (SentrySensor.SENSOR_FIELD_SENTRY_PASSIVE_INFRARED.equals(propertyName)) {
					mInfraredTextView.setText(String.format(Locale.US, "%.01f", event.getNewValue()));
					if(((SentrySensor)mSensor).isInfraredAlarmSet()) {
						showAlert(getString(R.string.sensor_sentry_alert_infrared));
					}
				} else if (SentrySensor.SENSOR_FIELD_SENTRY_ACCELEROMETER_ALARM_SET.equals(propertyName)) {
					mAccelerometerSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} else if (SentrySensor.SENSOR_FIELD_SENTRY_PASSIVE_INFRARED_ALARM_SET.equals(propertyName)) {
					mInfraredSwitch.setChecked(((Boolean)event.getNewValue()).booleanValue());
				} 
			}
		});
	}
	
	private void showAccelerometerDatePickerView() {
		Date date = new Date();
		mAccelerometerDatePickerView.setMinMaxDate(date, date);
		
		mView.addView(mAccelerometerDatePickerView);

		mAccelerometerDatePickerView.show();
	}
	
	private void showInfraredDatePickerView() {
		Date date = new Date();
		mInfraredDatePickerView.setMinMaxDate(date, date);
		
		mView.addView(mInfraredDatePickerView);

		mInfraredDatePickerView.show();
	}
}
