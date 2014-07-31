package com.marknicholas.wimoto.model;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection.WimotoProfile;
import com.marknicholas.wimoto.utils.AppContext;

public class SentrySensor extends Sensor {
	
	public SentrySensor() {
		title = AppContext.getContext().getString(R.string.sensor_sentry);
	}

	public SentrySensor(BluetoothConnection connection) {
		super(connection);
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.SENTRY;
	}
}
