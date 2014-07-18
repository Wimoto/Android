package com.marknicholas.wimoto.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.os.Handler;
import android.os.Message;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.View;
import com.couchbase.lite.util.Log;
import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection;
import com.marknicholas.wimoto.bluetooth.BluetoothConnection.BluetoothDiscoveryListener;
import com.marknicholas.wimoto.bluetooth.BluetoothService;
import com.marknicholas.wimoto.models.sensor.ClimateSensor;
import com.marknicholas.wimoto.models.sensor.GrowSensor;
import com.marknicholas.wimoto.models.sensor.Sensor;
import com.marknicholas.wimoto.models.sensor.ThermoSensor;
import com.marknicholas.wimoto.models.sensor.WaterSensor;
import com.marknicholas.wimoto.utils.AppContext;

public class SensorsManager implements BluetoothDiscoveryListener {

	private final static String WIMOTO_DB 		= "wimoto";
	private final static String WIMOTO_SENSORS 	= "wimoto_sensors";
	
	private final static String DOC_TYPE		= "sensor";
	
	private final static String TAG = "SensorsManager";
	
	private Map<String, Sensor> mSensors;
	
	private Database mDatabase;
	
	private BluetoothService mBluetoothService;
	
	public interface SensorObserver {
		void didUpdateSensors(ArrayList<Sensor> sensors);
	}
	 
	private ArrayList<SensorObserver> mUnregisteredSensorObservers;
	private ArrayList<SensorObserver> mRegisteredSensorObservers;
	
	private static SensorsManager sManager = null;
	
	public static SensorsManager getInstance() {
		if (sManager == null) {
			sManager = new SensorsManager();
		}
		
		return sManager;
	}
	
