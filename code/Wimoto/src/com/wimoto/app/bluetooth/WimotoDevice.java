package com.wimoto.app.bluetooth;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.mobitexoft.utils.propertyobserver.PropertyObservable;
import com.wimoto.app.AppContext;
import com.wimoto.app.model.demosensors.ClimateDemoSensor;
import com.wimoto.app.model.demosensors.ThermoDemoSensor;

public class WimotoDevice extends PropertyObservable {

	public enum Profile {
		CLIMATE(0),
		GROW(1),
		SENTRY(2),
		THERMO(3),
		WATER(4),
		CLIMATE_DEMO(5),
		THERMO_DEMO(6);
		
		private int value;
		
		private Profile(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
		
	public enum State {
		DISCONNECTED(0),
		CONNECTING(1),
		CONNECTED(2),
		DISCONNECTING(3);
		
		private int value;
		
		private State(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	public interface WimotoDeviceCallback {
		void onConnectionStateChange(State state);
		
		void onCharacteristicChanged(BluetoothGattCharacteristic characteristic);
		void onCharacteristicWritten(BluetoothGattCharacteristic characteristic, int status);
		
		void onReadRemoteRssi(int rssi);
	}
	
	public static final String WIMOTO_DEVICE_FIELD_RSSI			= "mRssi";
	
	private final static String BLE_CLIMATE_AD_SERVICE_UUID 	= "EC484ED09F3B5419C00A94FD";
	private final static String BLE_GROW_AD_SERVICE_UUID 		= "BFB04DD8929362AF5F545E31";
	private final static String BLE_SENTRY_AD_SERVICE_UUID 		= "E433442083D8CDAACCD2E312";
	private final static String BLE_THERMO_AD_SERVICE_UUID 		= "B61E4F828FE9B12CF2497338";
	private final static String BLE_WATER_AD_SERVICE_UUID 		= "9D7843C2AB2E0E48CAC2DBDA";
	
	private final static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	
	private AppContext mContext;
	
	private BluetoothDevice mBluetoothDevice;
	private BluetoothGatt mBluetoothGatt;
	
	private WimotoDeviceCallback mWimotoDeviceCallback;
	
	private Profile mProfile;
	
	private LinkedList<CharacteristicRequest> mRequests;
	
	private int mRssi;
	private Timer mRssiTimer;
	
	public static WimotoDevice getInstance(AppContext context, BluetoothDevice bluetoothDevice) {
		return new WimotoDevice(context, bluetoothDevice);
	}
	
	public static WimotoDevice getInstance(AppContext context, BluetoothDevice bluetoothDevice, byte[] scanRecord) {
        char[] hexChars = new char[scanRecord.length * 2];
        int v;
        for ( int j = 0; j < scanRecord.length; j++ ) {
            v = scanRecord[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
		
		String scanString = new String(hexChars).toUpperCase();
		if (scanString.contains(BLE_CLIMATE_AD_SERVICE_UUID)) {
			return new WimotoDevice(context, bluetoothDevice, Profile.CLIMATE);
		} else if (scanString.contains(BLE_GROW_AD_SERVICE_UUID)) {
			return new WimotoDevice(context, bluetoothDevice, Profile.GROW);
		} else if (scanString.contains(BLE_SENTRY_AD_SERVICE_UUID)) {
			return new WimotoDevice(context, bluetoothDevice, Profile.SENTRY);
		} else if (scanString.contains(BLE_THERMO_AD_SERVICE_UUID)) {
			return new WimotoDevice(context, bluetoothDevice, Profile.THERMO);
		} else if (scanString.contains(BLE_WATER_AD_SERVICE_UUID)) {
			return new WimotoDevice(context, bluetoothDevice, Profile.WATER);
		}
				
		return null;
	}
	
	public static WimotoDevice getDemoInstance(AppContext context, Profile profile) {
		return new WimotoDevice(context, null, profile);
	}
	
	private WimotoDevice() { 
		//
	}
	
	private WimotoDevice(AppContext context) { 
		mContext = context;
		
		mRequests = new LinkedList<WimotoDevice.CharacteristicRequest>();
	}
	
	private WimotoDevice(AppContext context, BluetoothDevice bluetoothDevice) {
		this(context);
		
		mBluetoothDevice = bluetoothDevice;
	}
	
	private WimotoDevice(AppContext context, BluetoothDevice bluetoothDevice, Profile profile) {
		this(context, bluetoothDevice);
		
		mProfile = profile;
	}
	
	public String getId() {
		if (mProfile == Profile.CLIMATE_DEMO) {
			return ClimateDemoSensor.SENSOR_CLIMATE_DEMO_ID;
		} else if (mProfile == Profile.THERMO_DEMO) {
			return ThermoDemoSensor.SENSOR_THERMO_DEMO_ID;
		}
		
		return mBluetoothDevice.getAddress();
	}
	
	public String getName() {
		if (mProfile == Profile.CLIMATE_DEMO) {
			return ClimateDemoSensor.SENSOR_CLIMATE_DEMO;
		} else if (mProfile == Profile.THERMO_DEMO) {
			return ThermoDemoSensor.SENSOR_THERMO_DEMO;
		}
		return mBluetoothDevice.getName();
	}
	
	public Profile getProfile() {
		return mProfile;
	}
	
	public void setRssi(int rssi) {
		notifyObservers(WIMOTO_DEVICE_FIELD_RSSI, mRssi, rssi);
		
		mRssi = rssi;
	}
	
	public void connect(WimotoDeviceCallback wimotoDeviceCallback) {
		mWimotoDeviceCallback = wimotoDeviceCallback;
		
		if (mBluetoothDevice == null) {
			//mWimotoDeviceCallback.onConnectionStateChange(State.CONNECTED);
			return;
		}
		
		mBluetoothGatt = mBluetoothDevice.connectGatt(mContext, true, new BluetoothGattCallback() {
	        @Override
	        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
	        	Log.e("", "BluetoothGattCallback " + newState + " ___status " + status);
	        	
	        	switch (newState) {
				case 0:
					mRequests = new LinkedList<WimotoDevice.CharacteristicRequest>();
					if (mWimotoDeviceCallback != null) {
						mWimotoDeviceCallback.onConnectionStateChange(State.DISCONNECTED);
					}
					
					mRssiTimer.cancel();
					mBluetoothGatt.close();
					
					break;

				case 1:
					if (mWimotoDeviceCallback != null) {
						mWimotoDeviceCallback.onConnectionStateChange(State.CONNECTING);
					}
					break;

				case 2:
					gatt.discoverServices();	
					
					TimerTask task = new TimerTask()
			         {
			            @Override
			            public void run()
			            {
			               mBluetoothGatt.readRemoteRssi();
			            }
			         };
			         mRssiTimer = new Timer();
			         mRssiTimer.schedule(task, 0, 2000);
					
					break;

				case 3:
					if (mWimotoDeviceCallback != null) {
						mWimotoDeviceCallback.onConnectionStateChange(State.DISCONNECTING);
					}
					break;

				default:
					break;
				}	        	
	        }

	        @Override
	        // New services discovered
	        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
	        	Log.w("", "onServicesDiscovered received: " + gatt.getServices().size());
				mWimotoDeviceCallback.onConnectionStateChange(State.CONNECTED);
//	        	
//	        	if (mSystemId == null) {
//	        		readCharacteristic(BLE_GENERIC_SERVICE_UUID_DEVICE, BLE_GENERIC_CHAR_UUID_SYSTEM_ID);
//	        	}
	        }
	        
	        @Override
	        // Result of a characteristic read operation
	        public void onCharacteristicRead(BluetoothGatt gatt,
	                BluetoothGattCharacteristic characteristic,
	                int status) {        	
	        	Log.e("", "onCharacteristicRead " + characteristic.getUuid());
				mWimotoDeviceCallback.onCharacteristicChanged(characteristic);
	        	performNextRequest();
	        	    
//	        	if (characteristic.getUuid().equals(UUID.fromString(BLE_GENERIC_CHAR_UUID_SYSTEM_ID))) {
//	        		mSystemId = new BigInteger(characteristic.getValue()).toString();        		
//	        	} else {
//	    			setChanged();
//	    			notifyObservers(characteristic);
//	        	}
//	        	
	        	
	        }

	        
			@Override
			public void onCharacteristicChanged(BluetoothGatt gatt,
					BluetoothGattCharacteristic characteristic) {
				Log.e("", "onCharacteristicChanged " + characteristic);
				mWimotoDeviceCallback.onCharacteristicChanged(characteristic);
				performNextRequest();
//				setChanged();
//				notifyObservers(characteristic);
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
				mWimotoDeviceCallback.onCharacteristicWritten(characteristic, status);
				//performNextRequest();
			}

			@Override
			public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
				//Log.e("", "onReadRemoteRssi " + rssi);
				mWimotoDeviceCallback.onReadRemoteRssi(rssi);
			}
		});
		//mBluetoothGatt.readRemoteRssi();
	}
	
	public void disconnect() {
		Log.e("", "WimotoDevice disconnect()");
		
		mWimotoDeviceCallback = null;
		
		if (mProfile == Profile.CLIMATE_DEMO || mProfile == Profile.THERMO_DEMO) {
			//mWimotoDeviceCallback.onConnectionStateChange(State.DISCONNECTED);
			return;
		}
		
		mBluetoothGatt.disconnect();
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
