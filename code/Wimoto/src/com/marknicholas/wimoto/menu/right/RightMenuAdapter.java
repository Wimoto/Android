package com.marknicholas.wimoto.menu.right;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.models.sensor.ClimateSensor;
import com.marknicholas.wimoto.models.sensor.GrowSensor;
import com.marknicholas.wimoto.models.sensor.Sensor;
import com.marknicholas.wimoto.models.sensor.ThermoSensor;

public class RightMenuAdapter extends BaseAdapter {

private ArrayList<Sensor> mSensors;
	
	public RightMenuAdapter() {
		initSensors();
	}
	
	private void initSensors() {
		mSensors = new ArrayList<Sensor> ();
		
		GrowSensor growSensor = new GrowSensor();
		growSensor.setId("ABCDEF-GHIJKL-MNOPQR-STUVWX-YZ");
		growSensor.setRrsi("-37dB");
		mSensors.add(growSensor);
		
		ThermoSensor thermoSensor = new ThermoSensor();
		thermoSensor.setId("123456-GHIJKL-765432-STUVWX-42");
		thermoSensor.setRrsi("-42dB");
		mSensors.add(thermoSensor);
		
		ClimateSensor climateSensor = new ClimateSensor();
		climateSensor.setId("xxxxxx-yyyyyy-zzzzzz-STUVWX-00");
		climateSensor.setRrsi("-11dB");
		mSensors.add(climateSensor);
		
		GrowSensor growSensor2 = new GrowSensor();
		growSensor2.setId("ABC123-456JKL-MNO789-STUVWX-00");
		growSensor2.setRrsi("-666dB");
		mSensors.add(growSensor2);
	}

	@Override
	public int getCount() {
		return mSensors.size();
	}

	@Override
	public Sensor getItem(int position) {
		return mSensors.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RightMenuView rightMenuView = null;
		if (convertView == null) {
			rightMenuView = new RightMenuView(MainActivity.getAppContext());
		} else {
			rightMenuView = (RightMenuView)convertView;
		}
		
		rightMenuView.setSensor(mSensors.get(position));
		
//		rightMenuView.setTag(position);
//		
//		rightMenuView.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				
//				int action = event.getAction();
//				
//				switch(action) {
//					
//				}
//				
//				return true;
//			}
//		});
		
		return rightMenuView;
	}

}
