package com.mobitexoft.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class Button extends android.widget.Button {

	private String mFontFamily;
	
	public Button(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray attributesArray = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.Font,
				0, 0);
		
		try {	
			mFontFamily = attributesArray.getString(R.styleable.Font_fontFamily);
			setTypeface(Typeface.createFromAsset(context.getAssets(), mFontFamily + ".ttf"));
		} catch (Exception e) {
			// we need catch to just prevent application from crashing
		} finally {
			attributesArray.recycle();
		}
		
	}

}
