package com.marknicholas.wimoto.model;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection.WimotoProfile;
import com.marknicholas.wimoto.utils.AppContext;

public class WaterSensor extends Sensor {
	
	public WaterSensor() {
		title = AppContext.getContext().getString(R.string.sensor_water);
	}

	public WaterSensor(BluetoothConnection connection) {
		super(connection);
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.WATER;
	}
}
