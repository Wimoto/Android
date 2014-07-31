package com.marknicholas.wimoto.model;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection.WimotoProfile;
import com.marknicholas.wimoto.utils.AppContext;

public class ThermoSensor extends Sensor {

	public ThermoSensor() {
		title = AppContext.getContext().getString(R.string.sensor_thermo);
	}

	public ThermoSensor(BluetoothConnection connection) {
		super(connection);
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.THERMO;
	}
}
