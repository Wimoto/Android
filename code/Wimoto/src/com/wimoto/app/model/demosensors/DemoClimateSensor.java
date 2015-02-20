package com.wimoto.app.model.demosensors;

import java.util.LinkedList;
import java.util.Random;

import android.os.Handler;

import com.wimoto.app.R;
import com.wimoto.app.bluetooth.BluetoothConnection.WimotoProfile;
import com.wimoto.app.model.ClimateSensor;
import com.wimoto.app.model.Sensor;
import com.wimoto.app.utils.AppContext;

public class DemoClimateSensor extends Sensor {

	public static final String BLE_CLIMATE_DEMO_MODEL	= "ClimateDemo";
	
	public static final String SENSOR_DEMO_FIELD_CLIMATE_TEMPERATURE				= "DemoClimateTemperature";	
	public static final String SENSOR_DEMO_FIELD_CLIMATE_HUMIDITY					= "DemoClimateHumidity";
	public static final String SENSOR_DEMO_FIELD_CLIMATE_LIGHT						= "DemoClimateLight";
	
	private float mTemperature;
	private float mHumidity;
	private float mLight;
	
	private boolean mTemperatureAlarmSet;
	private float mTemperatureAlarmLow;
	private float mTemperatureAlarmHigh;

	private boolean mHumidityAlarmSet;
	private float mHumidityAlarmLow;
	private float mHumidityAlarmHigh;

	private boolean mLightAlarmSet;
	private float mLightAlarmLow;
	private float mLightAlarmHigh;
	
	private Handler mHandler;
	
	public DemoClimateSensor() {
		super();
		
		mTitle = AppContext.getContext().getString(R.string.sensor_climate_demo);
		mId    = BLE_CLIMATE_DEMO_MODEL;
		
		mTemperature = 22.0f;
		mHumidity	 = 50.0f;
		mLight 		 = 60.0f;
		
		mTemperatureAlarmLow  = 15;
		mTemperatureAlarmHigh =	28;
		
		mHumidityAlarmLow  = 37;
		mHumidityAlarmHigh = 69;
		
		mLightAlarmLow  = 49;
		mLightAlarmHigh = 72;
		
		mSensorValues.put(SENSOR_DEMO_FIELD_CLIMATE_TEMPERATURE, new LinkedList<Float>());
		mSensorValues.put(SENSOR_DEMO_FIELD_CLIMATE_HUMIDITY, new LinkedList<Float>());
		mSensorValues.put(SENSOR_DEMO_FIELD_CLIMATE_LIGHT, new LinkedList<Float>());
		
		mHandler = new Handler();
	}
	
	public WimotoProfile getType() {
		return WimotoProfile.CLIMATE;
	}
	
	public void runDemo() {
		runnable.run();
	}
	
	public void stopDemo() {
		mHandler.removeCallbacks(runnable);
	}

	private Runnable runnable = new Runnable() 
	{
	    public void run() 
	    {
	    	demoUpdate();
	        mHandler.postDelayed(this, 2000);
	    }
	};
	
	public void demoUpdate() {
		Random random = new Random(); 
		
		int temperatureStep = 2 - random.nextInt(5);
		if (mTemperature + temperatureStep < -5) {
			mTemperature += 2.0;
		} else if (mTemperature + temperatureStep > 50) {
			mTemperature -= 2.0;
		} else {
			mTemperature += temperatureStep;
		}
		
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE, mTemperature, mTemperature);

