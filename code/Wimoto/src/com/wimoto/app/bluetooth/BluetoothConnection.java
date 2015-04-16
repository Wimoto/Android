package com.wimoto.app.bluetooth;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Observable;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.wimoto.app.model.SensorProfile;
import com.wimoto.app.utils.AppContext;

public class BluetoothConnection extends Observable {
	
	private static String BLE_GENERIC_SERVICE_UUID_DEVICE 			= "0000180A-0000-1000-8000-00805F9B34FB";
	private static String BLE_GENERIC_CHAR_UUID_SYSTEM_ID 			= "00002A23-0000-1000-8000-00805F9B34FB";
	
	private static String BLE_CLIMATE_AD_SERVICE_UUID 	= "EC484ED09F3B5419C00A94FD";
	private static String BLE_GROW_AD_SERVICE_UUID 		= "BFB04DD8929362AF5F545E31";
	private static String BLE_SENTRY_AD_SERVICE_UUID 	= "E433442083D8CDAACCD2E312";
	private static String BLE_THERMO_AD_SERVICE_UUID 	= "B61E4F828FE9B12CF2497338";
	private static String BLE_WATER_AD_SERVICE_UUID 	= "9D7843C2AB2E0E48CAC2DBDA";
	
	final private static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	
	private String mSystemId;
	private SensorProfile mSensorProfile;
	
	private int mRssi;
		
	private BluetoothDevice mBluetoothDevice;
	private BluetoothGatt mBluetoothGatt;
	
	private LinkedList<CharacteristicRequest> mRequests;
		
	public interface BluetoothConnectionStateListener {
		void didConnectionStateChanged(BluetoothConnection connection);
	}
	
	private BluetoothConnectionStateListener mBluetoothConnectionStateListener;
	
	public static BluetoothConnection createConnection(BluetoothConnectionStateListener listener, BluetoothDevice device) {
		BluetoothConnection connection = new BluetoothConnection(listener, device);
		connection.connect();
		return connection;
	}
	
	public static BluetoothConnection createConnection(BluetoothConnectionStateListener listener, BluetoothDevice device,
            byte[] scanRecord) {
		BluetoothConnection connection = new BluetoothConnection(listener, device, scanRecord);
		
		if (connection.getSensorProfile() == SensorProfile.UNDEFINED) {
			return null;
		} else {
			connection.connect();
			return connection;
		}
	}
	
	private BluetoothConnection(BluetoothConnectionStateListener listener, BluetoothDevice device) {
		mBluetoothConnectionStateListener = listener;
		mBluetoothDevice = device;
		mRequests = new LinkedList<BluetoothConnection.CharacteristicRequest>();
	}
	
	private BluetoothConnection(BluetoothConnectionStateListener listener, BluetoothDevice device,
            byte[] scanRecord) {
		this(listener, device);

		mSensorProfile = defineProfile(scanRecord);
	}
	
	private BluetoothConnection getConnection() {
		return this;
	}
	
	public BluetoothDevice getBluetoothDevice() {
		return mBluetoothDevice;
	}

	public String getId() {
		return mBluetoothDevice.getAddress();
	}
	
	public String getName() {
		return mBluetoothDevice.getName();
	}
	
	public String getAddress() {
		return mBluetoothDevice.getAddress();
	}
	
	public int getRssi() {
		return mRssi;
	}

	public void setRssi(int rssi) {
		mRssi = rssi;
		
		setChanged();
		notifyObservers(rssi);
	}

	public void readRssi() {
		if (mBluetoothGatt != null) {
			mBluetoothGatt.readRemoteRssi();
		}
	}
	
	public SensorProfile getSensorProfile() {
		return mSensorProfile;
	}
	
	public void setSystemId(String systemId) {
		mSystemId = systemId;
	}

	private SensorProfile defineProfile(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
		
		String scanString = new String(hexChars).toUpperCase();
		if (scanString.contains(BLE_CLIMATE_AD_SERVICE_UUID)) {
			return SensorProfile.CLIMATE;
		} else if (scanString.contains(BLE_GROW_AD_SERVICE_UUID)) {
			return SensorProfile.GROW;
		} else if (scanString.contains(BLE_SENTRY_AD_SERVICE_UUID)) {
			return SensorProfile.SENTRY;
		} else if (scanString.contains(BLE_THERMO_AD_SERVICE_UUID)) {
			return SensorProfile.THERMO;
		} else if (scanString.contains(BLE_WATER_AD_SERVICE_UUID)) {
			return SensorProfile.WATER;
		}
		
		return SensorProfile.UNDEFINED;
	}

	public void connect() {
		mBluetoothGatt = mBluetoothDevice.connectGatt(AppContext.getContext(), false, mGattCallback);
	}
	
	public void disconnect() {
		if (mBluetoothGatt != null) {
			Log.e("", "disconnect() " + mBluetoothDevice.getName());
			
			mBluetoothConnectionStateListener = null;
			
			mBluetoothGatt.disconnect();
			mBluetoothGatt.close();
		}
	}
		
