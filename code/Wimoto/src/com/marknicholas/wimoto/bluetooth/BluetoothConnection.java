package com.marknicholas.wimoto.bluetooth;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.marknicholas.wimoto.models.sensor.Sensor;
import com.marknicholas.wimoto.utils.AppContext;

public class BluetoothConnection {
	
	private static String BLE_CLIMATE_AD_SERVICE_UUID 	= "EC484ED09F3B5419C00A94FD";
	private static String BLE_GROW_AD_SERVICE_UUID 		= "BFB04DD8929362AF5F545E31";
	private static String BLE_SENTRY_AD_SERVICE_UUID 	= "E433442083D8CDAACCD2E312";
	private static String BLE_THERMO_AD_SERVICE_UUID 	= "B61E4F828FE9B12CF2497338";
	private static String BLE_WATER_AD_SERVICE_UUID 	= "9D7843C2AB2E0E48CAC2DBDA";
	
	public interface BluetoothDiscoveryListener {
		void connectionEstablished(BluetoothConnection connection);
		void connectionAborted(BluetoothConnection connection);
	}
	
	final private static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	
	private String mSystemId;
	private WimotoProfile mWimotoProfile;
	private int mState;
	
	private BluetoothDiscoveryListener mDiscoveryListener;
	private BluetoothDevice mBluetoothDevice;
	
	private BluetoothGatt mBluetoothGatt;
	
	private LinkedList<CharacteristicRequest> readRequests;
		
	public static BluetoothConnection createConnection(BluetoothDiscoveryListener discoveryListener, BluetoothDevice device,
            byte[] scanRecord) {
		return new BluetoothConnection(discoveryListener, device, scanRecord);
	}
	
	private BluetoothConnection(BluetoothDiscoveryListener discoveryListener, BluetoothDevice device,
            byte[] scanRecord) {
		
		mDiscoveryListener = discoveryListener;
		mWimotoProfile = defineProfile(scanRecord);
		
		mBluetoothDevice = device;
		
		readRequests = new LinkedList<BluetoothConnection.CharacteristicRequest>();
	}
	
	public String getId() {
		return mBluetoothDevice.getAddress();
	}
	
	public String getName() {
		return mBluetoothDevice.getName();
	}
	
	public int getState() {
		return mState;
	}
	
	public WimotoProfile getWimotoProfile() {
		return mWimotoProfile;
	}
	
	public void setSystemId(String systemId) {
		mSystemId = systemId;
		
	}

	private WimotoProfile defineProfile(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
		
		String scanString = new String(hexChars).toUpperCase();
		if (scanString.contains(BLE_CLIMATE_AD_SERVICE_UUID)) {
			return WimotoProfile.CLIMATE;
		} else if (scanString.contains(BLE_GROW_AD_SERVICE_UUID)) {
			return WimotoProfile.GROW;
		} else if (scanString.contains(BLE_SENTRY_AD_SERVICE_UUID)) {
			return WimotoProfile.SENTRY;
		} else if (scanString.contains(BLE_THERMO_AD_SERVICE_UUID)) {
			return WimotoProfile.THERMO;
		} else if (scanString.contains(BLE_WATER_AD_SERVICE_UUID)) {
			return WimotoProfile.WATER;
		}
		
		return WimotoProfile.UNDEFINED;
	}

	public void connect() {
		mBluetoothGatt = mBluetoothDevice.connectGatt(AppContext.getContext(), false, mGattCallback);
	}
	
	public void disconnect() {
		if (mBluetoothGatt != null) {
			mBluetoothGatt.disconnect();
		}
	}
	
	private void didConnectionEstablished() {
		if (mDiscoveryListener != null) {
			mDiscoveryListener.connectionEstablished(this);
		}
	}
	
	private void didConnectionAborted() {
		if (mDiscoveryListener != null) {
			mDiscoveryListener.connectionAborted(this);
		}
	}
	
	public void readCharacteristic(String serviceUuidString, String characteristicUuidString) {
		CharacteristicRequest request = new CharacteristicRequest(serviceUuidString, characteristicUuidString);
		readRequests.add(request);
		if (readRequests.size() == 1) {
			performReadRequest(request);
		}
	}
	
	private void performReadRequest(CharacteristicRequest request) {
    	BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(request.getServiceUuidString()));
		mBluetoothGatt.readCharacteristic(service.getCharacteristic(UUID.fromString(request.getCharacteristicUuidString())));
	}
	
    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                int newState) {
            
        	Log.i("", "onConnectionStateChange " + newState);
        	
        	mState = newState;
        	
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("", "Connected to GATT server.");
                Log.i("", "Attempting to start service discovery:" +
                		gatt.discoverServices());                
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("", "Disconnected from GATT server.");  
                didConnectionAborted();
            }
        }

        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        	Log.w("", "onServicesDiscovered received: " + gatt.getServices().size());
        	
        	if (mSystemId == null) {
        		readCharacteristic(Sensor.BLE_GENERIC_SERVICE_UUID_DEVICE, Sensor.BLE_GENERIC_CHAR_UUID_SYSTEM_ID);
        	}
        }
        
        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic,
                int status) {        	
        	Log.e("", "Fvalue " + characteristic.getUuid());
        	    
        	if (characteristic.getUuid().equals(UUID.fromString(Sensor.BLE_GENERIC_CHAR_UUID_SYSTEM_ID))) {
        		mSystemId = new BigInteger(characteristic.getValue()).toString();
        		
        		didConnectionEstablished();
        	}
        	
        	readRequests.removeFirst();
    		if (readRequests.size() > 0) {
    			performReadRequest(readRequests.getFirst());
    		}        	
        }
    };

    public enum WimotoProfile {
    	UNDEFINED(0),
    	CLIMATE(1),
    	GROW(2),
    	SENTRY(3),
    	THERMO(4),
    	WATER(5);
    	
    	private int value;
    	private WimotoProfile(int value) {
    		this.value = value;
    	}
    }
    
    private class CharacteristicRequest {
    	
    	private String mServiceUuidString;
    	private String mCharacteristicUuidString;
    	
    	public CharacteristicRequest(String serviceUuidString, String characteristicUuidString) {
    		mServiceUuidString = serviceUuidString;
    		mCharacteristicUuidString = characteristicUuidString;
    	}

		public String getServiceUuidString() {
			return mServiceUuidString;
		}

		public String getCharacteristicUuidString() {
			return mCharacteristicUuidString;
		}
    }
}
