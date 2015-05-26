package com.wimoto.app.widgets.temperature;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.AttributeSet;
import android.widget.TextView;

public class TemperatureTextView extends TextView implements OnSharedPreferenceChangeListener {

	public enum TemperatureMeasure {
		Celsius, 
		Fahrenheit
	};
	
	private SharedPreferences mPreferences;
	protected TemperatureMeasure mTempMeasure;
	
	public TemperatureTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if(!isInEditMode()) {
			mPreferences = context.getSharedPreferences("Wimoto", Context.MODE_PRIVATE);
			mPreferences.registerOnSharedPreferenceChangeListener(this);
			
			updateWithPreferences(mPreferences);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
		mPreferences.unregisterOnSharedPreferenceChangeListener(this);
	}

	protected void setTempMeasure(TemperatureMeasure tempMeasure) {
		this.mTempMeasure = tempMeasure;
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
	
	protected void switchMeasure() {
		String temparatureNewUnitString = (mTempMeasure == TemperatureMeasure.Celsius)?"F":"C";
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString("cOrF", temparatureNewUnitString);
		editor.commit();
	}
}
