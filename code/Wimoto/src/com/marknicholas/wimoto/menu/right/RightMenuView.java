package com.marknicholas.wimoto.menu.right;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.models.sensor.Sensor;

public class RightMenuView extends RelativeLayout {
	
	private Sensor mSensor;

	public RightMenuView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_right_cell, this);
	}
	
	public RightMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_right_cell, this);
	}
	
	public RightMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.menu_right_cell, this);
	}
	
	public void setSensor(Sensor sensor) {
		if (mSensor != null) {
			return;
		}
		
		this.mSensor = sensor;
		
		TextView titleView = (TextView)findViewById(R.id.sensor_title);
		titleView.setText(mSensor.getTitle());
	}

}
