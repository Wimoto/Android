package com.wimoto.app.widgets.temperature;

import android.content.Context;
import android.util.AttributeSet;

public class TemperatureSignTextView extends TemperatureTextView {
	
	public TemperatureSignTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void setTempMeasure(TemperatureMeasure tempMeasure) {
		super.setTempMeasure(tempMeasure);
		
		setText((mTempMeasure == TemperatureMeasure.Celsius)?"˚C":"˚F");
	}
}
