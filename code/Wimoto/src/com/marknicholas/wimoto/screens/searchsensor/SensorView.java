package com.marknicholas.wimoto.screens.searchsensor;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.model.Sensor;

public class SensorView extends LinearLayout implements Observer {
	
	private Sensor mSensor;
	private TextView mRssiTextView;
	
	public SensorView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sensor_view, this);
        
        mRssiTextView = (TextView)findViewById(R.id.sensor_rssi);
	}

	public void setSensor(Sensor sensor) {
		if (mSensor != null) {
			if (mSensor.equals(sensor)) {
				return;
			}
			
			mSensor.deleteObserver(this);
		}
		
		this.mSensor = sensor;
		mSensor.addObserver(this);
		
		Log.e("", "SensorView setSensor " + mSensor.getTitle());
		
		TextView titleView = (TextView)findViewById(R.id.sensor_title);
		titleView.setText(mSensor.getTitle());
		
		TextView idView = (TextView)findViewById(R.id.sensor_id);
		idView.setText(mSensor.getId());		
	}

	@Override
	public void update(Observable observable, Object data) {		
		((MainActivity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				int rssiLevel = 0;
				if (mSensor != null) {
					rssiLevel = mSensor.getRssi();
				}
				
				if (rssiLevel == 0) {
					mRssiTextView.setVisibility(View.INVISIBLE);
				} else {
					mRssiTextView.setVisibility(View.VISIBLE);
					mRssiTextView.setText(rssiLevel + "dB");
				}				
			}
		});
	}
}
