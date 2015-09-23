package com.wimoto.app.bluetooth;

import java.util.HashMap;
import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.wimoto.app.AppContext;

public class BluetoothService {
	
//	private static final UUID[] wimotoDeviceUuids = { UUID.fromString(ClimateSensor.BLE_CLIMATE_AD_SERVICE_UUID_TEMPERATURE),
//													UUID.fromString(ClimateSensor.BLE_CLIMATE_AD_SERVICE_UUID_HUMIDITY),
//													UUID.fromString(ClimateSensor.BLE_CLIMATE_AD_SERVICE_UUID_LIGHT),
//													UUID.fromString(WaterSensor.BLE_WATER_AD_SERVICE_UUID_CONTACT),
//													UUID.fromString(WaterSensor.BLE_WATER_AD_SERVICE_UUID_LEVEL),
//													UUID.fromString(GrowSensor.BLE_GROW_AD_SERVICE_UUID_LIGHT),
//													UUID.fromString(GrowSensor.BLE_GROW_AD_SERVICE_UUID_MOISTURE),
//													UUID.fromString(GrowSensor.BLE_GROW_AD_SERVICE_UUID_TEMPERATURE),
//													UUID.fromString(SentrySensor.BLE_SENTRY_AD_SERVICE_UUID_ACCELEROMETER),
//													UUID.fromString(SentrySensor.BLE_SENTRY_AD_SERVICE_UUID_PASSIVE_INFRARED),
//													UUID.fromString(ThermoSensor.BLE_THERMO_AD_SERVICE_UUID_TEMPERATURE), 
//													UUID.fromString(ThermoSensor.BLE_THERMO_AD_SERVICE_UUID_PROBE)};
	
//	private static final UUID[] wimotoDeviceUuids = { UUID.fromString(ClimateSensor.BLE_CLIMATE_AD_SERVICE_UUID_TEMPERATURE), 
//		UUID.fromString(ClimateSensor.BLE_CLIMATE_AD_SERVICE_UUID_HUMIDITY), 
//		UUID.fromString(ClimateSensor.BLE_CLIMATE_AD_SERVICE_UUID_LIGHT),
//		UUID.fromString(GrowSensor.BLE_GROW_AD_SERVICE_UUID_LIGHT),
//		UUID.fromString(GrowSensor.BLE_GROW_AD_SERVICE_UUID_MOISTURE),
//		UUID.fromString(GrowSensor.BLE_GROW_AD_SERVICE_UUID_TEMPERATURE) };

	
	//private static final UUID[] wimotoDeviceUuids = { UUID.fromString(GrowSensor.BLE_GROW_SERVICE_UUID_LIGHT) };

	
	private AppContext mContext;
	
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
		
	private DiscoveryListener mDiscoveryListener;
	
	public BluetoothService(AppContext context) {
		mContext = context;
		
		mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
	}
	
	public WimotoDevice getDevice(String address) {
		return WimotoDevice.getInstance(mContext, mBluetoothAdapter.getRemoteDevice(address));
	}
	
	public boolean isBluetoothEnabled() {
		if ((mBluetoothAdapter == null) || (!mBluetoothAdapter.isEnabled())) {
			return false;
		}
		
		return true;
	}
	
	public void startScan(DiscoveryListener discoveryListener) {
		mDiscoveryListener = discoveryListener;
		
		mBluetoothAdapter.startLeScan(new WimotoScanCallback(mContext) {
			@Override
			protected void onWimotoDeviceDiscovered(WimotoDevice device) {
				if (mDiscoveryListener != null) {
					mDiscoveryListener.onWimotoDeviceDiscovered(device);
				}
				
			}
		});
	}
	
	public void stopScan() {
		mDiscoveryListener = null;
		
		mBluetoothAdapter.stopLeScan(null);
	}
		
	private static abstract class WimotoScanCallback implements BluetoothAdapter.LeScanCallback {

		private AppContext mContext;
		
		// TODO can be removed if you make "UUID[] wimotoDeviceUuids" to work with mBluetoothAdapter.startLeScan(wimotoDeviceUuids, mLeScanCallback);
		private Map<String, BluetoothDevice> mDiscoveredDevices = new HashMap<String, BluetoothDevice>();
		
		public WimotoScanCallback(AppContext context) {
			mContext = context;
		}
		
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			if (!mDiscoveredDevices.containsKey(device.getAddress())) {
				mDiscoveredDevices.put(device.getAddress(), device);
				
				WimotoDevice wimotoDevice = WimotoDevice.getInstance(mContext, device, scanRecord);
				if (wimotoDevice != null) {
					onWimotoDeviceDiscovered(wimotoDevice);
				}
			}
		}
		
		protected abstract void onWimotoDeviceDiscovered(WimotoDevice device);
	}	
}
