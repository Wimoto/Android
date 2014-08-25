package com.wimoto.app.model;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;

import android.bluetooth.BluetoothGattCharacteristic;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.View;
import com.wimoto.app.R;
import com.wimoto.app.bluetooth.BluetoothConnection;
import com.wimoto.app.bluetooth.BluetoothConnection.WimotoProfile;
import com.wimoto.app.utils.AppContext;

public class ClimateSensor extends Sensor {

	private static String BLE_CLIMATE_SERVICE_UUID_TEMPERATURE 			= "E0035608-EC48-4ED0-9F3B-5419C00A94FD";
	private static String BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT 	= "E0035609-EC48-4ED0-9F3B-5419C00A94FD";
	
	private static String BLE_CLIMATE_SERVICE_UUID_LIGHT 				= "E003560E-EC48-4ED0-9F3B-5419C00A94FD";
	private static String BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT		 	= "E003560F-EC48-4ED0-9F3B-5419C00A94FD";

	private static String BLE_CLIMATE_SERVICE_UUID_HUMIDITY 			= "E0035614-EC48-4ED0-9F3B-5419C00A94FD";
	private static String BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT	 	= "E0035615-EC48-4ED0-9F3B-5419C00A94FD";
	
	public static final String CLIMATE_TEMPERATURE						= "ClimateTemperature";
	public static final String CLIMATE_LIGHT							= "ClimateLight";
	public static final String CLIMATE_HUMIDITY							= "ClimateHumidity";
		
	private float mTemperature;
	private float mLight;
	private float mHumidity;
	
	public ClimateSensor() {
		super();
		
		title = AppContext.getContext().getString(R.string.sensor_climate);
		
		mSensorValues.put(CLIMATE_TEMPERATURE, new LinkedList<Float>());
		mSensorValues.put(CLIMATE_LIGHT, new LinkedList<Float>());
		mSensorValues.put(CLIMATE_HUMIDITY, new LinkedList<Float>());
	}

	public ClimateSensor(BluetoothConnection connection) {
		this();
		
		setConnection(connection);
	}
	
	@Override
	public void setConnection(BluetoothConnection connection) {
		super.setConnection(connection);
		
		enableChangesNotification();
	}
	
	@Override
	public void setDocument(Document document) {
		super.setDocument(document);
		
		enableChangesNotification();
		
		Database database = mDocument.getDatabase(); 
		
		// pull temperature values
		View view = database.getView(CLIMATE_TEMPERATURE);
		if (view.getMap() == null) {
			Mapper mapper = new Mapper() {
				@Override
				public void map(Map<String, Object> document, Emitter emitter) {
					String type = (String) document.get(SensorValue.CB_DOCUMENT_TYPE);
                    if (CLIMATE_TEMPERATURE.equals(type)) {
                        emitter.emit(document.get(SensorValue.CB_DOCUMENT_SENSOR_VALUE_SENSOR_ID), document);
                    }
				}
			};
			view.setMap(mapper, "1.0");
		}
		
		try {
			LinkedList<Float> list = mSensorValues.get(CLIMATE_TEMPERATURE);
			QueryEnumerator enumerator = view.createQuery().run();
			
			while (enumerator.hasNext()) {
				SensorValue sensorValue = new SensorValue(enumerator.next().getDocument());
				list.add(sensorValue.getValue());
				
				if (list.size() > 20) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void enableChangesNotification() {
		if ((mConnection != null) && (mDocument != null)) {
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT);
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT);
			mConnection.enableChangesNotification(BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT);
		}
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.CLIMATE;
	}
	
	public float getTemperature() {
		return mTemperature;
	}

	public float getLight() {
		return mLight;
	}

	public float getHumidity() {
		return mHumidity;
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof BluetoothGattCharacteristic) {
			BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) data;
			
			String uuid = characteristic.getUuid().toString().toUpperCase();
			
			BigInteger bi = new BigInteger(characteristic.getValue());
			if (uuid.equals(BLE_CLIMATE_CHAR_UUID_TEMPERATURE_CURRENT)) {
				mTemperature = (float)(-46.85 + (175.72*bi.floatValue()/65536));
				
				addValue(CLIMATE_TEMPERATURE, mTemperature);
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_LIGHT_CURRENT)) {
				mLight = (float)(0.96 * bi.floatValue());	
			} else if (uuid.equals(BLE_CLIMATE_CHAR_UUID_HUMIDITY_CURRENT)) {
				mHumidity = (float)(-6.0 + (125.0*bi.floatValue()/65536)) * (-1);	
			}
		}
		
		super.update(observable, data);
	}
}
