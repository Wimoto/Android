package com.wimoto.app.widgets.pickers;

import java.util.Calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.DatePicker;

import com.wimoto.app.R;

public class DatePickerView extends BasePickerView {

	public interface DatePickerListener {
		void onSave(Calendar calendar);
		void onCancel();
	}
	
	private DatePickerListener mListener;
	
	private DatePicker mDatePicker;
	private Calendar mCalendar;
	
	public DatePickerView(Context context, String sensorCharacteristic, DatePickerListener listener) {
		super(context, sensorCharacteristic);
		
		mListener = listener;
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.date_picker_view, this);
        
        initButtons();

		mDatePicker = (DatePicker)findViewById(R.id.datePicker);
	}
	
	public void setSelectedCalendar(Calendar calendar) {
		mCalendar = calendar;
		
		mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
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
			mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
			Log.e("new date", String.format("%d %d %d", mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)));
			
			mListener.onSave(mCalendar);
		}
	}
}
