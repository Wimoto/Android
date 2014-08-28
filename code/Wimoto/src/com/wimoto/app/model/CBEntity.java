package com.wimoto.app.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.couchbase.lite.Document;

public abstract class CBEntity {
	
	public static final String CB_DOCUMENT_TYPE			= "type";
	
	private static final String DATE_FORMAT 			= "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	protected Document mDocument;
	protected Date createdAt;
	
	public CBEntity() {
		
	}
	
	public CBEntity(Document doc) {
		mDocument = doc;
		createdAt = new Date();
	}
	
	private Object getProperty(String property) {
		try {
			return mDocument.getProperty(property);
		} catch (Exception e) {
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
	
	protected String getCreatedAt() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
		return dateFormatter.format(createdAt);
	}
}
