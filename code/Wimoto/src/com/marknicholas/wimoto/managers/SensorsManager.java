package com.marknicholas.wimoto.managers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.View;
import com.couchbase.lite.util.Log;
import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.models.sensor.Sensor;

public class SensorsManager {

	private final static String WIMOTO_DB 		= "wimoto";
	private final static String WIMOTO_SENSORS 	= "wimoto_sensors";
	private final static String WIMOTO_ID 		= "wimoto_id";
	private final static String WIMOTO_NAME 	= "wimoto_name";
	private final static String WIMOTO_RSSI 	= "wimoto_rssi";
	
	private final static String DOC_TYPE		= "sensor";
	
	private final static String TAG = "SensorsManager";
	
	private ArrayList<Sensor> mSensors;
	
	private Manager mManager;
	private Database mDatabase;
	
	private static SensorsManager sManager = null;
	
	public static SensorsManager getManager() {
		if (sManager == null) {
			sManager = new SensorsManager();
		}
		
		return sManager;
	}
	
	private SensorsManager () {
		mSensors = new ArrayList<Sensor> ();
		
	   	 try {
	   	     mManager = new Manager(MainActivity.getAppContext().getFilesDir(), Manager.DEFAULT_OPTIONS);
	   	 } catch (IOException e) {
	   	     Log.e(TAG, "Cannot create Manager object");
	   	     return;
	   	 }
	   	 
	   	try {
	   		mDatabase = mManager.getDatabase(WIMOTO_DB);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot get Database", e);
            return;
        }
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
	
	public void registerSensor(Sensor sensor) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    Calendar calendar = GregorianCalendar.getInstance();
	    String currentTimeString = dateFormatter.format(calendar.getTime());
		
		Map<String, Object> properties = new HashMap<String, Object>();
		
		properties.put("type", DOC_TYPE);
		properties.put("title", sensor.getTitle());
		properties.put("id", sensor.getId());
		properties.put("created_at", currentTimeString);
		
		try {
			Document document = mDatabase.createDocument();
			document.putProperties(properties);
			
			Log.e("DOC", document.getId());
		} catch (CouchbaseLiteException e) {
			e.printStackTrace();
		} 
	}
	
	public ArrayList<Sensor> getSensors() {
		ArrayList<Sensor> resultList = new ArrayList<Sensor> ();
		
		return resultList;
	}
}
