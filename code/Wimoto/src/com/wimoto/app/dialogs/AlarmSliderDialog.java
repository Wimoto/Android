package com.wimoto.app.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobitexoft.widgets.RangeBar;
import com.mobitexoft.widgets.RangeBar.OnRangeBarChangeListener;
import com.wimoto.app.R;

public class AlarmSliderDialog extends AlertDialog.Builder {

	public interface AlarmSliderDialogListener {
		void onSave(AlarmSliderDialog dialog);
	}
	
	private AlarmSliderDialogListener mListener;
	private String mSensorCharacteristic;
	
	private float mSelectedMinValue;
	private float mSelectedMaxValue;
	
	private RangeBar<Float> mSeekBar;
	private TextView mResultTextView;
	
	public AlarmSliderDialog(Context context, String sensorCharacteristic, AlarmSliderDialogListener listener, float absoluteMinValue, float absoluteMaxValue) {
		super(context);
		
		mSensorCharacteristic = sensorCharacteristic;
		mListener = listener;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rangeView = inflater.inflate(R.layout.range_view, null);
		mResultTextView = (TextView)rangeView.findViewById(R.id.resultTextView);
		
		mSeekBar = new RangeBar<Float>(absoluteMinValue, absoluteMaxValue, context);
		mSeekBar.setOnRangeBarChangeListener(new OnRangeBarChangeListener<Float>() {
		        @Override
		        public void onRangeBarValuesChanged(RangeBar<?> bar, Float minValue, Float maxValue) {
		        	mResultTextView.setText(String.format("%.2f - %.2f", minValue, maxValue));
		        }
		});
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(60, 40, 60, 60);
		mSeekBar.setLayoutParams(params);
		
		LinearLayout layout = (LinearLayout)rangeView.findViewById(R.id.rangeView);
		layout.addView(mSeekBar);
		
		setCancelable(false);
		setPositiveButton(context.getString(R.string.sensor_save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    	save();
                        dialog.cancel();
                    }
                });
        setNegativeButton(context.getString(R.string.sensor_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

		setView(rangeView);
	}
	
	public String getSensorCharacteristic() {
		return mSensorCharacteristic;
	}

	public float getSelectedMinValue() {
		return mSelectedMinValue;
	}

	public void setSelectedMinValue(float selectedMinValue) {
		mSelectedMinValue = selectedMinValue;
		
		mSeekBar.setSelectedMinValue(mSelectedMinValue);
		mResultTextView.setText(String.format("%.2f - %.2f", mSeekBar.getSelectedMinValue(), mSeekBar.getSelectedMaxValue()));
	}

	public float getSelectedMaxValue() {
		return mSelectedMaxValue;
	}

	public void setSelectedMaxValue(float selectedMaxValue) {
		mSelectedMaxValue = selectedMaxValue;
		
		mSeekBar.setSelectedMaxValue(mSelectedMaxValue);
		mResultTextView.setText(String.format("%.2f - %.2f", mSeekBar.getSelectedMinValue(), mSeekBar.getSelectedMaxValue()));
	}

	private void save() {
		if (mListener != null) {
			mListener.onSave(this);
		}
	}
}
