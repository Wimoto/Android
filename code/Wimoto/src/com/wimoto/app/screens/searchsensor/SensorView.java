package com.wimoto.app.screens.searchsensor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wimoto.app.R;
import com.wimoto.app.model.Sensor;

public class SensorView extends LinearLayout implements PropertyChangeListener {
	
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
			
			mSensor.removeChangeListener(this);
		}
		
		mSensor = sensor;
		mSensor.addChangeListener(this, Sensor.SENSOR_FIELD_RSSI);
		
		TextView titleView = (TextView)findViewById(R.id.sensor_title);
		titleView.setText(mSensor.getTitle());
		
		TextView idView = (TextView)findViewById(R.id.sensor_id);
		idView.setText(mSensor.getId());		
	}

//	@Override
//	public void update(Observable observable, Object data) {		
//		((MainActivity) getContext()).runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				int rssiLevel = 0;
//				if (mSensor != null) {
//					rssiLevel = mSensor.getRssi();
//				}
//				
//				if (rssiLevel == 0) {
//					mRssiTextView.setVisibility(View.INVISIBLE);
//				} else {
//					mRssiTextView.setVisibility(View.VISIBLE);
//					mRssiTextView.setText(rssiLevel + "dB");
//				}				
//			}
//		});
//	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
