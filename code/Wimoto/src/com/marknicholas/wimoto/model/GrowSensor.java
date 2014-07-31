package com.marknicholas.wimoto.model;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection.WimotoProfile;
import com.marknicholas.wimoto.utils.AppContext;

public class GrowSensor extends Sensor {
	
	public GrowSensor() {
		title = AppContext.getContext().getString(R.string.sensor_grow);
	}

	public GrowSensor(BluetoothConnection connection) {
		super(connection);
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.GROW;
	}
}
