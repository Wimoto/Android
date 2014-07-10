package com.marknicholas.wimoto.screens.searchsensor;

import java.util.ArrayList;
import java.util.Iterator;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.managers.SensorsManager;
import com.marknicholas.wimoto.models.sensor.ClimateSensor;
import com.marknicholas.wimoto.models.sensor.GrowSensor;
import com.marknicholas.wimoto.models.sensor.Sensor;
import com.marknicholas.wimoto.models.sensor.ThermoSensor;

public class SearchSensorAdapter extends BaseAdapter {
	
	private ArrayList<Sensor> mSensors;
	
	public SearchSensorAdapter() {
		initSensors();
		removeAlreadyRegisteredSensors();
	}
	
	private void initSensors() {
		mSensors = new ArrayList<Sensor> ();
		
		GrowSensor growSensor = new GrowSensor();
		growSensor.setId("ABCDEF-GHIJKL-MNOPQR-STUVWX-YZ");
		growSensor.setRssi("-37dB");
		mSensors.add(growSensor);
		
		ThermoSensor thermoSensor = new ThermoSensor();
		thermoSensor.setId("123456-GHIJKL-765432-STUVWX-42");
		thermoSensor.setRssi("-42dB");
		mSensors.add(thermoSensor);
		
		ClimateSensor climateSensor = new ClimateSensor();
		climateSensor.setId("xxxxxx-yyyyyy-zzzzzz-STUVWX-00");
		climateSensor.setRssi("-11dB");
		mSensors.add(climateSensor);
		
		GrowSensor growSensor2 = new GrowSensor();
		growSensor2.setId("ABC123-456JKL-MNO789-STUVWX-00");
		growSensor2.setRssi("-666dB");
		mSensors.add(growSensor2);
	}
	
	private void removeAlreadyRegisteredSensors() {
		for (Sensor sensor:mSensors) {
			for (Sensor registeredSensor:SensorsManager.getManager().getSensors()) {
				if (sensor.getId().equals(registeredSensor.getId())) {
					sensor.setRegistered(true);
				}
			}
		}
		
		Iterator<Sensor> iterator = mSensors.iterator();
		while (iterator.hasNext()) {
			Sensor sensor = iterator.next();
			
			if (sensor.isRegistered()) {
				iterator.remove();
			}
		}
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
		SensorView sensorView = null;
		if (convertView == null) {
			sensorView = new SensorView(MainActivity.getAppContext());
		} else {
			sensorView = (SensorView)convertView;
		}
		
		sensorView.setSensor(mSensors.get(position));
		
		return sensorView;
	}

}
