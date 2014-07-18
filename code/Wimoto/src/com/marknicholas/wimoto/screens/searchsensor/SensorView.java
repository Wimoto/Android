package com.marknicholas.wimoto.screens.searchsensor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.models.sensor.Sensor;

public class SensorView extends LinearLayout {
	
	private Sensor mSensor;

	public SensorView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sensor_view, this);
	}

	public void setSensor(Sensor sensor) {
		if (mSensor == sensor) {
			return;
		}
		
		this.mSensor = sensor;
		
		Log.e("", "SensorView setSensor " + mSensor.getTitle());
		
		TextView titleView = (TextView)findViewById(R.id.sensor_title);
		titleView.setText(mSensor.getTitle());
		
		TextView idView = (TextView)findViewById(R.id.sensor_id);
		idView.setText(mSensor.getId());
		
		TextView rssiView = (TextView)findViewById(R.id.sensor_rssi);
		rssiView.setText(mSensor.getRssi());
	}
	
}
