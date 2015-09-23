package com.wimoto.app.bluetooth;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.wimoto.app.AppContext;
import com.wimoto.app.bluetooth.BluetoothConnection.BluetoothConnectionStateListener;
import com.wimoto.app.model.ClimateSensor;
import com.wimoto.app.model.GrowSensor;
import com.wimoto.app.model.SentrySensor;
import com.wimoto.app.model.ThermoSensor;
import com.wimoto.app.model.WaterSensor;

public class BluetoothService implements BluetoothConnectionStateListener {
	
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
	private Map<String, BluetoothConnection> mManagedConnections;
	
	public interface BluetoothServiceListener {
		void connectionEstablished(BluetoothConnection connection);
		void connectionAborted(BluetoothConnection connection);
	}
	
	private BluetoothServiceListener mBleServiceListener;
	
	private DiscoveryListener mDiscoveryListener;
	
	public BluetoothService(AppContext context, BluetoothServiceListener bleServiceListener) {
		mContext = context;
		
		mBleServiceListener = bleServiceListener;
		
		mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		mManagedConnections = new HashMap<String, BluetoothConnection>();		
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
	
	public void disconnectGatts() {
		Collection<BluetoothConnection> connections = mManagedConnections.values();
		
		for (BluetoothConnection connection:connections) {
			if (mBleServiceListener != null) {
				mBleServiceListener.connectionAborted(connection);
			}

			connection.disconnect();			
		}
		mManagedConnections.clear();
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
		
//	private WimotoScanCallback mLeScanCallback =
//	        new WimotoScanCallback(mContext) {
//
//				@Override
//				protected void onWimotoDeviceDiscovered(WimotoDevice device) {
//					if (mDiscoveryListener != null) {
//						mDiscoveryListener.onWimotoDeviceDiscovered(device);
//					}
//					
//				}
////	    @Override
////	    public void onLeScan(final BluetoothDevice device, int rssi,
////	            byte[] scanRecord) {
////	    	Log.e("", "onLeScan device found " + device.getName() + " ____ " + mBluetoothManager.getConnectionState(device, BluetoothProfile.GATT));
////	    	
////	    	BluetoothConnection connection = mManagedConnections.get(device.getAddress());
////	    	if (connection == null) {
////		    	connection = BluetoothConnection.createConnection(mContext, BluetoothService.this, device, scanRecord);
////	    		
////		    	if (connection != null) {
////		    		mManagedConnections.put(device.getAddress(), connection);
////		    		if (mDiscoveryListener != null) {
////		    			mDiscoveryListener.onWimotoDeviceDiscovered(connection);
////		    		}
////		    	}		    	
////	    	}
////	    	if (connection != null) {
////	    		connection.setRssi(rssi);
////	    	}
////	   }
//	};
	
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
	
	@Override
	public void didConnectionStateChanged(final BluetoothConnection connection) {
		int state = mBluetoothManager.getConnectionState(connection.getBluetoothDevice(), BluetoothProfile.GATT);
//		
//		Log.e("", "sensor " + connection.getBluetoothDevice().getName() + " _ " + state);
//		
//		if (state == BluetoothProfile.STATE_DISCONNECTED) {
//			connection.disconnect();
//			mManagedConnections.remove(connection.getAddress());	
//			
//			mContext.runOnUiThread(new Runnable() {
//				
//				@Override
//				public void run() {
//					registerConnection(connection.getBluetoothDevice().getAddress());		
//				}
//			});
//		}
//		
////		if (state == BluetoothProfile.STATE_CONNECTED) {
////			if (mBleServiceListener != null) {
////				mBleServiceListener.connectionEstablished(connection);
////			}
////		} else if (state == BluetoothProfile.STATE_DISCONNECTED) {
////			if (mBleServiceListener != null) {
////				mBleServiceListener.connectionAborted(connection);
////			}
////			
////			//connection.disconnect();
////			//mManagedConnections.remove(connection.getAddress());			
////		}
	}	
}