		int humidityStep = 2 - random.nextInt(5);
		if (mHumidity + humidityStep < 0) {
			mHumidity += 2.0;
		} else if (mHumidity + humidityStep > 100) {
			mHumidity -= 2.0;
		} else {
			mHumidity += humidityStep;
		}
		
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY, mHumidity, mHumidity);
		
		int lightStep = 2 - random.nextInt(5);
		if (mLight + lightStep < 0) {
			mLight += 2.0;
		} else if (mLight + lightStep > 200) {
			mLight -= 2.0;
		} else {
			mLight += lightStep;
		}
		
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT, mLight, mLight);
	}
	
	public float getTemperature() {
		return mTemperature;
	}
	
	public void setTemperature(float temperature) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE, mTemperature, temperature);

		mTemperature = temperature;
		//addValue(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE, mTemperature);
	}

	public float getLight() {
		return mLight;
	}

	public void setLight(float light) {		
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT, mLight, light);

		mLight = light;
		//addValue(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT, mLight);
	}

	public float getHumidity() {
		return mHumidity;
	}

	public void setHumidity(float humidity) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY, mHumidity, humidity);
		
		mHumidity = humidity;
		//addValue(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY, mHumidity);
	}

	public float getTemperatureAlarmLow() {
		return mTemperatureAlarmLow;
	}

	public void setTemperatureAlarmLow(float temperatureAlarmLow) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW, mTemperatureAlarmLow, temperatureAlarmLow);
		
		mTemperatureAlarmLow = temperatureAlarmLow;
		//writeAlarmValue(mTemperatureAlarmLow, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, ClimateSensor.BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_LOW);
	}

	public float getTemperatureAlarmHigh() {
		return mTemperatureAlarmHigh;
	}

	public void setTemperatureAlarmHigh(float temperatureAlarmHigh) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH, mTemperatureAlarmHigh, temperatureAlarmHigh);

		mTemperatureAlarmHigh = temperatureAlarmHigh;
		//writeAlarmValue(mTemperatureAlarmHigh, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, ClimateSensor.BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_HIGH);
	}
	
	public boolean isTemperatureAlarmSet() {
		return mTemperatureAlarmSet;
	}

	public void setTemperatureAlarmSet(boolean temperatureAlarmSet) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_SET, mTemperatureAlarmSet, temperatureAlarmSet);
		
		mTemperatureAlarmSet = temperatureAlarmSet;
		//enableAlarm(mTemperatureAlarmSet, BLE_CLIMATE_SERVICE_UUID_TEMPERATURE, BLE_CLIMATE_CHAR_UUID_TEMPERATURE_ALARM_SET);
	}

	public boolean isHumidityAlarmSet() {
		return mHumidityAlarmSet;
	}

	public void setHumidityAlarmSet(boolean humidityAlarmSet) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_SET, mHumidityAlarmSet, humidityAlarmSet);
		
		mHumidityAlarmSet = humidityAlarmSet;
		//enableAlarm(mHumidityAlarmSet, BLE_CLIMATE_SERVICE_UUID_HUMIDITY, BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_SET);
	}

	public float getHumidityAlarmLow() {
		return mHumidityAlarmLow;
	}

	public void setHumidityAlarmLow(float humidityAlarmLow) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW, mHumidityAlarmLow, humidityAlarmLow);
		
		mHumidityAlarmLow = humidityAlarmLow;
		//writeAlarmValue(mHumidityAlarmLow, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_HUMIDITY, ClimateSensor.BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_LOW);	
	}

	public float getHumidityAlarmHigh() {
		return mHumidityAlarmHigh;
	}

	public void setHumidityAlarmHigh(float humidityAlarmHigh) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH, mHumidityAlarmHigh, humidityAlarmHigh);
		
		mHumidityAlarmHigh = humidityAlarmHigh;
		//writeAlarmValue(mHumidityAlarmHigh, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_HUMIDITY, ClimateSensor.BLE_CLIMATE_CHAR_UUID_HUMIDITY_ALARM_HIGH);
	}

	public boolean isLightAlarmSet() {
		return mLightAlarmSet;
	}

	public void setLightAlarmSet(boolean lightAlarmSet) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_SET, mLightAlarmSet, lightAlarmSet);
		
		mLightAlarmSet = lightAlarmSet;
		//enableAlarm(mLightAlarmSet, BLE_CLIMATE_SERVICE_UUID_LIGHT, BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_SET);
	}

	public float getLightAlarmLow() {
		return mLightAlarmLow;
	}

	public void setLightAlarmLow(float lightAlarmLow) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW, mLightAlarmLow, lightAlarmLow);
		
		mLightAlarmLow = lightAlarmLow;
		//writeAlarmValue(mLightAlarmLow, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_LIGHT, ClimateSensor.BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_LOW);
	}

	public float getLightAlarmHigh() {
		return mLightAlarmHigh;
	}

	public void setLightAlarmHigh(float lightAlarmHigh) {
		notifyObservers(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH, mLightAlarmHigh, lightAlarmHigh);
		
		mLightAlarmHigh = lightAlarmHigh;
		//writeAlarmValue(mLightAlarmHigh, ClimateSensor.BLE_CLIMATE_SERVICE_UUID_LIGHT, ClimateSensor.BLE_CLIMATE_CHAR_UUID_LIGHT_ALARM_HIGH);
	}
	
	public String getPropertyString(String sensorCharacteristic) {
		if (sensorCharacteristic.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE) ||
				sensorCharacteristic.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_HIGH) ||
				sensorCharacteristic.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_TEMPERATURE_ALARM_LOW)) {
			return ClimateSensor.SENSOR_FIELD_TEMPERATURE;
		} else if (sensorCharacteristic.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY) ||
					sensorCharacteristic.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_HIGH) ||
					sensorCharacteristic.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_HUMIDITY_ALARM_LOW)) {
			return ClimateSensor.SENSOR_FIELD_HUMIDITY;
		} else if (sensorCharacteristic.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT) ||
					sensorCharacteristic.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_HIGH) ||
					sensorCharacteristic.equals(ClimateSensor.SENSOR_FIELD_CLIMATE_LIGHT_ALARM_LOW)) {
			return ClimateSensor.SENSOR_FIELD_LIGHT;
		}
			
		return "";
	}
	
	public String getAlarmMessage(String propertyString) {
		if (propertyString.equals(ClimateSensor.SENSOR_FIELD_TEMPERATURE)) {
				return getTemperatureAlarm();
			} else if (propertyString.equals(ClimateSensor.SENSOR_FIELD_HUMIDITY)) {
					return getHumidityAlarm();
			} else if (propertyString.equals(ClimateSensor.SENSOR_FIELD_LIGHT)) {
					return getLightAlarm();
			}
			
			return "";
	}
	
	private String getTemperatureAlarm() {
		if (!mTemperatureAlarmSet) {
			return "";
		}
		
		if (mTemperature < mTemperatureAlarmLow) {
			return "alarm low";
		} else if (mTemperature > mTemperatureAlarmHigh) {
			return "alarm high";
		}
		
		return "";
	}
	
	private String getHumidityAlarm() {
		if (!mHumidityAlarmSet) {
			return "";
		}
		
		if (mHumidity < mHumidityAlarmLow) {
			return "alarm low";
		} else if (mHumidity > mHumidityAlarmHigh) {
			return "alarm high";
		}
		
		return "";
	}
	
	private String getLightAlarm() {
		if (!mLightAlarmSet) {
			return "";
		}
		
		if (mLight < mLightAlarmLow) {
			return "alarm low";
		} else if (mLight > mLightAlarmHigh) {
			return "alarm high";
		}
		
		return "";
	}
}
