package com.wimoto.app.widgets.pickers;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import com.wimoto.app.R;

public class AlarmPickerView extends BasePickerView {

	public interface AlarmPickerListener {
		void onSave(float lowerValue, float upperValue);
		void onCancel();
	}
	
	private AlarmPickerListener mListener;
	
	private NumberPicker mMinIntegerPicker, mMinFractPicker;
	private NumberPicker mMaxIntegerPicker, mMaxFractPicker;
	
	protected float mMinValue, mMaxValue;
	public int mMinusZero;
	
	private int mPickerMinValue;
	private int mPickerMaxValue;
	
	public AlarmPickerView(Context context, String sensorCharacteristic, int absoluteMinValue, int absoluteMaxValue, final AlarmPickerListener listener) {
		super(context, sensorCharacteristic);
		
		mListener = listener;
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.alarm_picker_view, this);
        
        initButtons();

        mMinIntegerPicker = (NumberPicker) findViewById(R.id.alarmMinIntegerNumberPicker);
        mMinFractPicker = (NumberPicker) findViewById(R.id.alarmMinFractNumberPicker);
        setNumberPickerTextColor(mMinFractPicker, Color.RED);

        mMaxIntegerPicker = (NumberPicker) findViewById(R.id.alarmMaxIntegerNumberPicker);
        mMaxFractPicker = (NumberPicker) findViewById(R.id.alarmMaxFractNumberPicker);
        setNumberPickerTextColor(mMaxFractPicker, Color.RED);

        mPickerMinValue = absoluteMinValue;
        mPickerMaxValue = absoluteMaxValue;
        
        mMinusZero = ((mPickerMinValue < 0) && (mPickerMaxValue >= 0)) ? 1 : 0;
        
        final int maxValue = mPickerMaxValue - mPickerMinValue + mMinusZero;
        
        mMinIntegerPicker.setMinValue(0);
        mMinIntegerPicker.setMaxValue(maxValue);
        
        mMinIntegerPicker.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if ((oldVal == 0) && (newVal == maxValue)) {
					mMinIntegerPicker.setValue(0);
				} else if ((oldVal == maxValue) && (newVal == 0)) {
					mMinIntegerPicker.setValue(maxValue);
				}
			}
        });
        
        mMaxIntegerPicker.setMinValue(0);
        mMaxIntegerPicker.setMaxValue(maxValue);
        
        mMaxIntegerPicker.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if ((oldVal == 0) && (newVal == maxValue)) {
					mMaxIntegerPicker.setValue(0);
				} else if ((oldVal == maxValue) && (newVal == 0)) {
					mMaxIntegerPicker.setValue(maxValue);
				}
			}
        });
        
        String[] nums = new String[mPickerMaxValue - mPickerMinValue + 1 + mMinusZero];
        for(int i=0; i<nums.length; i++) {
        	if (mMinusZero == 0) {
        		nums[i] = Integer.toString(i + mPickerMinValue);
        	} else {
        		int value = i + mPickerMinValue;
            	
        		if (value < 0) {
        			nums[i] = Integer.toString(i + mPickerMinValue);
        		} else if (value == 0) {
        			nums[i] = "-0";
        		} else if (value > 0) {
        			nums[i] = Integer.toString(i + mPickerMinValue - 1);
        		}
        	}
        }
        
        mMinIntegerPicker.setDisplayedValues(nums);
        mMaxIntegerPicker.setDisplayedValues(nums);
        
        mMinFractPicker.setMinValue(0);
        mMinFractPicker.setMaxValue(9);

        mMaxFractPicker.setMinValue(0);
        mMaxFractPicker.setMaxValue(9);        
	}
	

	@Override
	void cancel() {
		if (mListener != null) {
			mListener.onCancel();
		}
	}

	@Override
	void save() {
		if (mListener != null) {
			checkValues();
			mListener.onSave(getMinValue(), getMaxValue());
		}	
	}
	
	private void checkValues() {
		int intMinValue = mMinIntegerPicker.getValue() + mPickerMinValue;
		int fractMinValue = mMinFractPicker.getValue();
		
		mMinValue = (float) ((intMinValue > 0) ? (intMinValue + ((float)fractMinValue / 10) - mMinusZero) : (intMinValue - ((float)fractMinValue / 10)));
		
		int intMaxValue = mMaxIntegerPicker.getValue() + mPickerMinValue;
		int fractMaxValue = mMaxFractPicker.getValue();
		
		mMaxValue = (float) ((intMaxValue > 0) ? (intMaxValue + ((float)fractMaxValue / 10) - mMinusZero) : (intMaxValue - ((float)fractMaxValue / 10)));
		
		if (mMinValue > mMaxValue) {
			swapValues();
		}
	}
	
	private void swapValues() {
		float temp = mMaxValue;
		mMaxValue = mMinValue;
		mMinValue = temp;
	}

	protected float getMinValue() {
		return mMinValue;
	}
	
	protected float getMaxValue() {
		return mMaxValue;
	}
	
	public float getSelectedMinValue() {
		return mMinValue;
	}

	public void setSelectedMinValue(float selectedMinValue) {
		int tenMultValue = (int) (Math.round(selectedMinValue * 10));
		
		mMinValue = (float)tenMultValue / 10.0f;

		int intValue = tenMultValue / 10;		
		int fractValue = Math.abs(tenMultValue) % 10;

		int value = intValue - mPickerMinValue;

		mMinIntegerPicker.setValue((mMinValue < 0) ? value : value + mMinusZero);
		mMinFractPicker.setValue(fractValue);
	}

	public float getSelectedMaxValue() {
		return mMaxValue;
	}

	public void setSelectedMaxValue(float selectedMaxValue) {
		int tenMultValue = (int) (Math.round(selectedMaxValue * 10));
		
		mMaxValue = (float)tenMultValue / 10.0f;
		
		int intValue = tenMultValue / 10;
		int fractValue = Math.abs(tenMultValue) % 10;
		
		int value = intValue - mPickerMinValue;
		
		mMaxIntegerPicker.setValue((mMaxValue < 0) ? value : value + mMinusZero);
		mMaxFractPicker.setValue(fractValue);
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
