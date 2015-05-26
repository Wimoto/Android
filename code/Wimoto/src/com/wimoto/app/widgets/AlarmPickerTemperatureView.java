package com.wimoto.app.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import com.wimoto.app.model.Sensor;
import com.wimoto.app.widgets.temperature.TemperatureTextView.TemperatureMeasure;

public class AlarmPickerTemperatureView extends AlarmPickerView implements OnSharedPreferenceChangeListener {

	private SharedPreferences mPreferences;
	private TemperatureMeasure mTempMeasure;
	
	public AlarmPickerTemperatureView(Context context,String sensorCharacteristic, int absoluteMinValue, int absoluteMaxValue, AlarmPickerListener listener) {
		super(context, sensorCharacteristic, absoluteMinValue, absoluteMaxValue, listener);
		
		mPreferences = context.getSharedPreferences("Wimoto", Context.MODE_PRIVATE);
		mPreferences.registerOnSharedPreferenceChangeListener(this);
		
		updateWithPreferences(mPreferences);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals("cOrF")) {
			updateWithPreferences(sharedPreferences);
		}
	}
	
	private void updateWithPreferences(SharedPreferences sharedPreferences) {
		String temparatureUnitString = sharedPreferences.getString("cOrF", "C");
		setTempMeasure(temparatureUnitString.equals("C")?TemperatureMeasure.Celsius:TemperatureMeasure.Fahrenheit);
	}
	
	protected void setTempMeasure(TemperatureMeasure tempMeasure) {
		this.mTempMeasure = tempMeasure;
	}

	@Override
	public void setSelectedMinValue(float selectedMinValue) {
		super.setSelectedMinValue((mTempMeasure == TemperatureMeasure.Fahrenheit)?Sensor.celsToFahr(selectedMinValue):selectedMinValue);
	}

	@Override
	public void setSelectedMaxValue(float selectedMaxValue) {
		super.setSelectedMaxValue((mTempMeasure == TemperatureMeasure.Fahrenheit)?Sensor.celsToFahr(selectedMaxValue):selectedMaxValue);
	}

	@Override
	protected float getMinValue() {
		return ((mTempMeasure == TemperatureMeasure.Fahrenheit)?Sensor.fahrToCels(mMinValue):mMinValue);
	}

	@Override
	protected float getMaxValue() {
		return ((mTempMeasure == TemperatureMeasure.Fahrenheit)?Sensor.fahrToCels(mMaxValue):mMaxValue);
	}
}
