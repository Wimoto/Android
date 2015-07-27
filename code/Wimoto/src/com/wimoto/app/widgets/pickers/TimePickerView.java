package com.wimoto.app.widgets.pickers;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.wimoto.app.R;

public class TimePickerView extends BasePickerView {

	public interface TimePickerListener {
		void onSave(Date lowerDate, Date upperDate);
		void onCancel();
	}
	
	private TimePickerListener mListener;
	private TimePicker mLeftTimePicker;
	private TimePicker mRightTimePicker;
	
	public TimePickerView(Context context, String sensorCharacteristic, TimePickerListener listener) {
		super(context, sensorCharacteristic);
		
		mListener = listener;
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_picker_view, this);
        
        initButtons();

        mLeftTimePicker = (TimePicker)findViewById(R.id.timePickerLeft);
        setTimePickerMargins(mLeftTimePicker, 0, 0, 0);
        
        mRightTimePicker = (TimePicker)findViewById(R.id.timePickerRight);
        setTimePickerMargins(mRightTimePicker, 0, 0, 0);
	}
	
	public void setMinMaxDate(Date minDate, Date maxDate) {
		Calendar c = Calendar.getInstance();
		
		c.setTime(minDate);
		mLeftTimePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		mLeftTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));
		
		c.setTime(maxDate);
		mRightTimePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
		mRightTimePicker.setCurrentMinute(c.get(Calendar.MINUTE));
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
			mListener.onSave(getLowerDate(), getUpperDate());
		}
	}
	
	private Date getLowerDate() {
		Date leftDate = getPickerDate(mLeftTimePicker);
		Date rightDate = getPickerDate(mRightTimePicker);
		
		return leftDate.before(rightDate) ? leftDate : rightDate;
	}
	
	private Date getUpperDate() {
		Date leftDate = getPickerDate(mLeftTimePicker);
		Date rightDate = getPickerDate(mRightTimePicker);
		
		return leftDate.after(rightDate) ? leftDate : rightDate;
	}
	
	private Date getPickerDate(TimePicker picker) {
		picker.clearFocus();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
		cal.set(Calendar.MINUTE, picker.getCurrentMinute());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}
	
	public void setTimePickerMargins (TimePicker timePicker, int topMargin, int leftMargin, int rightMargin) {
	    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	        try {
	            // mHourSpinner
	            Field f = TimePicker.class.getDeclaredField("mHourSpinner");
	            f.setAccessible(true);
	            View v = (View) f.get(timePicker);
	            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
	            params.topMargin = topMargin;
	            params.leftMargin = leftMargin;
	            params.rightMargin = rightMargin;
	            // mMinuteSpinner
	            f = TimePicker.class.getDeclaredField("mMinuteSpinner");
	            f.setAccessible(true);
	            v = (View) f.get(timePicker);
	            params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
	            params.topMargin = topMargin;
	            params.leftMargin = leftMargin;
	            params.rightMargin = rightMargin;
	            // mAmPmSpinner
	            f = TimePicker.class.getDeclaredField("mAmPmSpinner");
	            f.setAccessible(true);
	            v = (View) f.get(timePicker);
	            params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
	            params.topMargin = topMargin;
	            params.leftMargin = leftMargin;
	            params.rightMargin = rightMargin;
	        } catch (Exception e) {

	        }
	    }
	}
}
