package com.marknicholas.wimoto.models.sensor;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.utils.AppContext;

public class ThermoSensor extends Sensor {

	public static String BLE_THERMO_AD_SERVICE_UUID_IR_TEMPERATURE 		= "00008E4E-0000-1000-8000-00805F9B34FB";
	public static String BLE_THERMO_AD_SERVICE_UUID_PROBE_TEMPERATURE 	= "00008E54-0000-1000-8000-00805F9B34FB";
	
	public ThermoSensor() {
		title = AppContext.getContext().getString(R.string.sensor_thermo);
		type = Sensor.TYPE_THERMO;
	}

	public ThermoSensor(BluetoothConnection connection) {
		super(connection);
	}
}
