package com.wimoto.app.screens.searchsensor;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wimoto.app.model.Sensor;
import com.wimoto.app.utils.AppContext;

public class SearchSensorAdapter extends BaseAdapter {
	
	private ArrayList<Sensor> mSensors;
	
	public SearchSensorAdapter() {
		mSensors = new ArrayList<Sensor> ();
		
//		initSensors();
//		removeAlreadyRegisteredSensors();
	}
	
	public void updateSensors(ArrayList<Sensor> sensors) {
		mSensors.clear();
		mSensors.addAll(sensors);
		
		notifyDataSetChanged();
	}
	
//	private void initSensors() {
//		
//		
//		GrowSensor growSensor = new GrowSensor();
//		growSensor.setId("ABCDEF-GHIJKL-MNOPQR-STUVWX-YZ");
//		growSensor.setRssi("-37dB");
//		mSensors.add(growSensor);
//		
//		ThermoSensor thermoSensor = new ThermoSensor();
//		thermoSensor.setId("123456-GHIJKL-765432-STUVWX-42");
//		thermoSensor.setRssi("-42dB");
//		mSensors.add(thermoSensor);
//		
//		ClimateSensor climateSensor = new ClimateSensor();
//		climateSensor.setId("xxxxxx-yyyyyy-zzzzzz-STUVWX-00");
//		climateSensor.setRssi("-11dB");
//		mSensors.add(climateSensor);
//		
//		GrowSensor growSensor2 = new GrowSensor();
//		growSensor2.setId("ABC123-456JKL-MNO789-STUVWX-00");
//		growSensor2.setRssi("-666dB");
//		mSensors.add(growSensor2);
//	}
//	
//	private void removeAlreadyRegisteredSensors() {
//		for (Sensor sensor:mSensors) {
//			for (Sensor registeredSensor:SensorsManager.getManager().getSensors()) {
//				if (sensor.getId().equals(registeredSensor.getId())) {
//					sensor.setRegistered(true);
//				}
//			}
//		}
//		
//		Iterator<Sensor> iterator = mSensors.iterator();
//		while (iterator.hasNext()) {
//			Sensor sensor = iterator.next();
//			
//			if (sensor.isRegistered()) {
//				iterator.remove();
//			}
//		}
//	}
//	
//	public void updateUnregisteredSensors() {
//		initSensors();
//		removeAlreadyRegisteredSensors();
//		notifyDataSetChanged();
//	}

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
		SensorView sensorView = null;
		if (convertView == null) {
			sensorView = new SensorView(AppContext.getContext());
		} else {
			sensorView = (SensorView)convertView;
		}
		
		sensorView.setSensor(mSensors.get(position));
		
		return sensorView;
	}

}
