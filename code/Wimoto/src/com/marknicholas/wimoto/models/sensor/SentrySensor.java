package com.marknicholas.wimoto.models.sensor;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.utils.AppContext;

public class SentrySensor extends Sensor {
	
	public static String BLE_SENTRY_AD_SERVICE_UUID_ACCELEROMETER 		= "0000DC68-0000-1000-8000-00805F9B34FB";
	public static String BLE_SENTRY_AD_SERVICE_UUID_PASSIVE_INFRARED 	= "0000DC6D-0000-1000-8000-00805F9B34FB";
	
	public SentrySensor() {
		title = AppContext.getContext().getString(R.string.sensor_sentry);
		type = Sensor.TYPE_SENTRY;
	}

	public SentrySensor(BluetoothConnection connection) {
		super(connection);
	}
}
