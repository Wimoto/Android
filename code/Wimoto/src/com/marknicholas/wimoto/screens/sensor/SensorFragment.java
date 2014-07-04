package com.marknicholas.wimoto.screens.sensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.models.sensor.Sensor;
import com.mobitexoft.navigation.PageFragment;

public class SensorFragment extends PageFragment {
	
	protected View mView;
	
	protected ImageView mBatteryImageView;
	protected TextView mRssiText;
	protected TextView mSensorNameText;
	protected TextView mLastUpdateText;
	
	protected Sensor mSensor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHeaderVisibility(View.GONE);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initViews();	
		return mView;
	}
	
	protected void initViews() {
		mBatteryImageView = (ImageView)mView.findViewById(R.id.battery_level);
		mRssiText = (TextView)mView.findViewById(R.id.rrsi_text);
		mSensorNameText = (TextView)mView.findViewById(R.id.sensor_name_text);
		mLastUpdateText = (TextView)mView.findViewById(R.id.last_updated_text);
		
		mSensorNameText.setText(mSensor.getTitle());
		mRssiText.setText(mSensor.getRrsi());
	}
	
	public void setSensor(Sensor sensor) {
		if (sensor == null) {
			return;
		}
		
		this.mSensor = sensor;
	}
}
