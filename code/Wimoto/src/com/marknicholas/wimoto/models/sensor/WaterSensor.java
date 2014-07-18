package com.marknicholas.wimoto.models.sensor;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.utils.AppContext;

public class WaterSensor extends Sensor {
	
	public static String BLE_WATER_AD_SERVICE_UUID_PRESENCE = "0000C7DB-0000-1000-8000-00805F9B34FB";
	public static String BLE_WATER_AD_SERVICE_UUID_LEVEL 	= "0000C7DF-0000-1000-8000-00805F9B34FB";
	
	public WaterSensor() {
		title = AppContext.getContext().getString(R.string.sensor_water);
		type = Sensor.TYPE_WATER;
	}

	public WaterSensor(BluetoothConnection connection) {
		super(connection);
	}
}
