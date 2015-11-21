package com.wimoto.app.model.datalog;

import org.json.JSONException;
import org.json.JSONObject;

public class DataLog {
	
	private static final String DATALOG_RAW		= "Raw";
	private static final String DATALOG_YEAR	= "Year";
	private static final String DATALOG_MONTH	= "Month";
	private static final String DATALOG_DAY		= "Day";	
	private static final String DATALOG_HOUR	= "Hour";
	private static final String DATALOG_MINUTES	= "Minutes";
	private static final String DATALOG_SECONDS	= "Seconds";
	private static final String DATALOG_LOG_ID	= "LogId";
	
	private String mRaw;
	
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private int mSeconds;
	private int mLogId;
	
	public DataLog() {
		
	}
	
	public DataLog(byte[] data) {
		this.mRaw = hexadecimalString(data);
		
		this.mYear 		= ((data[0] & 0xff) << 8) | (data[1] & 0xff);
		this.mMonth 	= data[2] & 0xff;
		this.mDay 		= data[3] & 0xff;
		this.mHour 		= data[4] & 0xff;
		this.mMinute 	= data[5] & 0xff;
		this.mSeconds 	= data[6] & 0xff;
		this.mLogId 	= ((data[14] & 0xff) << 8) | (data[15] & 0xff);
	}
	
	public JSONObject getJSONObject() {
		JSONObject object = new JSONObject();
			
		try {
			object.put(DATALOG_RAW, mRaw);
			object.put(DATALOG_YEAR, mYear);
			object.put(DATALOG_MONTH, mMonth);
			object.put(DATALOG_DAY, mDay);
			object.put(DATALOG_HOUR, mHour);
			object.put(DATALOG_MINUTES, mMinute);
			object.put(DATALOG_SECONDS, mSeconds);
			object.put(DATALOG_LOG_ID, mLogId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return object;
	}
	
	private String hexadecimalString(byte[] data) {
		StringBuilder sb = new StringBuilder(data.length * 2);
		for (byte b: data)
		   sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}
}
