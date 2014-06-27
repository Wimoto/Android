package com.marknicholas.wimoto.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.util.Log;
import com.marknicholas.wimoto.MainActivity;
import com.marknicholas.wimoto.models.sensor.Sensor;

public class DBManager {

	private final static String WIMOTO_DB 	= "wimoto";
	private final static String WIMOTO_ID 	= "wimoto_id";
	private final static String WIMOTO_NAME = "wimoto_name";
	private final static String WIMOTO_RRSI = "wimoto_rrsi";
	
	private static Database sDatabase = null;
	
	public static void initDB() {
	Manager manager = null;
	   	 try {
	   	     manager = new Manager(MainActivity.getAppContext().getFilesDir(), Manager.DEFAULT_OPTIONS);
	   	 } catch (IOException e) {
	   	     Log.e(WIMOTO_DB, "Cannot create manager object");
	   	     return;
	   	 }
	   	 
	   	 if (!Manager.isValidDatabaseName(WIMOTO_DB)) {
    	     Log.e(WIMOTO_DB, "Bad database name");
    	     return;
    	 } 
	   	 
	   	 sDatabase = null;
    	 try {
    		 sDatabase = manager.getDatabase(WIMOTO_DB);
    	 } catch (CouchbaseLiteException e) {
    	     Log.e(WIMOTO_DB, "Cannot get database");
    	     return;
    	 }
    	 
    	
	}
	
	public static void addSensor(Sensor sensor) {
		
		if (!isExistingSensor(sensor)) {
			Document document = sDatabase.createDocument();

		   	 Map<String, Object> docContent = new HashMap<String, Object>();
		   	 docContent.put(WIMOTO_ID, sensor.getId());
		   	 docContent.put(WIMOTO_NAME, sensor.getTitle());
		   	 docContent.put(WIMOTO_RRSI, sensor.getRrsi());

	    	 // write the document to the database
	    	 try {
	    	     document.putProperties(docContent);
	    	 } catch (CouchbaseLiteException e) {
	    	     Log.e(WIMOTO_DB, "Cannot write document to database", e);
	    	 }

	    	 Log.e("DOC", document.getId());
		}
	}
	
	public static boolean isExistingSensor(Sensor sensor) {
		
//		View view = sDatabase.getView(WIMOTO_DB);
//		Query query = view.createQuery();
//		List <Object> list = query.getKeys();
//		
//		Log.e("LIST", Integer.toString(list.size()));
//		
		return false;
	}
	
	public static void deleteSensor (Sensor sensor) {
		
	}
	
	public static ArrayList<Sensor> getAllSensors() {
		ArrayList<Sensor> resultList = new ArrayList<Sensor> ();
		
		return resultList;
	}
	
//	private void testCouchBase() {
	
// 		create an empty document
//		 Document document = database.createDocument();
//
//    	 // create an object that contains data for a document
//    	 Map<String, Object> docContent = new HashMap<String, Object>();
//    	 docContent.put("message", "Hello Couchbase Lite");
//    	 docContent.put("creationDate", currentTimeString);
//
//
//
//    	 // display the data for the new document
//    	 Log.d(TAG, "docContent=" + String.valueOf(docContent));
//
//
//    	 // write the document to the database
//    	 try {
//    	     document.putProperties(docContent);
//    	 } catch (CouchbaseLiteException e) {
//    	     Log.e(TAG, "Cannot write document to database", e);
//    	 }
//
//
//
//    	 // save the ID of the new document
//    	 String docID = document.getId();
//
//
//
//    	 // retrieve the document from the database
//    	 Document retrievedDocument = database.getDocument(docID);
//
//
//
//    	 // display the retrieved document
//    	 Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
//
//
//
//    	 Log.d(TAG, "End Hello World App");
//    }
//	
}
