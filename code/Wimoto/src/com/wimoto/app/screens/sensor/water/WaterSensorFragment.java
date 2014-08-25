package com.wimoto.app.screens.sensor.water;

import java.util.Observable;

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

public class WaterSensorFragment extends SensorFragment {

	private TextView mContactTextView;
	private TextView mLevelTextView;
	
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
		
		mContactTextView = (TextView) mView.findViewById(R.id.contact_text);
		mContactSwitch = (AnimationSwitch)mView.findViewById(R.id.contact_switch);
		mContactSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
		
		mLevelTextView = (TextView) mView.findViewById(R.id.level_text);
		mLevelSwitch = (AnimationSwitch)mView.findViewById(R.id.level_switch);
		mLevelSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(AnimationSwitch view, boolean isChecked) {
				
			}
		});
	}
	
	@Override
	public void update(Observable observable, Object data) {
		super.update(observable, data);
		
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	int colorId = R.color.color_sensor_water;
        		if (mSensor == null) {
        			colorId = R.color.color_light_gray;
        			
        			mContactTextView.setText(getString(R.string.sensor_two_hyphens));
        			mLevelTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else if (!mSensor.isConnected()){
        			colorId = R.color.color_light_gray;
      
        			mContactTextView.setText(getString(R.string.sensor_two_hyphens));
        			mLevelTextView.setText(getString(R.string.sensor_two_hyphens));
        		} else {
        			WaterSensor waterSensor = (WaterSensor) mSensor;
        			
        			mContactTextView.setText(String.format("%.01f", waterSensor.getContact()));
        			mLevelTextView.setText(String.format("%.01f", waterSensor.getLevel()));
        		}
        		
        		mView.setBackgroundColor(getResources().getColor(colorId));
            }
        });
	}
}
