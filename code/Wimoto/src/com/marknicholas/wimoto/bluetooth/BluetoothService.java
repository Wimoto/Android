package com.marknicholas.wimoto.bluetooth;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.marknicholas.wimoto.bluetooth.BluetoothConnection.BluetoothConnectionStateListener;
import com.marknicholas.wimoto.utils.AppContext;

public class BluetoothService implements BluetoothConnectionStateListener {
	
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private Map<String, BluetoothConnection> mManagedConnections;
	
	public interface BluetoothServiceListener {
		void connectionEstablished(BluetoothConnection connection);
		void connectionAborted(BluetoothConnection connection);
	}
	
	private BluetoothServiceListener mBleServiceListener;
		
	public BluetoothService(BluetoothServiceListener bleServiceListener) {
		mBleServiceListener = bleServiceListener;
		
		mBluetoothManager = (BluetoothManager)AppContext.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		mManagedConnections = new HashMap<String, BluetoothConnection>();		
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
		    	connection = BluetoothConnection.createConnection(getCurrentService(), device, scanRecord);
	    		
		    	if (connection != null) {
		    		mManagedConnections.put(device.getAddress(), connection);
		    	}		    	
	    	}
	   }
	};
	
	private BluetoothService getCurrentService() {
		return this;
	}

	@Override
	public void didConnectionStateChanged(BluetoothConnection connection) {
		int state = mBluetoothManager.getConnectionState(connection.getBluetoothDevice(), BluetoothProfile.GATT);
		
		Log.e("", "sensor " + connection.getBluetoothDevice().getName() + " _ " + state);
		
		if (state == BluetoothProfile.STATE_CONNECTED) {
			if (mBleServiceListener != null) {
				mBleServiceListener.connectionEstablished(connection);
			}
		} else if (state == BluetoothProfile.STATE_DISCONNECTED) {
			if (mBleServiceListener != null) {
				mBleServiceListener.connectionAborted(connection);
			}
			
			connection.disconnect();
			mManagedConnections.remove(connection.getAddress());			
		}
	}	
}
