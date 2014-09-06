package com.wimoto.app.screens.sensor.water;

import java.util.Observable;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.WaterSensor;
import com.wimoto.app.screens.sensor.SensorFragment;
import com.wimoto.app.widgets.AnimationSwitch;
import com.wimoto.app.widgets.AnimationSwitch.OnCheckedChangeListener;
import com.wimoto.app.widgets.sparkline.LineSparkView;

public class WaterSensorFragment extends SensorFragment {

	private TextView mContactTextView;
	private TextView mLevelTextView;

	private LineSparkView mContactSparkView;
	private LineSparkView mLevelSparkView;
	
	private AnimationSwitch mContactSwitch;
	private AnimationSwitch mLevelSwitch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView  = inflater.inflate(R.layout.sensor_water_fragment, null);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		mContactSparkView = (LineSparkView) mView.findViewById(R.id.contactSparkView);
		mContactSparkView.setValues(mSensor.getLastValues(WaterSensor.WATER_CONTACT));
		mContactSparkView.setBackgroundColor(Color.TRANSPARENT);
		mContactSparkView.setLineColor(Color.BLACK);
		
		mContactTextView = (TextView) mView.findViewById(R.id.contact_text);
		mContactSwitch = (AnimationSwitch)mView.findViewById(R.id.contact_switch);
		mContactSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mLevelSparkView = (LineSparkView) mView.findViewById(R.id.levelSparkView);
		mLevelSparkView.setValues(mSensor.getLastValues(WaterSensor.WATER_LEVEL));
		mLevelSparkView.setBackgroundColor(Color.TRANSPARENT);
		mLevelSparkView.setLineColor(Color.BLACK);
		
		mLevelTextView = (TextView) mView.findViewById(R.id.level_text);
		mLevelSwitch = (AnimationSwitch)mView.findViewById(R.id.level_switch);
		mLevelSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
	}
	
	@Override
	protected int getBackgroundColorRes() {
		return R.color.color_sensor_water;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
        		if (mSensor == null) {        			
        			mContactTextView.setText(getString(R.string.sensor_two_hyphens));
        			mLevelTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else if (!mSensor.isConnected()){      
        			mContactTextView.setText(getString(R.string.sensor_two_hyphens));
        			mLevelTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else {
        			WaterSensor waterSensor = (WaterSensor) mSensor;
        			
        			mContactTextView.setText(String.format("%.01f", waterSensor.getContact()));
        			mLevelTextView.setText(String.format("%.01f", waterSensor.getLevel()));
        		}        		
            }
        });
	}
}
