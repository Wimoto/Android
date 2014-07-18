package com.marknicholas.wimoto.models.sensor;

import com.marknicholas.wimoto.R;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.utils.AppContext;

public class ClimateSensor extends Sensor {

	public static String BLE_CLIMATE_AD_SERVICE_UUID_TEMPERATURE 	= "00005608-0000-1000-8000-00805F9B34FB";
	public static String BLE_CLIMATE_AD_SERVICE_UUID_LIGHT 			= "0000560E-0000-1000-8000-00805F9B34FB";
	public static String BLE_CLIMATE_AD_SERVICE_UUID_HUMIDITY 		= "00005614-0000-1000-8000-00805F9B34FB";
	
	public ClimateSensor() {
		title = AppContext.getContext().getString(R.string.sensor_climate);
		type = Sensor.TYPE_CLIMATE;
	}

	public ClimateSensor(BluetoothConnection connection) {
		super(connection);
	}
}
