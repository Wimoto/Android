package com.marknicholas.wimoto.models.sensor;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.utils.AppContext;

public class GrowSensor extends Sensor {
	
	public static String BLE_GROW_AD_SERVICE_UUID_LIGHT 			= "0000470C-0000-1000-8000-00805F9B34FB";
	public static String BLE_GROW_AD_SERVICE_UUID_SOIL_MOISTURE 	= "00004712-0000-1000-8000-00805F9B34FB";
	public static String BLE_GROW_AD_SERVICE_UUID_SOIL_TEMPERATURE 	= "00004706-0000-1000-8000-00805F9B34FB";
	
	public GrowSensor() {
		title = AppContext.getContext().getString(R.string.sensor_grow);
		type = Sensor.TYPE_GROW;
	}

	public GrowSensor(BluetoothConnection connection) {
		super(connection);
	}
}
