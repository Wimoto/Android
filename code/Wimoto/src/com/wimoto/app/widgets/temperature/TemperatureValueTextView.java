package com.wimoto.app.widgets.temperature;

import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;

import com.wimoto.app.model.Sensor;

public class TemperatureValueTextView extends TemperatureTextView {
	
	private float mTemperature;
	
	public TemperatureValueTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void setTempMeasure(TemperatureMeasure tempMeasure) {
		super.setTempMeasure(tempMeasure);
		
		setTemperature(mTemperature);
	}
	
	public void setTemperature(float temperature) {
		this.mTemperature = temperature;
		
		float value = (mTempMeasure == TemperatureMeasure.Celsius)?mTemperature:Sensor.celsToFahr(mTemperature);
		setText(String.format(Locale.US, "%.1f", value));
	}

}
