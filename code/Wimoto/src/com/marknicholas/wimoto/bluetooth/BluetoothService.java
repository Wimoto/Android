package com.marknicholas.wimoto.bluetooth;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.marknicholas.wimoto.bluetooth.BluetoothConnection.BluetoothDiscoveryListener;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection.WimotoProfile;
import com.marknicholas.wimoto.models.sensor.ClimateSensor;
import com.marknicholas.wimoto.models.sensor.GrowSensor;
import com.marknicholas.wimoto.utils.AppContext;

public class BluetoothService {
	
	private BluetoothAdapter mBluetoothAdapter;
	private Map<String, BluetoothConnection> mManagedConnections;
	
	private BluetoothDiscoveryListener mDiscoveryListener;
		
	public BluetoothService(BluetoothDiscoveryListener discoveryListener) {
		mDiscoveryListener = discoveryListener;
		
		mBluetoothAdapter = ((BluetoothManager)AppContext.getContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
		mManagedConnections = new HashMap<String, BluetoothConnection>();		
	}
	
	public static boolean hasBluetoothFeature() {
		return AppContext.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
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
			connection.disconnect();
		}		
		mManagedConnections.clear();
	}
	
	public void scanLeDevices(boolean enable) {
		if (enable) {
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback =
	        new BluetoothAdapter.LeScanCallback() {
	    @Override
	    public void onLeScan(final BluetoothDevice device, int rssi,
	            byte[] scanRecord) {
	    	Log.e("", "onLeScan device found " + device.getName() + " ____ " + mManagedConnections.size());
	    	
	    	BluetoothConnection connection = mManagedConnections.get(device.getAddress());
	    	if (connection == null) {
		    	connection = BluetoothConnection.createConnection(mDiscoveryListener, device, scanRecord);
	    		
	    		Log.e("", "onLeScan conn created " + device.getName());
	    		if (connection.getWimotoProfile() != WimotoProfile.UNDEFINED) {
	    			Log.e("", "onLeScan conn added " + device.getName());
	    			mManagedConnections.put(device.getAddress(), connection);
	    			connection.connect();
	    		}
	    	} else if (connection.getState() != BluetoothProfile.STATE_CONNECTED) {
	    		connection.connect(); 
	    	}
	   }
	};
}