	private void addRequest(CharacteristicRequest request) {
		Log.e("", "addRequest(CharacteristicRequest request)");
		
		mRequests.add(request);
		if (mRequests.size() == 1) {
			performRequest(request);
		}
	}
	
	private void performRequest(CharacteristicRequest request) {
    	BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(request.getServiceUuidString()));

		switch (request.getType()) {
			case CharacteristicRequest.REQUEST_TYPE_READ: {
				mBluetoothGatt.readCharacteristic(service.getCharacteristic(UUID.fromString(request.getCharacteristicUuidString())));
				break;
			}	
			case CharacteristicRequest.REQUEST_TYPE_WRITE: {
				Log.e("", "CharacteristicRequest.REQUEST_TYPE_WRITE");
				
		    	BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(request.getCharacteristicUuidString()));
		    	characteristic.setValue(request.getValue());
				if (!mBluetoothGatt.writeCharacteristic(characteristic)) {
					performNextRequest();
				}
				break;
			}	
			case CharacteristicRequest.REQUEST_TYPE_UPDATE: {		    	
		    	BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(request.getCharacteristicUuidString()));
				mBluetoothGatt.setCharacteristicNotification(characteristic, true);
				
				BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
				        UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
				descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				mBluetoothGatt.writeDescriptor(descriptor);
				break;
			}
		}
	}
	
	private void performNextRequest() {
		if (!mRequests.isEmpty()) {
			mRequests.removeFirst();
			if (!mRequests.isEmpty()) {
				performRequest(mRequests.getFirst());
			}
		}
	}
	
	public void readCharacteristic(String serviceUuidString, String characteristicUuidString) {
		addRequest(new CharacteristicRequest(CharacteristicRequest.REQUEST_TYPE_READ, serviceUuidString, characteristicUuidString));
	}
	
	public void enableChangesNotification(String serviceUuidString, String characteristicUuidString) {
		addRequest(new CharacteristicRequest(CharacteristicRequest.REQUEST_TYPE_UPDATE, serviceUuidString, characteristicUuidString));
	}
	
	public void writeCharacteristic(String serviceUuidString, String characteristicUuidString, byte[] value) {
		addRequest(new CharacteristicRequest(CharacteristicRequest.REQUEST_TYPE_WRITE, serviceUuidString, characteristicUuidString, value));
	}
	
    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                int newState) {
            
        	Log.i("", "onConnectionStateChange " + newState);
        	
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("", "Connected to GATT server.");
                Log.i("", "Attempting to start service discovery:" +
                		gatt.discoverServices());                
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("", "Disconnected from GATT server."); 
                if (mBluetoothConnectionStateListener != null) {
                	mBluetoothConnectionStateListener.didConnectionStateChanged(getConnection());
                }
            }
        }

        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        	Log.w("", "onServicesDiscovered received: " + gatt.getServices().size());
        	
        	if (mSystemId == null) {
        		readCharacteristic(BLE_GENERIC_SERVICE_UUID_DEVICE, BLE_GENERIC_CHAR_UUID_SYSTEM_ID);
        	}
        }
        
        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic,
                int status) {        	
        	Log.e("", "Fvalue " + characteristic.getUuid());
        	    
        	if (characteristic.getUuid().equals(UUID.fromString(BLE_GENERIC_CHAR_UUID_SYSTEM_ID))) {
        		mSystemId = new BigInteger(characteristic.getValue()).toString();
        		
                if (mBluetoothConnectionStateListener != null) {
                	mBluetoothConnectionStateListener.didConnectionStateChanged(getConnection());
                }
        	} else {
    			setChanged();
    			notifyObservers(characteristic);
        	}
        	
        	performNextRequest();
        }

        
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			setChanged();
			notifyObservers(characteristic);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			Log.e("", "onDescriptorWrite " + status);
			performNextRequest();
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			Log.e("", "onCharacteristicWrite " + status);
			performNextRequest();
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			setRssi(rssi);
		}
    };

    private class CharacteristicRequest {
    	
    	public static final int REQUEST_TYPE_READ 			= 8499;
    	public static final int REQUEST_TYPE_WRITE 			= 8599;
    	public static final int REQUEST_TYPE_UPDATE 		= 8699;
    	
    	private int mType;
    	private String mServiceUuidString;
    	private String mCharacteristicUuidString;
    	private byte[] mValue;
    	
    	public CharacteristicRequest(int type, String serviceUuidString, String characteristicUuidString) {
    		mType = type;
    		mServiceUuidString = serviceUuidString;
    		mCharacteristicUuidString = characteristicUuidString;
    	}

    	public CharacteristicRequest(int type, String serviceUuidString, String characteristicUuidString, byte[] value) {
    		this(type, serviceUuidString, characteristicUuidString);
    		mValue = value;
    	}
    	
		public int getType() {
			return mType;
		}

		public String getServiceUuidString() {
			return mServiceUuidString;
		}

		public String getCharacteristicUuidString() {
			return mCharacteristicUuidString;
		}

		public byte[] getValue() {
			return mValue;
		}
    }
}
