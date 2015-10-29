package com.wimoto.app.screens.sensor.grow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MoistureTextView extends TextView {

	public MoistureTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setMoistureCalibration(float moisture, Number lowCalibrationValue, Number highCalibrationValue) {
		if ((lowCalibrationValue != null) && (highCalibrationValue != null)) {
			float lowCalibration = lowCalibrationValue.floatValue(); 
			float highCalibration = highCalibrationValue.floatValue();
			
			float calibrationStep = (highCalibration - lowCalibration) / 4;
			if ((lowCalibration + calibrationStep * 1) > moisture) {
	           setText("Very wet");
	        } else if ((lowCalibration + calibrationStep * 2) > moisture) {
	        	setText("Wet");
	        } else if ((lowCalibration + calibrationStep * 3) > moisture) {
	        	setText("Normal");
	        } else if ((lowCalibration + calibrationStep * 4) > moisture) {
	        	setText("Dry");
	        } else if (highCalibration < moisture) {
	        	setText("Very dry");
	        } else {
	        	setText("Very dry");
	        }
		}
 	}
}