	private SensorsManager () {
		mSensors = new HashMap<String, Sensor>();
		
		mUnregisteredSensorObservers = new ArrayList<SensorObserver> ();
		mRegisteredSensorObservers = new ArrayList<SensorObserver> ();

		try {
			Manager manager = new Manager(AppContext.getContext().getFilesDir(), Manager.DEFAULT_OPTIONS);
			mDatabase = manager.getDatabase(WIMOTO_DB);
			
			QueryEnumerator enumerator = getQuery().run();
			
			while (enumerator.hasNext()) {
				Document doc = enumerator.next().getDocument();
				
				Sensor sensor = Sensor.getSensorFromDocument(doc);
				mSensors.put(sensor.getId(), sensor);
			}
			
			mBluetoothService = new BluetoothService(this);
			
			Log.e("Loaded Sensors Count", Integer.toString(mSensors.size()));	
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	public static boolean isBluetoothEnabled() {
		return SensorsManager.getInstance().mBluetoothService.isBluetoothEnabled();
	}
	
	public static void startScan() {
		SensorsManager.getInstance().mBluetoothService.scanLeDevices(true);
	}
	
	public static void stopScan() {
		SensorsManager.getInstance().mBluetoothService.scanLeDevices(false);
	}
	
	public static void disconnectGatts() {
		SensorsManager.getInstance().mBluetoothService.disconnectGatts();
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
	
	public void registerSensor(Sensor newSensor) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    Calendar calendar = GregorianCalendar.getInstance();
	    String currentTimeString = dateFormatter.format(calendar.getTime());
		
		Map<String, Object> properties = new HashMap<String, Object>();
		
		properties.put("type", DOC_TYPE);
		properties.put("title", newSensor.getTitle());
		properties.put("id", newSensor.getId());
		properties.put("sensor_type", newSensor.getType());
		properties.put("created_at", currentTimeString);
		
		try {
			Document document = mDatabase.createDocument();
			document.putProperties(properties);
			
			newSensor.setRegistered(true);
			
			notifyRegisteredSensorObservers();
			notifyUnregisteredSensorObservers();
			
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		} 
	}
	
	public void unregisterSensor(Sensor sensor) {
		try {
			QueryEnumerator enumerator = getQuery().run();
			
			if (enumerator == null) {
				return;
			}
			
			while(enumerator.hasNext()) {
				Document document = enumerator.next().getDocument();
				String sensorId = (String)document.getProperty("id");
				if (sensor.getId().equals(sensorId)) {
					document.delete();
					
					sensor.setRegistered(false);
					
					if (!sensor.isConnected()) {
						mSensors.remove(sensor);
					}
					
					notifyRegisteredSensorObservers();
					notifyUnregisteredSensorObservers();
				}
			}
			
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		}
	}
	
	public void addObserverForUnregisteredSensors(SensorObserver observer) {
		mUnregisteredSensorObservers.add(observer);
		
		observer.didUpdateSensors(getUnregisteredSensors());
	}
	
	public void removeObserverForUnregisteredSensors(SensorObserver observer) {
		mUnregisteredSensorObservers.remove(observer);
	}
	
	public void addObserverForRegisteredSensors(SensorObserver observer) {
		mRegisteredSensorObservers.add(observer);
		
		observer.didUpdateSensors(getRegisteredSensors());
	}
	
	public void removeObserverForRegisteredSensors(SensorObserver observer) {
		mRegisteredSensorObservers.remove(observer);
	}
	
	public ArrayList<Sensor> getRegisteredSensors() {
		ArrayList<Sensor> resultList = new ArrayList<Sensor> ();
		
		Collection<Sensor> values = mSensors.values();
		for (Sensor sensor: values) {
			if (sensor.isRegistered()) {
				resultList.add(sensor);
			}
		}
		return resultList;
	}
	
	public ArrayList<Sensor> getUnregisteredSensors() {
		ArrayList<Sensor> resultList = new ArrayList<Sensor> ();
		
		Collection<Sensor> values = mSensors.values();
		for (Sensor sensor: values) {
			if (!sensor.isRegistered()) {
				resultList.add(sensor);
			}
		}
		return resultList;
	}
	
	private void notifyRegisteredSensorObservers() {
		ArrayList<Sensor> sensors = getRegisteredSensors();
		for (SensorObserver observer: mRegisteredSensorObservers) {
			observer.didUpdateSensors(sensors);
		}
	}
	
	private void notifyUnregisteredSensorObservers() {
		ArrayList<Sensor> sensors = getUnregisteredSensors();
		for (SensorObserver observer: mUnregisteredSensorObservers) {
			observer.didUpdateSensors(sensors);
		}
	}
	
	private Sensor getRandomSensor() {
		ArrayList<Sensor> randomList = new ArrayList<Sensor>();
		
		GrowSensor growSensor = new GrowSensor();
		growSensor.setId("ABCDEF-GHIJKL-MNOPQR-STUVWX-YZ");
		growSensor.setRssi("-37dB");
		randomList.add(growSensor);
		
		ThermoSensor thermoSensor = new ThermoSensor();
		thermoSensor.setId("123456-GHIJKL-765432-STUVWX-42");
		thermoSensor.setRssi("-42dB");
		randomList.add(thermoSensor);
		
		ClimateSensor climateSensor = new ClimateSensor();
		climateSensor.setId("xxxxxx-yyyyyy-zzzzzz-STUVWX-00");
		climateSensor.setRssi("-11dB");
		randomList.add(climateSensor);
		
		GrowSensor growSensor2 = new GrowSensor();
		growSensor2.setId("ABC123-456JKL-MNO789-STUVWX-00");
		growSensor2.setRssi("-666dB");
		randomList.add(growSensor2);
		
		WaterSensor waterSensor = new WaterSensor();
		waterSensor.setId("pppppp-fffffff-zzzzzz-iiiiii-rr");
		waterSensor.setRssi("-213dB");
		randomList.add(waterSensor);
		
		Random random = new Random();
		int index = random.nextInt(randomList.size());
		Sensor randomSensor = randomList.get(index);
		
		Log.e("RANDOM", String.format("index = %d, id = %s", index, randomSensor.getId()));
		
		return randomSensor;
	}
	
	private void generateRandomSensorBehavior() {
		Sensor sensor = getRandomSensor();
		
		Sensor equalSensor = getEqualSensor(sensor);
		if (equalSensor == null) {
			connectSensor(sensor);
		} else {
			if (equalSensor.isConnected()) {
				disconnectSensor(equalSensor);
			} else {
				connectSensor(equalSensor);
			}
		}
	}
	
	private Sensor getEqualSensor(Sensor newSensor) {
//		for (Sensor sensor:mSensors) {
//			if (newSensor.getId().equals(sensor.getId())) {
//				return sensor;
//			}
//		}
		return null;
	}
	
	private void connectSensor(Sensor sensor) {
//		Sensor equalSensor = getEqualSensor(sensor);
//		if (equalSensor == null) {
//			mSensors.add(sensor);
//		} else {
//			sensor = equalSensor;
//		}
//		
//		sensor.setConnected(true);
//		Log.e("Connected", sensor.getTitle() + " " + sensor.getId());
//		
//		mHandler.sendEmptyMessage(0);
	}
	
	private void disconnectSensor(Sensor sensor) {
		if (sensor.isRegistered()) {
			sensor.setConnected(false);
		} else {
			mSensors.remove(sensor);
		}
		
		Log.e("Disconnected", sensor.getTitle() + " " + sensor.getId());
		
		mHandler.sendEmptyMessage(0);
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message message) {
			notifyUnregisteredSensorObservers();
		}
	};

	@Override
	public void connectionEstablished(BluetoothConnection connection) {
		Sensor sensor = mSensors.get(connection.getId());
		if (sensor == null) {
			mSensors.put(connection.getId(), Sensor.getSensorFromConnection(connection));
		} else {
			sensor.setConnection(connection);
		}
		
		((MainActivity)AppContext.getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyUnregisteredSensorObservers();
			}
		});
	}

	@Override
	public void connectionAborted(BluetoothConnection connection) {
		Sensor sensor = mSensors.get(connection.getId());
		if (sensor != null) {
			if (!sensor.isRegistered()) {
				mSensors.remove(sensor);
			}
		}
		((MainActivity)AppContext.getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyUnregisteredSensorObservers();
			}
		});
	}
}
