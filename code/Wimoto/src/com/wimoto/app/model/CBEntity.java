package com.wimoto.app.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.couchbase.lite.Document;

public abstract class CBEntity {
	
	public static final String CB_DOCUMENT_TYPE			= "type";
	
	protected Document mDocument;
	
	public CBEntity() {
		
	}
	
	public CBEntity(Document doc) {
		mDocument = doc;
	}
	
	private Object getProperty(String property) {
		try {
			return mDocument.getProperty(property);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected String getString(String property) {
		Object object = getProperty(property);
		if (object instanceof String) {
			return (String) object;
		}
			
		return null;
	}
	
	protected Float getFloat(String property) {
		Object object = getProperty(property);
		if (object instanceof String) {
			return Float.valueOf((String) object);
		}
		return null;
	}
	
	protected Date getDate(String property) {
		Object object = getProperty(property);
		if (object instanceof String) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		    try {
				return dateFormatter.parse((String) object);
			} catch (ParseException e) {
				e.printStackTrace();
			}			
		}
		return null;
	}
}
