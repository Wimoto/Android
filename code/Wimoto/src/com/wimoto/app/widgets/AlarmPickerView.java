package com.wimoto.app.widgets;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.wimoto.app.R;

public class AlarmPickerView extends RelativeLayout {

	public interface AlarmPickerListener {
		void onSave(float lowerValue, float upperValue);
		void onCancel();
	}
	
	private Context mContext;
	private boolean mIsShown;
	
	private AlarmPickerListener mListener;
	
	private String mSensorCharacteristic;
	
	private NumberPicker mMinIntegerPicker, mMinFractPicker;
	private NumberPicker mMaxIntegerPicker, mMaxFractPicker;
	
	private float mMinValue, mMaxValue;
	
	private int mPickerMinValue;
	private int mPickerMaxValue;
	
	public AlarmPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.alarm_picker_view, this);

        mMinIntegerPicker = (NumberPicker) findViewById(R.id.alarmMinIntegerNumberPicker);
        mMinFractPicker = (NumberPicker) findViewById(R.id.alarmMinFractNumberPicker);
        setNumberPickerTextColor(mMinFractPicker, Color.RED);

        mMaxIntegerPicker = (NumberPicker) findViewById(R.id.alarmMaxIntegerNumberPicker);
        mMaxFractPicker = (NumberPicker) findViewById(R.id.alarmMaxFractNumberPicker);
        setNumberPickerTextColor(mMaxFractPicker, Color.RED);
        
        Button cancelButton = (Button) findViewById(R.id.alarmCancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//save(false);
			}	
        });
        
        Button saveButton = (Button) findViewById(R.id.alarmSaveButton);
        saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//save(true);
			}	
        });
	}
	
	public AlarmPickerView(Context context, String sensorCharacteristic, int absoluteMinValue, int absoluteMaxValue, final AlarmPickerListener listener) {
		super(context);
		
		mContext = context;
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.alarm_picker_view, this);
        
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
		        RelativeLayout.LayoutParams.WRAP_CONTENT,
		        RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		setLayoutParams(layoutParams);
        
        mSensorCharacteristic = sensorCharacteristic;

        mMinIntegerPicker = (NumberPicker) findViewById(R.id.alarmMinIntegerNumberPicker);
        mMinFractPicker = (NumberPicker) findViewById(R.id.alarmMinFractNumberPicker);
        setNumberPickerTextColor(mMinFractPicker, Color.RED);

        mMaxIntegerPicker = (NumberPicker) findViewById(R.id.alarmMaxIntegerNumberPicker);
        mMaxFractPicker = (NumberPicker) findViewById(R.id.alarmMaxFractNumberPicker);
        setNumberPickerTextColor(mMaxFractPicker, Color.RED);
        
        Button cancelButton = (Button) findViewById(R.id.alarmCancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onCancel();
				}
			}	
        });
        
        Button saveButton = (Button) findViewById(R.id.alarmSaveButton);
        saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					checkValues();
					listener.onSave(mMinValue, mMaxValue);
				}				
			}	
        });
		
        mPickerMinValue = absoluteMinValue;
        mPickerMaxValue = absoluteMaxValue;
        
        mMinIntegerPicker.setMinValue(0);
        mMinIntegerPicker.setMaxValue(mPickerMaxValue - mPickerMinValue);
        
        mMaxIntegerPicker.setMinValue(0);
        mMaxIntegerPicker.setMaxValue(mPickerMaxValue - mPickerMinValue);
        
        String[] nums = new String[mPickerMaxValue - mPickerMinValue + 1];
        for(int i=0; i<nums.length; i++) {
        	nums[i] = Integer.toString(i + mPickerMinValue);
        }
        mMinIntegerPicker.setDisplayedValues(nums);
        mMaxIntegerPicker.setDisplayedValues(nums);
        
        mMinFractPicker.setMinValue(0);
        mMinFractPicker.setMaxValue(9);

        mMaxFractPicker.setMinValue(0);
        mMaxFractPicker.setMaxValue(9);        
	}

	public String getSensorCharacteristic() {
		return mSensorCharacteristic;
	}
	
	private void checkValues() {
		int intMinValue = mMinIntegerPicker.getValue() + mPickerMinValue;
		int fractMinValue = mMinFractPicker.getValue();
		
		mMinValue = (float) (intMinValue + ((float)fractMinValue / 10));
		
		int intMaxValue = mMaxIntegerPicker.getValue() + mPickerMinValue;
		int fractMaxValue = mMaxFractPicker.getValue();
		
		mMaxValue = (float) (intMaxValue + ((float)fractMaxValue / 10));
		
		if (mMinValue > mMaxValue) {
			swapValues();
		}
	}
	
	private void swapValues() {
		float temp = mMaxValue;
		mMaxValue = mMinValue;
		mMinValue = temp;
	}

	public float getSelectedMinValue() {
		return mMinValue;
	}

	public void setSelectedMinValue(float selectedMinValue) {
		mMinValue = selectedMinValue;
		
		int intValue = (int)selectedMinValue;
		int fractValue = (int) ((selectedMinValue - intValue) * 10);
		
		mMinIntegerPicker.setValue(intValue - mPickerMinValue);
		mMinFractPicker.setValue(fractValue);
	}

	public float getSelectedMaxValue() {
		return mMaxValue;
	}

	public void setSelectedMaxValue(float selectedMaxValue) {
		mMaxValue = selectedMaxValue;
		
		int intValue = (int)selectedMaxValue;
		int fractValue = (int) ((selectedMaxValue - intValue) * 10);
		
		mMaxIntegerPicker.setValue(intValue - mPickerMinValue);
		mMaxFractPicker.setValue(fractValue);
	}
	
	public void show() {
		if (mIsShown) {
			return;
		}
		
		 Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alarm_in);
		 startAnimation(animation);
		 
		 setVisibility(View.VISIBLE);
		 
		 mIsShown = true;
	}
	
	public void hide() {
		if (!mIsShown) {
			return;
		}
		
		 Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alarm_out);
		 animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				setVisibility(View.GONE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
			 
		 });
		 
		 startAnimation(animation);
		 mIsShown = false;
	}
	
	private void setNumberPickerTextColor(NumberPicker numberPicker, int color)
	{
	    final int count = numberPicker.getChildCount();
	    for(int i = 0; i < count; i++){
	        View child = numberPicker.getChildAt(i);
	        if(child instanceof EditText){
	            try{
	                Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
	                selectorWheelPaintField.setAccessible(true);
	                ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
	                ((EditText)child).setTextColor(color);
	                numberPicker.invalidate();
	            }
	            catch(NoSuchFieldException e){
	                
	            }
	            catch(IllegalAccessException e){
	                
	            }
	            catch(IllegalArgumentException e){

	            }
	        }
	    }
	}
}
