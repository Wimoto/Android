package com.mobitexoft.widgets;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LinkTextView extends TextView {

	public LinkTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
				} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
					setPaintFlags(0);
				}
				return false;
			}
		});
	}
}
