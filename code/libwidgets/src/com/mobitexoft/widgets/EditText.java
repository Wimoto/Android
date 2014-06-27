package com.mobitexoft.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class EditText extends android.widget.EditText {
	
	private String mFontFamily;
	private Context mContext;
	
	private OnEditorActionListener externalEditorActionListener;
	
	public EditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		
		TypedArray attributesArray = context.getTheme().obtainStyledAttributes(
					attrs,
					R.styleable.Font,
					0, 0);
		try {	
			mFontFamily = attributesArray.getString(R.styleable.Font_fontFamily);
			setTypeface(Typeface.createFromAsset(context.getAssets(), mFontFamily + ".ttf"));
			
			setOnEditorActionListener(null);
		} catch (Exception e) {
			// we need catch to just prevent application from crashing
		} finally {
			attributesArray.recycle();
		}
	}
	
	@Override
	public void setInputType(int type) {
		super.setInputType(type);
		setTypeface(Typeface.createFromAsset(mContext.getAssets(), mFontFamily + ".ttf"));
	}
	
	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
	        clearFocus();
	        return false;
	    }
	    return false;
	}

	@Override
	public void setOnEditorActionListener(OnEditorActionListener l) {
		externalEditorActionListener = l;
		
		OnEditorActionListener listener = new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {					
					v.clearFocus();
					InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
	                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
				
				if (externalEditorActionListener != null) {
					return externalEditorActionListener.onEditorAction(v, actionId, event);
				} else {
					return false;
				}
			}
		};
		super.setOnEditorActionListener(listener);
	}
	
}
