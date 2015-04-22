package com.wimoto.app.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.ManagerOptions;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
import com.wimoto.app.MainActivity;
import com.wimoto.app.bluetooth.BluetoothConnection;
import com.wimoto.app.bluetooth.BluetoothService;
import com.wimoto.app.bluetooth.BluetoothService.BluetoothServiceListener;
import com.wimoto.app.model.Sensor;
import com.wimoto.app.model.demosensors.ClimateDemoSensor;
import com.wimoto.app.model.demosensors.ThermoDemoSensor;
import com.wimoto.app.utils.AppContext;

public class SensorsManager implements BluetoothServiceListener {

	private final static String WIMOTO_DB 		= "wimoto";
	private final static String WIMOTO_SENSORS 	= "wimoto_sensors";
	
	private final static String DOC_TYPE		= "sensor";
	
	private Map<String, Sensor> mSensors;
	
	private Database mDatabase;
	
	private BluetoothService mBluetoothService;
	
	public interface SensorsManagerListener {
		void didUpdateSensors(ArrayList<Sensor> sensors);
	}
	 
	private Set<SensorsManagerListener> mUnregisteredSensorListeners;
	private Set<SensorsManagerListener> mRegisteredSensorListeners;
		
	public SensorsManager () {
		mSensors = new HashMap<String, Sensor>();
		
		mUnregisteredSensorListeners = new HashSet<SensorsManagerListener> ();
		mRegisteredSensorListeners = new HashSet<SensorsManagerListener> ();

		try {
			mBluetoothService = new BluetoothService(this);
			
			Manager manager = new Manager(new AndroidContext(AppContext.getContext()), Manager.DEFAULT_OPTIONS);
			
			mDatabase = manager.getDatabase(WIMOTO_DB);
			
			QueryEnumerator enumerator = getQuery().run();
			
			while (enumerator.hasNext()) {
				Document doc = enumerator.next().getDocument();
				
				Sensor sensor = Sensor.getSensorFromDocument(doc);

				mSensors.put(sensor.getId(), sensor);
				mBluetoothService.registerConnection(sensor.getId());
			}
			
			Log.e("Loaded Sensors Count", Integer.toString(mSensors.size()));	
			
			addDemoSensors();
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	public boolean isBluetoothEnabled() {
		return mBluetoothService.isBluetoothEnabled();
	}
	
	public void startScan() {
		mBluetoothService.scanLeDevices(true);
	}
	
	public void stopScan() {
		mBluetoothService.scanLeDevices(false);
	}
	
	public void disconnectGatts() {
		mBluetoothService.disconnectGatts();
	}
	
	public Query getQuery() {
		View view = mDatabase.getView(WIMOTO_SENSORS);
		if (view.getMap() == null) {
			Mapper mapper = new Mapper() {
				@Override
				public void map(Map<String, Object> document, Emitter emitter) {
					String type = (String)document.get("type");
                    if (DOC_TYPE.equals(type)) {
                        emitter.emit(document.get("id"), document);
                    }
				}
			};
			view.setMap(mapper, "1.0");
		}
		
		Query query = view.createQuery();
		return query;
	}
	
	private void addDemoSensors() {
		if (!mSensors.containsKey(ClimateDemoSensor.SENSOR_CLIMATE_DEMO)) {
			mSensors.put(ClimateDemoSensor.SENSOR_CLIMATE_DEMO, new ClimateDemoSensor());
		}
		
		if (!mSensors.containsKey(ThermoDemoSensor.SENSOR_THERMO_DEMO)) {
			mSensors.put(ThermoDemoSensor.SENSOR_THERMO_DEMO, new ThermoDemoSensor());
		}
		
		Log.e("Total Count with demo", Integer.toString(mSensors.size()));
	}
	
	public void registerSensor(Sensor newSensor) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    Calendar calendar = GregorianCalendar.getInstance();
	    String currentTimeString = dateFormatter.format(calendar.getTime());
		
		Map<String, Object> properties = new HashMap<String, Object>();
		
		properties.put("type", DOC_TYPE);
		properties.put("title", newSensor.getTitle());
		properties.put("id", newSensor.getId());
		properties.put("sensor_type", newSensor.getType().getValue());
		properties.put("created_at", currentTimeString);

		try {
			Document document = mDatabase.createDocument();
			document.putProperties(properties);
			
			newSensor.setDocument(document);
			
			notifyRegisteredSensorObservers();
			notifyUnregisteredSensorObservers();
			
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		} 
	}
	
	public void unregisterSensor(Sensor sensor) {
		try {
			sensor.getDocument().delete();
			sensor.setDocument(null);
			
			if (!sensor.isConnected()) {
				mSensors.remove(sensor.getId());
			}
			
			notifyRegisteredSensorObservers();
			notifyUnregisteredSensorObservers();
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}
	
	public void addListenerForUnregisteredSensors(SensorsManagerListener listener) {
		mUnregisteredSensorListeners.add(listener);
		
		listener.didUpdateSensors(getUnregisteredSensors());
	}
	
	public void removeListenerForUnregisteredSensors(SensorsManagerListener observer) {
		mUnregisteredSensorListeners.remove(observer);
	}
	
	public void addListenerForRegisteredSensors(SensorsManagerListener observer) {
		mRegisteredSensorListeners.add(observer);
		
		observer.didUpdateSensors(getRegisteredSensors());
	}
	
	public void removeListenerForRegisteredSensors(SensorsManagerListener observer) {
		mRegisteredSensorListeners.remove(observer);
	}
	
	public ArrayList<Sensor> getRegisteredSensors() {
		ArrayList<Sensor> resultList = new ArrayList<Sensor> ();
		
		Collection<Sensor> values = mSensors.values();
		for (Sensor sensor: values) {
			if (sensor.getDocument() != null) {
				resultList.add(sensor);
			}
		}
		return resultList;
	}
	
	public ArrayList<Sensor> getUnregisteredSensors() {
		ArrayList<Sensor> resultList = new ArrayList<Sensor> ();
		
		Collection<Sensor> values = mSensors.values();
		for (Sensor sensor: values) {
			if (sensor.getDocument() == null) {
				resultList.add(sensor);
			}
		}
		return resultList;
	}
	
	private void notifyRegisteredSensorObservers() {
		ArrayList<Sensor> sensors = getRegisteredSensors();
		for (SensorsManagerListener observer: mRegisteredSensorListeners) {
			observer.didUpdateSensors(sensors);
		}
	}
	
	private void notifyUnregisteredSensorObservers() {
		ArrayList<Sensor> sensors = getUnregisteredSensors();
		for (SensorsManagerListener observer: mUnregisteredSensorListeners) {
			observer.didUpdateSensors(sensors);
		}
	}
					
	@Override
	public void connectionEstablished(BluetoothConnection connection) {
		Sensor sensor = mSensors.get(connection.getId());
		if (sensor == null) {
			mSensors.put(connection.getId(), Sensor.getSensorFromConnection(connection));
			
			((MainActivity)AppContext.getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					notifyUnregisteredSensorObservers();
				}
			});
		} else {
			sensor.setConnection(connection);
		}		
	}

	@Override
	public void connectionAborted(BluetoothConnection connection) {
		Sensor sensor = mSensors.get(connection.getId());
		if (sensor != null) { 
			sensor.setConnection(null);
			
			if (sensor.getDocument() == null) {
				mSensors.remove(connection.getId());
				
				((MainActivity)AppContext.getContext()).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						notifyUnregisteredSensorObservers();
					}
				});
			}
		}
	}
}
