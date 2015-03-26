package com.mobitexoft.utils.propertyobserver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PropertyObservable {
	
	private Map<String, List<PropertyChangeListener>> listenersMap = new HashMap<String, List<PropertyChangeListener>>();
	
	public void addChangeListener(PropertyChangeListener changeListener, String propertyName) {
		addChangeListener(changeListener, propertyName, false);
	}
	
	public void addChangeListener(PropertyChangeListener changeListener, String propertyName, boolean pushUpdate) {
		List<PropertyChangeListener> listenersArray = listenersMap.get(propertyName);
		
		if (listenersArray == null) {
			listenersArray = new ArrayList<PropertyChangeListener>();
			listenersMap.put(propertyName, listenersArray);			
		}
		
		if (pushUpdate) {
			Field field = null;
			
			Class c = getClass();
			while ((c != null) && (field == null)) {
				try {
					field = c.getDeclaredField(propertyName);
					field.setAccessible(true);
					
					Object object = field.get(this);
					changeListener.propertyChange(new PropertyChangeEvent(this, propertyName, object, object));
				} catch (Exception e) { }
				finally {
					try {
						c = c.getSuperclass();
					} catch (Exception e) { }
				}
			}			
		}
		
		listenersArray.add(changeListener);
	}
	
	public void removeChangeListener(PropertyChangeListener changeListener) {
		Set<String> keys = listenersMap.keySet();
		
		for (String key : keys) {
			removeChangeListener(changeListener, key);
		}
	}
	
	public void removeChangeListener(PropertyChangeListener changeListener, String propertyName) {
		List<PropertyChangeListener> listenersArray = listenersMap.get(propertyName);
		
		if (listenersArray != null) {
			listenersArray.remove(changeListener);
		}
	}
	
	protected void notifyObservers(String propertyName, Object oldValue, Object newValue) {
		List<PropertyChangeListener> listenersArray = listenersMap.get(propertyName);
		
		if (listenersArray != null) {
		    for (PropertyChangeListener listener : listenersArray) {
		    	listener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
		    }		    
		}
	}	
}

