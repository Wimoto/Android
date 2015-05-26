package com.wimoto.app.widgets.temperature;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class TemperatureValueView extends TemperatureValueTextView {

	public TemperatureValueView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchMeasure();
			}
		});
	}

}
